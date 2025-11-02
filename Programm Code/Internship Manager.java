import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Ultra-simple manager:
 * - keeps everything in a Map
 * - enforces max 5 postings per company
 * - very small CRUD and query methods
 * - CSV save/load delegated to InternshipCsv
 */
public class InternshipManager {
    public static final int MAX_PER_COMPANY = 5;

    private final Map<String, Internship> byId = new HashMap<>();

    // ---------- CRUD ----------
    public void add(Internship i) {
        if (byId.containsKey(i.id)) throw new IllegalArgumentException("Duplicate id " + i.id);
        enforceCap(i.companyName, null);
        byId.put(i.id, i);
    }

    public Internship get(String id) {
        Internship i = byId.get(id);
        if (i == null) throw new NoSuchElementException("Not found: " + id);
        return i;
    }

    public void update(String id, Consumer<Internship> edit) {
        Internship i = get(id);
        String oldCompany = i.companyName;
        edit.accept(i);
        if (!i.companyName.equalsIgnoreCase(oldCompany)) enforceCap(i.companyName, id);
    }

    public void remove(String id) { byId.remove(id); }

    // ---------- Actions ----------
    public void setVisible(String id, boolean visible) {
        Internship i = get(id);
        if (i.status == InternshipStatus.FILLED && visible)
            throw new IllegalStateException("FILLED postings cannot be visible");
        i.visible = visible;
    }

    public void approve(String id) { get(id).status = InternshipStatus.APPROVED; }
    public void reject(String id)  { Internship i = get(id); i.status = InternshipStatus.REJECTED; i.visible = false; }
    public void recordPlacement(String id) { get(id).consumeSlot(); }

    // ---------- Queries ----------
    public List<Internship> listAll() { return sort(byId.values()); }
    public List<Internship> listVisible() {
        return sort(byId.values().stream().filter(x -> x.visible).collect(Collectors.toList()));
    }
    public List<Internship> listByCompany(String company) {
        String key = company.toLowerCase(Locale.ROOT).trim();
        return sort(byId.values().stream()
                .filter(x -> x.companyName.toLowerCase(Locale.ROOT).trim().equals(key))
                .collect(Collectors.toList()));
    }
    public List<Internship> listByLevel(InternshipLevel lvl) {
        return sort(byId.values().stream().filter(x -> x.level == lvl).collect(Collectors.toList()));
    }
    public List<Internship> listActiveOn(LocalDate date) {
        return sort(byId.values().stream().filter(x -> x.isActiveOn(date)).collect(Collectors.toList()));
    }

    // ---------- CSV I/O ----------
    public void saveCsv(Path file) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            out.write(InternshipCsv.header()); out.newLine();
            for (Internship i : listAll()) { out.write(InternshipCsv.toCsv(i)); out.newLine(); }
        }
    }
    public void loadCsv(Path file) throws IOException {
        byId.clear();
        if (!Files.exists(file)) return;
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Internship i = InternshipCsv.fromCsv(line);
                add(i);
            }
        }
    }

    // ---------- helpers ----------
    private void enforceCap(String company, String ignoreId) {
        long count = byId.values().stream()
                .filter(x -> x.companyName.equalsIgnoreCase(company))
                .filter(x -> ignoreId == null || !x.id.equals(ignoreId))
                .count();
        if (count >= MAX_PER_COMPANY)
            throw new IllegalStateException(company + " already has " + count + " postings (max " + MAX_PER_COMPANY + ")");
    }

    private static List<Internship> sort(Collection<Internship> c) {
        return c.stream()
                .sorted(Comparator.comparing((Internship i) -> i.companyName)
                        .thenComparing(i -> i.title))
                .collect(Collectors.toList());
    }
}