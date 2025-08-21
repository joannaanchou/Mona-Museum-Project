package util;

public class OrderNo {
	
	private OrderNo() {}
    public static String genOrderNo() {
        String ts = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int rnd = (int) (Math.random() * 1000);
        return ts + String.format("-%03d", rnd);
    }
	
}
