/*  Name: Nguyễn Công Tuấn Vũ
    ID: ITITWE22160
    Name: Nguyễn Minh Trí
    ID: ITITIU22164
    Purpose: The MineTile class serves as a custom button component 
    in the Minesweeper game, representing each individual tile on the 
    game board. Each tile can potentially contain a mine or be a safe 
    tile that indicates the number of adjacent mines.
*/

import javax.swing.JButton;

public class MineTile extends JButton{
    public int r;
    public int c;

    public MineTile(int r, int c) {
        this.r = r;
        this.c = c;
    }
}