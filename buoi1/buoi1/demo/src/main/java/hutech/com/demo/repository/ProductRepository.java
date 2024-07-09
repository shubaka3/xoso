package hutech.com.demo.repository;

import hutech.com.demo.model.Product;
import hutech.com.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:price IS NULL OR p.price = :price)")
    List<Product> searchProducts(@Param("name") String name,
                                 @Param("price") BigDecimal price);

}

