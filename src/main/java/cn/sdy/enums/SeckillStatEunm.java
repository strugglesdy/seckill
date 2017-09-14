package cn.sdy.enums;

/**
 * 使用枚举表述常量字符字段,保证了一些常用常量数据被封装在枚举类型里
 */
public enum SeckillStatEunm {

    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATE_REWRITE(-3, "数据篡改");

    private int state;
    private String Info;

    SeckillStatEunm(int state, String info) {
        this.state = state;
        Info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return Info;
    }

    public static SeckillStatEunm statOf(int index) {
        for (SeckillStatEunm state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
