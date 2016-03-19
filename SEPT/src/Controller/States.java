package Controller;

import java.util.ArrayList;
import Model.State;

public class States {

	public static ArrayList<State> getStates(String search) {
		ArrayList<State> states = new ArrayList<State>();

		if (search.length() == 0) {
			// FAKE data
			// TODO: get from WEB.
			states.add(new State("Victoria", "VIC"));
			states.add(new State("New South Wales", "NSW"));
			states.add(new State("Queensland", "QLD"));
			states.add(new State("South Australia", "SA"));
			states.add(new State("Western Australia", "WA"));
			states.add(new State("Northern Territory", "NT"));
			states.add(new State("Tasmania", "TAS"));
			return states;
		} else {
			// TODO: Filter states by search key
			return states;
		}
	}

	public static ArrayList<State> getStates() {
		return getStates("");
	}

}
