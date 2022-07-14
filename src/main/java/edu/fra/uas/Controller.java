package edu.fra.uas;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

import edu.fra.uas.bet.Bet;
import edu.fra.uas.bet.BetAdmin;
import edu.fra.uas.bet.BetService;
import edu.fra.uas.match.Match;
import edu.fra.uas.match.MatchService;
import edu.fra.uas.user.User;
import edu.fra.uas.user.UserService;

@RestController
public class Controller {

	@Autowired
	UserService userService;
	@Autowired
	MatchService matchService;
	@Autowired
	BetService betService;

	// Mapping to create a user
	@RequestMapping(value = "/user/{username}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> useradden(@PathVariable String username) {
		if (userService.getUserbyName(username) == null) {
			userService.addUser(username);
			return new ResponseEntity<String>(HttpStatus.CREATED);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists. Please use another username");
	}

	// Mapping to list all User
	@RequestMapping(value = "/admin/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUserList(@RequestHeader String code) {
		if (code.equals("admin123")) {
			if (userService.getUserlist().isEmpty()) {
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}
			return ResponseEntity.status(HttpStatus.OK).body(userService.getUserlist());
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to change the username
	@RequestMapping(value = "/user/{username}/change/{newUsername}", method = RequestMethod.PATCH)
	public ResponseEntity<?> changeUsername(@PathVariable String username, @PathVariable String newUsername) {
		if (userService.changeUsername(username, newUsername)) {
			return ResponseEntity.status(HttpStatus.OK).body("Username changed");
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

	// Mapping to delete a user
	@RequestMapping(value = "/user/delete/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteuser(@PathVariable String username) {
		if (userService.deleteUser(username)) {
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}

	// Mapping to list the Bets of a User
	@RequestMapping(value = "/user/{username}/bet", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUsersBetList(@PathVariable String username) {
		User user = userService.getUserbyName(username);
		int indexOfUser = userService.getUserlist().indexOf(user);
		if (user != null) {
			if (user.getBetlist().isEmpty()) {
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(userService.betofUser(userService.getUserlist().get(indexOfUser)));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	}

	// Mapping to create a Match
	@RequestMapping(value = "/admin/match", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createMatch(@RequestHeader String code, @RequestBody Match match) {
		// if (betService.getUserService().adminCheck(code) == true) {
		if (code.equals("admin123")) {
			if (matchService.getMatchbyMatchname(match.getMatchname()) == null) {
				matchService.createMatch(match);
				return new ResponseEntity<String>(HttpStatus.CREATED);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Match already exists. Please add another Match");
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to delete a Match
	@RequestMapping(value = "/admin/delete/{matchname}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMatch(@PathVariable String matchname, @RequestHeader String code) {
		if (code.equals("admin123")) {
			if (matchService.getMatchbyMatchname(matchname).getStatus().toLowerCase().equals("closed")) {
				matchService.deleteMatch(matchname);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Match is still opened. Please close the Match before");
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to cancel a match
	@RequestMapping(value = "/admin/cancel/{matchname}", method = RequestMethod.PATCH)
	public ResponseEntity<?> canelMatch(@PathVariable String matchname, @RequestHeader String code) {
		if (code.equals("admin123")) {
			matchService.cancelMatch(matchname);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to list the matches
	@RequestMapping(value = "/admin/match/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMatchList(@RequestHeader String code) {
		if (code.equals("admin123")) {
			if (!matchService.getMatches().isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatches());
			} else {
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
			}
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to create a Bet
	@RequestMapping(value = "/admin/bet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBet(@RequestHeader String code, @RequestBody Bet bet) {
		if (code.equals("admin123")) {
			if (betService.getBet(bet.getMatchname(), bet.getType()) == false) {
				if (betService.createBet(bet)) {
					return new ResponseEntity<String>(HttpStatus.CREATED);
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Matchname does not exists. Please check the matchname");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bet already exists. Please add another Bet");
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to get the betlist
	@RequestMapping(value = "/admin/bet/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBetList(@RequestHeader String code) {
		ArrayList<BetAdmin> bets = new ArrayList<>();
		for (int i = 0; i < betService.getBetList().size(); i++) {
			bets.add(new BetAdmin(betService.getBetList().get(i).getMatchname(),
					betService.getBetList().get(i).getType(), betService.getBetList().get(i).getAmount()));
		}
		if (code.equals("admin123")) {
			return ResponseEntity.status(HttpStatus.OK).body(bets);
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to set a Bet of a User
	@RequestMapping(value = "/user/{username}/bet/matchname/type/amount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> usersBet(@PathVariable String username, @RequestBody Bet bet) {
		if (userService.getUserbyName(username) != null) {
			if (betService.usersBet(username, bet)) {
				return new ResponseEntity<String>(HttpStatus.CREATED);
			}
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	}

	// Mapping to change the amount of the bet of the user
	@RequestMapping(value = "user/{username}/bet/{matchname}/{type}/{newAmount}", method = RequestMethod.PATCH)
	public ResponseEntity<?> changeUsersBet(@PathVariable String username, @PathVariable String matchname,
			@PathVariable String type, @PathVariable int newAmount) {
		User user = userService.getUserbyName(username);
		if (betService.changeUsersBetsAmount(username, matchname, type, newAmount)) {
			return ResponseEntity.status(HttpStatus.OK).body(userService.betofUser(user));
		}
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

	// Mapping to complete the Bet with the real amount
	@RequestMapping(value = "/admin/bet/matchname/type/amount", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> amount_of_Bet(@RequestHeader String code, @RequestBody Bet bet) {
		if (code.equals("admin123")) {
			if (betService.add_amount(bet)) {
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to compare the amount of the bet of the User with the Real amount
	@RequestMapping(value = "/admin/user/amount", method = RequestMethod.PATCH)
	public ResponseEntity<?> amount_check(@RequestHeader String code) {
		if (code.equals("admin123")) {
			betService.amount_check();
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to see the ranking of the user
	@RequestMapping(value = "users/ranking", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRanking() {
		if (userService.ranking().isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.status(HttpStatus.OK).body(userService.ranking());
	}

	// Mapping to see the bet with the most points
	@RequestMapping(value = "admin/bet/most/points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRankingofBets(@RequestHeader String code) {
		if (code.equals("admin123")) {
			return ResponseEntity.status(HttpStatus.OK).body(betService.betWithMostPoints());
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to get the bet which the most user did
	@RequestMapping(value = "/admin/bet/most/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBetWithMostUser(@RequestHeader String code) {
		if (code.equals("admin123")) {
			return ResponseEntity.status(HttpStatus.OK).body(betService.betWithMostUser());
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to get the bet which has the highest average points
	@RequestMapping(value = "/admin/bet/average/points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getaveragepoints(@RequestHeader String code) {
		if (code.equals("admin123")) {
			return ResponseEntity.status(HttpStatus.OK).body(betService.betWithHighestAveragePoints());
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}

	// Mapping to spend the bet that has the highest percentage of accurate hits
	@RequestMapping(value = "/admin/bet/average/accurate/hits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAverageAccurateHits(@RequestHeader String code) {
		if (code.equals("admin123")) {
			return ResponseEntity.status(HttpStatus.OK).body(betService.betWithAverageMostAccurateHits());
		}
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}
}