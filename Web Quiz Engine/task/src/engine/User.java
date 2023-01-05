package engine;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;


@Entity
@Table(name = "user")
public class User {

    @Pattern(regexp = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}\\.[a-z]{2,}$")
    @Email
    @NotBlank
    @Column
    @Id
    private String email;

    @Size(min = 5)
    @NotBlank
    @Column
    private String password;

//    @Column
//    @ElementCollection
//    private Map<Question, String> completedQuestions; //String represents the timestamp


    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public User() {};

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public Map<Question, String> getCompletedQuestions() {
//        return completedQuestions;
//    }
////
//    public void addCompletedQuestion(Question question, String date) {
//        this.completedQuestions.put(question, date);
//    }
}
