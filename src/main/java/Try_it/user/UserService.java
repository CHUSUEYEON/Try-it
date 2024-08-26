package Try_it.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity deleteUser(final Long userPk, final String password){
        UserEntity user = userRepository.findByUserPk(userPk)
           .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        if(user!= null && passwordEncoder.matches(password, user.getUserPassword())){
            userRepository.delete(user);
            return user;
        } else if(!passwordEncoder.matches(password, user.getUserPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        } else throw new RuntimeException("회원탈퇴 실패");
    }
}
