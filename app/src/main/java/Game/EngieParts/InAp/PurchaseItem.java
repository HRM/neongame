package Game.EngieParts.InAp;

public class PurchaseItem {
    private String name;
    private String price;
    private String description;
    private boolean isPayed;

    public PurchaseItem(String name, String price, String description, boolean isPayed) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.isPayed = isPayed;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setPayed(boolean payed) {
        isPayed = payed;
    }
}
