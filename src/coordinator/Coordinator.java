package coordinator;

import gui.Window;
import logika.Igra;
import logika.Player;
import splosno.KdoIgra;
import splosno.Poteza;

import javax.swing.*;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Coordinator {
    public static Map<Player,PlayerType> playerTypeMap;
    public static Map<Player, KdoIgra> kdoIgra;

    public static Window window;
    public static Igra game = null;
    public static boolean HumanTurn = false;


    public static void playNewGame () {
        game = new Igra ();
        window = new Window();
        window.pack();
        window.setVisible(true);
        window.canvas.setGame(game);
        play();
    }

    public static void play () {
        window.refreshGUI();
        switch (game.status) {
            case BLACK_WINS, WHITE_WINS, DRAW:
                return;
            case IN_PROGRESS:
                Player player = game.getPlayer();
                PlayerType playerType = playerTypeMap.get(player);
                switch (playerType) {
                    case C -> playComputerMove();
                    case H -> HumanTurn = true;
                }
        }
    }

    private static Random random = new Random();
/*
    public static void playComputerMove(Igra game) {
        Poteza[] possibleMoves = game.allPossible().keySet().toArray(new Poteza[0]);
        int randomIndex = random.nextInt(possibleMoves.length);
        Poteza move = possibleMoves[randomIndex];
        game.odigraj(move);
        play();
    }

 */

    public static void playComputerMove() {
        Igra zacetkaIgra = game;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
                return null;
            }
            @Override
            protected void done () {
                if (game != zacetkaIgra) return;
                Poteza[] possibleMoves = game.getPossibleMoves().keySet().toArray(new Poteza[0]);
                int randomIndex = random.nextInt(possibleMoves.length);
                Poteza poteza = possibleMoves[randomIndex];
                game.odigraj(poteza);
                play();
            }
        };
        worker.execute();
    }

    public static void playHumanMove(Poteza poteza) {
        if (game.odigraj(poteza)) HumanTurn = false;
        play();
    }

}
