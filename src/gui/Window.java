package gui;

import coordinator.Coordinator;
import coordinator.PlayerType;
import logika.Igra;
import logika.Player;
import splosno.KdoIgra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

public class Window extends JFrame implements ActionListener {
    public Canvas canvas;
    public JLabel status;

    private JMenuItem igraClovekRacunalnik;
    private JMenuItem igraRacunalnikClovek;
    private JMenuItem igraClovekClovek;
    private JMenuItem igraRacunalnikRacunalnik;

    public Window() {
        this.setTitle("Othello");
        this.setLayout(new GridLayout());

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        this.add(main);

        canvas = new Canvas(1000, 1000);
        main.add(canvas);

        status = new JLabel();
        status.setText("Nova igra!");
        main.add(status);


        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu gameMenu = addMenu(menuBar, "Nova igra");

        igraClovekRacunalnik = addMenuItem(gameMenu, "Človek – računalnik");
        igraRacunalnikClovek = addMenuItem(gameMenu, "Računalnik – človek");
        igraClovekClovek = addMenuItem(gameMenu, "Človek – Človek");
        igraRacunalnikRacunalnik = addMenuItem(gameMenu, "Računalnik – računalnik");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void refreshGUI() {
        if (canvas.game == null) { status.setText("Nova igra!"); }
        else {
            switch(canvas.game.status) {
                case IN_PROGRESS: status.setText("Na potezi je " + canvas.game.getPlayer().toString()); break;
                case DRAW: status.setText("Neodloceno!"); break;
                case BLACK_WINS: status.setText("Zmaga črni igralec!"); break;
                case WHITE_WINS: status.setText("Zmaga beli igralec!"); break;
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
        if (e.getSource() == igraClovekRacunalnik) {
            Coordinator.playerTypeMap = new EnumMap<Player, PlayerType>(Player.class);
            Coordinator.playerTypeMap.put(Player.BLACK, PlayerType.H);
            Coordinator.playerTypeMap.put(Player.WHITE, PlayerType.C);
            Coordinator.kdoIgra = new EnumMap<Player,KdoIgra>(Player.class);
            Coordinator.kdoIgra.put(Player.BLACK, new KdoIgra("Človek"));
            Coordinator.kdoIgra.put(Player.WHITE, new KdoIgra("Računalnik"));
            Coordinator.playNewGame();
        } else if (e.getSource() == igraRacunalnikClovek) {
            Coordinator.playerTypeMap = new EnumMap<Player,PlayerType>(Player.class);
            Coordinator.playerTypeMap.put(Player.BLACK, PlayerType.C);
            Coordinator.playerTypeMap.put(Player.WHITE, PlayerType.H);
            Coordinator.kdoIgra = new EnumMap<Player,KdoIgra>(Player.class);
            Coordinator.kdoIgra.put(Player.BLACK, new KdoIgra("Računalnik"));
            Coordinator.kdoIgra.put(Player.WHITE, new KdoIgra("Človek"));
            Coordinator.playNewGame();
        } else if (e.getSource() == igraClovekClovek) {
            Coordinator.playerTypeMap = new EnumMap<Player,PlayerType>(Player.class);
            Coordinator.playerTypeMap.put(Player.BLACK, PlayerType.H);
            Coordinator.playerTypeMap.put(Player.WHITE, PlayerType.H);
            Coordinator.kdoIgra = new EnumMap<Player,KdoIgra>(Player.class);
            Coordinator.kdoIgra.put(Player.BLACK, new KdoIgra("Človek"));
            Coordinator.kdoIgra.put(Player.WHITE, new KdoIgra("Človek"));
            Coordinator.playNewGame();
        } else if (e.getSource() == igraRacunalnikRacunalnik) {
            Coordinator.playerTypeMap= new EnumMap<Player,PlayerType>(Player.class);
            Coordinator.playerTypeMap.put(Player.BLACK, PlayerType.C);
            Coordinator.playerTypeMap.put(Player.WHITE, PlayerType.C);
            Coordinator.kdoIgra = new EnumMap<Player,KdoIgra>(Player.class);
            Coordinator.kdoIgra.put(Player.BLACK, new KdoIgra("Računalnik"));
            Coordinator.kdoIgra.put(Player.WHITE, new KdoIgra("Računalnik"));
            Coordinator.playNewGame();
        }

    }
}
