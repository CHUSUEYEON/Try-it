package Try_it.auth;

import Try_it.config.EmailConfig;
import Try_it.user.UserDTO;
import Try_it.user.UserEntity;
import Try_it.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {
    private final Integer EXPIRATION_IN_MINUTES = 5;
    @Value("${spring.mail.username")
    private String serviceEmail;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, VerificationCodeRepository verificationCodeRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeRepository = verificationCodeRepository;
        this.mailSender = mailSender;
    }

    public UserEntity createUser(final UserEntity userEntity){
        checkId(userEntity);

        return userRepository.save(userEntity);
    }

    public UserDTO checkId(final UserEntity userEntity){
        final String userId = userEntity.getUserId();

        if(userRepository.existsByUserId(userId)){
            throw new RuntimeException("userId already exists");
        }
        return UserDTO.builder().userId(userId).build();
    }

    public UserEntity getUserByCredentials(final String userId, final String password){
        UserEntity user = userRepository.findByUserId(userId);
        Timestamp deletedUser = userRepository.findDeletedUserByUserId(userId);

        if (deletedUser != null) {
            throw new RuntimeException("탈퇴한 회원입니다.");
        } else if(user != null && passwordEncoder.matches(password, user.getUserPassword())){
            return UserEntity.builder()
                .userId(userId)
                .userName(user.getUserName())
                .userGender(user.getUserGender())
                .userAddress(user.getUserAddress())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .userIdx(user.getUserIdx())
                .userCreatedAt(user.getUserCreatedAt())
                .userUpdatedAt(user.getUserUpdatedAt())
                .userIsAdmin(user.getUserIsAdmin())

                .build();
        } else if(user == null){
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        } else if(!passwordEncoder.matches(password, user.getUserPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return null;
    }

    public void sendVerificationCode(String to, LocalDateTime sentAt){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(serviceEmail);
        mailMessage.setTo(to);
        mailMessage.setSubject(String.format("Email Verificaiton For %s", to));

        VerificationCode verificationCode = generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);

        String text = verificationCode.generateCodeMessage("email");
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }

    public VerificationCode sendPhoneVerificationCode(LocalDateTime sentAt){
        VerificationCode verificationCode = generateVerificationCode(sentAt);
        verificationCodeRepository.save(verificationCode);
        String code = verificationCode.generateCodeMessage("sms");
        return VerificationCode.builder()
            .code(code)
            .createAt(sentAt)
            .expirationTimeInMinutes(EXPIRATION_IN_MINUTES)
            .build();
    }

    public VerificationCode generateVerificationCode(LocalDateTime sentAt){
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        String codeString = Integer.toString(code);
//        String code = UUID.randomUUID().toString(); // 너무 길어서 숫자 6개로 바꿈
        return VerificationCode.builder()
            .code(codeString)
            .createAt(sentAt)
            .expirationTimeInMinutes(EXPIRATION_IN_MINUTES)
            .build();
    }

    public void verifyCode(final String code, LocalDateTime verifiedAt){
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
            .orElseThrow(()-> new RuntimeException("유효하지 않은 코드입니다."));

        if(verificationCode.isExpired(verifiedAt)){
            throw new RuntimeException("인증 기간이 만료된 코드입니다.");
        }

        verificationCodeRepository.remove(verificationCode);
    }
}
