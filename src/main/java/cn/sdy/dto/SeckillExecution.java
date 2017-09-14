package cn.sdy.dto;

import cn.sdy.entity.SuccessKilled;
import cn.sdy.enums.SeckillStatEunm;
import cn.sdy.exception.SeckillException;

/**
 * 封装执行秒杀后的结果:是否秒杀成功
 */
public class SeckillExecution {

    private long seckillId;

    //秒杀执行结果的状态
    private int state;

    //状态的明文标识
    private String stateInfo;

    //当秒杀成功时，需要传递秒杀成功的对象回去
    private SuccessKilled successKilled;

    //秒杀成功，返回所有的信息
    public SeckillExecution(long seckillId, SeckillStatEunm statEunm, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEunm.getState();
        this.stateInfo = statEunm.getInfo();
        this.successKilled = successKilled;
    }

    //秒杀失败
    public SeckillExecution(long seckillId, SeckillStatEunm statEunm) {
        this.seckillId = seckillId;
        this.state = statEunm.getState();
        this.stateInfo = statEunm.getInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
