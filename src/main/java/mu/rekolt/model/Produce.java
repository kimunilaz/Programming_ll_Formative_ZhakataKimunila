package mu.rekolt.model;

public abstract class Produce {
    private String code;
    private double massKg;
    private int qualityScore;

    public Produce(String code, double massKg, int qualityScore) {
        this.code = code;
        this.massKg = massKg;
        this.qualityScore = qualityScore;
    }

    public double baseValue() {
        return massKg * unitPrice();
    }

    protected abstract double unitPrice();
    public abstract double categoryMultiplier();

    public String getCode() { return code; }
    public double getMassKg() { return massKg; }
    public int getQualityScore() { return qualityScore; }


    public static Produce create(String code, double massKg, int qualityScore) {
        switch (code) {
            case "MZE":
                return new CerealProduce(code, massKg, qualityScore, 30);
            case "BNS":
                return new CerealProduce(code, massKg, qualityScore, 90);
            case "POT":
                return new PerishableProduce(code, massKg, qualityScore, 45);
            case "TEA":
                return new CashCropProduce(code, massKg, qualityScore, 25);
            default:
                throw new IllegalArgumentException("Unknown produce code: " + code);
        }
    }
}

