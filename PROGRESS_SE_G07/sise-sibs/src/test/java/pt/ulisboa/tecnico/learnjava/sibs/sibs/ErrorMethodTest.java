package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ErrorMethodTest {
	@Test
	public void mockErrorRegisteredTest() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 200);
		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(true);
		assertTrue(operation.getStatus() instanceof Registered);
		doThrow(new AccountException()).when(mockServices).withdraw("sourceIban", 200);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Error);
	}

	@Test
	public void mockErrorWithdrawnTest() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 200);
		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(true);
		assertTrue(operation.getStatus() instanceof Registered);

		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Withdrawn);

		doThrow(new AccountException()).when(mockServices).deposit("targetIban", 200);
		operation.getStatus().process(sibs, operation);
		verify(mockServices).deposit("sourceIban", 200);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Error);
	}

	@Test
	public void mockErrorDepositedTestDifferentBanks() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 100);
		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(false);
		assertTrue(operation.getStatus() instanceof Registered);

		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Withdrawn);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Deposited);

		doThrow(new AccountException()).when(mockServices).withdraw("sourceIban", 6);
		operation.getStatus().process(sibs, operation);
		verify(mockServices).deposit("sourceIban", 100);
		verify(mockServices).withdraw("targetIban", 100);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Error);
	}

	@Test
	public void mockProcessErrorTest() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 200);
		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(true);
		assertTrue(operation.getStatus() instanceof Registered);
		doThrow(new AccountException()).when(mockServices).withdraw("sourceIban", 200);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Error);
	}

	@Test
	public void mockCancelErrorTest() throws SibsException, AccountException, OperationException {
		Services mockServices = mock(Services.class);
		Sibs sibs = new Sibs(100, mockServices);
		TransferOperation operation = new TransferOperation("sourceIban", "targetIban", 200);
		when(mockServices.verifyBanks("sourceIban", "targetIban")).thenReturn(true);
		assertTrue(operation.getStatus() instanceof Registered);
		doThrow(new AccountException()).when(mockServices).withdraw("sourceIban", 200);
		operation.getStatus().process(sibs, operation);
		assertTrue(operation.getStatus() instanceof Retry);
		operation.getStatus().cancel(sibs, operation);
		assertTrue(operation.getStatus() instanceof Cancelled);
	}
}
