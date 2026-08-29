package mu.rekolt.model;

import java.util.List;

public interface Reportable {
    String toReportRow();

    public static void printReportable(List<Reportable> items) {
        for (Reportable item : items) {
            System.out.println(item.toReportRow());
        }
    }
}
