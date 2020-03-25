package sample;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    int count, orderCount;
    double total;
    ArrayList<String> items;
    ArrayList<String> barcodes;

    public Cart(int count, double total) {
        this.count = count;
        this.total = total;
        this.orderCount = 0;
        this.items= new ArrayList<String>();
        this.barcodes= new ArrayList<String>();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getOrderCount() { return orderCount; }

    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<String> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<String> barcodes) {
        this.barcodes = barcodes;
    }
}
