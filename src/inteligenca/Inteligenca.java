package inteligenca;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Poteza;


public class Inteligenca extends KdoIgra {

    private static final String TEAMNAME = "IMEEKIPE";

    public Inteligenca() {
        super(TEAMNAME);
    }

    public Poteza izberiPotezo(Igra igra) {
        return new Poteza(0, 0);
    }

}
