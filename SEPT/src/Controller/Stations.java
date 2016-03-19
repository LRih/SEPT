package Controller;

import java.util.ArrayList;
import Model.Station;

public class Stations {

	public static ArrayList<Station> getStations(String search) {
		ArrayList<Station> stations = new ArrayList<Station>();

		if (search.length() == 0)
			return stations;
		else {
			// TODO: Filter stations by search key
			return stations;
		}
	}

	public static ArrayList<Station> getStates() {
		return getStations("");
	}

}
