package Try_it.auth;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class VerificationCodeRepository {
    private final Map<String, VerificationCode> repository = new ConcurrentHashMap<>();

    public VerificationCode save(VerificationCode verificationCode){
        if (verificationCode.getCode() == null) {
            throw new IllegalArgumentException("Verification code cannot be null");
        }
        return repository.put(verificationCode.getCode(), verificationCode);
    }

    public Optional<VerificationCode> findByCode(String code){

        if (code == null) {
            return Optional.empty(); // 또는 Optional.ofNullable(null)
        }
        return Optional.ofNullable(repository.get(code));
    }

    public void remove(VerificationCode verificationCode){
        if (verificationCode.getCode() == null) {
            throw new IllegalArgumentException("Verification code cannot be null");
        }
        repository.remove(verificationCode.getCode());
    }
}
