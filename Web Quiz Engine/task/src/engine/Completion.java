package engine;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "completion")
public class Completion {
    @Id
    String email;

    @Column(name = "ids")
    Long question_id;


    @Column(name = "completedAt")
    String completionTime;


    public Completion(){};

    public Completion(String email, Long question_id, String completionTime){
        this.email = email;
        this.question_id = question_id;
        this.completionTime = completionTime;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompletionTime(String completion_time) {
        this.completionTime = completion_time;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public String getEmail() {
        return email;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public Long getQuestion_id() {
        return question_id;
    }
}

