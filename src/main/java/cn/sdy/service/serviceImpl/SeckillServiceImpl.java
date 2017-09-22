package cn.sdy.service.serviceImpl;

import cn.sdy.dao.SeckillDao;
import cn.sdy.dao.SuccessKilledDao;
import cn.sdy.dao.cache.RedisDao;
import cn.sdy.dto.Exposer;
import cn.sdy.dto.SeckillExecution;
import cn.sdy.entity.Seckill;
import cn.sdy.entity.SuccessKilled;
import cn.sdy.enums.SeckillStatEnum;
import cn.sdy.exception.RepeatKillException;
import cn.sdy.exception.SeckillCloseException;
import cn.sdy.exception.SeckillException;
import cn.sdy.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

// @Component @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {
    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "adfsgdvcxqwe";

    //注入Service依赖
    @Autowired //@Resource
    private SeckillDao seckillDao;

    @Autowired //@Resource
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;


    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    //秒杀是否开启
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化:超时的基础上维护一致性
        //1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3.放入redis
                redisDao.putSeckill(seckill);
            }
        }

        //若秒杀未开启
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
    @Transactional
    /**使用注解控制事务方法的优点
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有方法都需要事务，如只有一条插入修改.只读操作 不需要事务控制
     */
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");//秒杀数据被重写
        }
        //执行秒杀逻辑：减库存+增加购买明细  (先插入再减库存，受网络延迟+GC影响的时间降低一倍)
        Date nowTime = new Date();
        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //看是否该明细被重复插入，即商品重复秒杀
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存，热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新库存记录，说明秒杀结束  rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功，得到成功插入的明细记录，并返回成功秒杀的信息  commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
