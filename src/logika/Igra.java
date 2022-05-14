package logika;

import splosno.Poteza;

import java.util.*;

public class Igra {
    public final int sizeX, sizeY;
    private Player player;
    public Set<Poteza> boundary;
    public Map<Poteza, HashMap<Direction, Integer>> possibleMoves;

    private final Player[][] board;

    public Status status;

    /**
     * Contains all valid directions to allow streaks in.
     */
    private final Direction[] allDirections = {
            new Direction(-1, -1),
            new Direction(-1, 0),
            new Direction(-1, 1),
            new Direction(0, -1),
            new Direction(0, 1),
            new Direction(1, -1),
            new Direction(1, 0),
            new Direction(1, 1)
    };

    public Player getPlayer() {
        return player;
    }

    public Player[][] getBoard() {
        return board;
    }

    public Status getStatus(){
        return status;
    }

    public Set<Poteza> getBoundary() {return boundary; }

    public Igra(int x, int y) {
        sizeX = x;
        sizeY = y;
        player = Player.BLACK;
        board = new Player[sizeX][sizeY];
        status = Status.IN_PROGRESS;
        int cx = x/2 - 1;
        int cy = y/2 - 1;
        board[cx][cy] = board[cx + 1][cy + 1] = Player.WHITE;
        board[cx][cy + 1] = board[cx + 1][cy] = Player.BLACK;
        boundary = new HashSet<>();
        for (int i = cx - 1; i < cx + 3; i = i+3) {
            for (int j = cy - 1; j < cy + 3; j++) {
                boundary.add(new Poteza(i, j));
            }
        }
        for (int i = cx; i < cx + 2; i++) {
            for (int j = cy - 1; j < cy + 3; j = j + 3) {
                boundary.add(new Poteza(i, j));
            }
        }
    }


    public Igra() {
        this(8, 8);
    }

