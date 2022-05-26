package othello;

import gui.Window;
import logika.*;

public class Othello {
    public static void main(String[] args) {
        Igra game = new Igra();
        Window window = new Window();
        window.pack();
        window.setVisible(true);
        window.canvas.setGame(game);
    }
}
