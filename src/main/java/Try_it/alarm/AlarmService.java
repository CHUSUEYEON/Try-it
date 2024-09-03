package Try_it.alarm;

import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class AlarmService {
    private final SseRepositoryImpl sseRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    @Autowired
    public AlarmService(SseRepositoryImpl sseRepository, UserRepository userRepository,AlarmRepository alarmRepository) {
        this.sseRepository = sseRepository;
        this.userRepository = userRepository;
        this.alarmRepository = alarmRepository;
    }

    public SseEmitter connect(final String userPk, final String lastEventId){
        UserEntity user = userRepository.findByUserPk(Long.valueOf(userPk))
            .orElseThrow(()->new IllegalStateException("User not found"));
        String emitterId = user.getUserName() + "_" + System.currentTimeMillis();
        log.info("emitterId: " + emitterId);
//        SseEmitter sseEmitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
//        log.info("new emitter added: {}", sseEmitter);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        log.info("Created SseEmitter: {}", sseEmitter);
        SseEmitter savedEmitter = sseRepository.save(emitterId, sseEmitter);
        log.info("Saved SseEmitter: {}", savedEmitter);
        log.info("Stored Emitter Map: {}", sseRepository.findAllEmitterStartsWithUsername(user.getUserName()));

        log.info("lastEventId: {}", lastEventId);

        // 상황별 emitter 삭제 처리(완료 시, 타임아웃 시, 에러 발생 시)
        savedEmitter.onCompletion(()-> sseRepository.deleteEmitterById(emitterId));
        savedEmitter.onTimeout(() -> sseRepository.deleteEmitterById(emitterId));
        savedEmitter.onError((e)-> sseRepository.deleteEmitterById(emitterId));

        //503 에러 방지용 더비 데이터 전송
        send(savedEmitter, emitterId, createDummyNotification(user.getUserName()));

        // client 가 미수신한 event 목록이 존재한 경우
        // Last-Event-ID 값이 헤더에 있는 경우, 저장된 데이터 캐시에서 id 값과 Last-Event-ID 값을 통해
        // 유실된 데이터들만 다시 보내준다.
        if(!lastEventId.isEmpty()){
            Map<String, AlarmEntity> eventCaches = sseRepository.findAllEventCacheStartsWithUsername(user.getUserName());
            eventCaches.entrySet().stream() // 미수신 상태인 event 목록 전송
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0 )
                .forEach(entry -> emitEventToClient(savedEmitter, entry.getKey(), entry.getValue()));
        }
        return savedEmitter;
    }

//    //특정 회원에게 알림 전송
    public void sendTo(final String receiver,
                     final String title,
                     final String content,
                     final String url){
        AlarmEntity alarmEntity = createNotification(receiver, title, content, url);
        // 로그인한 client의 sseEmitter 전체 호출
        Map<String, SseEmitter> sseEmitters = sseRepository.findAllEmitterStartsWithUsername(receiver);
        sseEmitters.forEach(
            (key, sseEmitter) -> {
                log.info("key, alarmEntity : {}, {}", key, alarmEntity);
                sseRepository.saveEventCache(key, alarmEntity);
                emitEventToClient(sseEmitter, key, alarmEntity);
            }
        );
    }

    private AlarmEntity createDummyNotification(final String alarmReceiver){
        UserEntity user = userRepository.findByUserId(alarmReceiver);
        return AlarmEntity.builder()
//            .alarmId(alarmReceiver + "_" + System.currentTimeMillis())
            .alarmContent("send dummy notification")
            .user(user)
//            .alarmUrl("/dummy-url")
            .alarmTitle("dummy notification")
            .alarmIsRead(false)
            .build();
        //Todo: alarmUrl method 만들어야 함. 시간 남으면 하기!
    }

    private AlarmEntity createNotification(final String receiver,
                                           final String title,
                                           final String content,
                                           final String url){

        UserEntity user = userRepository.findByUserId(receiver);

        return AlarmEntity.builder()
            .user(user)
            .alarmTitle(title)
            .alarmContent(content)
            .alarmIsRead(false)
            .alarmUrl(url)
            .build();
    }

    private void send(final SseEmitter sseEmitter, final String emitterId, final AlarmEntity data){
        try{
            sseEmitter.send(SseEmitter.event()
                .id(emitterId)
                .name("sse")
                .data(data, MediaType.APPLICATION_JSON));
        }catch(IOException e){
            sseRepository.deleteEmitterById(emitterId);
        }
    }

    private void emitEventToClient(final SseEmitter sseEmitter, final String emitterId, final AlarmEntity data){
        try {
            send(sseEmitter, emitterId, data);
        } catch (Exception exception) {
            throw new RuntimeException("Connection Failed.");
        } finally {
            sseRepository.deleteEmitterById(emitterId);

        }
    }
}
