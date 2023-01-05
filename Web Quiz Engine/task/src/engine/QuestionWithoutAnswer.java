package engine;

import java.util.List;

public class QuestionWithoutAnswer {
    Long id;
    public String title;
    public String text;
    public List<String> options;

    public QuestionWithoutAnswer(Long id, String title, String text, List<String> options){
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;

    }

    public List<String> getOptions() {
        return options;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}


