/**
 *
 */
package unsw.automata;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Conway's Game of Life on a 10x10 grid.
 *
 * @author Robert Clifton-Everest
 *
 */
public class GameOfLife {
    final static int BOARD_SIZE = 10;
    private BooleanProperty[][] board;

    public GameOfLife() {
        board = new BooleanProperty[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                BooleanProperty tmp = new SimpleBooleanProperty();
                tmp.set(false);
                board[i][j] = tmp;

            }
        }
    }

    public void ensureAlive(int x, int y) {
        // TODO Set the cell at (x,y) as alive
        board[x][y].set(true);
        
    }

    public void ensureDead(int x, int y) {
        // TODO Set the cell at (x,y) as dead
        board[x][y].set(false);
    }

    public boolean isAlive(int x, int y) {
        // TODO Return true if the cell is alive
        return board[x][y].get();
    }

    public int findNeighbours(int x, int y) {
        int res = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int horizontally = i + x;
                int vertically = y + j;
                if (horizontally < 0){
                    horizontally = BOARD_SIZE -1;
                } else if (horizontally > BOARD_SIZE - 1){
                    horizontally = 0;
                }
                if (vertically < 0){
                    vertically = BOARD_SIZE -1;
                } else if (vertically > BOARD_SIZE - 1){
                    vertically = 0;
                }
                if (i == 0 && j == 0) {continue;}
                if (board[horizontally][vertically].get() == true) {res++;}
            }
        }
        return res;
    }

    public void tick() {
        // TODO Transition the game to the next generation.
        BooleanProperty[][] temp = new BooleanProperty[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                BooleanProperty tmp = new SimpleBooleanProperty();
                int neighbours = findNeighbours(i,j);
                if (board[i][j].get()) {
                    if (neighbours < 2){
                        tmp.set(false);
                    } else if (neighbours == 2 || neighbours == 3){
                        tmp.set(true);
                    } else{
                        tmp.set(false);
                    }
                } else if (neighbours == 3) {
                    tmp.set(true);
                } else {
                    tmp.set(false);
                }
                temp[i][j] = tmp;
                
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j].set(temp[i][j].get());
            }
        }
    
    }


    public BooleanProperty cellProperty(int x, int y){
        return board[x][y];
    }
    

}
