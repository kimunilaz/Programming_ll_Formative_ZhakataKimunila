package mu.rekolt.model;

public class Member implements Comparable<Member>, Reportable {
    private String id;
    private String name;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public int compareTo(Member other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public String toReportRow() {
        return id + "  " + name;
    }
}
