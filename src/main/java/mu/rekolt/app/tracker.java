package mu.rekolt.app;

import mu.rekolt.model.Deliveries;
import mu.rekolt.model.Grade;
import mu.rekolt.model.Member;
import mu.rekolt.model.Produce;
import mu.rekolt.service.ReportService;
import mu.rekolt.service.SeasonService;
import mu.rekolt.util.InputValidator;

import java.util.Comparator;

import java.util.*;
import java.util.Collections;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import static java.lang.IO.print;


public class tracker {










 //   public static void recordDelivery(double[][] weeklyGrid, List<Deliveries> deliveries, Map<String, Double> individualsTotals,Map<String, List<Deliveries>> member_deliveries, Set<String> memberIds, Map<String, Member> members) {





        //calling methods
//        String product_code = Product_Code();
//        double massKg = promptMass();
//        String member_Id = MemberId();
//        int score = Score();
//        int week = Week();
//
//        int column = produceColumnIndex(product_code);
//        weeklyGrid[week][column] += massKg;
//
//
//        Produce produce = Produce.create(product_code, massKg, score);
//        Grade grade = Grade.fromScore(score);
//
//        String deliveryId = "D-" + (1000 + deliveries.size() + 1);
//        Deliveries delivery = new Deliveries(deliveryId, member_Id, product_code,
//                massKg, score, week);
//        deliveries.add(delivery);
//        double netPayable = delivery.netPayable();
//
//
//        double currentTotal = individualsTotals.getOrDefault(member_Id, 0.0);
//        individualsTotals.put(member_Id, currentTotal + netPayable);
//        List<Deliveries> memberDeliveries = member_deliveries.getOrDefault(member_Id, new ArrayList<>());
//        memberDeliveries.add(delivery);
//        member_deliveries.put(member_Id, memberDeliveries);
//
//        memberIds.add(member_Id);
//        members.putIfAbsent(member_Id, new Member(member_Id));

        //testing
        //System.out.println(member_deliveries.get(member_Id).size());
        //System.out.println(memberIds.size());
//
//        System.out.printf("Delivery %s recorded. Grade %s%n", deliveryId, grade);
//        System.out.printf("NET PAYABLE = %.2f MUR%n", netPayable);
//    }
//




//
//        System.out.printf("Grade %s%n", gradeLabel);
//        System.out.printf("Base value %.2f%n", baseValue);
//        System.out.printf("Graded value %.2f%n", gradedValue);
//        System.out.printf("Category value %.2f%n", categoryValue);
//        System.out.printf("NET PAYABLE = %.2f MUR%n", netPayable);





//    public static int produceColumnIndex(String product_code) {
//        switch (product_code) {
//            case "MZE":
//                return 0;
//            case "BNS":
//                return 1;
//            case "POT":
//                return 2;
//            case "TEA":
//                return 3;
//            default:
//                throw new IllegalArgumentException("Unknown produce code: " + product_code);
//        }
//
//    }







    public static void main(String[] args) {

        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        SeasonService season = new SeasonService();

//        double[][] weeklyGrid = new double[21][4];
//        List<Deliveries> deliveries = new ArrayList<>();
//        Map<String, Double> individualsTotals = new HashMap<>();
//        Map<String, List<Deliveries>> member_deliveries = new HashMap<>();
//
//        Set<String> member_Ids = new HashSet<>();
//
//        Map<String, Member> members = new HashMap<>();




        //session loop

        while (running) {
            System.out.println();
            System.out.println("REKOLT PRODUCE TRACKER");
            System.out.println("1. Record a delivery   3. Generate the season report");
            System.out.println("2. Season figures on screen   4. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

//here the switch calls the method associated with the choice of the user
            switch (choice) {
                case "1":
                    String member_Id = InputValidator.MemberId();
                    //String memberName = InputValidator.promptMemberName();
                    String produceCode = InputValidator.Product_Code();
                    double massKg = InputValidator.promptMass();
                    int qualityScore = InputValidator.Score();
                    int week = InputValidator.Week();
                    season.addDelivery(member_Id, produceCode, massKg, qualityScore, week);

                    break;

                case "2":
                    for (Member m : season.getSortedMembers()) {
                        System.out.println(m.toReportRow());
                    }
                    season.printMemberTotals();
                    season.printWeeklyGrid();
                    System.out.println("Top five deliveries by value");
                    for (Deliveries d : season.topDeliveriesByValue(5)) {
                        System.out.println(d.toReportRow());
                    }
                    break;


//                    printWeeklyGrid();
//                    List<Deliveries> top5 = topDeliveriesByValue(deliveries, 5);
//
//                    System.out.println("top five deliveries");
//
//                    for (Deliveries d : top5) {
//                        System.out.printf("%s   %s   %s   %.1f kg   %s   %.2f%n",
//                                d.getId(), d.getMemberId(), d.getProduceCode(), d.getMassKg(), d.getGrade(), d.getNetPayable());
//                    }
//
//                    List<Deliveries> payingOnly = excludingRejected(deliveries);
//                    System.out.println("Deliveries excluding REJECT: " + payingOnly.size() + " of " + deliveries.size() + " total");
//                    break;

                case "3":
                    new ReportService().generate(season);;
                    break;


                case "4":
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