package edu.fra.uas.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fra.uas.bet.BetUser;
import edu.fra.uas.repository.AllRepository;

@Service
public class UserService {

	@Autowired
	AllRepository repository;

	public ArrayList<User> getUserlist() {
		return repository.userlist;
	}

	public AllRepository getRepository() {
		return repository;
	}

	public void setRepository(AllRepository repository) {
		this.repository = repository;
	}

	// add User method
	public void addUser(String username) {
		repository.userlist.add(new User(username));
		System.out.println("User " + username + " added");

	}

	// method to return th ebetlist of the useer with the right attributes
	public ArrayList<BetUser> betofUser(User user) {
		ArrayList<BetUser> bets = new ArrayList<>();
		for (int i = 0; i < user.getBetlist().size(); i++) {
			bets.add(new BetUser(user.getBetlist().get(i).getMatchname(), user.getBetlist().get(i).getType(),
					user.getBetlist().get(i).getUsersAmount()));
		}
		return bets;

	}

	// method to change the username
	public boolean changeUsername(String username, String newUsername) {
		User user = getUserbyName(username);
		User newUser = getUserbyName(newUsername);
		if (user != null) {
			if (newUser == null) {
				repository.userlist.get(repository.userlist.indexOf(user)).setUsername(newUsername);
				return true;
			}
		} else {
			System.out.println("Username not found");
			return false;
		}
		return false;
	}

	// method to delete a user from the list
	public boolean deleteUser(String username) {
		User user = getUserbyName(username);
		if (user != null) {
			// goes over each part of the list to find the index where the user is and then
			// delete the user from the list
			repository.userlist.remove(repository.userlist.indexOf(user));
			System.out.println("user " + user.getUsername() + " deleted");
			if (!repository.userlist.isEmpty())
				return true;
		}
		// to output the the list
		System.out.println(repository.userlist);
		return false;
	}

	// method to get the user
	public User getUserbyName(String username) {
		for (int i = 0; i < repository.userlist.size(); i++) {
			if (repository.userlist.get(i).getUsername().toLowerCase().equals(username.toLowerCase())) {
				return repository.userlist.get(i);
			}
		}
		return null;
	}

	// method to get a rankinglist of all user
	public ArrayList<User> ranking() {
		ArrayList<User> rankinglist = repository.userlist;
		Collections.sort(rankinglist, new Comparator<User>() {
			public int compare(User user1, User user2) {
				return Integer.valueOf(user2.getScore()).compareTo(user1.getScore());
			}
		});
		return rankinglist;
	}
}