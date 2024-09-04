package Try_it.alarm;

import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class AlarmService {
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Autowired
    private final EmitterRepositoryImpl emitterRepository = new EmitterRepositoryImpl();
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1시간 -> SSE 연결은 1시간동안 지속

    @Autowired
    public AlarmService(UserRepository userRepository, AlarmRepository alarmRepository) {
        this.userRepository = userRepository;
        this.alarmRepository = alarmRepository;
    }

    public SseEmitter connect(final String userPk, final String lastEventId){
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(()->new IllegalStateException("User not found"));
        String emitterId = user.getUserPk() + "_" + System.currentTimeMillis();
        log.info("emitterId: " + emitterId);
        // SseEmitter 객체 생성(유효시간 DEFAULT_TIMEOUT) -> 시간이 지나면 자동으로 클라이언트에서 재연결 요청 보냄.
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        log.info("Created SseEmitter: {}", sseEmitter);
        // SseEmitter 저장(emitterId도 함께 저장, SseEmitter)
        SseEmitter emitter = emitterRepository.save(emitterId, sseEmitter);
        log.info("Saved SseEmitter: {}", emitter);

        // 상황별 emitter 삭제 처리(완료 시, 타임아웃 시, 에러 발생 시)
        emitter.onCompletion(()-> emitterRepository.deleteEmitterById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteEmitterById(emitterId));
        emitter.onError((e)-> emitterRepository.deleteEmitterById(emitterId));

        log.info("lastEventId: {}", lastEventId);


        //503 에러 방지용 더비 데이터 전송 => SSE 연결이 이뤄진 후, 데이터가 하나도 전송되지 않았는데 SseEmitter의 유효시간이 끝나면 503에러가 발생하기 때문.
        sendToClient(emitter, emitterId, "EventStream Created, [userPk=" + userPk + "]");

        // client 가 미수신한 event 목록이 존재한 경우
        // Last-Event-ID 값이 헤더에 있는 경우, 저장된 데이터 캐시에서 id 값과 Last-Event-ID 값을 통해
        // 유실된 데이터들만 다시 보내준다.
        if(!lastEventId.isEmpty()){
            Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserPk(userPk);
            eventCaches.entrySet().stream() // 미수신 상태인 event 목록 전송
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0 )
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

//    알림 전송 메소드
    public void send(final UserEntity receiver,
                     final String title,
                     final String content){
        AlarmEntity alarmEntity = alarmRepository.save(createNotification(receiver, title, content));
        String userPk = String.valueOf(receiver.getUserPk());
        // 로그인한 client의 Emitter 전체 호출(여러 브라우저에서 접속할 수 있기 때문에 emitter가 여러 개일 수 있음)
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmiiterStartWithByUserPk(userPk);
        // 해당 데이터를 EventCache 에 저장
        sseEmitters.forEach(
            (key, emitter) -> {
                log.info("key, alarmEntity : {}, {}", key, alarmEntity);
                emitterRepository.saveEventCache(key, alarmEntity);
                sendToClient(emitter, key, alarmEntity);
            }
        );
    }
// emitter, emitterId 와 함게 알림 내용을 클라이언트에게 전달
    private void sendToClient(final SseEmitter emitter, final String emitterId, final Object data){
        try{
            emitter.send(SseEmitter.event()
                .id(emitterId)
                .name("sse")
//                .data(data));
                .data(data, MediaType.APPLICATION_JSON));
        }catch(IOException e){
            emitterRepository.deleteEmitterById(emitterId);
            throw new IllegalStateException("클라이언트에게 알림 전송 실패");
        }
    }

//    private AlarmEntity createDummyNotification(final String alarmReceiver){
//        UserEntity user = userRepository.findByUserId(alarmReceiver);
//        return AlarmEntity.builder()
////            .alarmId(alarmReceiver + "_" + System.currentTimeMillis())
//            .alarmContent("send dummy notification")
//            .user(user)
////            .alarmUrl("/dummy-url")
//            .alarmTitle("dummy notification")
//            .alarmIsRead(false)
//            .build();
//        //Todo: alarmUrl method 만들어야 함. 시간 남으면 하기!
//    }

    private AlarmEntity createNotification(final UserEntity receiver,
                                           final String title,
                                           final String content){

        UserEntity user = userRepository.findByUserPk(receiver.getUserPk())
            .orElseThrow(() ->new IllegalStateException("User not found"));

        return AlarmEntity.builder()
            .user(user)
            .alarmTitle(title)
            .alarmContent(content)
            .alarmIsRead(false)
            .build();
    }


//    private void emitEventToClient(final SseEmitter sseEmitter, final String emitterId, final AlarmEntity data){
//        try {
//            send(sseEmitter, emitterId, data);
//        } catch (Exception exception) {
//            throw new RuntimeException("Connection Failed.");
//        } finally {
//            sseRepository.deleteEmitterById(emitterId);
//
//        }
//    }
}
