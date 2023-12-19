package Scripts;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import Scripts.Menu;

public class Loggin {
	
	private static void createTableIfNotExists() {
	    String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
	            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
	            "username TEXT NOT NULL," +
	            "email TEXT NOT NULL," +
	            "password TEXT NOT NULL" +
	            ")";

	    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datenbank.db")) {
	        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
	            statement.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    public static void main(String[] args) {
    	createTableIfNotExists();
    	
        SwingUtilities.invokeLater(Loggin::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("SE Learning Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JPanel panel = new JPanel(null);
        frame.getContentPane().add(panel);

        // Labels und Textfelder
        String[] labels = {"Nutzername:", "E-Mail:", "Passwort:"};
        JTextField[] textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JTextField textField;
            if (i == 2) {
                textField = new JPasswordField(20);
            } else {
                textField = new JTextField(20);
            }

            textField.setBounds(100, 20 + i * 40, 150, 25);
            addPlaceholder(textField, labels[i]);
            textFields[i] = textField;
            panel.add(textField);
        }
        
        

        JButton registerButton = new JButton("Registrieren");
        registerButton.setBounds(50, 200, 100, 25);
        panel.add(registerButton);

        JButton loginButton = new JButton("Anmelden");
        loginButton.setBounds(170, 200, 100, 25);
        panel.add(loginButton);

        // Hintergrundbild
        try {
            Image backgroundImage = ImageIO.read(new File("./src/Assets/LogginUI.jpg"));
            Image scaledImage = backgroundImage.getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);

            JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
            backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
            panel.add(backgroundLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ActionListener für die Registrieren-Taste
        registerButton.addActionListener(e -> {
            String[] inputValues = new String[labels.length];
            for (int i = 0; i < labels.length; i++) {
                inputValues[i] = textFields[i].getText();
            }

            if (isRegistrationValid(inputValues)) {
                insertDataIntoDatabase(inputValues);
                JOptionPane.showMessageDialog(frame, "Registrierung erfolgreich.");
            } else {
                JOptionPane.showMessageDialog(frame, "Ungültige Registrierung.");
            }
        });

        // ActionListener für die Anmelden-Taste
        loginButton.addActionListener(e -> {
            String[] inputValues = new String[labels.length];
            for (int i = 0; i < labels.length; i++) {
                inputValues[i] = textFields[i].getText();
            }

            if (isLoginValid(inputValues)) {
                JOptionPane.showMessageDialog(frame, "Anmeldung erfolgreich.");
                frame.dispose(); // Schließt das Anmeldefenster
                Menu menu = new Menu(); // Erstellt ein neues Menu-Objekt
                menu.setVisible(true); // Zeigt das Menü an
            } else {
                JOptionPane.showMessageDialog(frame, "Ungültige Anmeldung.");
            }
        });

        // Größe und Position der Eingabefelder und Buttons
        int fieldWidth = 300;
        int fieldHeight = 30;

        for (int i = 0; i < labels.length; i++) {
            textFields[i].setBounds((screenSize.width - fieldWidth) / 2, screenSize.height / 2 + i * fieldHeight, fieldWidth, fieldHeight);
        }

        registerButton.setBounds((screenSize.width - 250) / 2, screenSize.height / 2 + 3 * fieldHeight, 100, 25);
        loginButton.setBounds((screenSize.width + 50) / 2, screenSize.height / 2 + 3 * fieldHeight, 100, 25);

        frame.setUndecorated(true);
        frame.setSize(screenSize);
        frame.setVisible(true);
    }

    private static boolean isRegistrationValid(String[] values) {
        String username = values[0];

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datenbank.db")) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return !resultSet.next(); // Gibt true zurück, wenn der Benutzername noch nicht existiert
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static void insertDataIntoDatabase(String[] values) {
        String email = values[1];
        String password = values[2];
        String username = values[0];

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datenbank.db")) {
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static boolean isLoginValid(String[] values) {
        String usernameOrEmail = values[0];
        String password = values[2];

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:Datenbank.db")) {
            // Prüfung, ob der Nutzername oder die E-Mail und das Passwort übereinstimmen
            String query = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, usernameOrEmail);
                statement.setString(2, usernameOrEmail);
                statement.setString(3, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); // Wenn es Ergebnisse gibt, war die Anmeldung erfolgreich
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    // Methode zum Hinzufügen von Platzhaltertext zu einem Textfeld
    private static void addPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
    
    
    
}
