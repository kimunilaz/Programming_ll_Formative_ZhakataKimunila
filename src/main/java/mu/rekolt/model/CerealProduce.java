package mu.rekolt.model;

public class CerealProduce extends Produce {
    private double price;

    public CerealProduce(String code, double massKg, int qualityScore, double price) {
        super(code, massKg, qualityScore);
        this.price = price;
    }

    protected double unitPrice() { return price; }
    public double categoryMultiplier() { return 1.00; }
    public String getCategoryName() { return "Cereal"; }
}
