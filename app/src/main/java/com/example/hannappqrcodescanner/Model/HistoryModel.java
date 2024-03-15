package com.example.hannappqrcodescanner.Model;

public class HistoryModel {
    String amount;
    String category;
    String brand;
    String condition;
    String date;
    String employee;
    String item;
    String property;
    String serial;
    String remarks;
    String scannedDate;
    String remarksDate;

    public HistoryModel(String amount, String category, String brand,
                        String condition, String date, String employee,
                        String item, String property, String serial,
                        String remarks, String scannedDate,String remarksDate) {
        this.amount = amount;
        this.category = category;
        this.brand = brand;
        this.condition = condition;
        this.date = date;
        this.employee = employee;
        this.item = item;
        this.property = property;
        this.serial = serial;
        this.remarks = remarks;
        this.scannedDate = scannedDate;
        this.remarksDate = remarksDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getCondition() {
        return condition;
    }

    public String getDate() {
        return date;
    }

    public String getEmployee() {
        return employee;
    }

    public String getItem() {
        return item;
    }

    public String getProperty() {
        return property;
    }

    public String getSerial() {
        return serial;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getScannedDate() {
        return scannedDate;
    }
    public String getRemarksDate(){return remarksDate;}
}
