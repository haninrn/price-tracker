import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        // Read CSV files and store data
        CSVReader csvReader = new CSVReader();
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV("lookup_item.csv");
        List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV("lookup_premise.csv");
        List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV("pricecatcher_2023-08.csv");

        // Pass data to login page or further processing
        IDandPasswords idandPasswords = new IDandPasswords();
        LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());

    }
}
