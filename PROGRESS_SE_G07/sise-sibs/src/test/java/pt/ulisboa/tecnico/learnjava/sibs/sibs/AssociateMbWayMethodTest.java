package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.CheckingAccount;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.cli.MbWay;
import pt.ulisboa.tecnico.learnjava.sibs.cli.associateMbWayController;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class AssociateMbWayMethodTest {

	@Test
	public void mockAssociateMbWay()
			throws OperationException, SibsException, AccountException, BankException, ClientException {
		Bank mockBank = mock(Bank.class);
		Client client = new Client(mockBank, "Tatiana", "Fernandes", "123456789", "987654321", "address", 24);
		CheckingAccount account = new CheckingAccount(client, 100);
		Services mockServices = mock(Services.class);
		associateMbWayController mbWayController = new associateMbWayController();
		when(mockServices.getAccountByIban("iban")).thenReturn(account);
		mbWayController.associate_mbway(mockServices, "iban", "987654321");
		assertEquals(mbWayController.getCode(), client.getMbwayCode());
		assertEquals("iban", MbWay.mbWayClients.get("987654321"));
	}
}
