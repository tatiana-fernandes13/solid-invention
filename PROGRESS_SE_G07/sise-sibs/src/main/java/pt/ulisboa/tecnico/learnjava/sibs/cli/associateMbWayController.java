package pt.ulisboa.tecnico.learnjava.sibs.cli;

import java.util.Random;

import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class associateMbWayController {
	Services services;

	public void associate_mbway(Services services, String iban, String phoneNumber) {
		services = new Services();
		Client client = services.getAccountByIban(iban).getClient();
		if (phoneNumber.equals(client.getPhoneNumber())) {
			Random random = new Random();
			Integer code = random.nextInt(100000) + random.nextInt(899999);
			client.setMbwayCode(code);
			MbWay.mbWayClients.put(phoneNumber, iban);
			System.out.println("Code: " + code.toString() + " (don’t share it with anyone)");
		} else {
			System.out.println("Wrong phone number!");
		}

	}

}
