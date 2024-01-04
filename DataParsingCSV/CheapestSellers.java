package DataParsingCSV;
import java.time.LocalDate;
import java.util.List;


public class CheapestSellers {

    public static void main(String[] args) {
        String lookupItemFilePath = "../lookup_item.csv";
        String lookupPremiseFilePath = "../lookup_premise.csv";
        String priceCatcherFilePath = "../pricecatcher.csv";

        CSVReader csvReader = new CSVReader();
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV(lookupItemFilePath);
        List<LookupPremise> lookupPremises = csvReader.readLookupPremiseCSV(lookupPremiseFilePath);
        List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV(priceCatcherFilePath);

        findCheapestSellers(lookupItems, lookupPremises, priceCatchers);
    }

    private static void findCheapestSellers(List<LookupItem> lookupItems, List<LookupPremise> lookupPremises, List<PriceCatcher> priceCatchers) {
        for (PriceCatcher priceCatcher : priceCatchers) {
            String itemCode = priceCatcher.getItemCode();
            String premiseCode = priceCatcher.getPremiseCode();
            double price = priceCatcher.getPrice();

            // Find LookupItem for the given itemCode
            LookupItem item = findItemByCode(lookupItems, itemCode);

            // Find LookupPremise for the given premiseCode
            LookupPremise premise = findPremiseByCode(lookupPremises, premiseCode);

            if (item != null && premise != null) {
                System.out.println("Item: " + item.getItem() + " at " + premise.getPremise() + " - Price: " + price);
            }
        }
    }

    private static LookupItem findItemByCode(List<LookupItem> lookupItems, String itemCode) {
        for (LookupItem item : lookupItems) {
            if (item.getItemCode().equals(itemCode)) {
                return item;
            }
        }
        return null;
    }

    private static LookupPremise findPremiseByCode(List<LookupPremise> lookupPremises, String premiseCode) {
        for (LookupPremise premise : lookupPremises) {
            if (premise.getPremiseCode().equals(premiseCode)) {
                return premise;
            }
        }
        return null;
    }
}
