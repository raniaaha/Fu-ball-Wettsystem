package edu.fra.uas.match;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fra.uas.repository.AllRepository;

@Service
public class MatchService {

	@Autowired
	AllRepository repository;

	// method to create a match
	public void createMatch(Match match) {
		repository.matches.add(match);
		System.out.println("Match " + match.getMatchname() + " created");
	}

	public ArrayList<Match> getMatches() {
		return repository.matches;
	}

	// Method to get the match by the matchname
	public Match getMatchbyMatchname(String matchname) {
		for (int i = 0; i < repository.matches.size(); i++) {
			if (repository.matches.get(i).getMatchname().toLowerCase().equals(matchname.toLowerCase())) {
				return repository.matches.get(i);
			}
		}
		return null;
	}

	// method to cancel a match
	public void cancelMatch(String matchname) {
		if (getMatchbyMatchname(matchname) != null) {
			Match match = getMatchbyMatchname(matchname);
			match.setStatus("closed");
		}
	}

	// method to delete a match
	public void deleteMatch(String matchname) {
		for (int i = 0; i < repository.matches.size(); i++) {
			if (getMatchbyMatchname(matchname) != null) {
				// if the status is closed you can remove the match
				if (repository.matches.get(i).getStatus().toLowerCase().equals("closed")) {
					repository.matches.remove(i);
					if (!repository.betlist.isEmpty()) {
						for (int j = 0; j < repository.betlist.size(); j++) {
							// if a bet has the matchname of the match you can delete it
							if (repository.betlist.get(j).getMatchname().toLowerCase()
									.equals(matchname.toLowerCase())) {
								repository.betlist.remove(j);
							}
						}
					}
					if (!repository.userlist.isEmpty()) {
						for (int j = 0; j < repository.userlist.size(); j++) {
							for (int x = 0; j < repository.userlist.get(j).getBetlist().size(); x++) {
								// if a user has a bet with the matchname you can remove it
								if (repository.userlist.get(j).getBetlist().get(x).getMatchname().toLowerCase()
										.equals(matchname.toLowerCase())) {
									repository.userlist.get(j).getBetlist().remove(x);
								}
							}
						}
					}
				}
			}
		}
	}

}