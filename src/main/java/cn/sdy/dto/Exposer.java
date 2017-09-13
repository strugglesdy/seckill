package cn.sdy.dto;
/**
 * 暴露秒杀地址(接口)DTO
 */
public class Exposer {

    //是否开启秒杀
    private boolean exposed;

    //对秒杀地址加密措施
    private String md5;

    //id为seckillId的商品的秒杀地址
    private long seckillId;

    //系统当前时间（毫秒）
    private long now;

    //秒杀开始时间
    private long start;

    //秒杀结束时间
    private long end;

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }


}
