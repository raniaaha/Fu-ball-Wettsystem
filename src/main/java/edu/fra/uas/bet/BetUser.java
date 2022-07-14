package edu.fra.uas.bet;

public class BetUser {
	private String matchname;
	private String type;
	private int usersAmount;

	public BetUser(String matchname, String type, int usersAmount) {
		super();
		this.matchname = matchname;
		this.type = type;
		this.usersAmount = usersAmount;
	}

	// getter and setter
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

	public int getUsersAmount() {
		return usersAmount;
	}

	public void setUsersAmount(int usersAmount) {
		this.usersAmount = usersAmount;
	}
}
