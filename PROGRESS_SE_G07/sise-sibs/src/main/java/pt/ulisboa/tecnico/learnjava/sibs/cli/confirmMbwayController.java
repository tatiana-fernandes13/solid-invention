package pt.ulisboa.tecnico.learnjava.sibs.cli;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class confirmMbwayController {

	public void confirm_mbway(Services services, String phoneNumber, String code) {
		if (MbWay.mbWayClients.containsKey(phoneNumber)) {
			if (services.getAccountByIban(MbWay.mbWayClients.get(phoneNumber)).getClient().getMbwayCode().toString()
					.equals(code)) {
				services.getAccountByIban(MbWay.mbWayClients.get(phoneNumber)).getClient().setMbwayState("Active");
				System.out.println("MBWay Association Confirmed Successfully!");
			} else {
				System.out.println("Wrong confirmation code!");
			}
		} else {
			System.out.println("Invalid phone number!");
		}
	}
}
