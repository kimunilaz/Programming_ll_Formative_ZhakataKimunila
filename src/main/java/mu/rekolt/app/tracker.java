package mu.rekolt.app;

import java.util.Scanner;

public class tracker {

    public static double promptMass() {
        double massKg = 0;
        boolean validMass = false;
        while (!validMass) {
            System.out.print("Mass in kg : ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            try {
                massKg = Double.parseDouble(input);
                if (massKg > 0 && massKg <= 5000) {
                    validMass = true;
                } else {
                    System.out.println("Mass must be above 0 and not more than 5000. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a number. Please try again.");
            }
        }
        return massKg;
    }
    public static void main(String[] args) {

        String memberId = "";
        boolean validId = false;

        while (!validId) {
            System.out.print("Member identifier : ");
            Scanner input = new Scanner(System.in);
            String member = input.nextLine();

            if (member.matches("M-\\d{4}")) {
                memberId = member;
                validId = true;
            } else {
                System.out.println("Member identifier must be in the form M-0000. Please try again.");
            }
        }

        int week = 0;
        boolean validWeek = false;

        while (!validWeek) {
            System.out.print("Week of delivery (1-20) : ");
            Scanner input = new Scanner(System.in);
            String Input = input.nextLine();
            try {
                week = Integer.parseInt(Input);
                if (week >= 1 && week <= 20) {
                    validWeek = true;
                } else {
                    System.out.println("Week must be between 1 and 20. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Please try again.");
            }
        }





        String product_code = "";
        boolean validCode = false;

        while (!validCode) {
            System.out.print("Product code (MZE/BNS/POT/TEA) : ");
            Scanner input = new Scanner(System.in);
            String code = input.nextLine().trim();
            if (code.equals("MZE") || code.equals("BNS") || code.equals("POT") || code.equals("TEA")) {
                product_code = code;
                validCode = true;
            } else {
                System.out.println("Produce code must be MZE, BNS, POT or TEA. Please try again.");
            }
        }
        //float massKg = 236;
        double massKg = promptMass();
        int qualityScore = 0;
        boolean validScore = false;

        while (!validScore) {
            System.out.print("Quality score (0-100) : ");
            Scanner input = new Scanner(System.in);
            String score = input.nextLine();
            try {
                qualityScore = Integer.parseInt(score);
                if (qualityScore >= 0 && qualityScore <= 100) {
                    validScore = true;
                } else {
                    System.out.println("Quality score must be between 0 and 100. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a whole number. Please try again.");
            }
        }


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