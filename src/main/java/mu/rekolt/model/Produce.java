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
}
