package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Deposited extends state {

	@Override
	public void process(Sibs sibs, TransferOperation operation) throws AccountException {
		try {
			sibs.services.withdraw(operation.getSourceIban(), operation.commission());
			operation.setStatus(new Completed());
			Retry.clearCount();
		} catch (AccountException AccountException) {
			sibs.services.deposit(operation.getSourceIban(), operation.getValue());
			sibs.services.withdraw(operation.getTargetIban(), operation.getValue());
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
		sibs.services.deposit(operation.getSourceIban(), operation.getValue());
		sibs.services.withdraw(operation.getTargetIban(), operation.getValue());
		operation.setStatus(new Cancelled());
	}

}
