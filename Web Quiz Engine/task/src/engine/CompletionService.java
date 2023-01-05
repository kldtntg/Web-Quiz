package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompletionService {
    @Autowired
    CompletionRepository completionRepository;

    public void save(Completion completion){
        completionRepository.save(completion);
    }

    public List<Completion> getCompletionsByEmail(String email, Pageable pageable){
        return completionRepository.getCompletionsByEmail(email, pageable);
    }


    public List<Completion> getCompletionsByEmail(String email_id, Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Completion> listResult = completionRepository.getCompletionsByEmail(email_id, paging); // findALl() implicitly provided by PagingandSortingRepository

        int questionsCount = listResult.size();

        Page<Completion> pagedResult = new PageImpl<>(listResult, paging, questionsCount);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Completion>();
        }
    }

}
