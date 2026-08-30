package mu.rekolt.model;

public class CashCropProduce extends Produce {
    private double price;
    public CashCropProduce(String code, double massKg, int qualityScore, double price) {
        super(code, massKg, qualityScore);
        this.price = price;
    }
    protected double unitPrice() { return price; }
    public double categoryMultiplier() { return 1.10; }
}
