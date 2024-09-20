package Try_it.order;

import Try_it.user.UserEntity;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
//    OrderEntity findByUser_userPk(Long userPk);
    List<OrderEntity> findAllByUser(UserEntity user);
}
