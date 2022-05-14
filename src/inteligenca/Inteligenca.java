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
     * @param node
     * @param win
     */
    private void backpropagation(TreeIndex path, TreeEntry node, boolean win){
        node.visits += 1;
        if (win) node.wins += 1;
        if (tree.containsKey(path)) tree.replace(path, node);
        else tree.put(path, node);
        if (!path.isRoot()) {
            TreeIndex newPath = path.parent();
            TreeEntry newNode = tree.get(newPath);
            backpropagation(newPath, newNode, win);
        }
    }

    private static Random random = new Random ();

    private Poteza rolloutPolicy(Igra game){
        Set<Poteza> possibleMoves = game.possibleMoves.keySet();
        int randomIndex = random.nextInt(possibleMoves.size());
        Poteza move = (Poteza) possibleMoves.toArray()[randomIndex];
        return move;
    }

    /**
     * plays a game from starting node choosing random moves until one player wins
     * @param node starting node
     * @return result of the game
     */
    private Status rollout(TreeEntry node){
        Igra newGame = node.game.copyOf();
        while (newGame.status.equals("IN_PROGRESS")) {
            Poteza move = rolloutPolicy(newGame);
            newGame.odigraj(move);
        }
        return newGame.status;
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
            TreeIndex newPath = path.child(p);
            if (tree.containsKey(newPath)) {
                children.put(newPath, tree.get(newPath));
            }
            else children.put(newPath, new TreeEntry(node.game.copyOf()));
        }
        return children;
    }
}
