package Mines.Controller;

import Mines.Model.Board;
import Mines.Model.Highscores;
import Mines.View.GUI;

import javax.swing.*;

import static Mines.Images.*;

/**
 * Created on 04.11.2015.
 *
 * Controller class that oversee work of both GUI and game model.
 *
 * @author Pawe³ Rybak
 * @version 1.0
 */
public class Controller {
    /**
     * Enumeration-type object which contains possible difficulty levels.
     */
    public enum GameType {CUSTOM, BEGINNER, INTERMEDIATE, EXPERT}

    public int height;
    public int width;
    private Board board ;
    private GUI gui;
    private MinesTimer timer;
    private Highscores highscores;
    private GameType gameType;

    /**
     * Creates controller object and gets reference to GUI, model, and creates timer object.
     * It also sets reference ing GUI to itself.
     * @param gui reference to GUI
     * @param board reference to model
     */
    public Controller(GUI gui, Board board) {
        this.gui = gui;
        gui.setController(this);
        this.board = board;
        this.timer = new MinesTimer(gui.getTimerLabel());
    }

    /**
     * Initiates new game on board with given parameters.
     * Throws exception if board size is inappropriate.
     * @param width board width.
     * @param height board height.
     * @param minesNumber number of mines on board.
     * @throws Exception thrown in case board does not meet map size conditions (min 8x8; max 32x50)
     * or there is less than 2 non mined fields.
     */
    public void newGame(int width, int height, int minesNumber) throws Exception
    {
        if (width < 8 || height < 8 || height > 32 || width > 50 || minesNumber > height * width - 2)
            throw new Exception("Bad size Error");

        gameType = GameType.CUSTOM;
        initiateGame(width, height, minesNumber);
    }

    /**
     * Initiates new game on board with parameters defined by difficulty level.
     * Difficulty is defined by special enum-type object called <code>GameType</code>.
     * Levels:
     * <code>BEGINNER</code> - 9x9 board with 10 mines,
     * <code>INTERMEDIATE</code> - 16x16 board with 30 mines,
     * <code>EXPERT</code> - 30x16 board with 99 mines.
     * @param type difficulty level
     */
    public void newGame(GameType type)
    {
        try
        {
            switch (type)
            {
                case BEGINNER:
                    gameType = GameType.BEGINNER;
                    initiateGame(9, 9, 10);
                    break;
                case INTERMEDIATE:
                    gameType = GameType.INTERMEDIATE;
                    initiateGame(16, 16, 30);
                    break;
                case EXPERT:
                    gameType = GameType.EXPERT;
                    initiateGame(30, 16, 99);
                    break;
            }
        }
        catch (Exception e)
        {}
    }

    /**
     * Function used to initiate new game with given parameters.
     * ATTENTION! Function isn't meant to be called directly by user, but through
     * one of <code>newGame</code> functions.
     * @param width board width.
     * @param height board height.
     * @param minesNumber number of mines in board.
     */
    private void initiateGame(int width, int height, int minesNumber)
    {
        highscores = Highscores.loadHighscores();

        this.height = height;
        this.width = width;


        board.newGame(height, width, minesNumber);
        gui.drawGUI(height, width);

        gui.setMinesLeft("Mines left: " + Integer.toString(minesNumber));
        timer.restartTimer();
    }

    /**
     * Function controls what happens when field in given point has been left clicked.
     * It uncovers field if it isn't flagged and tells GUI to refresh. When mine is uncovered
     * it sends information about it to both GUI and Model.
     * @param x vertical position of clicked field (Counting from 0).
     * @param y horizontal position of clicked field (Counting from 0).
     */
    public void leftClicked (int x, int y)
    {
        timer.startTimer();
        if (!board.isFlagged(x, y))
        {
            if (board.uncover(x, y))
            {
                gameOverLost();
            }
        }
        if (board.isGameWon())
            gameOverWon();

        gui.refreshBoard();
    }

    /**
     * Function controls what happens when field in given point has been right clicked.
     * It toggles flag on field if it isn't uncovered and tells GUI to refresh.
     * @param x vertical position of clicked field (Counting from 0).
     * @param y horizontal position of clicked field (Counting from 0).
     */
    public void rightClicked (int x, int y)
    {
        board.toggleFlag(x, y);
        gui.setMinesLeft("Mines left: " + Integer.toString(board.getMinesLeft()));

        gui.refreshBoard();
    }

    /**
     * Function gets appropriate icon for field in given point.
     * @param x vertical position of clicked field (Counting from 0).
     * @param y horizontal position of clicked field (Counting from 0).
     * @return icon for field.
     */
    public ImageIcon getIcon (int x, int y)
    {
        if (board.isFlagged(x, y))
        {
            return IMAGE_FLAGGED;
        }
        else if (!board.isUncovered(x, y))
        {
            return IMAGE_COVER;
        }
        else if (board.isMined(x, y))
        {
            return IMAGE_MINE;
        }
        else
        {
            switch (board.getNearbyMines(x, y))
            {
                case 0:
                    return IMAGE_0;
                case 1:
                    return IMAGE_1;
                case 2:
                    return IMAGE_2;
                case 3:
                    return IMAGE_3;
                case 4:
                    return IMAGE_4;
                case 5:
                    return IMAGE_5;
                case 6:
                    return IMAGE_6;
                case 7:
                    return IMAGE_7;
                case 8:
                    return IMAGE_8;
                default:
                    return IMAGE_COVER;
            }
        }
    }

    /**
     * Function called to set game over state when mine is uncovered.
     * Stops timer, disables GUI buttons and shows "Game over" text in statusbar.
     */
    private void gameOverLost()
    {
        timer.stopTimer();
        gui.disableBoard();
        gui.showGameOverText();
        board.showAllMined();
        gui.refreshBoard();
    }

    /**
     * Function called to set game over when wll not mined fields are uncovered.
     * It stops timer, disables GUI buttons and shows "Game won" text in statusbar.
     */
    private void gameOverWon()
    {
        timer.stopTimer();
        gui.disableBoard();
        gui.showGameWonText();

        switch (gameType)
        {
            case BEGINNER:
                if (highscores.newBeginnerTime(timer.getTime()))
                {
                    highscores.setNameBeginner(gui.getName());
                }
                break;
            case INTERMEDIATE:
                if (highscores.newIntermediateTime(timer.getTime()))
                {
                    highscores.setNameIntermediate(gui.getName());
                }
                break;
            case EXPERT:
                if (highscores.newExpertTime(timer.getTime()))
                {
                    highscores.setNameExpert(gui.getName());
                }
                break;
            default:
                break;
        }

        Highscores.saveHighscores(highscores);
    }

    /**
     * Function gets highscores entry as three-lined <code>String</code>.
     * Each line style is: [Difficulty level]: [name], [time].
     * @return Highscores text.
     */
    public String getHighscoresText()
    {
        return highscores.getBeginnerEntry() + "\n" +
                highscores.getIntermediateEnty() + "\n" +
                highscores.getExpertEntry();
    }
}
