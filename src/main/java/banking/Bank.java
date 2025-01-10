package banking;

public class Bank {
	String name;
	int bankCode;
	
	public Bank(String name, int bankCode) {
		this.name = name;
		this.bankCode = bankCode;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBankCode() {
		return bankCode;
	}
}
