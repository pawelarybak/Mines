package Mines.Controller;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 13.11.2015.
 *
 * Class implements timer based on <code> java.util.Timer </code> class.
 * It counts time with respect to two decimal places and shows it in
 * label given in constructor.
 *
 * @author Pawe³ Rybak
 * @version 1.0
 */
public class MinesTimer
{
    private Timer timer;
    private JLabel labeledTime;
    private DecimalFormat format = new DecimalFormat("#.##");
    private double timerText = 0.00;
    private boolean isStarted = false;

    /**
     * Creates timer and gets <code>JLabel</code> which represents timer.
     * @param label label that represents timer.
     */
    MinesTimer(JLabel label)
    {
        labeledTime = label;
    }

    /**
     * Starts timer if it isn't on the go, and refreshes label.
     * If time counted is above 999.99 it stops timer.
     */
    public void startTimer()
    {
        if (isStarted)
            return;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timerText >= 999.99) {
                    stopTimer();
                }
                timerText = timerText + 0.01;
                labeledTime.setText("Time: " + Integer.toString((int)timerText));
            }
        }, 10, 10);
        isStarted = true;
    }

    /**
     * Stops timer if it it on the go and refreshes label.
     */
    public void stopTimer() {
        if (!isStarted)
            return;

        timer.cancel();
        if (timerText >= 999.99)
            labeledTime.setText("Time: >999,99");
        else
            labeledTime.setText("Time: " + format.format(timerText));

        isStarted = false;
    }

    /**
     * Restarts timer.
     */
    public void restartTimer()
    {
        timerText = 0.00;
        labeledTime.setText("Time: 0");
    }

    /**
     * Function returns current time converted to <code>float</code>.
     * @return current time.
     */
    public float getTime()
    {
        return (float)timerText;
    }
}
