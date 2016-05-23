package bean;

public class Transaction {

	private int id;
	private int numberOfItems;
	private int price;
	
	public Transaction(int id, int noItems, int price) {
		this.id = id;
		this.numberOfItems = noItems;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Transaction() {
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return this.getId()+", "+this.getNumberOfItems() + ", " + this.getPrice();
	}
}
