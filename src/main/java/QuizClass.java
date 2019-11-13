public class QuizClass {
    private String question;
    private String[] answers;

    public QuizClass(String question, String[] answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }
}
