package mu.rekolt.service;

import mu.rekolt.model.*;
import java.util.*;

public class SeasonService {
    private double[][] weeklyGrid = new double[21][4];
    private List<Deliveries> deliveries = new ArrayList<>();
    private Map<String, Double> memberTotals = new HashMap<>();
    private Map<String, List<Deliveries>> deliveriesByMember = new HashMap<>();
    private Set<String> memberIds = new HashSet<>();
    private Map<String, Member> members = new HashMap<>();



    public void addDelivery(String member_Id, String memberName, String produceCode, double massKg, int score, int week) {
        Grade grade = Grade.fromScore(score);
        String deliveryId = "D-" + (1000 + deliveries.size() + 1);
        Deliveries delivery = new Deliveries(
                deliveryId, member_Id, memberName, produceCode, massKg, score, week);
        double netPayable = delivery.netPayable();


        deliveries.add(delivery);
        weeklyGrid[week][produceColumnIndex(produceCode)] += massKg;
        memberTotals.put(member_Id, memberTotals.getOrDefault(member_Id, 0.0) + netPayable);

        List<Deliveries> md = deliveriesByMember.getOrDefault(member_Id, new ArrayList<>());
        md.add(delivery);
        deliveriesByMember.put(member_Id, md);

        memberIds.add(member_Id);
        members.putIfAbsent(member_Id, new Member(member_Id, memberName));

        System.out.printf("Delivery %s recorded. Grade %s%n", deliveryId, grade);
        System.out.printf("NET PAYABLE = %.2f MUR%n", netPayable);
    }

    private int produceColumnIndex(String produceCode) {
        switch (produceCode) {
            case "MZE": return 0;
            case "BNS": return 1;
            case "POT": return 2;
            case "TEA": return 3;
            default: throw new IllegalArgumentException("Unknown produce code: " + produceCode);
        }
    }

    public void printMemberTotals() {
        System.out.println("Total payment per member (MUR)");
        for (String id : memberTotals.keySet()) {
            System.out.printf("%s   %s   %.2f%n", id, members.get(id).getName(), memberTotals.get(id));
        }
    }

    public void printWeeklyGrid() {
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

//    public static void printWeeklyGrid(double[][] weeklyGrid, int singleWeek) {
//        System.out.println("Weekly volume grid (kg) — week " + singleWeek);
//        double weekTotal = 0;
//        for (int col = 0; col < 4; col++) {
//            weekTotal += weeklyGrid[singleWeek][col];
//        }
//        System.out.printf("MZE %.1f  BNS %.1f  POT %.1f  TEA %.1f  Total %.1f%n",
//                weeklyGrid[singleWeek][0], weeklyGrid[singleWeek][1],
//                weeklyGrid[singleWeek][2], weeklyGrid[singleWeek][3], weekTotal);
//
//    }

    public  List<Deliveries> topDeliveriesByValue(int n) {
        List<Deliveries> sorted = new ArrayList<>(deliveries);
        sorted.sort(new Comparator<Deliveries>() {
            public int compare(Deliveries a, Deliveries b) {
                return Double.compare(b.getNetPayable(), a.getNetPayable());
            }
        });
        List<Deliveries> top = new ArrayList<>();
        for (int i = 0; i < n && i < sorted.size(); i++) {
            top.add(sorted.get(i));
        }
        return top;
    }

    public  Member findMemberById(Map<String, Member> members, String id) {
        if (members.containsKey(id)) {
            return members.get(id);
        } else {
            return null;
        }
    }

    public static List<Deliveries> excludingRejected(List<Deliveries> deliveries) {
        List<Deliveries> copy = new ArrayList<>(deliveries);
        Iterator<Deliveries> it = copy.iterator();
        while (it.hasNext()) {
            Deliveries d = it.next();
            if (d.getGrade().equals("REJECT")) {
                it.remove();
            }
        }
        return copy;
    }


    public List<Member> getSortedMembers() {
        List<Member> list = new ArrayList<>(members.values());
        Collections.sort(list);
        return list;
    }

    public List<Deliveries> getDeliveries() { return deliveries; }

    public List<Deliveries> getDeliveriesForMember(String memberId) {
        return deliveriesByMember.getOrDefault(memberId, new ArrayList<>());
    }

    public double getSeasonTotal() {
        double total = 0;
        for (double value : memberTotals.values()) {
            total += value;
        }
        return total;
    }
}
