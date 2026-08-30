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



    public static void main(String[] args) {

        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        SeasonService season = new SeasonService();



        //session loop

        while (running) {
            System.out.println();
            System.out.println("REKOLT PRODUCE TRACKER");
            System.out.println("1. Record a delivery   3. Generate the season report");
            System.out.println("2. Season figures on screen   4. Exit");
            System.out.println("5. Search member by ID");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

//here the switch calls the method associated with the choice of the user
            switch (choice) {
                case "1":
                    String member_Id = InputValidator.MemberId();
                    String memberName = InputValidator.promptMemberName();
                    String produceCode = InputValidator.Product_Code();
                    double massKg = InputValidator.promptMass();
                    int qualityScore = InputValidator.Score();
                    int week = InputValidator.Week();
                    season.addDelivery(member_Id, memberName, produceCode, massKg, qualityScore, week);

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

                case "3":
                    new ReportService().generate(season);;
                    break;


                case "4":
                    running = false;
                    System.out.println("Exiting Session");
                    break;

                case "5":
                    String searchId = InputValidator.MemberId();
                    Member foundMember = season.findMemberById(searchId);
                    if (foundMember != null) {
                        System.out.println("Member found");
                        System.out.println("Member ID: " + foundMember.getId());
                        System.out.println("Member name: " + foundMember.getName());
                        System.out.println("Deliveries recorded: "
                                + season.getDeliveriesForMember(foundMember.getId()).size());
                    } else {
                        System.out.println("No member found with ID " + searchId);
                    }
                    break;

                default:
                    System.out.println("Please choose an option from 1 to 5.");
                    break;
            }
        }



    }
}
