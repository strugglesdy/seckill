package cn.sdy.dao;

import cn.sdy.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        /* 第一次insertCount=1
         * 第二次insertCount=0
         * （同一id，联合主键起作用，秒单货品号和手机号码组成联合主键）防止重复秒杀*/
        long seckillId = 1000;
        long userPhone = 13476191877L;
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long seckillId = 1000L;
        long userPhone = 13476191877L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}