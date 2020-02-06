package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Registered extends state {

	@Override
	public void process(Sibs sibs, TransferOperation operation) throws AccountException {
		try {
			sibs.services.withdraw(operation.getSourceIban(), operation.getValue());
			operation.setStatus(new Withdrawn());
			Retry.clearCount();

		} catch (AccountException accountException) {
			sibs.services.deposit(operation.getSourceIban(), operation.getValue());
			Retry.addCount();
			if (Retry.getCount() == 4) {
				operation.setStatus(new Error());
				Retry.clearCount();

			} else {
				operation.setStatus(new Retry(operation, this));
			}
		}
	}

	@Override
	public void cancel(Sibs sibs, TransferOperation operation) throws AccountException {
		operation.setStatus(new Cancelled());
	}
}
