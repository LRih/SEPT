import Utils.NetUtils;

import java.io.IOException;

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
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
