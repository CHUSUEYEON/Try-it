package Try_it.alarm;//package Try_it.alarm;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Repository
//public interface SseRepository{
//
//    final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    //Emitter 저장
//    SseEmitter save(String emitterId, SseEmitter sseEmitter);
//
//    // 이벤트 저장
//    void saveEventCache(String eventCacheId, Object event);
//
//    // 해당 회원과 관련된 모든 Emitter를 찾는다. 브라우저당 여러 개 연결이 가능하기에 여러 Emitter가 존재할 수 있다.
//    Map<String, SseEmitter> findAllEmitterStartsWithUsername(String username);
//
//    // 해당 회원과 관련된 모든 이벤트를 찾는다.
//    Map<String, AlarmEntity> findAllEventCacheStartsWithUsername(String username);
//
//    //Emitter를 지운다.
//    void deleteEmitterById(String id);
//
//    // 해당 회원과 관련된 모든 Emitter를 지운다.
//    void deleteAllEmitterStartsWithId(String id);
//
//    //해당 회원과 관련된 모든 이벤트를 지운다.
//    void deleteEventCacheStartsId(String id);
//
//}

import Try_it.alarm.AlarmEntity;
import Try_it.alarm.SseRepository.SseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SseRepositoryImpl implements SseRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, AlarmEntity> eventCache = new ConcurrentHashMap<>();

    // Emitter 저장
//    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
//        return emitters.put(emitterId, sseEmitter);
//    }
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        log.info("Saving SseEmitter with ID: {}", emitterId);
        SseEmitter previousEmitter = emitters.put(emitterId, sseEmitter);
        log.info("Previous Emitter: {}", previousEmitter);
        return previousEmitter;
    }

    @Override
    public void saveEventCache(String emitterId, Object event) {

    }

    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId) {
        return Map.of();
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByUserId(String userId) {
        return Map.of();
    }

    // 이벤트 저장
    public void saveEventCache(String eventCacheId, AlarmEntity event) {
        eventCache.put(eventCacheId, event);
    }

    // 해당 회원과 관련된 모든 Emitter를 찾는다.
    public Map<String, SseEmitter> findAllEmitterStartsWithUsername(String username) {
        // username으로 필터링하여 반환하는 로직 구현 필요
        return new ConcurrentHashMap<>(emitters);
    }

    // 해당 회원과 관련된 모든 이벤트를 찾는다.
    public Map<String, AlarmEntity> findAllEventCacheStartsWithUsername(String username) {
        // username으로 필터링하여 반환하는 로직 구현 필요
        return new ConcurrentHashMap<>(eventCache);
    }

    // Emitter를 지운다.
    public void deleteEmitterById(String id) {
        emitters.remove(id);
    }

    // 해당 회원과 관련된 모든 Emitter를 지운다.
    public void deleteAllEmitterStartsWithId(String id) {
        emitters.keySet().removeIf(key -> key.startsWith(id));
    }

    // 해당 회원과 관련된 모든 이벤트를 지운다.
    public void deleteEventCacheStartsId(String id) {
        eventCache.keySet().removeIf(key -> key.startsWith(id));
    }
}