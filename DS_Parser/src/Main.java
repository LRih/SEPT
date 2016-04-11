import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {

	private static String path = "/src/smallset.csv";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String filePath = new File("").getAbsolutePath();

		BufferedReader bufferedReader = null;
		String line = "";
		String separator = ",";

		int count = 0;

		try {
			bufferedReader = new BufferedReader(new FileReader(filePath + path));
			while ((line = bufferedReader.readLine()) != null) {

				String[] fields = line.split(separator);
				// System.out.println(line);
				count++;
				// if (fields.length > 23)
				// System.out.println(count + ". " + fields.length);
				if (count == 290524 || count == 289514) {
					System.out.println(line);
					for (int i = 0; i < fields.length; i++) {
						System.out.println(i + " " + fields[i]);
					}
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

		System.out.println("Done. " + count + " found.");

	}

}
