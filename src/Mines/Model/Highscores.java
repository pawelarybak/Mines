package Mines.Model;

import java.io.*;

/**
 * Created by someoddperson on 12/13/15.
 */
public class Highscores implements Serializable
{
    String nameExpert, nameIntermediate, nameBeginner;
    float timeExpert, timeIntermediate, timeBeginner;

    public Highscores()
    {
        nameExpert = "nobody";
        nameIntermediate = "nobody";
        nameBeginner = "nobody";
        timeExpert = 999.99f;
        timeIntermediate = 999.99f;
        timeBeginner = 999.99f;
    }

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

    public void setNameBeginner(String nameBeginner) {
        this.nameBeginner = nameBeginner;
    }

    public void setNameExpert(String nameExpert) {
        this.nameExpert = nameExpert;
    }

    public void setNameIntermediate(String nameIntermediate) {
        this.nameIntermediate = nameIntermediate;
    }

    public boolean newExpertTime(float time)
    {
        if (time < timeExpert)
        {
            timeExpert = time;
            return true;
        }
        return false;
    }

    public boolean newIntermediateTime(float time)
    {
        if (time < timeIntermediate)
        {
            timeIntermediate = time;
            return true;
        }
        return false;
    }

    public boolean newBeginnerTime(float time)
    {
        if (time < timeBeginner)
        {
            timeBeginner = time;
            return true;
        }
        return false;
    }

    public String getBeginnerEntry()
    {
        return "Beginner: " + nameBeginner + ", " + timeBeginner;
    }

    public String getIntermediateEnty()
    {
        return "Intermediate: " + nameIntermediate + ", " + timeIntermediate;
    }

    public String getExpertEntry()
    {
        return "Expert: " + nameExpert + ", " + timeExpert;
    }
}
