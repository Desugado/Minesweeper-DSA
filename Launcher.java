/*  Name: Nguyễn Công Tuấn Vũ
    ID: ITITWE22160
    Name: Nguyễn Minh Trí
    ID: ITITIU22164
    Purpose: Set up and launch a Minesweeper game application 
    using Java's Swing framework for the graphical user interface
*/

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Launcher {
    private static final String START_PANEL = "start";
    private static final String DIFFICULTY_PANEL = "difficulty";

    private LeaderBoard lb;

    private JFrame mainFrame;
    private URL imageURL;
    private ImageIcon backgroundImage;
    private JLabel backgroundLabel;
    private JPanel cardPanel;
    private JButton startButton = new JButton("Start Game");
    private JButton exButton = new JButton("Exit Game");
    private JButton LBButton = new JButton("Leader Board");
    private JPanel startPanel;
    private GridBagConstraints gbc = new GridBagConstraints();

    public Launcher() {
        lb = new LeaderBoard();

        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Minesweeper");

            // Load background image
            imageURL = Launcher.class.getResource("img/background.jpg");
            backgroundImage = new ImageIcon(imageURL);

            // Create a JLabel with the background image
            backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setLayout(new BorderLayout());

            cardPanel = new JPanel(new CardLayout());

            startButton.setPreferredSize(new Dimension(200, 60));
            exButton.setPreferredSize(new Dimension(200, 60));
            LBButton.setPreferredSize(new Dimension(200, 60));
            
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                    cardLayout.show(cardPanel, DIFFICULTY_PANEL);
                }
            });

            exButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    Object[] options = { "Yes", "No" };
                    int choice = JOptionPane.showOptionDialog(
                        null,
                        "Do you want to exit the game?",
                        "Restart",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );
                    if(choice == JOptionPane.YES_OPTION){
                        mainFrame.dispose();
                    }
                }
            });

            LBButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    lb.showLeaderBoard();
                }
            });

            startPanel = new JPanel();
            startPanel.setLayout(new BorderLayout(10, 10));
            startPanel.add(startButton, BorderLayout.NORTH);
            startPanel.add(LBButton, BorderLayout.CENTER);
            startPanel.add(exButton, BorderLayout.SOUTH);

            cardPanel.add(startPanel, START_PANEL);
            cardPanel.add(new DifficultyPanel(cardPanel), DIFFICULTY_PANEL);
            cardPanel.setBackground(null);
            cardPanel.setOpaque(false);

            // Add the background label as the content pane of the main frame
            mainFrame.setContentPane(backgroundLabel);

            // Set the layout for the background label
            backgroundLabel.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            backgroundLabel.add(cardPanel, gbc);

            mainFrame.setResizable(false);
            mainFrame.setSize(626, 626);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }
}