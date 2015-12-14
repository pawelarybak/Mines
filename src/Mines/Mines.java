package Mines;

import Mines.Controller.*;
import Mines.Model.Board;
import Mines.View.GUI;

/**
 * Main class that starts the game.
 *
 * TODO:
 * Reasonable images
 */
public class Mines
{
    public static void main(String[] args)
    {
        Board board = new Board();
        GUI gui = new GUI();
        Controller controller = new Controller(gui, board);
        try
        {
            controller.newGame(Controller.GameType.EXPERT);
        }
        catch (Exception e) {}

    }
}
