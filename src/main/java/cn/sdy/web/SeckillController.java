package cn.sdy.web;

import cn.sdy.dto.Exposer;
import cn.sdy.dto.SeckillResult;
import cn.sdy.entity.Seckill;
import cn.sdy.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/seckill")  //url:模块/资源/{}/细分  /seckill/list
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    //访问我们商品的列表页
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //list.jsp + model = ModelAndView
        //获取页面列表
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";    // /WEB-INF/jsp/"list".jsp
    }

    //访问商品的详情页
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //ajax ,json暴露秒杀接口的方法  (返回一个json数据，数据中封装了我们商品的秒杀地址)
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }

        return result;
    }

    //封装用户是否秒杀成功的信息


}
