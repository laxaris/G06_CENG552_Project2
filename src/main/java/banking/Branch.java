package banking;

public class Branch {
	private int id;
	private String name;
	private Bank bank;
	
	public Branch(int id, String name, Bank bank) {
		this.id = id;
		this.name = name;
		this.bank = bank;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Bank getBank() {
		return bank;
	}
}
