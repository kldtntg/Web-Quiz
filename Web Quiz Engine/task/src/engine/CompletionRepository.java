package engine;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletionRepository extends PagingAndSortingRepository<Completion, String> {
    List<Completion> getCompletionsByEmail(String email, Pageable pageable);


}
