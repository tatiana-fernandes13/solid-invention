package pt.ulisboa.tecnico.learnjava.bank.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class verifyAccountsMethodTest {
	private Bank bank;
	private Client client;
	private Services services;

	@Before
	public void setUp() throws BankException, ClientException {
		this.services = new Services();
		this.bank = new Bank("CGD");
		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
	}

	@Test
	public void existingAccountMethodTest() throws BankException, AccountException, ClientException {
		String iban = this.bank.createAccount(AccountType.CHECKING, this.client, 100, 0);
		String iban2 = this.bank.createAccount(AccountType.SAVINGS, this.client, 100, 0);

		assertTrue(services.verifyAccounts(iban, iban2));
	}

	@Test
	public void nonExistingAccountMethodTest() throws BankException, AccountException, ClientException {
		String iban = this.bank.createAccount(AccountType.SAVINGS, this.client, 100, 0);

		assertFalse(services.verifyAccounts(iban, "CGDCK3"));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
