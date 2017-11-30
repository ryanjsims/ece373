
public abstract class Person {
	private String name;
	protected int maxBooks;

	public String getName() {
		return this.name;
	}

	public int getMaxBooks() {
		return this.maxBooks;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaxBooks(int maxBooks) {
		this.maxBooks = maxBooks;
	}

	public abstract void borrowBook( Book b1 );

	public abstract void returnBook( Book b1 );
}
