import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class IDandPasswords {

	HashMap<String,String> logininfo = new HashMap<String,String>();
	
	IDandPasswords(){ //use sql to get mutiple data
		// Read data from a file and populate the HashMap
        loadDataFromFile("../user_data.txt"); 
	}

	private void loadDataFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(","); //Standardize it ---> e.g: Khaira2004,abcd1234  *ENSURE NO SPACE!!!

                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    logininfo.put(username, password);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public HashMap<String, String> getLoginInfo() {
        return logininfo;
    }     
}


