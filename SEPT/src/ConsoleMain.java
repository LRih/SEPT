import Model.Favorite;
import Model.Favorites;
import Utils.FavoritesManager;
import Utils.NetUtils;

import java.io.IOException;
import java.util.Calendar;

public final class ConsoleMain
{
    public static void main(String[] args)
    {
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
            Favorites favs = FavoritesManager.load();
            favs.add(String.valueOf(Calendar.getInstance().getTimeInMillis()), "Testing");

            for (Favorite fav : favs.getItems())
                System.out.println(fav.id + ": " + fav.name);

            FavoritesManager.save(favs);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
