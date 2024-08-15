package Try_it.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserId(String userId);
    UserEntity findByUserId(String userId);
    Optional<UserEntity> findByUserIdx(Long userIdx);

    @Query("SELECT u.userDeletedAt FROM UserEntity u WHERE u.userId = :userId")
    Timestamp findDeletedUserByUserId(String userId);
}
