package se.lexicon.ecommerceworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.lexicon.ecommerceworkshop.entity.Promotion;

import java.time.LocalDate;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("""
            SELECT promotion FROM Promotion promotion
            WHERE promotion.startDate <= :date
            AND (promotion.endDate IS NULL OR promotion.endDate >= :date)
            """)
    List<Promotion> findActiveOn(@Param("date") LocalDate date);
}
