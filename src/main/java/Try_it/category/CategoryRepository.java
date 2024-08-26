package Try_it.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoriesEntity, Long> {
    Optional<CategoriesEntity> findByCategoryName(String categoryName);
}
