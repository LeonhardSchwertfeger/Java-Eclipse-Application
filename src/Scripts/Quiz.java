package Scripts;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Scripts.Question;

public class Quiz {
    private JFrame frame;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String username; // Benutzername des Spielers

    public Quiz(List<Question> questions, String username) {
        this.questions = questions;
        this.username = username;
        frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        showNextQuestion();
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(createQuestionPanel(currentQuestion), BorderLayout.CENTER);
            frame.getContentPane().validate();
            frame.getContentPane().repaint();
        } else {
            calculateScore();
            updateDatabaseWithScore();
            showResults();
        }
    }

    private void calculateScore() {
        // Berechnen der Punkte basierend auf der Anzahl der richtigen Antworten
        int totalBits = score * 10; // 10 Bits pro richtige Antwort
        updateDatabaseWithScore();
    }

  

    private void showResults() {
        // Anzeigen der Ergebnisse in einem neuen Fenster
        JFrame resultsFrame = new JFrame("Quiz Ergebnisse");
        resultsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        resultsFrame.setUndecorated(true);
        resultsFrame.setLayout(new BorderLayout());

        JTextArea resultsText = new JTextArea();
        resultsText.setText("Richtige Antworten: " + score + " von " + questions.size() + "\nErreichte Bits: " + (score * 10));
        resultsFrame.add(resultsText, BorderLayout.CENTER);

        JButton closeButton = new JButton("Schließen");
        closeButton.addActionListener(e -> resultsFrame.dispose());
        resultsFrame.add(closeButton, BorderLayout.SOUTH);

        resultsFrame.setVisible(true);
    }
    
    private JPanel createQuestionPanel(Question question) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        
        JLabel questionLabel = new JLabel(question.getQuestionText());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        
        for (int i = 0; i < question.getAnswers().size(); i++) {
            JRadioButton answerButton = new JRadioButton(question.getAnswers().get(i));
            answerButton.setActionCommand(String.valueOf(i));
            group.add(answerButton);
            answerPanel.add(answerButton);
        }
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener((ActionEvent e) -> {
            int selectedAnswerIndex = Integer.parseInt(group.getSelection().getActionCommand());
            if (question.getCorrectAnswers().contains(selectedAnswerIndex)) {
                score++;
            }
            currentQuestionIndex++;
            showNextQuestion();
        });
        
        questionPanel.add(answerPanel, BorderLayout.CENTER);
        questionPanel.add(submitButton, BorderLayout.SOUTH);
        
        return questionPanel;
    }
    
    private void showScore() {
        JOptionPane.showMessageDialog(frame, "Your score is: " + score + " out of " + questions.size());
        frame.dispose();
    }
    
    public void start() {
        frame.setVisible(true);
    }
    
    public static void showDifficultySelection() {
        String[] difficulties = { "Leicht", "Mittel", "Schwer" };
        String selectedDifficulty = (String) JOptionPane.showInputDialog(
            null,
            "Wähle den Schwierigkeitsgrad:",
            "Schwierigkeitsauswahl",
            JOptionPane.QUESTION_MESSAGE,
            null,
            difficulties,
            difficulties[0]);

        if (selectedDifficulty != null) {
            // Laden Sie die Fragen für den ausgewählten Schwierigkeitsgrad
            List<Question> questions = loadQuestionsForDifficulty(selectedDifficulty);
            Quiz quiz = new Quiz(questions, "Benutzername");
            quiz.start();
        }
    }
    
    private static List<Question> loadQuestionsForDifficulty(String difficulty) {
        List<Question> questions = new ArrayList<>();
        // Implementieren Sie das Laden der Fragen
        return questions;
    }
    
    private void updateDatabaseWithScore() {
        int newBits = score * 10; // 10 Bits pro richtige Antwort
        String newBadge = calculateBadge(newBits);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datenbank.db")) {
            String updateSQL = "UPDATE users SET bits = bits + ?, badge = ? WHERE username = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
                pstmt.setInt(1, newBits);
                pstmt.setString(2, newBadge);
                pstmt.setString(3, username);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String calculateBadge(int newBits) {
        int totalBits = getTotalBits() + newBits;
        if (totalBits >= 10000) {
            return "Senior Engineer";
        } else if (totalBits >= 5000) {
            return "Mid-level Engineer";
        } else if (totalBits >= 1000) {
            return "Junior Engineer";
        } else {
            return "Anfänger";
        }
    }

    private int getTotalBits() {
        int totalBits = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datenbank.db")) {
            String query = "SELECT bits FROM users WHERE username = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        totalBits = rs.getInt("bits");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalBits;
    }

}
