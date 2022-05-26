package inteligenca;

import logika.Igra;

public class TreeEntry {
    public Igra game;
    public int visits;
    public double wins;

    public TreeEntry(Igra game){
        this.game = game;
        this.visits = 0;
        this.wins = 0;
    }

}
