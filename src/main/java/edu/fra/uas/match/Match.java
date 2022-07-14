package edu.fra.uas.match;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Match {
	private String matchname;
	private String team1;
	private String team2;
	@JsonIgnore
	private String status = "open";

	public Match(String matchname, String team1, String team2) {
		super();
		this.matchname = matchname;
		this.team1 = team1;
		this.team2 = team2;
	}

	public Match(String matchname) {
		super();
		this.matchname = matchname;
	}

	// default constructor
	public Match() {
		super();
	}

	// getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMatchname() {
		return matchname;
	}

	public void setMatchname(String matchname) {
		this.matchname = matchname;
	}

	public String getTeam1() {
		return team1;
	}

	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	public String getTeam2() {
		return team2;
	}

	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	@Override
	public String toString() {
		return "Match " + " -> " + matchname + "(Team1:" + team1 + " vs. Team2:" + team2 + ")";
	}

}