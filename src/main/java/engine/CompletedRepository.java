package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompletedRepository  extends JpaRepository<Completed, Integer> {
    @Query(value = "SELECT * FROM Completed c WHERE c.user = :id ORDER BY c.Time DESC",
    countQuery = "SELECT COUNT(*) FROM Completed c WHERE c.user = :id",
            nativeQuery = true)
    Page<Completed> findAllCompletedPaginated(Pageable pageable, @Param("id") int id);
}
