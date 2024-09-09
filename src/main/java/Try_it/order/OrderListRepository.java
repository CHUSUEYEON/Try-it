package Try_it.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface OrderListRepository extends JpaRepository<OrderListEntity, Long> {
    OrderListEntity findByOrder_orderPk(Long orderPk);
}
