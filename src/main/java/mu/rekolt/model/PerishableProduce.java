package mu.rekolt.model;

public class PerishableProduce extends Produce {
    private double price;
    public PerishableProduce(String code, double massKg, int qualityScore, double price) {
        super(code, massKg, qualityScore);
        this.price = price;
    }
    protected double unitPrice() { return price; }
    public double categoryMultiplier() { return 0.90; }
}