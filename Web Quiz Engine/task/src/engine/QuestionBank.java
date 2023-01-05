package engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


@RestController
public class QuestionBank {
    static public ConcurrentHashMap<Long, Question> questionbank = new ConcurrentHashMap<>();

    @Autowired
    QuestionService questionService;

    @Autowired
    UserDetailsServiceImpl userService;

    @Autowired
    CompletionService completionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Long count;




    @GetMapping("/api/quizzes/{id}")
    public String getQuestionById(@PathVariable Long id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        //Question q = questionbank.get(id);
        Question q = questionService.findQuestionById(id);


        if (q == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Quiz not found");
        }
        QuestionWithoutAnswer q2 = new QuestionWithoutAnswer(q.getId(), q.getTitle(), q.getText(), q.getOptions());

        return objectMapper.writeValueAsString(q2); //get the question from the database and return the question  (EXCLUDING the answer) in JSON format
    }

//    @GetMapping("/api/quizzes")
//    public String getAllQuestions() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//
//        List<Question> list = questionService.getAllQuestions(); // get all the available questions store in the db
//        Set<QuestionWithoutAnswer> set = new LinkedHashSet<>();
//        for (Question q : list){
//            set.add(new QuestionWithoutAnswer(q.getId(), q.getTitle(), q.getText(), q.getOptions()));
//        }
//
//        return objectMapper.writeValueAsString(set.toArray());
//
//    }

    @GetMapping("/api/quizzes")
    public String getAllQuestions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) throws JsonProcessingException {
        List<Question> list = questionService.getAllQuestions(page, pageSize, sortBy);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new ResponseEntity<List<Question>>(list, new HttpHeaders(), HttpStatus.OK));

    }

    @GetMapping("/api/quizzes/completed")
    public String getAllCompletedQuestions( //get completed quiz questions by a user
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "completionTime") String sortBy,
            @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Completion> list = completionService.getCompletionsByEmail(userDetails.getUsername(), page, pageSize, sortBy);

        return objectMapper.writeValueAsString(new ResponseEntity<List<Completion>>(list, new HttpHeaders(), HttpStatus.OK));
//        return new ResponseEntity<List<Completion>>(list, new HttpHeaders(), HttpStatus.OK);
    }



    @PostMapping("/api/quizzes/{id}/solve")
    public String solveQuestion(@PathVariable Long id, @RequestBody Answer answer, @AuthenticationPrincipal UserDetails userDetails ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Question q = questionService.findQuestionById(id);

        if (q == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Quiz not found");
        }

        else {

            Feedback fb;
            if (q.getAnswer() == null){ // when the true answer in the db is null,
                if (answer.getAnswer() == null || Arrays.equals(answer.getAnswer(), new Integer[]{})){ // return true if the user provides an empty answer, null, or an empty array []
                    fb = new Feedback(true, "Congratulations");

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String timestamp = dtf.format(now);
                    completionService.save(new Completion(userDetails.getUsername(), q.getId(), timestamp));


                }
                else {
                    fb = new Feedback(false, "Wrong answer");
                }

                return objectMapper.writeValueAsString(fb);

            }

            // else, if the true answer in the db is not null:

            List<Integer> right_answers = new ArrayList<>(q.getAnswer());
            List<Integer> provided_answers = new ArrayList<>(Arrays.asList(answer.getAnswer()));

            Collections.sort(right_answers);
            Collections.sort(provided_answers);


            if (right_answers.equals(provided_answers)){

                fb = new Feedback(true, "Congratulations");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String timestamp = dtf.format(now);
                completionService.save(new Completion(userDetails.getUsername(), q.getId(), timestamp));

            } else {
                fb = new Feedback(false, "Wrong answer");
            }

            return objectMapper.writeValueAsString(fb);
        }
    }

    @PostMapping("/api/quizzes")
    public Question createQuestion(@RequestBody @Valid Question question, @AuthenticationPrincipal UserDetails details){

        if (count == null){
             count = questionService.count(); // get the number of rows of the database (the number of questions currently in database)
        }

        String email = details.getUsername();
        question.setAuthor(email); // get username (email) from details (the user who made this request) and set that as author variable of question

        question.setId(count); // count will reset if application is terminated. This likely causes some questions have similar ids. To be fixed

        questionService.save(question); // save the question with populated id to database

        questionbank.put(count, question);// save the question to the memory questionbank as a safeguard - might be useful
        return questionbank.get(count++); // return from questionbank (hashmap) instead of from database cuz it might be more efficient this way

    }

    @PostMapping("/api/register")
    public void addUser(@Valid @RequestBody  User new_user){
        if (!userService.userExist(new_user.getEmail())){

            System.out.println(new_user.getPassword() + " Email: " +  new_user.getEmail());
            new_user.setPassword(passwordEncoder.encode(new_user.getPassword())); // encrypt the password before saving to db
            userService.save(new_user);
            System.out.println("User created: " + new_user.getEmail() + " ,encrypted password: " + new_user.getPassword());
    }

    else{
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email taken");
        }
    }


    @DeleteMapping("/api/quizzes/{id}")
    @Transactional

    public ResponseEntity<String> deleteQuestion(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();


         if (!questionService.existsById(id)){
             return new ResponseEntity<>("Quiz not found", HttpStatus.NOT_FOUND); // because @Transactional, return a response entity instead of throwing exception
         } else { // the question does exist
             Question question = questionService.findQuestionById(id);
             if (question.getAuthor().equals(email)){

                 System.out.println(questionService.count());
                 questionService.deleteById(id);
                 System.out.println(questionService.count());

                 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
             }
             else {
                 return new ResponseEntity<>(HttpStatus.FORBIDDEN);
             }

         }
    }
}


