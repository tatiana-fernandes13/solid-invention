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

public class verifyBanksMethodTest {
	private Bank bank;
	private Bank bank2;
	private Client client;
	private Client client2;
	private Services services;
	private String sourceIban;
	private String targetIban;

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.services = new Services();
		this.bank = new Bank("CGD");
		this.bank2 = new Bank("BPI");
		this.client = new Client(this.bank, "José", "Manuel", "123456789", "987654321", "Street", 33);
		this.client2 = new Client(this.bank2, "José", "Manuel", "123456789", "987654221", "Street", 33);
	}

	@Test
	public void sameBankMethodTest() throws BankException, AccountException, ClientException {
		this.sourceIban = this.bank.createAccount(AccountType.CHECKING, this.client, 100, 0);
		this.targetIban = this.bank.createAccount(AccountType.CHECKING, this.client, 100, 0);
		assertTrue(services.verifyBanks(sourceIban, targetIban));
	}

	@Test
	public void differentBanksMethodTest() throws BankException, AccountException, ClientException {
		this.sourceIban = this.bank.createAccount(AccountType.CHECKING, this.client, 100, 0);
		this.targetIban = this.bank2.createAccount(AccountType.SAVINGS, this.client2, 100, 0);
		assertFalse(services.verifyBanks(sourceIban, targetIban));
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
