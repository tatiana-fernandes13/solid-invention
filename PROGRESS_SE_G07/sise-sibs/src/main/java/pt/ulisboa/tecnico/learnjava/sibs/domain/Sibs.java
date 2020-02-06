package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;
	Operation operation;

	private static Set<TransferOperation> transferOperationSet = new HashSet<TransferOperation>();

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {
		if (!this.services.verifyAccounts(sourceIban, targetIban)) {
			throw new SibsException();
		}
		TransferOperation operation = (TransferOperation) getOperation(
				addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount));
		transferOperationSet.add(operation);
	}

	public void processOperations() throws AccountException {
		for (TransferOperation operation : transferOperationSet) {
			while (!(operation.getStatus() instanceof Completed || operation.getStatus() instanceof Cancelled
					|| operation.getStatus() instanceof Error)) {
				operation.getStatus().process(this, operation);
			}
		}
	}

	public void cancelOperation(int position) throws SibsException, AccountException {
		Operation operation = getOperation(position);
		if (operation instanceof TransferOperation) {
			((TransferOperation) operation).getStatus().cancel(this, (TransferOperation) operation);
		}
	}

	public int addOperation(String type, String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}
		if (position == -1) {
			throw new SibsException();
		}
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			this.operation = new TransferOperation(sourceIban, targetIban, value);
		} else {
			this.operation = new PaymentOperation(targetIban, value);
		}
		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
}
