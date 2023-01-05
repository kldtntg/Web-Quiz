package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question findQuestionById(Long id) {
        return questionRepository.findQuestionById(id);
    }

    public Question save(Question toSave) {
        return questionRepository.save(toSave);
    }

    public List<Question> getAllQuestions(){
        return questionRepository.findAllByOrderByIdAsc();
    }

    public Long count(){
        return questionRepository.count();
    }

    public void deleteById(Long id){
        questionRepository.deleteById(id);
    }

    boolean existsById(Long id){
        return questionRepository.existsById(id);
    }


    public List<Question> getAllQuestions(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Question> pagedResult = questionRepository.findAll(paging); //findAll auto-provided inside QuestionRepository

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Question>();
        }
    }
}
