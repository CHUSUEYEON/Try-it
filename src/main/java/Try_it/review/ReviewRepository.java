package Try_it.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
