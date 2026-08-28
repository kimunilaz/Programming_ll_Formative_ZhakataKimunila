package mu.rekolt.app;

import mu.rekolt.model.Deliveries;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import static java.lang.IO.print;


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

    public static String MemberId() {

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
        return memberId;
    }

    public static int Week() {
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

        return week;
    }

    public static String Product_Code() {

        String product_code = "";
        boolean validCode = false;

        while (!validCode) {
            System.out.print("Product code (MZE/BNS/POT/TEA) : ");
            Scanner input = new Scanner(System.in);
            String code = input.nextLine();
            if (code.equals("MZE") || code.equals("BNS") || code.equals("POT") || code.equals("TEA")) {
                product_code = code;
                validCode = true;
            } else {
                System.out.println("Produce code must be MZE, BNS, POT or TEA. Please try again.");
            }
        }

        return product_code;
    }

    public static int Score() {

        int qualityScore = 0;
        boolean validScore = false;

        while (!validScore) {
            System.out.print("Quality score (0-100) : ");
            Scanner input = new Scanner(System.in);
            String score = input.nextLine();
            qualityScore = Integer.parseInt(score);
            if (qualityScore >= 0 && qualityScore <= 100) {
                validScore = true;
                } else {
                    System.out.println("Quality score must be between 0 and 100. Please try again.");
                }

        }


        return qualityScore;
    }



    public static void recordDelivery(double[][] weeklyGrid, List<Deliveries> deliveries) {





        //calling methods
        String product_code = Product_Code();
        double massKg = promptMass();
        String member_Id = MemberId();
        int score = Score();
        int week = Week();

        int column = produceColumnIndex(product_code);
        weeklyGrid[week][column] += massKg;

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
        if (score >= 85) {
            gradeMultiplier = 1.15;
            gradeLabel = "A";
        } else if (score >= 70) {
            gradeMultiplier = 1.00;
            gradeLabel = "B";
        } else if (score >= 50) {
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


        String deliveryId = "D-" + (1000 + deliveries.size() + 1);
        Deliveries delivery = new Deliveries(deliveryId, member_Id, memberName, product_code,
                massKg, score, week, gradeLabel, netPayable);
        deliveries.add(delivery);




        System.out.printf("Grade %s%n", gradeLabel);
        System.out.printf("Base value %.2f%n", baseValue);
        System.out.printf("Graded value %.2f%n", gradedValue);
        System.out.printf("Category value %.2f%n", categoryValue);
        System.out.printf("NET PAYABLE = %.2f MUR%n", netPayable);

    }

    public static int produceColumnIndex(String product_code) {
        switch (product_code) {
            case "MZE":
                return 0;
            case "BNS":
                return 1;
            case "POT":
                return 2;
            case "TEA":
                return 3;
            default:
                throw new IllegalArgumentException("Unknown produce code: " + product_code);
        }

    }


    public static void printWeeklyGrid(double[][] weeklyGrid) {
        System.out.println("Weekly volume grid (kg)");
        System.out.println("Week    MZE     BNS     POT     TEA     Total");
        for (int week = 1; week <= 20; week++) {
            double weekTotal = 0;
            for (int col = 0; col < 4; col++) {
                weekTotal += weeklyGrid[week][col];
            }
            if (weekTotal > 0) {
                System.out.printf("%-8d%-8.1f%-8.1f%-8.1f%-8.1f%-8.1f%n",
                        week, weeklyGrid[week][0], weeklyGrid[week][1],
                        weeklyGrid[week][2], weeklyGrid[week][3], weekTotal);
            }
        }
    }

    public static void printWeeklyGrid(double[][] weeklyGrid, int singleWeek) {
        System.out.println("Weekly volume grid (kg) — week " + singleWeek);
        double weekTotal = 0;
        for (int col = 0; col < 4; col++) {
            weekTotal += weeklyGrid[singleWeek][col];
        }
        System.out.printf("MZE %.1f  BNS %.1f  POT %.1f  TEA %.1f  Total %.1f%n",
                weeklyGrid[singleWeek][0], weeklyGrid[singleWeek][1],
                weeklyGrid[singleWeek][2], weeklyGrid[singleWeek][3], weekTotal);

        }

    public static void main(String[] args) {

        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        double[][] weeklyGrid = new double[21][4];
        List<Deliveries> deliveries = new ArrayList<>();


        //session loop

        while (running) {
            System.out.println();
            System.out.println("REKOLT PRODUCE TRACKER");
            System.out.println("1. Record a delivery ");
            System.out.println("2. Print weekly grid ");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

//here the switch calls the method associated with the choice of the user
            switch (choice) {
                case "1":
                    recordDelivery(weeklyGrid, deliveries);
                    break;

                case "2":
                    printWeeklyGrid(weeklyGrid);

                case "3":
                    running = false;
                    System.out.println("Exiting Session");
                    break;
                default:
                    System.out.println("Please choose 1, 2, or 3.");
                    break;
            }
        }



    }
}