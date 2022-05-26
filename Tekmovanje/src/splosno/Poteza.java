package splosno;

public record Poteza(int x, int y) {

	@Override
	public String toString() {
		return "Poteza [x=" + x + ", y=" + y + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		Poteza k = (Poteza) o;
		return this.x == k.x && this.y == k.y;
	}
}