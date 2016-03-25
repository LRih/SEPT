package Controller;

import java.util.ArrayList;
import Model.Area;

public final class Areas {

	public static ArrayList<Area> getAreas(String search) {
		ArrayList<Area> areas = new ArrayList<>();

		if (search.length() == 0)
			return areas;
		else {
			// TODO: Filter areas by search key
			return areas;
		}
	}

	public static ArrayList<Area> getAreas() {
		return getAreas("");
	}

}
