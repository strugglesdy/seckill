package cn.sdy.service.serviceImpl;

import cn.sdy.dao.SeckillDao;
import cn.sdy.dao.SuccessKilledDao;
import cn.sdy.dto.Exposer;
import cn.sdy.dto.SeckillExecution;
import cn.sdy.entity.Seckill;
import cn.sdy.entity.SuccessKilled;
import cn.sdy.exception.RepeatKillException;
import cn.sdy.exception.SeckillCloseException;
import cn.sdy.exception.SeckillException;
import cn.sdy.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

public class SeckillServiceImpl implements SeckillService {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "adfsgdvcxqwe";

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null) {  //说明查不到这个秒杀商品的记录
            return new Exposer(false, seckillId);
        }
        //如果秒杀未开启
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //秒杀开启，返回商品的Id，用给接口加密的md5
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //秒杀是否成功，成功：减库存，增加明细  失败：抛出异常，事务回滚
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");//秒杀数据被重写
        }
        //执行秒杀逻辑：件库存+增加购买明细
        Date nowTime = new Date();
        try {
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新库存记录，说明秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //否则更新了库存，秒杀成功,增加明细
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //看是该明细被重复插入，即商品重复秒杀
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //否则秒杀成功，得到成功插入的明细记录，并返回秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, 1, "秒杀成功", successKilled);
                }
            }

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所又编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}