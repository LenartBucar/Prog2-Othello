package inteligenca;

import java.util.LinkedList;
import java.util.List;

import splosno.Poteza;


public final class TreeIndex {
	
	private final LinkedList<Poteza> potList;
	
	public TreeIndex () {
		this.potList = new LinkedList<> ();
	}

	public TreeIndex (TreeIndex ti, Poteza p) {
		// Append p to a deep copy of ti
		this.potList = new LinkedList<>();
		for (Poteza q : ti.potList) this.potList.addLast(q);
		this.potList.addLast(p);
	}

	public TreeIndex child(Poteza p) {
		return new TreeIndex(this, p);
	}
	
	public boolean isRoot() {
		return potList.isEmpty();
	}
	
	public List<Poteza> asList() {
		return potList;
	}
	
	public Poteza lastMove() {
		return potList.getLast();
	}
	
	public TreeIndex parent() {
		TreeIndex ti = new TreeIndex();
		for (Poteza p : this.potList) ti.potList.addLast(p);
		ti.potList.removeLast();
		return ti;
	}

	@Override 
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		TreeIndex k = (TreeIndex) o;
		return this.potList.equals(k.potList);
	}
	
	@Override
	public int hashCode () {
		return this.potList.hashCode();
	}

	@Override
	public String toString() {
		return potList.toString();
	}

}
