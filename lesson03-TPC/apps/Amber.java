package apps;

import java.time.LocalDate;

public class Amber {
    private short id;
    private String title;
    private String author;
    private static final String DEFAULT_AUTHOR = "unknown";
    private LocalDate checkedOut;
    private static final LocalDate DEFAULT_CHECKED;
    private int pageCount;
    private boolean isAvailable;
    static {
        DEFAULT_CHECKED = LocalDate.now();
    }

    static void foo() {
        System.out.println(DEFAULT_AUTHOR);
    }
}