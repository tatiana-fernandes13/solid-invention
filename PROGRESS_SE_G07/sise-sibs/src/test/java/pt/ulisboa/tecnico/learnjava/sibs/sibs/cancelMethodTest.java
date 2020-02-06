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

public class cancelMethodTest {

	@Test
	public void mockCancelRegisteredTest() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		operation.getStatus().cancel(sibs, operation);

		assertTrue(operation.getStatus() instanceof Cancelled);
	}

	@Test
	public void mockCancelWithdrawnTest() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Withdrawn);
		operation.getStatus().cancel(sibs, operation);

		verify(mockServices).deposit("sourceIban", 100);
		assertTrue(operation.getStatus() instanceof Cancelled);
	}

	@Test
	public void mockCancelDepositedTestDifferentBanks() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(false);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Withdrawn);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Deposited);
		operation.getStatus().cancel(sibs, operation);

		verify(mockServices).deposit("sourceIban", 100);
		verify(mockServices).withdraw("targetIban", 100);
		assertTrue(operation.getStatus() instanceof Cancelled);
	}

	@Test
	public void mockCancelCompletedTestDifferentBanks() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		assertTrue(operation.getStatus() instanceof Registered);

		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(false);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Withdrawn);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Deposited);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Completed);
		operation.getStatus().cancel(sibs, operation);

		assertTrue(operation.getStatus() instanceof Completed);
	}
}
