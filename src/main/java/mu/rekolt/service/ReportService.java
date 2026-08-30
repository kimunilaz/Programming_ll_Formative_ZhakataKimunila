package mu.rekolt.service;

import mu.rekolt.model.Deliveries;
import mu.rekolt.model.Member;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportService {

    private void appendRunLog() throws IOException {
        String timestamp = java.time.LocalDateTime.now().toString();
        try (java.io.FileWriter writer = new java.io.FileWriter("output/run-log.txt", true)) {
            writer.write(timestamp + " — season-report.docx generated\n");
        }
    }

    public void generate(SeasonService season) {
        try (XWPFDocument document = new XWPFDocument()) {

            List<Member> members = season.getSortedMembers();

            for (int i = 0; i < members.size(); i++) {
                Member member = members.get(i);

                if (i > 0) {
                    XWPFParagraph pageBreak = document.createParagraph();
                    pageBreak.setPageBreak(true);
                }

                XWPFParagraph heading = document.createParagraph();
                XWPFRun headingRun = heading.createRun();
                headingRun.setText("Payment Statement — " + member.getId() + "  " + member.getName());
                headingRun.setBold(true);
                headingRun.setFontSize(14);

                List<Deliveries> memberDeliveries = season.getDeliveriesForMember(member.getId());
                double memberTotal = 0;

                for (Deliveries d : memberDeliveries) {
                    XWPFParagraph line = document.createParagraph();
                    XWPFRun lineRun = line.createRun();
                    lineRun.setText(d.toReportRow());
                    memberTotal += d.getNetPayable();
                }

                XWPFParagraph netPara = document.createParagraph();
                XWPFRun netRun = netPara.createRun();
                netRun.setText(String.format("NET PAYABLE: %.2f MUR", memberTotal));
                netRun.setBold(true);

                XWPFParagraph signature = document.createParagraph();
                XWPFRun sigRun = signature.createRun();
                sigRun.setText("Signature: _______________________");
            }

            XWPFParagraph closingBreak = document.createParagraph();
            closingBreak.setPageBreak(true);

            XWPFParagraph closingHeading = document.createParagraph();
            XWPFRun closingRun = closingHeading.createRun();
            closingRun.setText("Season Totals");
            closingRun.setBold(true);
            closingRun.setFontSize(14);

            XWPFParagraph closingTotal = document.createParagraph();
            XWPFRun closingTotalRun = closingTotal.createRun();
            closingTotalRun.setText(String.format("Total season payout: %.2f MUR", season.getSeasonTotal()));

            try (FileOutputStream out = new FileOutputStream("output/season-report.docx")) {
                document.write(out);
            }

            appendRunLog();
            System.out.println("Writing output/season-report.docx ... " + members.size() + " member sections, done.");


        } catch (IOException e) {
            System.out.println("Could not write the report: " + e.getMessage());
        }
    }
}
