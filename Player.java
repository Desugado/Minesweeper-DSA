/*  Name: Nguyễn Công Tuấn Vũ
    ID: ITITWE22160
    Name: Nguyễn Minh Trí
    ID: ITITIU22164
    Purpose: The Player class is designed to represent a 
    player in the Minesweeper game. It stores the player's 
    name and the time taken to complete the game. This 
    information is essential for maintaining a record of 
    scores and times, which can be displayed on leaderboards.
*/

import java.io.Serializable;

public class Player implements Serializable{
    private String name;
    private String time;
    
    public Player(String name, String time) {
        this.name = name;
        this.time = time;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return name + ":" + time;
    }
}