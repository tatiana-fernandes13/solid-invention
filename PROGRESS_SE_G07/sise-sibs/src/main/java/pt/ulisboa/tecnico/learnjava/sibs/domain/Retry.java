package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Retry extends state {
	private static int cont;
	private state lastState;
	private TransferOperation operation;

	public Retry(TransferOperation operation, state lastState) {
		this.lastState = lastState;
		this.operation = operation;
		this.setState(lastState);
	}

	public void setState(state lastState) {
		this.operation.setStatus(lastState);
	}

	public static int addCount() {
		return cont++;
	}

	public static int getCount() {
		return cont;
	}

	public static void clearCount() {
		cont = 0;
	}

	@Override
	public void process(Sibs sibs, TransferOperation operation) throws AccountException {
		this.lastState.process(sibs, operation);
	}

	@Override
	public void cancel(Sibs sibs, TransferOperation operation) throws AccountException {
		this.lastState.cancel(sibs, operation);
	}
}
