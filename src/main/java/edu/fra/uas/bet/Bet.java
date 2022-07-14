package edu.fra.uas.bet;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Bet {
	private String matchname;
	private String type;
	private int amount = -1;
	private int usersAmount;
	@JsonIgnore
	private double userCounter = 0;
	@JsonIgnore
	private double scoresum = 0;
	@JsonIgnore
	private double averagePoints;
	@JsonIgnore
	private double counterAccurateHits = 0;
	@JsonIgnore
	private double averageAccurateHits;

	public Bet(String matchname, String type) {
		super();
		this.matchname = matchname;
		this.type = type;
	}

	public Bet(String matchname, String type, int usersAmount) {
		super();
		this.matchname = matchname;
		this.type = type;
		this.usersAmount = usersAmount;
	}

	public Bet(String matchname, int amount, String type) {
		super();
		this.matchname = matchname;
		this.type = type;
		this.amount = amount;
	}

	// default Constructor
	public Bet() {
		super();
	}

	// Getter and Setter
	public double getCounterAccurateHits() {
		return counterAccurateHits;
	}

	public void setCounterAccurateHits(double counterAccurateHits) {
		this.counterAccurateHits = counterAccurateHits;
	}

	public double getAverageAccurateHits() {
		return averageAccurateHits;
	}

	public void setAverageAccurateHits(double averageAccurateHits) {
		this.averageAccurateHits = averageAccurateHits;
	}

	public double getAveragePoints() {
		return averagePoints;
	}

	public void setAveragePoints(double averagePoints) {
		this.averagePoints = averagePoints;
	}

	public double getUserCounter() {
		return userCounter;
	}

	public void setUserCounter(double userCounter) {
		this.userCounter = userCounter;
	}

	public double getScoresum() {
		return scoresum;
	}

	public void setScoresum(double scoresum) {
		this.scoresum = scoresum;
	}

	public int getUsersAmount() {
		return usersAmount;
	}

	public void setUsersAmount(int usersAmount) {
		this.usersAmount = usersAmount;
	}

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

	@Override
	public String toString() {
		return "Bet " + " -> " + matchname + " Type:" + type;
	}
}