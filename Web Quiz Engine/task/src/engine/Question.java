package engine;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "question")
public class Question {


    @Id // could use @GeneratedValue here
    Long id;

    @NotBlank(message = "Empty title not allowed")
    @Column
    private String title;

    @NotBlank(message = "Empty text not allowed")
    @Column
    private String text;

    @NotEmpty(message = "Please make sure there are at least 2 options")
    @Size(min = 2, message = "Please make sure there are at least 2 options")
    @Column
    @ElementCollection
    private List<String> options;

    @ElementCollection
    @Column
    private List<Integer> answer;

    @Column
    private String author; // store the unique email of the author of this quiz question


    public Question(){};




//    public Question(){
//        this.title = "The Java Logo";
//        this.text =  "What is depicted on the Java logo?";
//        this.options =  new String[]{"Robot","Tea leaf","Cup of coffee","Bug"};
//    };

    public Question(String title, String text, List<String> options, List<Integer> answer){

        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;

    }


    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author){
        this.author = author;
    }

     public String getAuthor(){
        return this.author;
     }
}
