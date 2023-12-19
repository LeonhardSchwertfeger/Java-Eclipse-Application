package Scripts;

//BackgroundPanel.java
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
 private Image backgroundImage;

 public BackgroundPanel() {
     try {
         backgroundImage = new ImageIcon("./path/to/background.jpg").getImage();
     } catch (Exception e) {
         // Behandlung fehlgeschlagener Ladevorgänge
     }
 }

 @Override
 protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
 }
}

