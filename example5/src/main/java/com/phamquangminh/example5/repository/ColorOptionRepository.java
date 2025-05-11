package com.phamquangminh.example5.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phamquangminh.example5.entity.ColorOption;
import java.util.Optional;

@Repository
public interface ColorOptionRepository extends JpaRepository<ColorOption, Long> {
    List<ColorOption> findByProductProductId(Long productId);

    Optional<ColorOption> findByColorName(String colorName);

}
