package Mines.Model;

import java.awt.Point;
import java.util.*;

/**
 * Created on 04.11.2015.
 *
 * Class represents a board of fields in Minesweeper-like game.
 *
 * Class can create an x by y board of fields with given number of mines
 * which are set in random points. It initiates fields by counting mines around each field
 * and decides if fields should be uncovered by user or automatically (in case when
 * field doesn't have any mine around).
 *
 * @author Pawe� Rybak
 * @version 1.0
 */
public class Board
{
    private int height;
    private int width;
    private int minesNumber;

    private Field[][] board;
    private Set<Point> minedFields;

    private int minesLeft;
    private boolean initiated = false;
    private Random rand = new Random();

    /**
     * Creates board with parameters given to function and initiates board with empty fields,
     * which are to be filled later.
     * @param height height of board.
     * @param width width of board.
     * @param minesNumber number of mines in the board.
     */
    public void newGame(int height, int width, int minesNumber) {
        this.height = height;
        this.width = width;
        this.minesNumber = minesNumber;
        this.minesLeft = minesNumber;
        this.minedFields = new HashSet<>(minesNumber);
        this.board = new Field[height][width];
        initiated = false;

        for (int x = 0; x < height; x++)
        {
            for (int y = 0; y < width; y++)
            {
                board[x][y] = new Field();
            }
        }
    }


    /**
     * Initiates board by filling it with mines and setting number of nearby mines around.
     * Function gets point which was first point clicked and does not set any mine in this field
     * or around it.
     * It returns without doing any changes if field was initiated before.
     * @param startField point clicked as first.
     */
    public void initiateBoard(Point startField)
    {
        if(initiated)
            return;

        randomizePoints(minesNumber, startField);
        initializeBoard();
        initiated = true;
    }

    /**
     * Function randomizes set of points in which mines will be set.
     * Randomized points cannot be outside the board or
     * mines in or around point given as parameter.
     * @param minesNumber number of mines to randomize.
     * @param startField point in and around which shouldn't be any mine.
     */
    private void randomizePoints (int minesNumber, Point startField)
    {
        Point minedPoint;
        for (int counter = 0; counter < minesNumber; counter++)
        {
            do
            {
                minedPoint = new Point(rand.nextInt(height), rand.nextInt(width));
            }
            while (minedFields.contains(minedPoint) || isAround(minedPoint, startField));
            minedFields.add(minedPoint);
        }

        if (minedFields.size() != minesNumber)
        {
            System.err.println("Wrong number of minedPoints in set");
        }
    }

    /**
     * Function checks if point given as first parameter is point given as second parameter
     * or around this point.
     * @param checkedPoint point that is being checked.
     * @param aroundWhich point around which its presence is checked.
     * @return <code>true</code> if point is around second or <code>false</code> otherwise.
     */
    private boolean isAround(Point checkedPoint, Point aroundWhich)
    {
//        if (checkedPoint.x <= aroundWhich.x + 1 && checkedPoint.x >= aroundWhich.x - 1 &&
//                checkedPoint.y <= aroundWhich.y + 1 && checkedPoint.y >= aroundWhich.y -1)
//            return true;

        if (checkedPoint.equals(aroundWhich))
            return true;

        return false;
    }

    /**
     * Function iterates through board initializing each field.
     */
    private void initializeBoard()
    {
        for (int x = 0; x < height; x++)
        {
            for (int y = 0; y < width; y++)
            {
                initializeField(new Point(x, y));
            }
        }
    }

    /**
     * Function initializes field in point given as parameter.
     * It checks whether field should be mined.
     * If so it sets field's mined flag.
     * If not it counts mines around and sets field's value.
     * @param point point in which is initiated field.
     */
    private void initializeField(Point point) {
        int nearbyMinesCounter = 0;

        if (minedFields.contains(point))
        {
            board[point.x][point.y].setMined(true);
            board[point.x][point.y].setNearbyMines(0);
            return;
        }
        else
        {
            for (int x = point.x - 1; x <= point.x + 1; x++) //iterates through square around field
            {
                if (x < 0 || x > height) //if x is beyond the map
                    continue;
                for (int y = point.y - 1; y <= point.y + 1; y++)
                {
                    if (y < 0 || y > width) //if y is beyond map
                        continue;

                    if (minedFields.contains(new Point(x, y)))
                        nearbyMinesCounter++;
                }

            }

            board[point.x][point.y].setMined(false);
            board[point.x][point.y].setNearbyMines(nearbyMinesCounter);
        }
    }

    /**
     * Toggles flag in given field.
     * @param x vertical position of field (counting from 0).
     * @param y horizontal position of field (counting from 0).
     */
    public void toggleFlag(int x, int y)
    {
        if (board[x][y].isUncovered())
            return;

        if (board[x][y].toggleFlag())
        {
            minesLeft++;
        }
        else
        {
            minesLeft--;
        }
    }


