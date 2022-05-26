package inteligenca;

import logika.Igra;
import logika.Player;
import logika.Status;
import splosno.KdoIgra;
import splosno.Poteza;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static logika.Status.IN_PROGRESS;


public class Inteligenca extends KdoIgra {

    private static final String TEAMNAME = "IMEEKIPE";
    private static final double C = Math.sqrt(2); /* constant in UCT function */
    private static final int MOVE_TIME_SEC = 4; /* Amount of time per move */

    private Map<TreeIndex, TreeEntry> tree;

    public Inteligenca() {
        super(TEAMNAME);
    }

    /** Selects the best move for a given game
     * @param igra game, for which the move should be selected
     * @return move to be played
     */
    public Poteza izberiPotezo(Igra igra) {
        if (igra.possibleMoves.size() == 1) return (Poteza) igra.possibleMoves.keySet().toArray()[0];  /* If only one move is possible, play that move */
        tree = new HashMap<>();
        TreeEntry startNode = new TreeEntry(igra);
        TreeIndex startPath = new TreeIndex();
        tree.put(startPath, startNode);
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(MOVE_TIME_SEC); System.nanoTime() < stop;) {
            traverse(startNode,startPath);
        }
        return bestMove(startPath, tree.get(startPath));
    }


    private static final Random random1 = new Random ();

    /** Traverses the possible moves, performs MCTS
     * @param node starting node
     * @param path path to the starting node
     */
    private void traverse(TreeEntry node, TreeIndex path){
        if (node.game.status != IN_PROGRESS) return;
        if (fullyExpanded(node, path)) {  /* Check the node with the max UCT */
            double maxUCT = 0;
            TreeIndex maxPath = null;
            TreeEntry maxNode = null;
            for (Map.Entry<TreeIndex, TreeEntry> child : children(path, node).entrySet()) {
                TreeIndex childPath = child.getKey();
                TreeEntry childNode = child.getValue();
                double uct = getUCT(childNode, node);
                if (uct >= maxUCT) {
                    maxUCT = uct;
                    maxNode = childNode;
                    maxPath = childPath;
                }
            }
            assert maxNode != null;
            traverse(maxNode, maxPath);
        }
        else {  /* pick a random unexplored node */
            HashMap<TreeIndex, TreeEntry> unvisited = new HashMap<>();
            for (Map.Entry<TreeIndex, TreeEntry> child : children(path, node).entrySet()) {
                if (!visited(child.getValue())) unvisited.put(child.getKey(), child.getValue());
            }
            int randomIndex = random1.nextInt(unvisited.size());
            TreeIndex chosenPath = (TreeIndex) unvisited.keySet().toArray()[randomIndex];
            TreeEntry chosenNode = unvisited.get(chosenPath);
            Status result = rollout(chosenPath, chosenNode);
            backpropagation(chosenPath, chosenNode, result);
        }
    }

    private boolean visited(TreeEntry node){
        return node.visits > 0;
    }

    private boolean fullyExpanded(TreeEntry node, TreeIndex path){
        if (node == null) return true;
        Set<Poteza> possibleMoves = node.game.possibleMoves.keySet();
        if (possibleMoves.isEmpty()) return true;
        for (Poteza p: possibleMoves) {
            TreeIndex newPath = path.child(p);
            if (!tree.containsKey(newPath)) return false;
        }
        return true;
    }


    /** Calculates UCT score for the given node
     * @param child node
     * @param parent path to the node
     * @return UCT score
     */
    private double getUCT(TreeEntry child, TreeEntry parent){
        if (child.game.getPlayer() == parent.game.getPlayer()) {
            return child.wins / child.visits + C * Math.sqrt(Math.log(parent.visits) / child.visits);
        }
        return (child.visits - child.wins) / child.visits + C * Math.sqrt(Math.log(parent.visits) / child.visits);
    }

    /**
     * update statistics for every node till root node
     * @param path list of moves to get to this node
     * @param node old node at the current path
     * @param status result of game: BLACK_WINS / WHITE_WINS / DRAW
     */
    private void backpropagation(TreeIndex path, TreeEntry node, Status status){
        node.visits += 1;
        Player player = node.game.getPlayer();
        if (player.status(status)) node.wins += 1;
        if (tree.containsKey(path)) tree.replace(path, node);
        else tree.put(path, node);
        if (!path.isRoot()) {
            TreeIndex parentPath = path.parent();
            TreeEntry parentNode = tree.get(parentPath);
            backpropagation(parentPath, parentNode, status);
        }
    }

    private static final Random random2 = new Random ();

    /** Chooses which move to make, when MCTS is reaching the position for the first time (selects random move)
     * @param game game, where the move should be selected
     * @return selected move
     */
    private Poteza rolloutPolicy(Igra game){
        Set<Poteza> possibleMoves = game.possibleMoves.keySet();
        int randomIndex = random2.nextInt(possibleMoves.size());
        return (Poteza) possibleMoves.toArray()[randomIndex];
    }

    /**
     * plays a game from starting node choosing random moves until one player wins
     * @param node starting node
     * @return result of the game
     */
    private Status rollout(TreeIndex path, TreeEntry node){
        Igra nextGame = node.game.copyOf();
        nextGame.odigraj(path.lastMove());
        while (nextGame.status.equals(IN_PROGRESS)) {
            Poteza move = rolloutPolicy(nextGame);
            boolean played = nextGame.odigraj(move);
            assert played;
        }
        return nextGame.status;
    }

    /**
     *
     * @param path path to the parent node
     * @param node parent node
     * @return all children nodes of a given node
     */
    public Map<TreeIndex, TreeEntry> children(TreeIndex path, TreeEntry node){
        Set<Poteza> possibleMoves = node.game.possibleMoves.keySet();
        HashMap<TreeIndex, TreeEntry> children = new HashMap<>();
        for (Poteza p: possibleMoves) {
            TreeIndex childPath = path.child(p);
            if (tree.containsKey(childPath)) {
                children.put(childPath, tree.get(childPath));
            }
            else {
                Igra nextGame = node.game.copyOf();
                nextGame.odigraj(p);
                children.put(childPath, new TreeEntry(nextGame));
            }
        }
        return children;
    }

    /**
     *
     * @param path path to the parent node
     * @param node parent node
     * @return move which was explored the most durint MCTS
     */
    private Poteza bestMove(TreeIndex path, TreeEntry node){
        int maxVisits = 0;
        TreeIndex maxPath = null;
        for (Map.Entry<TreeIndex, TreeEntry> child: children(path, node).entrySet()) {
            if (child.getValue().visits > maxVisits) {
                maxPath = child.getKey();
                maxVisits = child.getValue().visits;
            }
        }
        assert maxPath != null;
        return maxPath.lastMove();
    }
}
