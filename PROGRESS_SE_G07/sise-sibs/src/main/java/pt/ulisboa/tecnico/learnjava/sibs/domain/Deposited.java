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
			catchProcess(sibs, operation);
			sibs.services.withdraw(operation.getTargetIban(), operation.getValue());
		}
	}

	@Override
	public void cancel(Sibs sibs, TransferOperation operation) throws AccountException {
		sibs.services.deposit(operation.getSourceIban(), operation.getValue());
		sibs.services.withdraw(operation.getTargetIban(), operation.getValue());
		operation.setStatus(new Cancelled());
	}

}
