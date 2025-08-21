package util;

import java.io.File;

import model.Member;
import model.Porder;


//封裝目前登入者/最後訂單
public final class Session { 
    private Session() {}

    private static final String MEMBER_FILE = "member.txt";
    private static final String PORDER_FILE = "porder.txt";

    public static Member getCurrentMember() {
        Object o = Tool.readFile(MEMBER_FILE);
        return (o instanceof Member) ? (Member) o : null;
    }

    public static void setCurrentMember(Member m) {
        Tool.saveFile(m, MEMBER_FILE);
    }

    public static Porder getCurrentPorder() {
        Object o = Tool.readFile(PORDER_FILE);
        return (o instanceof Porder) ? (Porder) o : null;
    }

    public static void setCurrentPorder(Porder p) {
        Tool.saveFile(p, PORDER_FILE);
    }

    //登出/清除暫存
    public static void clear() {
        new File(MEMBER_FILE).delete();
        new File(PORDER_FILE).delete();
    }
}
