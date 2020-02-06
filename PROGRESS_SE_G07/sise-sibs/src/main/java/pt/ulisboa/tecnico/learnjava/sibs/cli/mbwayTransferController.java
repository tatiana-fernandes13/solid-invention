package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class mbwayTransferController {
	Services services;
	Sibs sibs;

	public void mbway_transfer(String sourcePhoneNumber, String targetPhoneNumber, String amount)
			throws SibsException, AccountException, OperationException {
		services = new Services();
		sibs = new Sibs(100, services);
		if (MbWay.mbWayClients.containsKey(sourcePhoneNumber) && MbWay.mbWayClients.containsKey(targetPhoneNumber)) {
			int value = Integer.parseInt(amount);
			String sourceIban = MbWay.mbWayClients.get(sourcePhoneNumber);
			String targetIban = MbWay.mbWayClients.get(targetPhoneNumber);
			if (services.getAccountByIban(sourceIban).getClient().getMbwayState().equals("Active")
					&& services.getAccountByIban(targetIban).getClient().getMbwayState().equals("Active")) {
				sibs.transfer(sourceIban, targetIban, value);
				sibs.processOperations();
				System.out.println("Transfer successful!");
			} else {
				System.out.println("Could not complete transfer!");
			}
		} else {
			System.out.println("Could not complete transfer!");
		}
	}
}
