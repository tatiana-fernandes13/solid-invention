package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class mbwayTransferController {
	Services services = new Services();
	Sibs sibs = new Sibs(100, services);

//	implementação da guideline 1
	public void mbway_transfer(String sourcePhoneNumber, String targetPhoneNumber, String amount)
			throws SibsException, AccountException, OperationException {
		if (MbWay.mbWayClients.containsKey(sourcePhoneNumber) && MbWay.mbWayClients.containsKey(targetPhoneNumber)) {
			String sourceIban = MbWay.mbWayClients.get(sourcePhoneNumber);
			String targetIban = MbWay.mbWayClients.get(targetPhoneNumber);
			if (services.getAccountByIban(sourceIban).getClient().getMbwayState().equals("Active")
					&& services.getAccountByIban(targetIban).getClient().getMbwayState().equals("Active")) {
				sibs.transfer(sourceIban, targetIban, Integer.parseInt(amount));
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
