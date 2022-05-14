package inteligenca;

import com.sun.source.tree.Tree;
import logika.Igra;
import logika.Status;
import splosno.KdoIgra;
import splosno.Poteza;

import java.util.*;


public class Inteligenca extends KdoIgra {

    private static final String TEAMNAME = "IMEEKIPE";
    private static final double C = Math.sqrt(2);

    public HashMap<TreeIndex, TreeEntry> tree;

    public Inteligenca() {
        super(TEAMNAME);
    }

    public Poteza izberiPotezo(Igra igra) {
        return new Poteza(0, 0);
    }


    private TreeEntry traverse(TreeEntry node, TreeIndex path){
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
        }
        else {
            /*
            Pick a random unexplored node
             */
        }
        return node;
    }
    /*TODO: traverse function */


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
     * @param win whether the game resulted in a win
     */
    private void backpropagation(TreeIndex path, TreeEntry node, boolean win){
        node.visits += 1;
        if (win) node.wins += 1;
        if (tree.containsKey(path)) tree.replace(path, node);
        else tree.put(path, node);
        if (!path.isRoot()) {
            TreeIndex parentPath = path.parent();
            TreeEntry parentNode = tree.get(parentPath);
            backpropagation(parentPath, parentNode, win);
        }
    }

    private static Random random = new Random ();

    private Poteza rolloutPolicy(Igra game){
        Set<Poteza> possibleMoves = game.possibleMoves.keySet();
        int randomIndex = random.nextInt(possibleMoves.size());
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
}
