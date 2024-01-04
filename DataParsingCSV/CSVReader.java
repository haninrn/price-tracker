package DataParsingCSV;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CSVReader {

    public List<LookupItem> readLookupItemCSV(String filePath) {
        List<LookupItem> lookupItems = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                LookupItem item = new LookupItem(data[0], data[1], data[2], data[3], data[4]);
                lookupItems.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lookupItems;
    }
    public List<LookupPremise> readLookupPremiseCSV(String filePath) {
        List<LookupPremise> lookupPremises = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                LookupPremise premise = new LookupPremise(data[0], data[1], data[2], data[3], data[4], data[5]);
                lookupPremises.add(premise);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lookupPremises;
    }

    public List<PriceCatcher> readPriceCatcherCSV(String filePath) {
        List<PriceCatcher> priceCatchers = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                LocalDate date = LocalDate.parse(data[0]); // Assuming the date is in ISO format (yyyy-MM-dd)
                String premiseCode = data[1];
                String itemCode = data[2];
                double price = Double.parseDouble(data[3]);

                PriceCatcher priceCatcher = new PriceCatcher(date, premiseCode, itemCode, price);
                priceCatchers.add(priceCatcher);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return priceCatchers;
    }

    // Similar methods for reading LookupPremise and PriceCatcher CSV files
}

class PriceCatcher {
    private LocalDate date;
    private String premiseCode;
    private String itemCode;
    private double price;

    public PriceCatcher(LocalDate date, String premiseCode, String itemCode, double price) {
        this.date = date;
        this.premiseCode = premiseCode;
        this.itemCode = itemCode;
        this.price = price;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPremiseCode() {
        return premiseCode;
    }

    public void setPremiseCode(String premiseCode) {
        this.premiseCode = premiseCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class LookupItem {
    private String itemCode;
    private String item;
    private String unit;
    private String itemGroup;
    private String itemCategory;

    public LookupItem(String itemCode, String item, String unit, String itemGroup, String itemCategory) {
        this.itemCode = itemCode;
        this.item = item;
        this.unit = unit;
        this.itemGroup = itemGroup;
        this.itemCategory = itemCategory;
    }

    // Getters
    public String getItemCode() {
        return itemCode;
    }

    public String getItem() {
        return item;
    }

    public String getUnit() {
        return unit;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    // Setters
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
}

class LookupPremise {
    private String premiseCode;
    private String premise;
    private String address;
    private String premiseType;
    private String state;
    private String district;

    public LookupPremise(String premiseCode, String premise, String address, String premiseType, String state, String district) {
        this.premiseCode = premiseCode;
        this.premise = premise;
        this.address = address;
        this.premiseType = premiseType;
        this.state = state;
        this.district = district;
    }

    // Getters
    public String getPremiseCode() {
        return premiseCode;
    }

    public String getPremise() {
        return premise;
    }

    public String getAddress() {
        return address;
    }

    public String getPremiseType() {
        return premiseType;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    // Setters
    public void setPremiseCode(String premiseCode) {
        this.premiseCode = premiseCode;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPremiseType(String premiseType) {
        this.premiseType = premiseType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}


