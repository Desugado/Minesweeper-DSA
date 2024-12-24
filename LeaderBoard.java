/*  Name: Nguyễn Công Tuấn Vũ
    ID: ITITWE22160
    Name: Nguyễn Minh Trí
    ID: ITITIU22164
    Purpose: Manage and display the game's leaderboard 
*/

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class LeaderBoard {
    private List<Player> players;
    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame frame;
    private static final int MAX_ROWS = 5;

    private JButton easy = new JButton("Easy");
    private JButton med = new JButton("Medium");
    private JButton hard = new JButton("Hard");
    private JPanel panel = new JPanel();

    public LeaderBoard() {
        players = new ArrayList<>();
        table = new JTable();
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);

        frame = new JFrame("LeaderBoard");
        frame.setSize(267, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        panel.setLayout(new GridLayout(1, 3, 5, 5));
        panel.add(easy);
        panel.add(med);
        panel.add(hard);

        loadFromFile("File/leaderboardes.dat");
        printLeaderboard();

        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile("File/leaderboardes.dat");
                printLeaderboard();
            }
        });

        med.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile("File/leaderboardme.dat");
                printLeaderboard();
            }
        });

        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile("File/leaderboardha.dat");
                printLeaderboard();
            }
        });

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void addPlayer(String name, String time) {
        Player player = findPlayerByName(name);
        if (player != null) {
            if (compareTimes(time, player.getTime()) < 0) {
                player.setTime(time);
            }
        } else {
            players.add(new Player(name, time));
        }
        sortLeaderboard();
        updateTable();
    }

    private Player findPlayerByName(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    private void sortLeaderboard() {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return compareTimes(p1.getTime(), p2.getTime());
            }
        });
    }

    private int compareTimes(String time1, String time2) {
        return time1.compareTo(time2);
    }

    public List<Player> getTopPlayers(int n) {
        if (n > players.size()) {
            n = players.size();
        }
        return players.subList(0, n);
    }

    public void printLeaderboard() {
        String[] columnNames = {"Rank", "Name", "Time"};
        Object[][] data = new Object[Math.min(players.size(), MAX_ROWS)][3];

        for (int i = 0; i < Math.min(players.size(), MAX_ROWS); i++) {
            Player player = players.get(i);
            data[i][0] = i + 1;            // Rank
            data[i][1] = player.getName(); // Name
            data[i][2] = player.getTime(); // Time
        }

        tableModel = new DefaultTableModel(data, columnNames);
        table.setModel(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50); // Rank
        columnModel.getColumn(1).setPreferredWidth(100); // Name
        columnModel.getColumn(2).setPreferredWidth(100); // Time     
    }

    private void updateTable() {
        String[] columnNames = {"Rank", "Name", "Time"};
        Object[][] data = new Object[Math.min(players.size(), MAX_ROWS)][3];

        for (int i = 0; i < Math.min(players.size(), MAX_ROWS); i++) {
            Player player = players.get(i);
            data[i][0] = i + 1;            // Rank
            data[i][1] = player.getName(); // Name
            data[i][2] = player.getTime(); // Time
        }

        tableModel.setDataVector(data, columnNames);
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found, initializing empty leaderboard.");
            players = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            players = (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            players = new ArrayList<>();
        }
    }

    public void showLeaderBoard() {
        frame.setVisible(true);
    }
}