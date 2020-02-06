package pt.ulisboa.tecnico.learnjava.sibs.cli;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class CLI {

	public void exit() {
		System.exit(0);
	}

	public static void main(String[] args)
			throws BankException, ClientException, AccountException, SibsException, OperationException {
		CLI cli = new CLI();
		Services services = new Services();
		Scanner reader = new Scanner(System.in);
		String command = reader.next();
		Bank bank = new Bank("CGD");
		Client client = new Client(bank, "Tatiana", "Fernandes", "123456789", "987654321", "address", 24);
		String iban = bank.createAccount(AccountType.CHECKING, client, 100, 10);
		Client client2 = new Client(bank, "Carla", "Corsino", "123456788", "987456123", "address", 24);
		String iban2 = bank.createAccount(AccountType.CHECKING, client2, 100, 10);
		Client client3 = new Client(bank, "Flavia", "Fernandes", "123456777", "912311111", "address", 24);
		String iban3 = bank.createAccount(AccountType.CHECKING, client3, 100, 10);

		while (true) {
			if (command.equals("exit")) {
				cli.exit();
			} else if (command.startsWith("associate-mbway")) {
				associateMbWayController mbWayController = new associateMbWayController();
				mbWayController.associate_mbway(services, reader.next(), reader.next());
			} else if (command.startsWith("confirm-mbway")) {
				confirmMbwayController mbWayController = new confirmMbwayController();
				mbWayController.confirm_mbway(services, reader.next(), reader.next());
			} else if (command.startsWith("mbway-transfer")) {
				mbwayTransferController mbWayController = new mbwayTransferController();
				mbWayController.mbway_transfer(services, reader.next(), reader.next(), reader.next());
			} else if (command.startsWith("mbway-split-bill")) {
				mbWaySplitBillController mbWayController = new mbWaySplitBillController();
				String numberOfFriends = reader.next();
				String amount = reader.next();
				mbWayController.createHash();
				command = reader.next();
				while ((!command.startsWith("end")) && command.startsWith("friend")) {
					String phoneNumber = reader.next();
					String value = reader.next();
					mbWayController.addFriend(mbWayController.friend(phoneNumber, value), value);
					command = reader.next();
				}
				mbWayController.mbwaySplitBill(services, numberOfFriends, amount);
			}
			command = reader.next();
		}
	}

}
