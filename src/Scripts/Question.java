package Scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    private String questionText;
    private List<String> answers;
    private List<Integer> correctAnswers;

    public Question(String questionText, List<String> answers, List<Integer> correctAnswers) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }
    
    private static List<Question> getSampleQuestions(String difficulty) {
        List<Question> questions = new ArrayList<>();

        switch (difficulty) {
            case "Leicht":
                questions.add(new Question("Was ist 2 + 2?", Arrays.asList("3", "4", "5"), Arrays.asList(1)));
                questions.add(new Question("Hauptstadt von Deutschland?", Arrays.asList("Berlin", "München", "Hamburg"), Arrays.asList(0)));
                break;
            case "Mittel":
                questions.add(new Question("5 * 5?", Arrays.asList("10", "25", "30"), Arrays.asList(1)));
                questions.add(new Question("Hauptstadt von Frankreich?", Arrays.asList("Lyon", "Paris", "Marseille"), Arrays.asList(1)));
                break;
            case "Schwer":
                questions.add(new Question("Wurzel aus 144?", Arrays.asList("12", "14", "16"), Arrays.asList(0)));
                questions.add(new Question("Wer schrieb Faust?", Arrays.asList("Goethe", "Schiller", "Lessing"), Arrays.asList(0)));
                break;
        }

        return questions;
    }


    // Getter-Methoden
    public String getQuestionText() {
        return questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }
    
    private static List<Question> loadQuestionsForDifficulty(String difficulty) {
        // Hier würden Sie normalerweise Fragen aus einer externen Quelle laden
        return getSampleQuestions(difficulty);
    }

}
