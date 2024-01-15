import java.util.List;

public class Main {

	public static void main(String[] args) {
		CSVReader csvReader = new CSVReader();
                List<LookupItem> lookupItems = csvReader.readLookupItemCSV("resources/lookup_item.csv");
                List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV("resources/lookup_premise.csv");
                List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV("resources/pricecatcher_2023-08.csv");

		IDandPasswords idandPasswords = new IDandPasswords();		
		LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());

	}
}