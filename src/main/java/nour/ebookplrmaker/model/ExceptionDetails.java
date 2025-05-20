package nour.ebookplrmaker.model;



import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExceptionDetails {
    private String message;
    private String error;

    @Override
    public String toString() {
        return "ExceptionDetails{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    public ExceptionDetails(String message,String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
