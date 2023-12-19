package Scripts;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



//Play.java
public class Play {
	
	
	public static void showGameModes() {
        JFrame gameModeFrame = new JFrame("Spielmodus");
        gameModeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameModeFrame.setUndecorated(true);
        gameModeFrame.setLayout(new BorderLayout());

        gameModeFrame.setContentPane(new BackgroundPanel());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout(2, 1));

        JButton quizButton = new JButton("Quiz");
        quizButton.addActionListener(e -> {
            gameModeFrame.dispose(); // Schließt das Auswahl-Fenster
            Quiz.showDifficultySelection(); // Öffnet das Schwierigkeitsauswahl-Fenster
        });

        JButton storyButton = new JButton("Story");
        storyButton.addActionListener(e -> {
            gameModeFrame.dispose(); // Optional: Schließt das Auswahl-Fenster
            Story.startStoryMode(); // Startet den Story-Modus
        });

        centerPanel.add(quizButton);
        centerPanel.add(storyButton);

        gameModeFrame.add(centerPanel, BorderLayout.CENTER);
        gameModeFrame.setVisible(true);
    }
}
