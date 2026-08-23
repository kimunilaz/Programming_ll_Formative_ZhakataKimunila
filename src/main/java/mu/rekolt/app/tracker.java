package mu.rekolt.app;

public class tracker {
    public static void main(String[] args) {

        String product_code = "BNS";
        float massKg = 236;
        int qualityScore = 91;


        double unitPrice;
        double categoryMultiplier;
        switch (product_code) {
            case "MZE":
                unitPrice = 30;
                categoryMultiplier = 1;
                break;
            case "BNS":
                unitPrice = 90;
                categoryMultiplier = 1;
                break;
            case "POT":
                unitPrice = 45;
                categoryMultiplier = 0.90;
                break;
            case "TEA":
                unitPrice = 25;
                categoryMultiplier = 1.10;
                break;
            default:
                throw new IllegalArgumentException("Unknown produce code: " + product_code);
        }


        double gradeMultiplier;
        String gradeLabel;
        if (qualityScore >= 85) {
            gradeMultiplier = 1.15;
            gradeLabel = "A";
        } else if (qualityScore >= 70) {
            gradeMultiplier = 1.00;
            gradeLabel = "B";
        } else if (qualityScore >= 50) {
            gradeMultiplier = 0.85;
            gradeLabel = "C";
        } else {
            gradeMultiplier = 0.00;
            gradeLabel = "REJECT";
        }

        double baseValue = massKg * unitPrice;
        double gradedValue = baseValue * gradeMultiplier;
        double categoryValue = gradedValue * categoryMultiplier;

        double netPayable;
        if (gradeLabel.equals("REJECT")) {
            netPayable = 0.00;
        } else {
            double commission = categoryValue * 0.05;
            double transportLevy = massKg * 2;
            netPayable = categoryValue - commission - transportLevy;
        }

        System.out.printf("Grade %s%n", gradeLabel);
        System.out.printf("Base value %.2f%n", baseValue);
        System.out.printf("Graded value %.2f%n", gradedValue);
        System.out.printf("Category value %.2f%n", categoryValue);
        System.out.printf("NET PAYABLE = %.2f MUR%n", netPayable);
    }
}