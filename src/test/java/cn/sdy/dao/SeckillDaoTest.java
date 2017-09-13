package cn.sdy.dao;

import cn.sdy.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/*配置spring和junit整合，这样junit在启动时就会加载spring容器*/
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:/spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillDao.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /*1000元秒杀iphone6
     Seckill{seckillId=1000,
     name='1000元秒杀iphone6',
     number=100,
     startTime=Fri Jan 01 00:00:00 CST 2016,
     endTime=Sat Jan 02 00:00:00 CST 2016,
     createTime=Mon Sep 11 21:48:17 CST 2017}
*/
    }

    @Test
    public void queryAll() throws Exception {
        //Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [1, 0, param1, param2]
        //java没有保存行参的记录，java在运行的时候会把List<Seckill> queryAll(int offset,int limit);中的参数变成这样:queryAll(int arg0,int arg1)
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        long seckillId = 1000;
        Date date = new Date();
        int updateCount = seckillDao.reduceNumber(seckillId, date);
        System.out.println(updateCount);
    }

}