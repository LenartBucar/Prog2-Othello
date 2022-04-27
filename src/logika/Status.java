package logika;


public enum Status {
        IN_PROGRESS, BLACK_WINS, WHITE_WINS, DRAW;

        Status() {}

        @Override
        public String toString() {
                if (this == IN_PROGRESS) return "V teku";
                else if (this == BLACK_WINS) return "Zmaga črni igralec";
                else if (this == WHITE_WINS) return "Zmaga beli igralec";
                else return "Neodločeno";
        }
}
