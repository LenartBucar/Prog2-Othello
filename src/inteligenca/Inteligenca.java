package inteligenca;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Poteza;

import java.util.HashMap;


public class Inteligenca extends KdoIgra {

    private static final String TEAMNAME = "IMEEKIPE";

    public HashMap<TreeIndex, TreeEntry> tree;

    public Inteligenca() {
        super(TEAMNAME);
    }

    public Poteza izberiPotezo(Igra igra) {
        return new Poteza(0, 0);
    }


}
