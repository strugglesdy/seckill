package cn.sdy.service;

import cn.sdy.dto.Exposer;
import cn.sdy.dto.SeckillExecution;
import cn.sdy.entity.Seckill;
import cn.sdy.exception.RepeatKillException;
import cn.sdy.exception.SeckillCloseException;
import cn.sdy.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

/*    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
        //Exposer{exposed=true, md5='166bd65348ab8a53bcb4e5bd3886f1e7', seckillId=1000, now=0, start=0, end=0}
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000;
        long phone = 13218273581L;
        String md5 = "166bd65348ab8a53bcb4e5bd3886f1e7";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(1000, phone, md5);
            logger.info("result={}", seckillExecution);
        } catch (RepeatKillException e) {
            e.printStackTrace();
        } catch (SeckillCloseException e) {
            e.printStackTrace();
        }
    }*/

    @Test   //完整逻辑代码测试，注意可重复执行
    public void seckillLogic() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {

            System.out.println(exposer);

            long userPhone = 13476191876L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
                System.out.println(seckillExecution);
            } catch (RepeatKillException e) {
                e.printStackTrace();
            } catch (SeckillCloseException e1) {
                e1.printStackTrace();
            }
        } else {
            //秒杀未开启
            System.out.println(exposer);
        }
    }

    @Test
    public void executeSeckillProcedure() throws Exception{
        long seckillId = 1002;
        long phone = 13218275888L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }

    }
}


