import Model.*;
import Utils.BOMUtils;
import Utils.FavoritesManager;
import Utils.NetUtils;

import java.io.IOException;

public final class ConsoleMain
{
    public static void main(String[] args)
    {
        System.out.println(BOMUtils.toDateTime("20160329220000"));

        // testing network functions
        try
        {
            System.out.println(NetUtils.get("http://www.bom.gov.au/fwo/IDV60901/IDV60901.95936.json"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            State state = new State("Victoria", "VIC");
            state.addStation("Olympic Park", "http://www.example.com");

            Station station = state.getStation("Olympic Park");

            Favorites favs = FavoritesManager.load();
            favs.add(station);

            for (Favorite fav : favs.getSortedItems())
                System.out.println(fav.key);

            FavoritesManager.save(favs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
