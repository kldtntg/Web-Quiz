package engine;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> { //extension of CrudRepository<Question, Long>
    Question findQuestionById(Long id); // Spring Data JPA provides a mechanism called "query creation from method names"
    // that allows you to define repository methods by simply providing the method name and a few parameters,
    // and Spring will automatically generate the necessary SQL queries to implement the method.
    // SO no need to implement this manually, Spring Data does it for ya

    List<Question> findAllByOrderByIdAsc();

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Question> findQuestionsById(long id, Pageable pageable);

    }
