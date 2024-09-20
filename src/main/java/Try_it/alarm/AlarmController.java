package Try_it.alarm;

import Try_it.common.dto.ResDTO;
import Try_it.common.vo.StatusCode;
import Try_it.security.TokenProvider;
import Try_it.user.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "Alarm")
@RestController
@RequestMapping("/users/notifications")
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;
    private final TokenProvider jwtTokenProvider;

    @Autowired
    public AlarmController(AlarmService alarmService, TokenProvider jwtTokenProvider) {
        this.alarmService = alarmService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Operation(summary = "SSE 세션 연결", description = "sse 연결을 위해선 'http://43.201.5.233/:8080/pages/login'에서 유저로 로그인하여, 관리자가 쪽지를 발송할 경우 화면을 확인해주세요.")
    @GetMapping(value = "/connect", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> connect(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                                              @RequestParam(value = "token") String token){
        // 토큰 검증 및 사용자 정보 추출
        String userPk = jwtTokenProvider.validateAndGetUserPk(token); // 토큰에서 userPk 추출
        log.info("userPk: {}", userPk);
        if (userPk == null || userPk.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 시 401 반환
        }


        SseEmitter sseEmitter = alarmService.connect(userPk, lastEventId);
        return ResponseEntity.ok().body(sseEmitter);
    }

    @Operation(summary = "알람 목록 조회" , description = "유저 토큰 필요")
    @GetMapping()
    public ResponseEntity<ResDTO> getAlarmsList(@AuthenticationPrincipal String userPk){
        List<AlarmEntity> alarmsList = alarmService.getAlarmsList(userPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
           .data(alarmsList)
           .message("알람 목록 조회 성공")
           .build());
    }

    @Operation(summary = "알림 읽음 상태 변경 [ 1 -> 읽음 ]으로 변경", description = "alarmPk와 토큰 필요")
    @PostMapping("/{alarmPk}")
    public ResponseEntity<ResDTO> checkIsRead(@AuthenticationPrincipal String userPk,
                                              @PathVariable Long alarmPk){
        AlarmEntity updatedAlarm = alarmService.checkIsRead(userPk, alarmPk);
        return ResponseEntity.ok().body(ResDTO.builder()
           .statusCode(StatusCode.OK)
            .data(updatedAlarm)
           .message("읽음 상태로 변경 성공")
           .build());
    }
}
