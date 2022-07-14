package edu.fra.uas.user;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.fra.uas.bet.Bet;

public class User {

	private String username;
	private int score = 0;
	@JsonIgnore
	private ArrayList<Bet> betlist = new ArrayList<>();

	public User(String username) {
		super();
		this.username = username;
		this.score = 0;
	}

	public User(String username, int score, ArrayList<Bet> betlist) {
		super();
		this.username = username;
		this.score = score;
	}

	public User(String username, int score) {
		super();
		this.username = username;
		this.score = score;
	}

	// default constructor
	public User() {
		super();
	}

	// getter and setter
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public ArrayList<Bet> getBetlist() {
		return betlist;
	}

	public void setBetlist(ArrayList<Bet> betlist) {
		this.betlist = betlist;
	}

	@Override
	public String toString() {
		return "User " + " ->" + username + ", score:" + score;
	}
}