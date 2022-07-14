package edu.fra.uas.bet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fra.uas.match.Match;
import edu.fra.uas.repository.AllRepository;
import edu.fra.uas.user.User;

@Service
public class BetService {

	@Autowired
	AllRepository repository;

	public BetService(AllRepository repository) {
		super();
		this.repository = repository;
	}

	public ArrayList<Bet> getBetList() {
		return repository.betlist;
	}

	// method to say if the bet is in the betlist
	public boolean getBet(String matchname, String type) {
		// loop over the betlist
		for (int i = 0; i < repository.betlist.size(); i++) {
			// if the matchname and the type equal the given matchname and type it returns
			// true
			if (repository.betlist.get(i).getMatchname().toLowerCase().equals(matchname.toLowerCase())
					&& repository.betlist.get(i).getType().toLowerCase().equals(type.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	// method to add the bet into the betlist
	public boolean createBet(Bet bet) {
		// if checkBet is true
		if (checkBet(bet)) {
			// add bet to betlist
			repository.betlist.add(bet);
			System.out.println("Bet " + bet.getMatchname() + " ,type: " + bet.getType() + " created");
			return true;
		}
		return false;
	}

	// method to give the bet by the matchname and the type
	public Bet getBetbyMatchname(String matchname, String type) {
		if (!repository.betlist.isEmpty()) {
			for (int i = 0; i < repository.betlist.size(); i++) {
				if (repository.betlist.get(i).getMatchname().toLowerCase().equals(matchname.toLowerCase())
						&& repository.betlist.get(i).getType().toLowerCase().equals(type.toLowerCase())) {
					return repository.betlist.get(i);
				}
			}
		}
		return null;
	}

	// method to check if the matchname of the bet is given in the matchlist
	public boolean checkBet(Bet bet) {
		if (!repository.matches.isEmpty()) {
			for (int i = 0; i < repository.matches.size(); i++) {
				if (bet.getMatchname().toLowerCase().equals(repository.matches.get(i).getMatchname().toLowerCase())) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	// method to add the true amount
	public boolean add_amount(Bet bet) {
		// attributes of bet
		String betname = bet.getMatchname();
		String bettype = bet.getType();
		int betAmount = bet.getAmount();
		for (int i = 0; i < repository.betlist.size(); i++) {
			// checks if the matchname and matchtype is equal to a bet in the betlist to add
			// the real amount
			if (repository.betlist.get(i).getMatchname().toLowerCase().equals(betname.toLowerCase())) {
				if (repository.betlist.get(i).getType().toLowerCase().equals(bettype.toLowerCase())) {
					if (repository.betlist.get(i).getAmount() == -1) {
						repository.betlist.get(i).setAmount(betAmount);
						System.out.println(repository.betlist.get(i).getAmount());
						Match match = getMatch(repository.betlist.get(i).getMatchname());
						match.setStatus("closed");
						return true;
					}
				}
			}
		}
		return false;
	}

	// method to get the match
	public Match getMatch(String matchname) {
		for (int i = 0; i < repository.matches.size(); i++) {
			if (repository.matches.get(i).getMatchname().toLowerCase().equals(matchname.toLowerCase())) {
				return repository.matches.get(i);
			}
		}
		return null;
	}

	// check the amount to give points
	public void amount_check() {
		// for each user
		if (!repository.userlist.isEmpty()) {
			for (int d = 0; d < repository.userlist.size(); d++) {
				System.out.println("hat geklappt");
				User user = repository.userlist.get(d);
				// System.out.println(user.getBetlist());
				// for the betlist of user d
				if (!user.getBetlist().isEmpty()) {
					for (int i = 0; i < user.getBetlist().size(); i++) {
						System.out.println("und es klappt immernoch");
						if (!repository.betlist.isEmpty()) {
							System.out.println("es klappt");
							for (int j = 0; j < repository.betlist.size(); j++) {
								System.out.println("Es klappt immer noch");
								if (user.getBetlist().get(i).getMatchname().toLowerCase()
										.equals(repository.betlist.get(j).getMatchname().toLowerCase())
										&& user.getBetlist().get(i).getType().toLowerCase()
												.equals(repository.betlist.get(j).getType().toLowerCase())) {
									System.out.println("Es geht immer noch");
									if (repository.betlist.get(j).getAmount() != -1) {
										int scoreBefore = user.getScore();
										// the difference between the real amount and the amount of the bet of the user
										int difference = (repository.betlist.get(j).getAmount())
												- (user.getBetlist().get(i).getUsersAmount());
										System.out.println(difference_check(difference, user));
										int scoreAfter = user.getScore();
										int scoredifference = scoreAfter - scoreBefore;
										repository.betlist.get(j)
												.setScoresum(repository.betlist.get(j).getScoresum() + scoredifference);
										repository.betlist.get(j).setCounterAccurateHits(
												repository.betlist.get(j).getCounterAccurateHits() + 1);
										user.getBetlist().remove(i);

									} else {
										System.out.println("No amount given yet");

									}
								}
							}
						}
					}
				}
			}
		}
	}

	// method to check if the betlist of a user is empty
	public boolean betlistCheck() {
		for (int d = 0; d < repository.userlist.size(); d++) {
			if (repository.userlist.get(d).getBetlist().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	// method to set the score
	public String difference_check(int difference, User user) {
		int score = user.getScore();
		String username = user.getUsername();
		// checks the differnece to set the score
		if (difference == 0) {
			user.setScore(score + 5);
			score += 5;
			return "Score of " + username + ": " + score;
		} else if (difference == -1 || difference == 1) {
			user.setScore(score + 3);
			score += 3;
			return "Score of " + username + ": " + score;
		} else if (difference == -2 || difference == 2) {
			user.setScore(score + 1);
			score += 1;
			return "Score of " + username + ": " + score;
		} else {
			return "No more points for " + username + "\tScore: " + score;
		}
	}

	// method to equal the bet with the official betlist
	public boolean checkBetinBetlist(String matchname, String betMatchname, String type, String betType) {
		// method to check if the betattributes that the user wants to add equals the
		// bet i of the official betlist
		if (matchname.toLowerCase().equals(betMatchname.toLowerCase())
				&& type.toLowerCase().equals(betType.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	// method to equal the bet with the usersbets
	public boolean checkBetinUsersBetlist(User user, String betmatchname, String betType) {
		// if the betmatchname is not in a bet in the betlist of the user or if the
		// betmatchname is in a bet in the betlist of the user but the type is not in it
		for (int i = 0; i < user.getBetlist().size(); i++) {
			if (user.getBetlist().get(i).getMatchname().toLowerCase().equals(betmatchname.toLowerCase())
					&& user.getBetlist().get(i).getType().toLowerCase().equals(betType.toLowerCase())) {
				i = user.getBetlist().size();
				return true;
			}
		}
		return false;
	}

	// method to create usersbet
	public boolean usersBet(String username, Bet bet) {
		// System.out.println(repository.userlist.get(0));
		User user = getUser(username);
		// matchname of the bet the user wants to add
		String betMatchname = bet.getMatchname();
		// type of the bet the user wants to add
		String betType = bet.getType();
		// if the user exists in the userlist
		if (user != null) {
			System.out.println("User found");
			// if the betlist of the user is empty
			if (user.getBetlist().isEmpty()) {
				System.out.println("Betlist of User is empty");
				// goes for the official betlist
				for (int i = 0; i < repository.betlist.size(); i++) {
					// matchname of the bet i
					String matchnamebetlist = repository.betlist.get(i).getMatchname();
					// type of the bet i
					String typebetlist = repository.betlist.get(i).getType();
					// if its true the bet will be added to the betlist of the user
					if (checkBetinBetlist(matchnamebetlist, betMatchname, typebetlist, betType) == true) {
						System.out.println("Bet is in official betlist");
						// add bet to the betlist of the user
						user.getBetlist().add(bet);
						repository.betlist.get(i).setUserCounter(repository.betlist.get(i).getUserCounter() + 1);
						System.out.println("bet added to " + username + "'s betlist");
						// set i to the betlist size to go out of the loop
						i = repository.betlist.size();
						return true;
						// if the method for checking the attributes is false, the bet is not in the
						// official betlist
					} else {
						System.out.println("Bet isn't in official betlist");
					}
				}
				// if the betlist of the user is not empty
			} else {
				// goes over the betlist
				for (int i = 0; i < repository.betlist.size(); i++) {
					// matchname of the bet i
					String matchnamebetlist = repository.betlist.get(i).getMatchname();
					// type of the bet i
					String typebetlist = repository.betlist.get(i).getType();
					// if its true the bet will be added to the betlist of the user
					if (checkBetinBetlist(matchnamebetlist, betMatchname, typebetlist, betType) == true) {
						System.out.println("Bet is in official betlist");
						// if it is true add bet to users betlist
						if (checkBetinUsersBetlist(user, betMatchname, betType) == false) {
							// add the bet to users betlist
							user.getBetlist().add(bet);
							System.out.println("Bet added to " + username + "'s betlist");
							// set i on betlist size and j on users betlist size to get out of the loop
							i = repository.betlist.size();
							return true;
							// if the if condition is not true
						} else {
							System.out.println("Bet is already in " + username + "'s betlist");
							// set i on betlist size and j on users betlist size to go out of the loop
							i = repository.betlist.size();
						}
						// if bet is not in official betlist
					} else {
						System.out.println("Bet is not in official betlist");
					}
				}
			}
			// if user not found
		} else {
			System.out.println("User not found");
			return false;
		}
		return false;
	}

	// method to change the amount of the bet of the user
	public boolean changeUsersBetsAmount(String username, String matchname, String type, int newAmount) {
		User user = getUser(username);
		for (int i = 0; i < user.getBetlist().size(); i++) {
			if (user.getBetlist().get(i).getMatchname().toLowerCase().equals(matchname.toLowerCase())
					&& user.getBetlist().get(i).getType().toLowerCase().equals(type.toLowerCase())) {
				Bet bet = getBetbyMatchname(matchname, type);
				if (bet != null) {
					if (bet.getAmount() == -1) {
						user.getBetlist().get(i).setUsersAmount(newAmount);
						System.out.println("Amount changed " + user.getBetlist().get(i).getUsersAmount());
						i = user.getBetlist().size();
						return true;
					}
				}
			}
		}
		return false;
	}

	// method to get a user
	public User getUser(String username) {
		for (int i = 0; i < repository.userlist.size(); i++) {
			System.out.println(repository.userlist.get(i));
			if (repository.userlist.get(i).getUsername().toLowerCase().equals(username.toLowerCase())) {
				return repository.userlist.get(i);
			}
		}
		return null;
	}

	// get bet with the most points
	public ArrayList<BetAdmin> betWithMostPoints() {
		ArrayList<BetAdmin> bets = new ArrayList<>();

		ArrayList<Bet> rankinglist = repository.betlist;
		Collections.sort(rankinglist, new Comparator<Bet>() {
			public int compare(Bet bet1, Bet bet2) {
				return Double.valueOf(bet2.getScoresum()).compareTo(bet1.getScoresum());
			}
		});
		for (int i = 0; i < rankinglist.size(); i++) {
			bets.add(new BetAdmin(rankinglist.get(i).getMatchname(), rankinglist.get(i).getType(),
					rankinglist.get(i).getAmount()));
		}
		return bets;
	}

	// the most user did this bet
	public ArrayList<BetAdmin> betWithMostUser() {
		ArrayList<BetAdmin> bets = new ArrayList<>();
		ArrayList<Bet> rankinglist = repository.betlist;
		Collections.sort(rankinglist, new Comparator<Bet>() {
			public int compare(Bet bet1, Bet bet2) {
				return Double.valueOf(bet2.getUserCounter()).compareTo(bet1.getUserCounter());
			}
		});
		for (int i = 0; i < rankinglist.size(); i++) {
			bets.add(new BetAdmin(rankinglist.get(i).getMatchname(), rankinglist.get(i).getType(),
					rankinglist.get(i).getAmount()));
		}
		return bets;
	}

	// set average points for each
	public void averagePointsofbet() {
		for (int i = 0; i < repository.betlist.size(); i++) {
			double average = repository.betlist.get(i).getScoresum() / repository.betlist.get(i).getUserCounter();
			double roundDouble = (int) Math.round(average * 100) / 100;
			repository.betlist.get(i).setAveragePoints(roundDouble);
		}
	}

	// Bet with the highest average points
	public ArrayList<BetAdmin> betWithHighestAveragePoints() {
		averagePointsofbet();
		ArrayList<BetAdmin> bets = new ArrayList<>();
		ArrayList<Bet> rankinglist = repository.betlist;
		Collections.sort(rankinglist, new Comparator<Bet>() {
			public int compare(Bet bet1, Bet bet2) {
				return Double.valueOf(bet2.getAveragePoints()).compareTo(bet1.getAveragePoints());
			}
		});
		for (int i = 0; i < rankinglist.size(); i++) {
			bets.add(new BetAdmin(rankinglist.get(i).getMatchname(), rankinglist.get(i).getType(),
					rankinglist.get(i).getAmount()));
		}
		return bets;
	}

	// method to set the average accurate Hits
	public void averageAccurateHits() {
		for (int i = 0; i < repository.betlist.size(); i++) {
			double average = (repository.betlist.get(i).getCounterAccurateHits())
					/ (repository.betlist.get(i).getUserCounter());
			double roundDouble = (int) Math.round(average * 100.0) / 100.0;
			repository.betlist.get(i).setAverageAccurateHits(roundDouble);
		}
	}

	// wette mit den durchschnittlich meistem genauen treffern
	public ArrayList<BetAdmin> betWithAverageMostAccurateHits() {
		averageAccurateHits();
		ArrayList<BetAdmin> bets = new ArrayList<>();
		ArrayList<Bet> rankinglist = repository.betlist;
		Collections.sort(rankinglist, new Comparator<Bet>() {
			public int compare(Bet bet1, Bet bet2) {
				return Double.valueOf(bet2.getAveragePoints()).compareTo(bet1.getAveragePoints());
			}
		});
		for (int i = 0; i < rankinglist.size(); i++) {
			bets.add(new BetAdmin(rankinglist.get(i).getMatchname(), rankinglist.get(i).getType(),
					rankinglist.get(i).getAmount()));
		}
		return bets;

	}

}