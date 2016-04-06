package Controller;

import java.util.ArrayList;
import Model.State;

public final class States {

	public static ArrayList<State> getStates(String search) {
		ArrayList<State> states = new ArrayList<State>();

		if (search.length() == 0) {
			// FAKE data
			// TODO: get from WEB.
			states.add(new State("Victoria"));
			states.add(new State("New South Wales"));
			states.add(new State("Queensland"));
			states.add(new State("South Australia"));
			states.add(new State("Western Australia"));
			states.add(new State("Northern Territory"));
			states.add(new State("Tasmania"));
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
