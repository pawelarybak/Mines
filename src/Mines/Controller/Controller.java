package Mines.Controller;

import Mines.Model.Board;
import Mines.Model.Highscores;
import Mines.View.GUI;

import javax.swing.*;


import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
    public enum GameType {CUSTOM, BEGINNER, INTERMEDIATE, EXPERT}

    public int height;
    public int width;
    private Board board ;
    private GUI gui;
    private MinesTimer timer;
    private Highscores highscores;
    private GameType gameType;

    public Controller(GUI gui, Board board) {
        this.gui = gui;
        gui.setController(this);
        this.board = board;
        this.timer = new MinesTimer(gui.getTimerLabel());
    }

    /**
     * Initiates new game on board with giver parameters and restarts timer.
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

    public void newGame(GameType type) throws Exception
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

    private void initiateGame(int width, int height, int minesNumber) throws Exception
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
        timer.startTimer();

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

    public String getHighscoresText()
    {
        return highscores.getBeginnerEntry() + "\n" +
                highscores.getIntermediateEnty() + "\n" +
                highscores.getExpertEntry();
    }
}