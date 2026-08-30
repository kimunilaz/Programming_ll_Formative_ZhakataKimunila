package mu.rekolt.service;

import mu.rekolt.model.Deliveries;
import mu.rekolt.model.Member;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportService {

    public void generate(SeasonService season) {
        try (XWPFDocument document = new XWPFDocument()) {

            // testing
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("REKOLT Season Report — test");

            try (FileOutputStream out = new FileOutputStream("output/season-report.docx")) {
                document.write(out);
            }

            System.out.println("Writing output/season-report.docx ... done.");

        } catch (IOException e) {
            System.out.println("Could not write the report: " + e.getMessage());
        }
    }
}
