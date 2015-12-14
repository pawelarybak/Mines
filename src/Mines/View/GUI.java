package Mines.View;


import Mines.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Mines.Images.*;
import static Mines.Controller.Controller.*;

/**
 * Graphical user interface for minesweeper-like game.
 *
 * @author Pawel Rybak
 * @version 1.0
 */
public class GUI
{
    private int height;
    private int width;

    private JFrame frame = new JFrame("Mines");
    private JPanel contentPane;
    private JPanel boardPanel;
    private JPanel statusBar;
    private Controller controller;
    private FieldGUI[] board;
    private JMenuBar menuBar = new JMenuBar();
    private JLabel timerText;
    private JLabel gameStateText;
    private JLabel minesLeft;
    private CustomBoardDialog customBoardDialog;


    /**
     * Creates GUI object. Make frame, with board, menus, and dialog fot custom board.
     */
    public GUI ()
    {
        initializeMenus();
        initializeStatusBar();
    }

    /**
     * Function sets reference to <code>Controller</code> object.
     * @param controller reference to controller.
     */
    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    /**
     * Function builds GUI window containing board with size given as parameter, status bar with mines
     * number label, time and Game won/Game lost information label.
     * @param height board height.
     * @param width board width.
     */
    public void drawGUI(int height, int width)
    {
        this.height = height;
        this.width = width;

        board = new FieldGUI[this.height * this.width];
        contentPane = new JPanel();
        boardPanel = new JPanel(new GridLayout(this.height, this.width, 0, 0));

        gameStateText.setVisible(false);

        boardPanel.setPreferredSize(new Dimension(IMAGE_WIDTH * this.width, IMAGE_HEIGHT * this.height));
        for (int counter = 0; counter < this.height * this.width; counter++) {
            boardPanel.add(board[counter] = new FieldGUI(counter / this.width, counter % this.width));
        }

        customBoardDialog = new CustomBoardDialog(frame, controller);

        contentPane.setLayout(new BorderLayout());
        contentPane.add(boardPanel);
        contentPane.add(statusBar, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.setJMenuBar(menuBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Function iterates through whole board asking controller for appropriate image for field and
     * setting that images.
     */
    public void refreshBoard()
    {
        for (FieldGUI button: board)
        {
            button.refreshImage();
        }
    }

    /**
     * Function shows "Game over" label in the statusbar.
     */
    public void showGameOverText()
    {
        gameStateText.setText("Game Over ");
        gameStateText.setVisible(true);
    }

    /**
     * Function shows "Game won" label in the statusbar.
     */
    public void showGameWonText()
    {
        gameStateText.setText("Game Won ");
        gameStateText.setVisible(true);
    }

    /**
     * Function sets number of mines left to label on statusbar.
     * Number of mines is given to the function as <code>string</code> parameter.
     * @param number number of mines left.
     */
    public void setMinesLeft(String number)
    {
        minesLeft.setText(number);
    }

    /**
     * Function returns <code>JLabel</code> that shows time.
     * @return label containing time.
     */
    public JLabel getTimerLabel()
    {
        return timerText;
    }

    /**
     * Function shows dialog in which user is supposed to enter his name.
     * @return name entered by user.
     */
    public String getName()
    {
        return (String)JOptionPane.showInputDialog(frame, "Enter your name: ");
    }

    /**
     * Function disables each field in board, so it cannot be clicked.
     */
    public void disableBoard()
    {
        for (FieldGUI button: board)
        {
            button.disableField();
        }
    }

    /**
     * Function initializes menus. It creates menus, fills it with items. Set listeners to each
     * item and adds initialized menu to menubar.
     */
    private void initializeMenus()
    {
        JMenu mainMenu = new JMenu("Menu");
        JLabel menuLabel = new JLabel("New game:");

        JMenuItem newGameBeginner = new JMenuItem("Beginner");
        JMenuItem newGameIntermediate = new JMenuItem("Intermediate");
        JMenuItem newGameExpert = new JMenuItem("Expert");
        JMenuItem newGameCustom = new JMenuItem("Custom");
        JMenuItem highscores = new JMenuItem("Highscores");

        newGameBeginner.addActionListener(e -> {
            try {
                controller.newGame(GameType.BEGINNER);
            }
            catch (Exception exception) {}
        });
        newGameIntermediate.addActionListener(e -> {
            try {
                controller.newGame(GameType.INTERMEDIATE);
            }
            catch (Exception exception) {}
        });
        newGameExpert.addActionListener(e -> {
            try {
                controller.newGame(GameType.EXPERT);
            }
            catch (Exception exception) {}
        });
        newGameCustom.addActionListener(e -> customBoardDialog.setVisible(true));
        highscores.addActionListener(e -> JOptionPane.showMessageDialog(frame, controller.getHighscoresText()));

        mainMenu.add(menuLabel);

        mainMenu.add(newGameBeginner);
        mainMenu.add(newGameIntermediate);
        mainMenu.add(newGameExpert);
        mainMenu.add(newGameCustom);
        mainMenu.add(highscores);
        mainMenu.setMnemonic(KeyEvent.VK_M);

        menuBar.add(mainMenu);
    }

    /**
     * Function initializes statusbar, by adding and aligning labels in it.
     */
    private void initializeStatusBar()
    {
        minesLeft = new JLabel();
        minesLeft.setHorizontalAlignment(JLabel.CENTER);

        gameStateText = new JLabel();
        gameStateText.setHorizontalAlignment(JLabel.RIGHT);
        gameStateText.setVisible(false);

        timerText = new JLabel("0");

        statusBar = new JPanel();
        statusBar.setLayout(new GridLayout(1, 3));

        statusBar.add(timerText);
        statusBar.add(minesLeft);
        statusBar.add(gameStateText);
    }

    /**
     * Class is extension of <code>JToggleButton</code>.
     * It knows its coordinates in board and whether it is enabled
     * It also has additional function to ask for image and refresh it.
     */
    private class FieldGUI extends JToggleButton
    {
        private int x;
        private int y;
        private boolean enabled;

        /**
         * Creates GUI of single field, giving it cover icon and sets its location in board.
         * It also sets field's listener.
         * @param x horizontal position in board.
         * @param y vertical position in board.
         */
        FieldGUI(int x, int y)
        {
            super("", IMAGE_COVER);

            enabled = true;
            this.x = x;
            this.y = y;


            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (enabled)
                    {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            System.out.println("R-Clicked: " + Integer.toString(x) + ", " + Integer.toString(y));
                            controller.rightClicked(x, y);
                        }
                        else
                        {
                            System.out.println("L-Clicked: " + Integer.toString(x) + ", " + Integer.toString(y));
                            controller.leftClicked(x, y);
                        }
                    }
                }
            });
        }

        /**
         * Gets icon for field from controller and sets it to the field.
         */
        private void refreshImage()
        {
            setIcon(controller.getIcon(x, y));
        }

        /**
         * Disables the field, so it won't be interactive.
         */
        private void disableField()
        {
            enabled = false;
        }

    }
}
