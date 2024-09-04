package Try_it.alarm.SseRepository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String emitterId, Object event);

    //해당 user와 관련된 모든 emitter 찾기
    Map<String, SseEmitter> findAllEmiiterStartWithByUserPk(String userPk);

    //해당 user와 관련된 모든 event 찾기
    Map<String, Object> findAllEventCacheStartWithByUserPk(String userPk);

    void deleteEmitterById(String emitterId);

    void deleteAllEmitterStartWithByUserId(String userId);
    void deleteAllEventCacheStartWithUserId(String userId);
}
