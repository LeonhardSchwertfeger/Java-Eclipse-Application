package Scripts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

    CardLayout cardLayout = new CardLayout();

    public Menu() {
        setTitle("SE Learning Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Setzt das Fenster in den Vollbildmodus
        setUndecorated(true); // Entfernt die Fensterdekoration für echten Vollbildmodus
        setLayout(cardLayout);
        getContentPane().setBackground(Color.DARK_GRAY);
        
        JPanel mainMenuPanel = createMainMenuPanel();
        JPanel settingsPanel = createSettingsPanel(); // Implementieren Sie diese Methode entsprechend
        JPanel instructionsPanel = createInstructionsPanel(); // Implementieren Sie diese Methode entsprechend
        JPanel creditsPanel = createCreditsPanel(); // Implementieren Sie diese Methode entsprechend

        add(mainMenuPanel, "MainMenu");
        add(settingsPanel, "Settings");
        add(instructionsPanel, "Instructions");
        add(creditsPanel, "Credits");
    }

    private JPanel createMainMenuPanel() {
        JPanel mainMenuPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        mainMenuPanel.setOpaque(false);

        // Icons und Buttons hier anpassen
        JButton playButton = new JButton("Play", new ImageIcon("./src/Assets/4.png"));
        playButton.addActionListener(e -> playGame());
        
        JButton settingsButton = new JButton("Settings", new ImageIcon("./src/Assets/3.png"));
        settingsButton.addActionListener(e -> showCard("Settings"));
        
        JButton instructionsButton = new JButton("Instructions", new ImageIcon("./src/Assets/2.png"));
        instructionsButton.addActionListener(e -> showCard("Instructions"));
        
        JButton creditsButton = new JButton("Credits", new ImageIcon("./src/Assets/1.png"));
        creditsButton.addActionListener(e -> showCard("Credits"));

        mainMenuPanel.add(playButton);
        mainMenuPanel.add(settingsButton);
        mainMenuPanel.add(instructionsButton);
        mainMenuPanel.add(creditsButton);
        
        return mainMenuPanel;
    }

    private void showCard(String cardName) {
        cardLayout.show(getContentPane(), cardName);
    }

    private void playGame() {
        // Starten Sie hier Ihr Spiel
    	dispose();
    	
    	Play.showGameModes();
    }

    // Implementieren Sie die restlichen Methoden für Settings, Instructions, Credits

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
    
    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setOpaque(false);
        // Fügen Sie hier Komponenten für die Einstellungen hinzu, z.B. Checkboxen, Dropdown-Menüs, etc.
        return settingsPanel;
    }

    private JPanel createInstructionsPanel() {
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setOpaque(false);
        // Fügen Sie hier Textfelder oder Dokumente für die Spielanweisungen hinzu
        return instructionsPanel;
    }

    private JPanel createCreditsPanel() {
        JPanel creditsPanel = new JPanel();
        creditsPanel.setOpaque(false);
        // Fügen Sie hier Informationen zu den Entwicklern, Mitwirkenden, etc. hinzu
        return creditsPanel;
    }

}
