package Mines.Model;

/**
 * Created on 04.11.2015.
 *
 * Class represents a model of single field in a Minesweeper-like game
 *
 * @author Pawe³ Rybak
 * @version 1.0
 */
public class Field
{
    /* Variables */
    private boolean mined;
    private int nearbyMines;
    private boolean flagged;
    private boolean uncovered;


    /* Constructors */

    /**
     * Creates new uncovered field without flag
     * @param mined says if field has mine
     * @param nearbyMines number of mines around a field
     *                    (should be -1 if field has mine *to be changed in next version*)
     */
    Field(boolean mined, int nearbyMines)
    {
        this.mined = mined;
        this.nearbyMines = nearbyMines;
        flagged = false;
        uncovered = false;
    }

    /**
     * Creates new field with no mine and no nearby mines. Useful to create empty board to be modified later.
     */
    Field()
    {
        this(false, 0);
    }

    /* Methods */

    /**
     * Checks if the field has flag on it.
     * @return <code> true </code> if field has mine and
     * <code> flase </code> otherwise.
     */
    public boolean isFlagged()
    {
        return flagged;
    }


    /**
     * Checks if field had been uncovered.
     * @return <code> true </code> if field is uncovered or
     * <code> false </code> otherwise.
     */
    public boolean isUncovered()
    {
        return uncovered;
    }

    /**
     * Returns number of fields next to it which have mines.
     * @return Numbers of mines in fields next to it.
     */
    public int getNearbyMines()
    {
        return nearbyMines;
    }

    /**
     * Sets <code> flagged </code> field to contrary value.
     * @return boolean if it was flagged before function was called.
     */
    public boolean toggleFlag()
    {
        flagged = !flagged;
        return !flagged;
    }

    /**
     * Checks if field has mine.
     * @return <code> true </code> if field has mine and <code> flase </code> otherwise.
     */
    public boolean isMined()
    {
        return mined;
    }

    /**
     * Uncovers field if it haven't been uncovered already.
     * @return <code> true </code> if field was uncovered before method was called and
     * <code> false </code> otherwise.
     */
    public boolean uncover ()
    {
        if (uncovered)
            return true;

        uncovered = true;
        return false;
    }

    /**
     * Sets number of mines nearby the field.
     * @param nearbyMines number of miens.
     */
    public void setNearbyMines(int nearbyMines) {
        this.nearbyMines = nearbyMines;
    }

    /**
     * Sets boolean value of whether field is mined.
     * @param mined boolean value.
     */
    public void setMined(boolean mined) {
        this.mined = mined;
    }
}
