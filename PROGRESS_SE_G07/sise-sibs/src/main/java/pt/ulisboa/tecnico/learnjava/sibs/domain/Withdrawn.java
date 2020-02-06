package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Withdrawn extends state {

	@Override
	public void process(Sibs sibs, TransferOperation operation) throws AccountException {
		try {
			sibs.services.deposit(operation.getTargetIban(), operation.getValue());
			if (!sibs.services.verifyBanks(operation.getSourceIban(), operation.getTargetIban())) {
				operation.setStatus(new Deposited());
			} else {
				operation.setStatus(new Completed());
			}
			Retry.clearCount();
		} catch (AccountException accountException) {
			catchProcess(sibs, operation);
		}
	}

	@Override
	public void cancel(Sibs sibs, TransferOperation operation) throws AccountException {
		sibs.services.deposit(operation.getSourceIban(), operation.getValue());
		operation.setStatus(new Cancelled());
	}

}
