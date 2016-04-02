import Model.*;
import Utils.DataManager;
import Utils.FavoritesManager;

import java.io.IOException;

public final class ConsoleMain
{
    public static void main(String[] args)
    {
        try
        {
            // test loading states and getting data from BOM for Mildura
            States states = DataManager.loadStates();
            Station station = states.get("Victoria").getStation("Mildura");

            System.out.println(station.getState().getName());
            System.out.println(station.getKey());
            System.out.println(station.getUrl());

            StationData data = DataManager.getStationData(station);
            if (data != null)
                System.out.println(data.getId());
            System.out.println();

            // test read favorites
            System.out.println("Favorites:");
            Favorites favs = FavoritesManager.load();
            
            for (Favorite fav : favs)
                System.out.println(fav.key);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
