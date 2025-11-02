import java.time.LocalDate;

/**
 * Super small CSV helper (no external libs).
 * Assumption for simplicity: fields do NOT contain commas or newlines.
 * If you need commas later, switch delimiter to TAB and update DELIM.
 */
public final class InternshipCsv {
    private InternshipCsv() {}
    private static final String DELIM = ",";
    private static final String HEADER = String.join(DELIM,
            new String[]{"id","title","description","openingDate","closingDate","company","level","slots","visible","status"});

    public static String header() { return HEADER; }

    public static String toCsv(Internship i) {
        return String.join(DELIM, new String[]{
                i.id,
                nz(i.title),
                nz(i.description),
                i.openingDate.toString(),
                i.closingDate.toString(),
                nz(i.companyName),
                i.level.name(),
                Integer.toString(i.slots),
                Boolean.toString(i.visible),
                i.status.name()
        });
    }

    public static Internship fromCsv(String line) {
        String[] c = line.split(DELIM, -1);
        return new Internship(
                c[0], c[1], c[2],
                LocalDate.parse(c[3]),
                LocalDate.parse(c[4]),
                c[5],
                InternshipLevel.valueOf(c[6]),
                Integer.parseInt(c[7]),
                Boolean.parseBoolean(c[8]),
                InternshipStatus.valueOf(c[9])
        );
    }

    private static String nz(String s) { return s == null ? "" : s; }
}