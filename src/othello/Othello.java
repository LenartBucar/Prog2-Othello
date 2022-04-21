package othello;

import logika.*;
import splosno.Poteza;

public class Othello {
    public static void main(String[] args) {
        Igra i1 = new Igra();
        System.out.println(i1);
        i1.odigraj(new Poteza(3, 2));
        System.out.println(i1);
    }
}
