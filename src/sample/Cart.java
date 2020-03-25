package sample;

public class Cart {
    int count;
    double total;

    public Cart(int count, double total) {
        this.count = count;
        this.total = total;
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
}
