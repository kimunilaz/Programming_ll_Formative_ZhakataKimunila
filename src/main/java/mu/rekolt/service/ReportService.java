package mu.rekolt.service;

import mu.rekolt.model.Deliveries;
import mu.rekolt.model.Member;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportService {

    private static final String DARK_BLUE = "1F4E78";
    private static final String LIGHT_BLUE = "D9EAF7";

    private void appendRunLog() throws IOException {
        String timestamp = java.time.LocalDateTime.now().toString();
        try (java.io.FileWriter writer = new java.io.FileWriter("output/run-log.txt", true)) {
            writer.write(timestamp + " - season-report.docx generated\n");
        }
    }

    private void addHeaderAndFooter(XWPFDocument document) {
        XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);
        XWPFParagraph headerParagraph = header.createParagraph();
        headerParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun headerRun = headerParagraph.createRun();
        headerRun.setText("REKOLT PRODUCE TRACKER");
        headerRun.setBold(true);
        headerRun.setColor(DARK_BLUE);
        headerRun.setFontFamily("Calibri");
        headerRun.setFontSize(10);

        XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);
        XWPFParagraph footerParagraph = footer.createParagraph();
        footerParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun footerRun = footerParagraph.createRun();
        footerRun.setText("Season payment report");
        footerRun.setColor("666666");
        footerRun.setFontFamily("Calibri");
        footerRun.setFontSize(9);
    }

    private void addReportTitle(XWPFDocument document, String title) {
        XWPFParagraph trackerTitle = document.createParagraph();
        trackerTitle.setAlignment(ParagraphAlignment.CENTER);
        trackerTitle.setSpacingAfter(60);
        XWPFRun trackerRun = trackerTitle.createRun();
        trackerRun.setText("REKOLT PRODUCE TRACKER");
        trackerRun.setBold(true);
        trackerRun.setColor(DARK_BLUE);
        trackerRun.setFontFamily("Calibri");
        trackerRun.setFontSize(18);

        XWPFParagraph reportTitle = document.createParagraph();
        reportTitle.setAlignment(ParagraphAlignment.CENTER);
        reportTitle.setSpacingAfter(240);
        XWPFRun reportRun = reportTitle.createRun();
        reportRun.setText(title);
        reportRun.setBold(true);
        reportRun.setFontFamily("Calibri");
        reportRun.setFontSize(14);
    }

    private void addLabelValue(XWPFDocument document, String label, String value) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingAfter(60);

        XWPFRun labelRun = paragraph.createRun();
        labelRun.setText(label + " ");
        labelRun.setBold(true);
        labelRun.setFontFamily("Calibri");
        labelRun.setFontSize(11);

        XWPFRun valueRun = paragraph.createRun();
        valueRun.setText(value);
        valueRun.setFontFamily("Calibri");
        valueRun.setFontSize(11);
    }

    private void addSectionHeading(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBefore(240);
        paragraph.setSpacingAfter(100);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setBold(true);
        run.setColor(DARK_BLUE);
        run.setFontFamily("Calibri");
        run.setFontSize(12);
    }

    private void writeCell(XWPFTableCell cell, String text, boolean header,
                           ParagraphAlignment alignment, int width) {
        cell.setWidth(String.valueOf(width));
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        if (header) {
            cell.setColor(LIGHT_BLUE);
        }

        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        paragraph.setAlignment(alignment);
        paragraph.setSpacingBefore(0);
        paragraph.setSpacingAfter(0);

        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontFamily("Calibri");
        run.setFontSize(10);
        run.setBold(header);
    }

    private XWPFTable createDeliveryTable(XWPFDocument document, List<Deliveries> deliveries) {
        int[] widths = {1150, 650, 750, 1500, 1050, 850, 1000, 2410};
        String[] headings = {"Delivery ID", "Week", "Code", "Category", "Mass (kg)", "Score", "Grade", "Net (MUR)"};

        XWPFTable table = document.createTable(1, headings.length);
        table.setStyleID("TableGrid");
        table.setTableAlignment(TableRowAlign.LEFT);
        table.setWidth(9360);
        table.setCellMargins(80, 120, 80, 120);
        table.getRow(0).setRepeatHeader(true);

        for (int column = 0; column < headings.length; column++) {
            writeCell(table.getRow(0).getCell(column), headings[column], true,
                    ParagraphAlignment.CENTER, widths[column]);
        }

        for (Deliveries delivery : deliveries) {
            XWPFTableRow row = table.createRow();
            writeCell(row.getCell(0), delivery.getId(), false, ParagraphAlignment.LEFT, widths[0]);
            writeCell(row.getCell(1), String.valueOf(delivery.getWeek()), false, ParagraphAlignment.CENTER, widths[1]);
            writeCell(row.getCell(2), delivery.getProduceCode(), false, ParagraphAlignment.CENTER, widths[2]);
            writeCell(row.getCell(3), delivery.getProduceCategory(), false, ParagraphAlignment.LEFT, widths[3]);
            writeCell(row.getCell(4), String.format("%.1f", delivery.getMassKg()), false, ParagraphAlignment.RIGHT, widths[4]);
            writeCell(row.getCell(5), String.valueOf(delivery.getQualityScore()), false, ParagraphAlignment.CENTER, widths[5]);
            writeCell(row.getCell(6), delivery.getGrade(), false, ParagraphAlignment.CENTER, widths[6]);
            writeCell(row.getCell(7), String.format("%.2f", delivery.getNetPayable()), false, ParagraphAlignment.RIGHT, widths[7]);
        }

        return table;
    }

    private double getMemberTotal(List<Deliveries> deliveries) {
        double total = 0;
        for (Deliveries delivery : deliveries) {
            total += delivery.getNetPayable();
        }
        return total;
    }

    private void addProduceSummary(XWPFDocument document, List<Deliveries> deliveries) {
        if (deliveries.isEmpty()) {
            XWPFParagraph emptyParagraph = document.createParagraph();
            emptyParagraph.createRun().setText("No produce deliveries were recorded for this season.");
            return;
        }

        int[] widths = {900, 1500, 1000, 1300, 1300, 1400, 1960};
        String[] headings = {"Code", "Category", "Deliveries", "Mass (kg)",
                "Unit price", "Category factor", "Payout (MUR)"};
        String[] produceCodes = {"MZE", "BNS", "POT", "TEA"};

        XWPFTable table = document.createTable(1, headings.length);
        table.setStyleID("TableGrid");
        table.setTableAlignment(TableRowAlign.LEFT);
        table.setWidth(9360);
        table.setCellMargins(80, 120, 80, 120);
        table.getRow(0).setRepeatHeader(true);

        for (int column = 0; column < headings.length; column++) {
            writeCell(table.getRow(0).getCell(column), headings[column], true,
                    ParagraphAlignment.CENTER, widths[column]);
        }

        for (String code : produceCodes) {
            int deliveryCount = 0;
            double totalMass = 0;
            double totalPayout = 0;
            Deliveries example = null;

            for (Deliveries delivery : deliveries) {
                if (delivery.getProduceCode().equals(code)) {
                    deliveryCount++;
                    totalMass += delivery.getMassKg();
                    totalPayout += delivery.getNetPayable();
                    example = delivery;
                }
            }

            if (example != null) {
                XWPFTableRow row = table.createRow();
                writeCell(row.getCell(0), code, false, ParagraphAlignment.CENTER, widths[0]);
                writeCell(row.getCell(1), example.getProduceCategory(), false, ParagraphAlignment.LEFT, widths[1]);
                writeCell(row.getCell(2), String.valueOf(deliveryCount), false, ParagraphAlignment.CENTER, widths[2]);
                writeCell(row.getCell(3), String.format("%.1f", totalMass), false, ParagraphAlignment.RIGHT, widths[3]);
                writeCell(row.getCell(4), String.format("%.2f", example.getUnitPrice()), false, ParagraphAlignment.RIGHT, widths[4]);
                writeCell(row.getCell(5), String.format("%.2f", example.getCategoryMultiplier()), false,
                        ParagraphAlignment.CENTER, widths[5]);
                writeCell(row.getCell(6), String.format("%.2f", totalPayout), false,
                        ParagraphAlignment.RIGHT, widths[6]);
            }
        }
    }

    private void addMemberStatement(XWPFDocument document, SeasonService season, Member member) {
        List<Deliveries> memberDeliveries = season.getDeliveriesForMember(member.getId());

        addReportTitle(document, "Member Payment Statement");
        addLabelValue(document, "Member ID:", member.getId());
        addLabelValue(document, "Member name:", member.getName());
        addLabelValue(document, "Number of deliveries:", String.valueOf(memberDeliveries.size()));

        addSectionHeading(document, "Delivery details");
        if (memberDeliveries.isEmpty()) {
            XWPFParagraph emptyParagraph = document.createParagraph();
            emptyParagraph.createRun().setText("No deliveries were recorded for this member.");
        } else {
            createDeliveryTable(document, memberDeliveries);
        }

        XWPFParagraph totalParagraph = document.createParagraph();
        totalParagraph.setAlignment(ParagraphAlignment.RIGHT);
        totalParagraph.setSpacingBefore(180);
        totalParagraph.setSpacingAfter(300);
        XWPFRun totalRun = totalParagraph.createRun();
        totalRun.setText(String.format("NET PAYABLE: %.2f MUR", getMemberTotal(memberDeliveries)));
        totalRun.setBold(true);
        totalRun.setColor(DARK_BLUE);
        totalRun.setFontFamily("Calibri");
        totalRun.setFontSize(12);

        XWPFParagraph signature = document.createParagraph();
        signature.setSpacingBefore(240);
        XWPFRun signatureRun = signature.createRun();
        signatureRun.setText("Member signature: __________________________    Date: _______________");
        signatureRun.setFontFamily("Calibri");
        signatureRun.setFontSize(11);
    }

    private void addSeasonSummary(XWPFDocument document, SeasonService season, List<Member> members) {
        addReportTitle(document, "Season Summary");
        addLabelValue(document, "Members recorded:", String.valueOf(members.size()));
        addLabelValue(document, "Deliveries recorded:", String.valueOf(season.getDeliveries().size()));
        addLabelValue(document, "Report date:",
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        addSectionHeading(document, "Payment summary by member");

        int[] widths = {1700, 3000, 1600, 3060};
        String[] headings = {"Member ID", "Member name", "Deliveries", "Amount (MUR)"};
        XWPFTable table = document.createTable(1, headings.length);
        table.setStyleID("TableGrid");
        table.setTableAlignment(TableRowAlign.LEFT);
        table.setWidth(9360);
        table.setCellMargins(80, 120, 80, 120);
        table.getRow(0).setRepeatHeader(true);

        for (int column = 0; column < headings.length; column++) {
            writeCell(table.getRow(0).getCell(column), headings[column], true,
                    ParagraphAlignment.CENTER, widths[column]);
        }

        for (Member member : members) {
            List<Deliveries> deliveries = season.getDeliveriesForMember(member.getId());
            XWPFTableRow row = table.createRow();
            writeCell(row.getCell(0), member.getId(), false, ParagraphAlignment.LEFT, widths[0]);
            writeCell(row.getCell(1), member.getName(), false, ParagraphAlignment.LEFT, widths[1]);
            writeCell(row.getCell(2), String.valueOf(deliveries.size()), false, ParagraphAlignment.CENTER, widths[2]);
            writeCell(row.getCell(3), String.format("%.2f", getMemberTotal(deliveries)), false,
                    ParagraphAlignment.RIGHT, widths[3]);
        }

        addSectionHeading(document, "Produce summary");
        addProduceSummary(document, season.getDeliveries());

        XWPFParagraph closingTotal = document.createParagraph();
        closingTotal.setAlignment(ParagraphAlignment.RIGHT);
        closingTotal.setSpacingBefore(240);
        XWPFRun closingTotalRun = closingTotal.createRun();
        closingTotalRun.setText(String.format("TOTAL SEASON PAYOUT: %.2f MUR", season.getSeasonTotal()));
        closingTotalRun.setBold(true);
        closingTotalRun.setColor(DARK_BLUE);
        closingTotalRun.setFontFamily("Calibri");
        closingTotalRun.setFontSize(13);

        XWPFParagraph note = document.createParagraph();
        note.setSpacingBefore(180);
        note.setSpacingAfter(0);
        XWPFRun noteRun = note.createRun();
        noteRun.setText("This total is the sum of all member payments shown in this report.");
        noteRun.setItalic(true);
        noteRun.setColor("666666");
        noteRun.setFontFamily("Calibri");
        noteRun.setFontSize(10);
    }

    public void generate(SeasonService season) {
        try (XWPFDocument document = new XWPFDocument()) {
            List<Member> members = season.getSortedMembers();
            addHeaderAndFooter(document);

            for (int i = 0; i < members.size(); i++) {
                if (i > 0) {
                    document.createParagraph().setPageBreak(true);
                }
                addMemberStatement(document, season, members.get(i));
            }

            if (!members.isEmpty()) {
                document.createParagraph().setPageBreak(true);
            }
            addSeasonSummary(document, season, members);

            Files.createDirectories(Path.of("output"));
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
