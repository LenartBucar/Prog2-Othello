package othello;

import logika.*;
import splosno.Poteza;

import java.util.Scanner;

public class Othello {
    public static void main(String[] args) {
        play();
    }

    public static void play() {
        Igra game = new Igra();
        Scanner in = new Scanner(System.in);
        System.out.println(game);
        boolean cont = true;
        while (cont) {
            boolean hasPlayed = false;
            while (!hasPlayed) {
                System.out.println("PLAYER " + game.getPlayer());
                System.out.println("Enter x coordinate:");
                int x = in.nextInt();
                if (x == -1) {cont = false; break;}
                System.out.println("Enter y coordinate:");
                int y = in.nextInt();
                hasPlayed = game.odigraj(new Poteza(x, y));
                if (!hasPlayed) {
                    System.out.println("Invalid move. Try again.");
                }
            }
            System.out.println(game);
        }

    }
}
