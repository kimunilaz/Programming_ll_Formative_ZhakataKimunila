package mu.rekolt.model;


public class Deliveries implements Payable, Reportable {
    private String id;
    private String member_Id;
    private String memberName;
    private Produce produce;
    private int week;
    private Grade grade;

    public Deliveries(String id, String member_Id, String memberName, String productCode,
                      double massKg, int score, int week) {
        this.id = id;
        this.member_Id = member_Id;
        this.memberName = memberName;
        this.produce = Produce.create(productCode, massKg, score);
        this.grade = Grade.fromScore(score);
        this.week = week;

    }

    @Override
    public double netPayable() {
        if (grade == Grade.REJECT) {
            return 0.00;
        }
        double categoryValue = produce.baseValue() * grade.getMultiplier() * produce.categoryMultiplier();
        double transportLevy = produce.getMassKg() * 2;
        return categoryValue - getCommission() - transportLevy;
    }

    public double getCommission() {
        if (grade == Grade.REJECT) {
            return 0.00;
        }
        double categoryValue = produce.baseValue() * grade.getMultiplier() * produce.categoryMultiplier();
        return categoryValue * 0.05;
    }

    @Override
    public String toReportRow() {
        return id + "  " + member_Id + "  " + memberName + "  " + produce.getCode() + "  " +
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
    public String getMemberName() { return memberName; }
    public String getProduceCode() { return produce.getCode(); }
    public String getProduceCategory() { return produce.getCategoryName(); }
    public double getMassKg() { return produce.getMassKg(); }
    public int getQualityScore() { return produce.getQualityScore(); }
    public double getUnitPrice() { return produce.getUnitPrice(); }
    public double getCategoryMultiplier() { return produce.categoryMultiplier(); }
    public String getGrade() { return grade.toString(); }
    public double getNetPayable() { return netPayable(); }
    public int getWeek() { return week; }
}
