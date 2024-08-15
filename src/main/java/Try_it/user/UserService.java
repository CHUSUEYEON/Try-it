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

    public UserEntity deleteUser(final Long userIdx, final String password){
        UserEntity user = userRepository.findByUserIdx(userIdx)
           .orElseThrow(() -> new RuntimeException("로그인을 해주세요."));

        if(user!= null && passwordEncoder.matches(password, user.getUserPassword())){
            userRepository.delete(user);
            return user;
        } else if(!passwordEncoder.matches(password, user.getUserPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        } else throw new RuntimeException("회원탈퇴 실패");
    }
}
