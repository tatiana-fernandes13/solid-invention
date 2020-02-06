package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest {
//	private static final String ADDRESS = "Ave.";
//	private static final String PHONE_NUMBER = "987654321";
//	private static final String NIF = "123456789";
//	private static final String LAST_NAME = "Silva";
//	private static final String FIRST_NAME = "António";
//
//	private Sibs sibs;
//	private Bank sourceBank;
//	private Bank targetBank;
//	private Client sourceClient;
//	private Client targetClient;
//	private Services services;
//
//	@Before
//	public void setUp() throws BankException, AccountException, ClientException {
//		this.services = new Services();
//		this.sibs = new Sibs(100, services);
//		this.sourceBank = new Bank("CGD");
//		this.targetBank = new Bank("BPI");
//		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
//		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
//	}
//
//	@Test
//	public void success() throws BankException, AccountException, SibsException, OperationException, ClientException {
//		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
//		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
//
//		this.sibs.transfer(sourceIban, targetIban, 100);
//
//		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());
//		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
//		assertEquals(1, this.sibs.getNumberOfOperations());
//		assertEquals(100, this.sibs.getTotalValueOfOperations());
//		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
//		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
//	}
//	
//

	Sibs sibs;

	@Test
	public void mockTransferTestDifferentBanks() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		sibs = new Sibs(100, mockServices);

		when(mockServices.verifyAccounts("Iban1", "Iban2")).thenReturn(true);
		when(mockServices.verifyBanks("Iban1", "Iban2")).thenReturn(false);
		sibs.transfer("Iban1", "Iban2", 100);
		sibs.processOperations();
		verify(mockServices).withdraw("Iban1", 100);
		verify(mockServices).deposit("Iban2", 100);
		verify(mockServices).withdraw("Iban1", 6);
		assertEquals(1, sibs.getNumberOfOperations());
	}

	@Test
	public void mockTransferTestSameBanks() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		sibs = new Sibs(100, mockServices);

		when(mockServices.verifyAccounts("Iban1", "Iban2")).thenReturn(true);
		when(mockServices.verifyBanks("Iban1", "Iban2")).thenReturn(true);
		sibs.transfer("Iban1", "Iban2", 100);
		sibs.processOperations();
		verify(mockServices).withdraw("Iban1", 100);
		verify(mockServices).deposit("Iban2", 100);
		assertEquals(1, sibs.getNumberOfOperations());
	}
}
