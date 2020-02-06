package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class processMethodTest {

	@Test
	public void mockProcessSameBanksMethodTest() throws OperationException, SibsException, AccountException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(true);
		operation.getStatus().process(sibs, operation);

		verify(mockServices).withdraw("sourceIban", 100);
		assertTrue(operation.getStatus() instanceof Withdrawn);

		operation.getStatus().process(sibs, operation);
		verify(mockServices).deposit("targetIban", 100);
		assertTrue(operation.getStatus() instanceof Completed);
	}

	@Test
	public void mockProcessDifferentBanksMethodTest() throws OperationException, SibsException, AccountException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(false);
		operation.getStatus().process(sibs, operation);

		verify(mockServices).withdraw("sourceIban", 100);
		assertTrue(operation.getStatus() instanceof Withdrawn);

		operation.getStatus().process(sibs, operation);
		verify(mockServices).deposit("targetIban", 100);
		assertTrue(operation.getStatus() instanceof Deposited);

		operation.getStatus().process(sibs, operation);
		verify(mockServices).withdraw("sourceIban", 6);
		assertTrue(operation.getStatus() instanceof Completed);
	}

	@Test
	public void mockProcessCancelledTest() throws OperationException, SibsException, AccountException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(false);
		operation.getStatus().cancel(sibs, operation);
		assertTrue(operation.getStatus() instanceof Cancelled);

		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Cancelled);
	}
}
