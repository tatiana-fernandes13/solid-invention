package pt.ulisboa.tecnico.learnjava.sibs.cli;

import java.util.HashMap;

public class MbWay {
	public static HashMap<String, String> mbWayClients = new HashMap<String, String>();

	public HashMap<String, String> getMbWayClients() {
		return mbWayClients;
	}

	public void setMbWayClients(HashMap<String, String> mbWayClients) {
		this.mbWayClients = mbWayClients;
	}
}
