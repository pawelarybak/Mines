package Mines.View;

import Mines.Controller.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pawel on 20.11.2015.
 */
public class CustomBoardDialog extends JDialog
{
    private JTextField rowsText = new JTextField();
    private JTextField colsText = new JTextField();
    private JTextField minesText = new JTextField();
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");

    /**
     * Creates dialog with fields to get attributes of custom map and creates custom map.
     * @param frame
     * @param controller
     */
    public CustomBoardDialog(JFrame frame, Controller controller) {
        super(frame, true);

        okButton.addActionListener(e -> {
            try {
                controller.newGame(Integer.parseInt(colsText.getText()),
                        Integer.parseInt(rowsText.getText()),
                        Integer.parseInt(minesText.getText()));
                dispose();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Board requirements not met.\n" +
                        "Board mustn't be smaller than 8x8, nor bigger than 32x50.\n" +
                        "Also there must be at least 2 non mined fields.");
            }

        });

        cancelButton.addActionListener(e -> dispose());

        setLayout(new GridLayout(4, 2));

        add(new JLabel("Rows: "));
        add(rowsText);

        add(new JLabel("Columns: "));
        add(colsText);

        add(new JLabel("Mines: "));
        add(minesText);

        add(okButton);
        add(cancelButton);

        pack();
        setResizable(false);
    }
}
