package Try_it.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByUser_userPk(Long userPk);
    Page<CartEntity> findAllByUser_userPk(Long userPk, Pageable pageable);
}
