import java.time.LocalDate;

public class Internship {
    public final String id;
    public String title;
    public String description;
    public LocalDate openingDate;
    public LocalDate closingDate;
    public String companyName;
    public InternshipLevel level;
    public int slots;
    public boolean visible;
    public InternshipStatus status;

    public Internship(String id, String title, String description,
                      LocalDate openingDate, LocalDate closingDate,
                      String companyName, InternshipLevel level,
                      int slots, boolean visible, InternshipStatus status) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
        if (companyName == null || companyName.isBlank()) throw new IllegalArgumentException("company required");
        if (openingDate == null || closingDate == null) throw new IllegalArgumentException("dates required");
        if (closingDate.isBefore(openingDate)) throw new IllegalArgumentException("closing < opening");
        if (slots < 0) throw new IllegalArgumentException("slots >= 0");

        this.id = id;
        this.title = title;
        this.description = description == null ? "" : description;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.companyName = companyName;
        this.level = (level == null ? InternshipLevel.BASIC : level);
        this.slots = slots;
        this.visible = visible;
        this.status = (status == null ? InternshipStatus.PENDING : status);
        autoFillIfNeeded();
    }

    public void consumeSlot() {
        if (slots <= 0) throw new IllegalStateException("No remaining slots");
        slots--;
        autoFillIfNeeded();
    }

    public boolean isActiveOn(LocalDate date) {
        return !date.isBefore(openingDate) && !date.isAfter(closingDate)
               && visible && slots > 0
               && (status == InternshipStatus.APPROVED || status == InternshipStatus.PENDING);
    }

    private void autoFillIfNeeded() {
        if (slots == 0) { status = InternshipStatus.FILLED; visible = false; }
    }

    @Override public String toString() {
        return id + " [" + title + " @" + companyName + ", " + level + ", " + status + ", slots=" + slots + ", vis=" + visible + "]";
    }
}