package Mines.Model;

import java.io.*;

/**
 * Class created to keep best times and names of best players,
 * one for each difficulty level. Class is adapted to be serialized to file "highscores.ser"
 *
 * @author Pawel Rybak
 * @version 1.0
 * @see java.io.Serializable
 */
public class Highscores implements Serializable
{
    String nameExpert, nameIntermediate, nameBeginner;
    float timeExpert, timeIntermediate, timeBeginner;

    /**
     * Creates new highscores object in case there is no other source to get it.
     * Constructor sets all names to "nobody", and all times to 999.99.
     */
    public Highscores()
    {
        nameExpert = "nobody";
        nameIntermediate = "nobody";
        nameBeginner = "nobody";
        timeExpert = 999.99f;
        timeIntermediate = 999.99f;
        timeBeginner = 999.99f;
    }

    /**
     * Function used to load object from file.
     * @return <code>Highscores</code> object loaded from file,
     *          or new <code>Highscores</code> object unless there was a file to load from.
     */
    public static Highscores loadHighscores()
    {
        Highscores highscores;
        try
        {
            FileInputStream fileStream = new FileInputStream("highscores.ser");
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            highscores = (Highscores)objectStream.readObject();
            objectStream.close();
            fileStream.close();
            return highscores;
        }
        catch (Exception e)
        {
            System.err.println("Could not load file");
            return new Highscores();
        }
    }

    /**
     * Function saves <code>Highscores</code> object to "highscores.ser" file.
     * @param highscores object to save.
     * @return <code>true</code> if object was saved correctly, or <code>false</code> otherwise.
     */
    public static boolean saveHighscores(Highscores highscores)
    {
        try
        {
            FileOutputStream fileStream = new FileOutputStream("highscores.ser");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(highscores);
            objectStream.close();
            fileStream.close();
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Could not save file");
            return false;
        }
    }

    /**
     * Sets new name for "Beginner" level best time.
     * @param nameBeginner new best "beginner" name.
     */
    public void setNameBeginner(String nameBeginner) {
        this.nameBeginner = nameBeginner;
    }

    /**
     * Sets new name for "Expert" level best time.
     * @param nameExpert new best "expert" name.
     */
    public void setNameExpert(String nameExpert) {
        this.nameExpert = nameExpert;
    }

    /**
     * Sets new name for "Intermediate" level best time.
     * @param nameIntermediate new best "intermediate" name.
     */
    public void setNameIntermediate(String nameIntermediate) {
        this.nameIntermediate = nameIntermediate;
    }

    /**
     * Gets new time in "expert" mode and compares it to current best.
     * If new time is better old one is override and function returns <code>true</code>
     * @param time new time.
     * @return boolean value whether new time is better
     */
    public boolean newExpertTime(float time)
    {
        if (time < timeExpert)
        {
            timeExpert = time;
            return true;
        }
        return false;
    }

    /**
     * Gets new time in "intermediate" mode and compares it to current best.
     * If new time is better old one is override and function returns <code>true</code>
     * @param time new time.
     * @return boolean value whether new time is better
     */
    public boolean newIntermediateTime(float time)
    {
        if (time < timeIntermediate)
        {
            timeIntermediate = time;
            return true;
        }
        return false;
    }

    /**
     * Gets new time in "beginner" mode and compares it to current best.
     * If new time is better old one is override and function returns <code>true</code>
     * @param time new time.
     * @return boolean value whether new time is better
     */
    public boolean newBeginnerTime(float time)
    {
        if (time < timeBeginner)
        {
            timeBeginner = time;
            return true;
        }
        return false;
    }

    /**
     * Function returns <code>String</code> for beginner entry.
     * Line style is: Beginner: [name], [time]
     * @return <code>String</code> for beginner entry.
     */
    public String getBeginnerEntry()
    {
        return "Beginner: " + nameBeginner + ", " + timeBeginner;
    }

    /**
     * Function returns <code>String</code> for intermediate entry.
     * Line style is: Intermediate: [name], [time]
     * @return <code>String</code> for intermediate entry.
     */
    public String getIntermediateEnty()
    {
        return "Intermediate: " + nameIntermediate + ", " + timeIntermediate;
    }

    /**
     * Function returns <code>String</code> for expert entry.
     * Line style is: Expert: [name], [time]
     * @return <code>String</code> for expert entry.
     */
    public String getExpertEntry()
    {
        return "Expert: " + nameExpert + ", " + timeExpert;
    }
}
