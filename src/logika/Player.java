package logika;

import java.awt.*;

public enum Player {
    WHITE("O", Color.WHITE), BLACK("X", Color.BLACK);
    final String playerName;
    final Color colour;

    Player(String playerName, Color colour) {
        this.playerName = playerName;
        this.colour = colour;
    }

    @Override
    public String toString() {
        return playerName;
    }

    public Color getColour() {
        return colour;
    }

    public Player copyOf() {
        return this;
        };
}
