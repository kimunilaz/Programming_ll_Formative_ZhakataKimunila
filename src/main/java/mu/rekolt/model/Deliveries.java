package mu.rekolt.model;


public class Deliveries{
    private String id;
    private String member_Id;
    private String product_code;
    private double massKg;
    private int score;
    private int week;
    private String grade;
    private double netPayable;

    public Deliveries(String id, String member_Id, String product_code,
                    double massKg, int score, int week, String grade, double netPayable) {
        this.id = id;
        this.member_Id = member_Id;
        this.product_code = product_code;
        this.massKg = massKg;
        this.score = score;
        this.week = week;
        this.grade = grade;
        this.netPayable = netPayable;
    }

    public String getId() {
        return id;
    }

    public String getMemberId() {
        return member_Id; }

    public double getNetPayable() {
        return netPayable; }

    public double getMassKg() {
        return massKg; }

    public String getProduceCode() {
        return product_code; }

    public String getGrade() {
        return grade; }
}