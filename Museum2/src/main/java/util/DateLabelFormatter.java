package util;

import javax.swing.JFormattedTextField.AbstractFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//JDatePicker的UI 轉換器，將文字↔︎日期（Date/Calendar）互轉
public class DateLabelFormatter extends AbstractFormatter {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private final SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

    public DateLabelFormatter() {
        sdf.setLenient(false); // 避開錯誤日期，如：2/31
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null) return null;
        String s = text.trim();
        if (s.isEmpty()) return null;   // 允許清空欄位
        return sdf.parse(s);             
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null) return "";
        if (value instanceof Calendar)  return sdf.format(((Calendar) value).getTime());
        if (value instanceof Date)      return sdf.format((Date) value);
        throw new ParseException("Unsupported value: " + value.getClass(), 0);
    }
}
