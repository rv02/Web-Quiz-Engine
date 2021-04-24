package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends
        JpaRepository<Quiz, Integer> {
    @Query(value = "SELECT * FROM Quizzes",
            countQuery = "SELECT COUNT(*) FROM QUIZZES",
            nativeQuery = true)
    Page<Quiz> findAllQuizzesWithPagination(Pageable pageable);
}

