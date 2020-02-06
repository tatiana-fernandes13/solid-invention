package pt.ulisboa.tecnico.learnjava.sibs.cli;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class mbWaySplitBillController {
	Services services = new Services();
	HashMap<String, String> friendsMap;
	String targetIban;
	String targetPhoneNumber;
	Sibs sibs = new Sibs(100, services);
	mbwayTransferController mbwayTransferController = new mbwayTransferController();
	int totalAmount = 0;

	public String friend(String PhoneNumber, String amount) {
		if (MbWay.mbWayClients.containsKey(PhoneNumber)) {
			return PhoneNumber;
		}
		return null;
	}

	public void createHash() {
		this.friendsMap = new HashMap<String, String>();
	}

	public void addFriend(String phoneNumber, String amount) {
		this.friendsMap.put(phoneNumber, amount);
		if (this.friendsMap.size() == 1) {
			targetIban = MbWay.mbWayClients.get(phoneNumber);
			targetPhoneNumber = phoneNumber;
		}
	}

	public void mbwaySplitBill(String numberOfFriends, String amount)
			throws SibsException, AccountException, OperationException {
		if (this.friendsMap.size() > Integer.parseInt(numberOfFriends)) {
			System.out.println("Oh no! Too many friends.");
			return;
		} else if (this.friendsMap.size() < Integer.parseInt(numberOfFriends)) {
			System.out.println("Oh no! One friend is missing.");
			return;
		} else {
			for (String val : this.friendsMap.values()) {
				totalAmount += Integer.parseInt(val);
			}
			if (Integer.parseInt(amount) == totalAmount) {
				for (String phoneNumber : this.friendsMap.keySet()) {
					Account account = (services.getAccountByIban(MbWay.mbWayClients.get(phoneNumber)));
					if (account.getBalance() < Integer.parseInt(this.friendsMap.get(phoneNumber))) {
						System.out.println("Oh no! One of your friends does not have money to pay!");
						return;
					} else {
						if (!(MbWay.mbWayClients.get(phoneNumber).equals(targetIban))) {
							mbwayTransferController.mbway_transfer(phoneNumber, targetPhoneNumber,
									this.friendsMap.get(phoneNumber));
						}
					}
				}
				System.out.println("Bill payed successfully!");
			} else {
				System.out.println("Something is wrong. Did you set the bill amount right?");
				return;
			}
		}
	}
}