    /**
     * Method checks if field in given point is uncovered and has exact same number
     * of flags and mines around. If so uncovers not flagged fields around this field.
     * @param posX vertical position of field (counting from 0).
     * @param posY  horizontal position of field (counting from 0).
     * @return <code> true </code> if there was not flagged, mined field uncovered.
     * <code> false </code> if everything is ok.
     */
    public boolean uncoverAround(int posX, int posY)
    {
        if (board[posX][posY].getNearbyMines() != 0 && !checkFlags(posX, posY))
            return false;

        for (int x = posX - 1; x <= posX + 1; x++)
        {
            if (x < 0 || x >= height)
                continue;
            for (int y = posY - 1; y <= posY + 1; y++)
            {
                if (y < 0 || y >= width)
                    continue;

                if(!board[x][y].isUncovered())
                    uncover(x, y);

                if (board[x][y].isMined() && !board[x][y].isFlagged())
                    return true;
            }
        }
        return false;
    }


    /**
     * Function checks whether number of set flags around it is equal to number of mines around
     * and returns appropriate boolean value.
     * It also returns <code>false</code> when checked field isn't uncovered.
     * @param posX horizontal position of point checked by function.
     * @param posY vertical position of point checked by function.
     * @return boolean value of whether number of flags and mines is equal.
     */
    private boolean checkFlags(int posX, int posY)
    {
        int flagNumber = 0;

        for (int x = posX - 1; x <= posX + 1; x++)
        {
            if (x < 0 || x >= height)
                continue;
            for (int y = posY - 1; y <= posY + 1; y++)
            {
                if (y < 0 || y >= width)
                    continue;

                if (board[x][y].isFlagged())
                    flagNumber++;
            }
        }

        if (!board[posX][posY].isUncovered() || flagNumber != board[posX][posY].getNearbyMines())
            return false;
        else
            return true;
    }

    /**
     * Uncovers field in given point and if it doesn't have any mine around
     * uncovers all fields around. (Method can be recursive).
     * @param x vertical position of field (counting from 0).
     * @param y horizontal position of field (counting from 0).
     */
    public boolean uncover (int x, int y)
    {
        initiateBoard(new Point(x, y));
        if (board[x][y].isFlagged())
            return false;

        if (board[x][y].uncover())
        {
            return uncoverAround(x, y);
        }
        else if (board[x][y].isMined())
        {
            return true;
        }
        else if (board[x][y].getNearbyMines() == 0)
        {
            uncoverAround(x, y);
        }
        return false;
    }

    /**
     * Checks if field in given point has mine.
     * @param x vertical position of field (counting from 0).
     * @param y horizontal position of field (counting from 0).
     * @return <code> true </code> if field has mine or <code> false </code> otherwise.
     */
    public boolean isMined(int x, int y)
    {
        return board[x][y].isMined();
    }

    /**
     * Gets numbers of mines around field in given point.
     * @param x vertical position of field (counting from 0).
     * @param y horizontal position of field (counting from 0).
     * @return number of mines aroud field.
     */
    public int getNearbyMines (int x, int y)
    {
        return board[x][y].getNearbyMines();
    }

    /**
     * Returns boolean value that states if field in given point is uncovered.
     * @param x vertical position of field (counting from 0)
     * @param y horizontal position of field (counting from 0).
     * @return <code> true </code> if given field is uncovered or <code> false </code> otherwise.
     */
    public boolean isUncovered (int x, int y)
    {
        return board[x][y].isUncovered();
    }

    /**
     * Returns boolean value that states if field in given point is flagged.
     * @param x vertical position of field (counting from 0)
     * @param y horizontal position of field (counting from 0).
     * @return <code> true </code> if given field is flagged or <code> false </code> otherwise.
     */
    public boolean isFlagged (int x, int y)
    {
        return board[x][y].isFlagged();
    }

    /**
     * Function checks whether all non-mined fields are uncovered.
     * Function breaks when found any non-mined not uncovered field.
     * @return <code>true</code> when all non-mined fields are uncovered or <code>false</code> otherwise.
     */
    public boolean isGameWon()
    {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (!board[x][y].isUncovered() && !board[x][y].isMined())
                    return false;
            }
        }
        return true;
    }

    /**
     * Function returns <code>minesLeft</code> variable which is number of mines minus number of set flags.
     * @return number of mines minus number of flags set.
     */
    public int getMinesLeft() {
        return minesLeft;
    }

    public void showAllMined() {
        for (Point mined: minedFields)
            board[mined.x][mined.y].uncover();
    }
}
