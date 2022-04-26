package gui;

import logika.Igra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class Window extends JFrame implements ActionListener {
    public Canvas canvas;
    private JLabel status;

    public Window() {
        this.setTitle("Othello");
        this.setLayout(new GridBagLayout());

        this.canvas = new Canvas(1000, 1000);
        GridBagConstraints canvas_layout = new GridBagConstraints();
        canvas_layout.gridx = 0;
        canvas_layout.gridy = 0;
        canvas_layout.fill = GridBagConstraints.BOTH;
        canvas_layout.weightx = 1.0;
        canvas_layout.weighty = 1.0;
        getContentPane().add(canvas, canvas_layout);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        status = new JLabel();
        GridBagConstraints status_layout = new GridBagConstraints();
        status_layout.gridx = 0;
        status_layout.gridy = 1;
        getContentPane().add(status, status_layout);
        status.setText("Nova igra!");
        this.add(status);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void refreshGUI() {
        if (canvas.game == null) { status.setText("Nova igra!"); }
        else {
            switch(canvas.game.getPlayer()) {
                case WHITE: status.setText("Na vrsti je beli igralec!"); break;
                case BLACK: status.setText("Na vrsti je črni igralec!"); break;
            }
        }
        canvas.repaint();
    }
    /*TODO: replace repaint with refreshGUI */

    private JMenu addMenu(JMenuBar menuBar, String title) {
        JMenu menu = new JMenu(title);
        menuBar.add(menu);
        return menu;
    }
    private JMenuItem addMenuItem(JMenu menu, String title) {
        JMenuItem menuItem = new JMenuItem(title);
        menu.add(menuItem);
        menuItem.addActionListener(this);
        return menuItem;
    }
    
    private Integer getInt(String message) {
        String val = JOptionPane.showInputDialog(this, message);
        try
        {
            return Integer.parseInt(val);
        }
        catch(Exception exc) {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

    }
}
