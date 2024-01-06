
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AveragePrices {

    public static void main(String[] args) {
        String lookupItemFilePath = "lookup_item.csv";
        String priceCatcherFilePath = "pricecatcher.csv";

        CSVReader csvReader = new CSVReader();
        List<LookupItem> lookupItems = csvReader.readLookupItemCSV(lookupItemFilePath);
        List<PriceCatcher> priceCatchers = csvReader.readPriceCatcherCSV(priceCatcherFilePath);

        calculateAveragePrices(lookupItems, priceCatchers);
    }

    private static void calculateAveragePrices(List<LookupItem> lookupItems, List<PriceCatcher> priceCatchers) {
        // lists to store total prices and counts for each date
        List<LocalDate> dates = new ArrayList<>();
        List<Double> totalPrices = new ArrayList<>();
        List<Integer> sellerCounts = new ArrayList<>();

        // calc total prices and counts for each date
        for (PriceCatcher priceCatcher : priceCatchers) {
            String itemCode = priceCatcher.getItemCode();
            LocalDate date = priceCatcher.getDate();
            double price = priceCatcher.getPrice();

            int index = dates.indexOf(date); //
            if (index == -1) {
                // date not found in the list, add it (new date)
                dates.add(date);
                totalPrices.add(price);
                sellerCounts.add(1);
            } else {
                // date found, update total price and seller count
                totalPrices.set(index, totalPrices.get(index) + price);
                sellerCounts.set(index, sellerCounts.get(index) + 1);
            }
        }

        // calculate average prices

        System.out.println("Price Trend Chart for " + lookupItems.get(0).getItem());
        System.out.println("Days | Price");
        System.out.println("--------------");

        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            double averagePrice = totalPrices.get(i) / sellerCounts.get(i);
            
            // Use the itemCode from the current priceCatcher (find item by code)
            String itemCode = priceCatchers.get(i).getItemCode();
            
            LookupItem item = findItemByCode(lookupItems, itemCode);
            
            System.out.printf("%02d | $%.2f\n", i + 1, averagePrice);
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
}