    public void updateBoundary(Poteza poteza) {
        int x = poteza.getX();
        int y = poteza.getY();
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                try {
                    if (board[i][j] == null) boundary.add(new Poteza(i, j));
                } catch (ArrayIndexOutOfBoundsException ignored) {};
            }
        }
        boundary.remove(poteza);
        updatePossibleMoves();
    }

    public void updatePossibleMoves() {
        Map<Poteza, HashMap<Direction, Integer>> newPossible = getPossibleMoves();
        if (newPossible.isEmpty()) {
            player = swapPlayer(player);
            Map<Poteza, HashMap<Direction, Integer>> possible1 = getPossibleMoves();
            if (possible1.isEmpty()) setWinner();
            else possibleMoves = possible1;
        }
        else possibleMoves = newPossible;
    }

    /**
     * @param poteza Location of the move to make
     * @return `true` if the move was successful, `false` if it was invalid
     */
    public boolean odigraj(Poteza poteza) {
        if (!boundary.contains(poteza)) return false;
        Map<Direction, Integer> directions = getDirections(poteza);
        if (directions.isEmpty()) {return false;}
        flipSquares(poteza, directions);
        board[poteza.getX()][poteza.getY()] = player;
        player = swapPlayer(player);
        updateBoundary(poteza);
        return true;
    }

    /**
     * counts discs of each color and sets status to the right winner
     */
    private void setWinner(){
        int[] count = count();
        int black = count[0];
        int white = count[1];
        if (black < white) status = Status.WHITE_WINS;
        else if (black > white) status = Status.BLACK_WINS;
        else status = Status.DRAW;
    }

    /**
     * @return array of numbers of black and white discs
     */
    private int[] count(){
        int black = 0;
        int white = 0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (board[i][j] == Player.BLACK) black += 1;
                else if (board[i][j] == Player.WHITE) white += 1;
            }
        }
        return new int[]{black, white};
    }

    /**
     *
     * @return all possible moves and which directions are valid for specific move
     */
    public Map<Poteza, HashMap<Direction, Integer>> getPossibleMoves(){
        Map<Poteza, HashMap<Direction, Integer>> possible = new HashMap<>();
        for (Poteza p: boundary) {
            HashMap<Direction, Integer> directions = getDirections(p);
            if (!directions.isEmpty()) possible.put(p, directions);
        }
        return possible;
    }


    /**
     * Flips the colour of the given square
     * @param x x-coordinate of the square to flip
     * @param y y-coordinate of the square to flip
     * @throws IllegalArgumentException if there is no token on the square yet
     */
    private void flipSquare(int x, int y) throws IllegalArgumentException{
        if (board[x][y] == null) {throw new IllegalArgumentException("Attempting to flip a non-flippable square");}
        board[x][y] = swapPlayer(board[x][y]);
    }

    /**
     * Flips all squares in all valid streaks originating at the given square
     * @param poteza Location where to start the flipping
     * @param directions Direction and length of all valid streaks
     */
    private void flipSquares(Poteza poteza, Map<Direction, Integer> directions) {
        for (Map.Entry<Direction, Integer> dir: directions.entrySet()) {
            Direction direction = dir.getKey();
            Integer distance = dir.getValue();
            for (int i = 1; i <= distance; i++) {
                flipSquare(poteza.getX() + i*direction.getX(), poteza.getY() + i*direction.getY());
            }
        }
    }

    /**
     * @param poteza Played move
     * @return Map of all directions in which the streak is valid and the length of the streak
     */
    private HashMap<Direction, Integer> getDirections(Poteza poteza) {
        HashMap<Direction, Integer> distances = new HashMap<>();
        for (Direction direction: allDirections) {
            Integer d = checkDirection(poteza, direction);
            if (d != null && d != 0 ) {distances.put(direction, d);}
        }
        return distances;
    }

    /**
     * @param poteza Location from where to perform the search
     * @param direction direction in which to search
     * @return number of consecutive tokens of the opposite colour that end with a token of the same colour or `null` if given direction is not a valid streak
     */
    private Integer checkDirection(Poteza poteza, Direction direction) {
        int dX = switch (direction.getX()) {
            case -1 -> poteza.getX();
            case 0 -> Integer.MAX_VALUE;
            case 1 -> sizeX - poteza.getX();
            default -> throw new IllegalArgumentException("Step X must be of size -1, 0, or 1.");
        };
        int dY = switch (direction.getY()) {
            case -1 -> poteza.getY();
            case 0 -> Integer.MAX_VALUE;
            case 1 -> sizeY - poteza.getY();
            default -> throw new IllegalArgumentException("Step Y must be of size -1, 0, or 1.");
        };
        int dist = Math.min(dX, dY);
        for (int i = 1; i < dist; i++) {
            int x = poteza.getX() + i*direction.getX();
            int y = poteza.getY() + i*direction.getY();
            Player c = board[x][y];
            if (c == null) {return null;}
            else if (c == player) {return i-1;}
        }
        return null;
    }
    /*TODO: check why it doesn't flip some squares(on edges of board) */

    private Player swapPlayer(Player player) {
        if (player.equals(Player.BLACK)) return Player.WHITE;
        return Player.BLACK;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (Player[] row: board) {
            for (Player p: row) {
                if (p == null) {
                    sb.append(" ");
                } else {
                    sb.append(p);
                }
                sb.append("|");
            }
            sb.append("\n");
            sb.append("-+".repeat(sizeX));
            sb.append("\n");
        }
        return sb.toString();
    }

    public Igra copyOf() {
        Igra cp = new Igra(this.sizeX, this.sizeY);
        for (int i = 0; i < this.board.length; i++) {
            cp.board[i] = Arrays.copyOf(this.board[i], this.board[i].length);
        }
        return cp;
    }
}

class Direction {
    private final int stepX, stepY;
    private static final ArrayList<Integer> VALID_STEPS = new ArrayList<>(Arrays.asList(-1, 0, 1));
    Direction(int x, int y) {
        if (!VALID_STEPS.contains(x) || !VALID_STEPS.contains(y) || (x == 0 && y == 0)) {
            throw new IllegalArgumentException("Invalid argument provided. X and Y must be from (-1, 0, 1) and not both 0");
        }
        stepX = x;
        stepY = y;
    }

    public int getX() {
        return stepX;
    }

    public int getY() {
        return stepY;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Direction{");
        sb.append("stepX=").append(stepX);
        sb.append(", stepY=").append(stepY);
        sb.append('}');
        return sb.toString();
    }
}

