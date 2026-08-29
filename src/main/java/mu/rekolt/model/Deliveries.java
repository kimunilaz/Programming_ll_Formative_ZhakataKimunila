package mu.rekolt.model;


public class Deliveries implements Payable, Reportable {
    private String id;
    private String member_Id;
    private Produce produce;
    private int week;
    private Grade grade;

    public Deliveries(String id, String member_Id, String productCode,
                      double massKg, int score, int week) {
        this.id = id;
        this.member_Id = member_Id;
        this.produce = Produce.create(productCode, massKg, score);
        this.week = week;

    }

    @Override
    public double netPayable() {
        if (grade == Grade.REJECT) {
            return 0.00;
        }
        double categoryValue = produce.baseValue() * grade.getMultiplier() * produce.categoryMultiplier();
        double commission = categoryValue * 0.05;
        double transportLevy = produce.getMassKg() * 2;
        return categoryValue - commission - transportLevy;
    }

    @Override
    public String toReportRow() {
        return id + "  " + member_Id + "  " + produce.getCode() + "  " +
                String.format("%.1f kg", produce.getMassKg()) + "  " +
                grade + "  " + String.format("%.2f", netPayable());
    }

    @Override
    public String toString() {
        return toReportRow();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Deliveries)) return false;
        Deliveries other = (Deliveries) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() { return id; }
    public String getMemberId() { return member_Id; }
   // public String getMemberName() { return memberName; }
    public String getProduceCode() { return produce.getCode(); }
    public double getMassKg() { return produce.getMassKg(); }
    public String getGrade() { return grade.toString(); }
    public double getNetPayable() { return netPayable(); }
    public int getWeek() { return week; }
}
