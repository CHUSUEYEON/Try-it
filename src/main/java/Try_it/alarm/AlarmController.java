package Try_it.alarm;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.user.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE 연결")
@RestController
@RequestMapping("/users/notifications")
public class AlarmController {

    private final AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }


    @Operation(summary = "SSE 세션 연결")
    @GetMapping(value = "/connect", produces = "text/event-stream")
    public SseEmitter connect(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                                              @AuthenticationPrincipal String userPk){
        SseEmitter sseEmitter = alarmService.connect(userPk, lastEventId);
        return sseEmitter;
    }
}
