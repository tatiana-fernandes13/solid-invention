package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public abstract class state {

	public void process(Sibs sibs, TransferOperation operation) throws AccountException {
	}

	public void cancel(Sibs sibs, TransferOperation operation) throws AccountException {
	}
}
