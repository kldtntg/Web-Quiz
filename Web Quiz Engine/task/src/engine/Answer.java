package engine;

import javax.validation.constraints.NotBlank;

public class Answer {

    Integer[] answer;


    public Answer(){};

    public void setAnswer(Integer[] answer) {
        this.answer = answer;
    }

    public Integer[] getAnswer() {
        return answer;
    }
}
