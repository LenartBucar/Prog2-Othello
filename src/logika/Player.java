package logika;

import coordinator.PlayerType;

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

    public boolean status(Object o) {
        if (o.getClass() != Status.class) {
            return false;
        }
        return this == Player.WHITE && o == Status.WHITE_WINS || this == Player.BLACK && o == Status.BLACK_WINS;
    }

    public Color getColour() {
        return colour;
    }

    public Player copyOf() {
        return this;
        };
}
