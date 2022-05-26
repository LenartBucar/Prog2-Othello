package coordinator;

import logika.Player;

public enum PlayerType {
    C, H;

    @Override
    public String toString() {
        switch (this) {
            case C: return "računalnik";
            case H: return "človek";
            default: assert false; return "";
        }
    }
}
