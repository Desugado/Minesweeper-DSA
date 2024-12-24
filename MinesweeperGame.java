/*  Name: Nguyễn Công Tuấn Vũ
    ID: ITITWE22160
    Name: Nguyễn Minh Trí
    ID: ITITIU22164
    Purpose: The MinesweeperGame class manages the state and behavior 
    of the Minesweeper game. It handles the setup of the game board, 
    manages user interactions, and implements the game logic such as 
    revealing tiles, counting mines, and managing game state transitions 
    like win and game over conditions.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MinesweeperGame{

    private Random random = new Random();

    public JFrame frame = new JFrame("Minesweeper");
    private JLabel textLabel = new JLabel();
    private JPanel textPanel = new JPanel();
    private JPanel boardPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    
    public int numRows;
    public int numCols;
    public int minesRemaining;
    public int hintcount;
    public int mineCount;
    public MineTile[][] board;
    public ArrayList<MineTile> mineList;
    public int tilesClicked = 0;
    public boolean gameOver = false;
    public boolean firstClick = true;

    private MenuBar menubar;
    private watch tiWatch;
    private LeaderBoard lb;
    private hint hint;


    public MinesweeperGame(int numRows, int numCols, int mineCount, int hintcount) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.mineCount = mineCount;
        this.hintcount = hintcount;
        board = new MineTile[numRows][numCols];

        if(numRows == 20 && numCols == 20){
            frame.setSize(700, 700);
        }else{
            frame.setSize(400, 400);
        }
        
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(true);

        textLabel.setFont(new Font("Arial", Font.BOLD, 15));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Booms Left: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols, -1, -1));
        frame.add(boardPanel);
        
        menubar = new MenuBar(this);
        tiWatch = new watch(this);
        
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(textPanel, BorderLayout.WEST);
        controlPanel.add(tiWatch, BorderLayout.EAST);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }

                        MineTile tile = (MineTile) e.getSource();

                        if (firstClick) {
                            handleFirstClick(tile.r, tile.c, e.getButton());
                            tiWatch.start();
                            firstClick = false;
                        } else {
                            handleRegularClick(tile, e.getButton());
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }
        
        frame.setJMenuBar(menubar);
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.setVisible(true);

        setMine();
        updateMinesRemaining();
    }

    void handleFirstClick(int r, int c, int button) {
        if(button == MouseEvent.BUTTON1){
            hint = new hint(this);
            controlPanel.add(hint, BorderLayout.CENTER);
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    board[i][j].setEnabled(true);
                    board[i][j].setText("");
                }
            }
            setMinesExcluding(r, c);
            checkMines(r, c);
        }else{
            JOptionPane.showMessageDialog(null, "Left Click");
        }
    }

    void handleRegularClick(MineTile tile, int button) {
        if (button == MouseEvent.BUTTON1) {
            // Left click
            if (tile.getText().equals("")) {
                if (mineList.contains(tile)) {
                    revealMines();
                } else {
                    checkMines(tile.r, tile.c);
                }
            }
        } else if (button == MouseEvent.BUTTON3) {
            // Right click
            if (tile.getText().equals("")) {
                tile.setText("🚩");
            } else if (tile.getText().equals("🚩")) {
                tile.setText("");
            }
            updateMinesRemaining();
        }else if(button == MouseEvent.BUTTON2){
            //Middle click
            if (tile.getText().equals("")) {
                tile.setText("❓️");
            } else if (tile.getText().equals("❓️")) {
                tile.setText("");
            }
        }
    }

    void setMinesExcluding(int clickedRow, int clickedCol) {
        mineList = new ArrayList<>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];

            if (!(Math.abs(r - clickedRow) <= 1 && Math.abs(c - clickedCol) <= 1) && !mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void updateMinesRemaining() {
        minesRemaining = mineCount - getFlaggedTilesCount();
        textLabel.setText("Booms Left: " + minesRemaining);
    }

    int getFlaggedTilesCount() {
        int flaggedCount = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c].getText().equals("🚩")) {
                    flaggedCount++;
                }
            }
        }
        return flaggedCount;
    }

    void setMine() {
        mineList = new ArrayList<>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        tiWatch.stop();
        textLabel.setText("Game Over!");
        gameover();
    }

    void checkMines(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        minesFound += countMine(r - 1, c - 1);
        minesFound += countMine(r - 1, c);
        minesFound += countMine(r - 1, c + 1);

        // left and right
        minesFound += countMine(r, c - 1);
        minesFound += countMine(r, c + 1);

        // bottom 3
        minesFound += countMine(r + 1, c - 1);
        minesFound += countMine(r + 1, c);
        minesFound += countMine(r + 1, c + 1);

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        } else {
            tile.setText("");

            checkMines(r - 1, c - 1);
            checkMines(r - 1, c);
            checkMines(r - 1, c + 1);

            checkMines(r, c - 1);
            checkMines(r, c + 1);

            checkMines(r + 1, c - 1);
            checkMines(r + 1, c);
            checkMines(r + 1, c + 1);
        }
        if (tilesClicked == numRows * numCols - mineList.size()) {
            win(this.mineCount);
        }
    }


    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }

    void resetGame() {
        firstClick = true;
        gameOver = false;
        tilesClicked = 0;
        tiWatch.reset();
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
        
        switch (mineCount) {
            case 5:
                hintcount = 1;
                break;
            case 20:
                hintcount = 3;
                break;
            case 75:
                hintcount = 5;
                break;
        }

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = board[r][c];
                tile.setEnabled(true);
                tile.setText("");
            }
        }
        setMine();
    }

    void gameover(){
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Object[] options = { "Yes", "Back to Menu" };
                int choice = JOptionPane.showOptionDialog(
                    null,
                    "Do you want to restart the game or go back to the menu?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );
                if(choice == JOptionPane.YES_OPTION){
                    resetGame();
                }else if(choice == JOptionPane.NO_OPTION){
                    new Launcher();
                    frame.dispose();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    void win(int mineCount){
        gameOver = true;
        textLabel.setText("Mine Clear");
        tiWatch.stop();
        String time = tiWatch.getTime();
        String message = "<html>You won!<br/>Time: " + time + "<br/>Please enter your name:</html>";
        lb = new LeaderBoard();
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        
        String name = JOptionPane.showInputDialog(null, messageLabel,
        "Congratulation", JOptionPane.PLAIN_MESSAGE);

        if (name == null || name.trim().isEmpty()) {
            name = "Anonymous";
        }

        switch (mineCount) {
            case 5:
                lb.loadFromFile("File/leaderboardes.dat");
                lb.addPlayer(name, time);
                lb.saveToFile("File/leaderboardes.dat");
                break;
            case 20:
                lb.loadFromFile("File/leaderboardme.dat");
                lb.addPlayer(name, time);
                lb.saveToFile("File/leaderboardme.dat");
                break;
            case 75:
                lb.loadFromFile("File/leaderboardha.dat");
                lb.addPlayer(name, time);
                lb.saveToFile("File/leaderboardha.dat");
                break;
        }
    }
}