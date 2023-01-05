package engine;

public class Feedback {
    boolean success;
    String feedback;

    Feedback(boolean success, String feedback){
        this.success = success;
        this.feedback = feedback;
    };
////// getters and setters MANDATORY for ObjectMapper to read and serialize this class
    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
