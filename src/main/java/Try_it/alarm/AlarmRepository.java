package Try_it.alarm;

import Try_it.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<AlarmEntity, Long> {
    List<AlarmEntity> findAllByUser_userPk(Long userPk);

    Optional<AlarmEntity> findByAlarmPkAndUser_UserPk(Long alarmPk, Long userPk);

    Optional<AlarmEntity> findByAlarmPk(Long alarmPk);
}
