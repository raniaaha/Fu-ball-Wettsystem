package edu.fra.uas.bet;

public class BetAdmin {
	private String matchname;
	private String type;
	private int amount = -1;

	public BetAdmin(String matchname, String type, int amount) {
		super();
		this.matchname = matchname;
		this.type = type;
		this.amount = amount;
	}

	// Getter and Setter
	public String getMatchname() {
		return matchname;
	}

	public void setMatchname(String matchname) {
		this.matchname = matchname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
