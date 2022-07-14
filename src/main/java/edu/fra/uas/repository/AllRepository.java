package edu.fra.uas.repository;

import java.util.ArrayList;

import edu.fra.uas.bet.Bet;
import edu.fra.uas.match.Match;
import edu.fra.uas.user.User;
import org.springframework.stereotype.Repository;

@Repository
public class AllRepository {

	// repository for the lists of matches bets and user
	public ArrayList<User> userlist = new ArrayList<>();
	public ArrayList<Match> matches = new ArrayList<>();
	public ArrayList<Bet> betlist = new ArrayList<>();

}
