package pt.ulisboa.tecnico.learnjava.sibs.cli;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class mbWaySplitBillController {
	HashMap<String, String> friendsMap;
	String targetIban;
	String targetPhoneNumber;
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

	public void checkBalance(Services services) throws SibsException, AccountException, OperationException {
		Sibs sibs = new Sibs(100, services);
		for (String phoneNumber : this.friendsMap.keySet()) {
			Account account = (services.getAccountByIban(MbWay.mbWayClients.get(phoneNumber)));
			if (account.getBalance() < Integer.parseInt(this.friendsMap.get(phoneNumber))) {
				System.out.println("Oh no! One of your friends does not have money to pay!");
				return;
			} else if (!(MbWay.mbWayClients.get(phoneNumber).equals(targetIban))) {
				mbwayTransferController.mbway_transfer(services, phoneNumber, targetPhoneNumber,
						this.friendsMap.get(phoneNumber));
			}
		}
	}

	public void checkAmount(Services services, String amount)
			throws SibsException, AccountException, OperationException {
		if (Integer.parseInt(amount) == totalAmount) {
			this.checkBalance(services);
			System.out.println("Bill payed successfully!");
		} else {
			System.out.println("Something is wrong. Did you set the bill amount right?");
		}
	}

	public void mbwaySplitBill(Services services, String numberOfFriends, String amount)
			throws SibsException, AccountException, OperationException {
		if (this.friendsMap.size() > Integer.parseInt(numberOfFriends)) {
			System.out.println("Oh no! Too many friends.");
			return;
		} else if (this.friendsMap.size() < Integer.parseInt(numberOfFriends)) {
			System.out.println("Oh no! One friend is missing.");
			return;
		} else {
			int totalAmount = this.friendsMap.values().stream().mapToInt(v -> Integer.parseInt(v)).sum();
			this.checkAmount(services, amount);
		}
	}
}
