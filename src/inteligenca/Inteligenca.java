package inteligenca;

import com.sun.source.tree.Tree;
import logika.Igra;
import logika.Player;
import logika.Status;
import splosno.KdoIgra;
import splosno.Poteza;

import java.util.*;


public class Inteligenca extends KdoIgra {

    private static final String TEAMNAME = "IMEEKIPE";
    private static final double C = Math.sqrt(2); /* constant in UCT function */
    private static final int n = 100; /* number of iterations */

    public HashMap<TreeIndex, TreeEntry> tree;

    public Inteligenca() {
        super(TEAMNAME);
    }

    public Poteza izberiPotezo(Igra igra) {
        tree = new HashMap<TreeIndex, TreeEntry>();
        TreeEntry startNode = new TreeEntry(igra);
        TreeIndex startPath = new TreeIndex();
        tree.put(startPath, startNode);
        int i = 0;
        while (i < n) {
            traverse(startNode, startPath);
            i++;
        }
        return bestMove(startPath, tree.get(startPath));
    }


    private static Random random1 = new Random ();

    private void traverse(TreeEntry node, TreeIndex path){
        if (fullyExpanded(node, path)) {
            double maxUCT = 0;
            TreeIndex maxPath = null;
            TreeEntry maxNode = null;
            for (Map.Entry<TreeIndex, TreeEntry> child: children(path, node).entrySet()) {
                TreeIndex childPath = child.getKey();
                TreeEntry childNode = child.getValue();
                double uct = getUCT(childNode, node);
                if (uct > maxUCT) {
                    maxUCT = uct;
                    maxNode = childNode;
                    maxPath = childPath;
                }
            }
            traverse(maxNode, maxPath);
        }
        else {
            HashMap<TreeIndex, TreeEntry> unvisited = new HashMap<TreeIndex, TreeEntry>();
            for (Map.Entry<TreeIndex, TreeEntry> child: children(path, node).entrySet()) {
                if (!visited(child.getValue())) unvisited.put(child.getKey(), child.getValue());
            }
            int randomIndex = random1.nextInt(unvisited.size());
            TreeIndex chosenPath = (TreeIndex) unvisited.keySet().toArray()[randomIndex];
            TreeEntry chosenNode = unvisited.get(chosenPath);
            Status result = rollout(chosenNode);
            backpropagation(chosenPath, chosenNode, result);
        }
    }

    private boolean visited(TreeEntry node){
        return node.visits > 0;
    }

    private boolean fullyExpanded(TreeEntry node, TreeIndex path){
        Set<Poteza> possibleMoves = node.game.possibleMoves.keySet();
        if (possibleMoves.isEmpty()) return true;
        for (Poteza p: possibleMoves) {
            TreeIndex newPath = path.child(p);
            if (!tree.containsKey(newPath)) return false;
        }
        return true;
    }


    private double getUCT(TreeEntry child, TreeEntry parent){
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
        if (player == Player.WHITE && status == Status.WHITE_WINS || player == Player.BLACK && status == Status.BLACK_WINS) node.wins += 1;
        if (tree.containsKey(path)) tree.replace(path, node);
        else tree.put(path, node);
        if (!path.isRoot()) {
            TreeIndex parentPath = path.parent();
            TreeEntry parentNode = tree.get(parentPath);
            backpropagation(parentPath, parentNode, status);
        }
    }

    private static Random random2 = new Random ();

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
    private Status rollout(TreeEntry node){
        Igra nextGame = node.game.copyOf();
        while (nextGame.status.equals(Status.IN_PROGRESS)) {
            Poteza move = rolloutPolicy(nextGame);
            nextGame.odigraj(move);
        }
        return nextGame.status;
    }

    /**
     *
     * @param path
     * @param node
     * @return all children nodes of a given node
     */
    public HashMap<TreeIndex, TreeEntry> children(TreeIndex path, TreeEntry node){
        Set<Poteza> possibleMoves = node.game.possibleMoves.keySet();
        HashMap<TreeIndex, TreeEntry> children = new HashMap<TreeIndex, TreeEntry>();
        for (Poteza p: possibleMoves) {
            TreeIndex childPath = path.child(p);
            if (tree.containsKey(childPath)) {
                children.put(childPath, tree.get(childPath));
            }
            else children.put(childPath, new TreeEntry(node.game.copyOf()));
        }
        return children;
    }

    /**
     *
     * @param path
     * @param node
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
    /* TODO: what to do if maxPath == null */
}
