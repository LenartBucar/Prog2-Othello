package gui;

import logika.Igra;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class Window extends JFrame implements ActionListener {
    public Canvas canvas;

    public Window() {
        this.setTitle("Urejevalnik grafov");
        this.canvas = new Canvas(1000, 1000);
        this.add(this.canvas);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
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
