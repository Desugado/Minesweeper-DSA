/*  Name: Nguyễn Công Tuấn Vũ
    ID: ITITWE22160
    Name: Nguyễn Minh Trí
    ID: ITITIU22164
    Purpose: The MenuBar class extends JMenuBar and serves as the menu bar 
    for the Minesweeper game. It provides a set of menu items that enable 
    players to manage their game sessions, including starting new games, 
    changing difficulty levels, restarting the game, and exiting the application.
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar extends JMenuBar{
    private MinesweeperGame mg;
    private JMenu menu;

    private JMenuItem newgame = new JMenuItem("New Game");
    private JMenuItem easy = new JMenuItem("Easy");
    private JMenuItem med = new JMenuItem("Medium");
    private JMenuItem hard = new JMenuItem("Hard");
    private JMenuItem exit = new JMenuItem("Exit");

    public MenuBar(MinesweeperGame mg){
        this.mg = mg;

        menu = new JMenu("Menu");
        
        newgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Object[] options = { "Yes", "No" };
                int choice = JOptionPane.showOptionDialog(
                    null,
                    "Do you want to restart the game?",
                    "Restart",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                if(choice == JOptionPane.YES_OPTION){
                    mg.resetGame();
                }
            }
        });

        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MinesweeperGame(5, 5, 5, 1);
                mg.frame.dispose();
            }
        });

        med.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MinesweeperGame(10, 10, 40, 3);
                mg.frame.dispose();
            }
        });

        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MinesweeperGame(20, 20, 100, 5);
                mg.frame.dispose();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Launcher();
                mg.frame.dispose();
            }
        });

        menu.add(newgame);
        menu.addSeparator();
        menu.add(easy);
        menu.add(med);
        menu.add(hard);
        menu.addSeparator();
        menu.add(exit);

        this.add(menu);
    }
}