package util;

import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//金額、通用日期/時間格式化
public final class Formatters {
    private Formatters() {}

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    // 空字串處理
    public static String nz(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }

    //金額：$ + 千分位
    public static String currency(int amount) {
        return "$" + NumberFormat.getNumberInstance(Locale.TAIWAN).format(amount);
    }

    public static String format(LocalDate d) {
        return d == null ? "—" : DATE.format(d);
    }

    public static String format(LocalDateTime dt) {
        return dt == null ? "—" : DATE_TIME.format(dt);
    }
}
