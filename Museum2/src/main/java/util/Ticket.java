package util;


public final class Ticket {

    private Ticket() {} // 工具類別不允許被實例化

    // 若查無對應預設值
    public static final int UNKNOWN = -1;

    // 票種（Type）ID 常數 
    public static final int TYPE_ADULT      = 1; // 全票
    public static final int TYPE_STUDENT    = 2; // 學生票
    public static final int TYPE_CONCESSION = 3; // 愛心票

    // 票期（Period）ID 常數
    public static final int PERIOD_SINGLE = 1; // 單日票
    public static final int PERIOD_SEASON = 2; // 季票
    public static final int PERIOD_ANNUAL = 3; // 年票

    //票種顯示名稱常數
    public static final String NAME_TYPE_ADULT      = "全票";
    public static final String NAME_TYPE_STUDENT    = "學生票";
    public static final String NAME_TYPE_CONCESSION = "愛心票";

    //票期顯示名稱常數
    public static final String NAME_PERIOD_SINGLE = "單日票";
    public static final String NAME_PERIOD_SEASON = "季票";
    public static final String NAME_PERIOD_ANNUAL = "年票";


    //由票種名稱取得 ID，查無則回傳 UNKNOWN(-1)
    public static int getTicketTypeIdFromName(String name) {
        if (name == null) return UNKNOWN;
        String n = name.trim();
        if (n.equals(NAME_TYPE_ADULT)) {
            return TYPE_ADULT;
        } else if (n.equals(NAME_TYPE_STUDENT)) {
            return TYPE_STUDENT;
        } else if (n.equals(NAME_TYPE_CONCESSION)) {
            return TYPE_CONCESSION;
        } else {
            return UNKNOWN;
        }
    }

    //由票期名稱取得 ID，查無則回傳 UNKNOWN(-1)
    public static int getTicketPeriodIdFromName(String name) {
        if (name == null) return UNKNOWN;
        String n = name.trim();
        if (n.equals(NAME_PERIOD_SINGLE)) {
            return PERIOD_SINGLE;
        } else if (n.equals(NAME_PERIOD_SEASON)) {
            return PERIOD_SEASON;
        } else if (n.equals(NAME_PERIOD_ANNUAL)) {
            return PERIOD_ANNUAL;
        } else {
            return UNKNOWN;
        }
    }


    //由票種 ID 取得顯示名稱；未知 ID 則回傳該數字字串
    public static String typeName(int id) {
        if (id == TYPE_ADULT) {
            return NAME_TYPE_ADULT;
        } else if (id == TYPE_STUDENT) {
            return NAME_TYPE_STUDENT;
        } else if (id == TYPE_CONCESSION) {
            return NAME_TYPE_CONCESSION;
        } else {
            return String.valueOf(id); // 若是不認得的ID，直接顯示數字
        }
    }

    //由票期 ID 取得顯示名稱；未知 ID 則回傳該數字字串
    public static String periodName(int id) {
        if (id == PERIOD_SINGLE) {
            return NAME_PERIOD_SINGLE;
        } else if (id == PERIOD_SEASON) {
            return NAME_PERIOD_SEASON;
        } else if (id == PERIOD_ANNUAL) {
            return NAME_PERIOD_ANNUAL;
        } else {
            return String.valueOf(id);
        }
    }


    // 組合產品名稱：票種-票期（例：全票-單日票）
    public static String productName(int typeId, int periodId) {
        return typeName(typeId) + "-" + periodName(periodId);
    }
}
