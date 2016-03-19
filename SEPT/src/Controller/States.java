package Controller;

import java.util.ArrayList;
import Model.State;

public class States {

	public static ArrayList<State> getStates(String search) {
		ArrayList<State> states = new ArrayList<State>();

		if (search.length() == 0)
			return states;
		else {
			// TODO: Filter states by search key
			return states;
		}
	}

	public static ArrayList<State> getStates() {
		return getStates("");
	}

}
