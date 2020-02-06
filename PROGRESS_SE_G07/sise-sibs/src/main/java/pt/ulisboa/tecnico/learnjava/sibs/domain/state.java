package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public abstract class state {

	public void catchProcess(Sibs sibs, TransferOperation operation) throws AccountException {
		sibs.services.deposit(operation.getSourceIban(), operation.getValue());
		Retry.addCount();
		if (Retry.getCount() == 4) {
			operation.setStatus(new Error());
			Retry.clearCount();

		} else {
			operation.setStatus(new Retry(operation, this));
		}
	}

	public void process(Sibs sibs, TransferOperation operation) throws AccountException {
	}

	public void cancel(Sibs sibs, TransferOperation operation) throws AccountException {
	}

}
