package com.jhmk.model.controller;

import com.alibaba.fastjson.JSONObject;
import com.jhmk.model.base.BaseEntityController;
import com.jhmk.model.bean.rule.Rule;
import com.jhmk.model.bean.sqlbean.BiaozhuZhubiao;
import com.jhmk.model.bean.sqlbean.ZlfaZhubiao;
import com.jhmk.model.bean.sqlbean.repository.service.BiaozhuZhubiaoRepService;
import com.jhmk.model.config.UrlPropertiesConfig;
import com.jhmk.model.model.AtResponse;
import com.jhmk.model.model.ResponseCode;
import com.jhmk.model.service.JbzdZhubiaoService;
import com.jhmk.model.util.UrlConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author ziyu.zhou
 * @date 2018/12/13 19:47
 * 鉴别诊断主表
 */
@Controller
@RequestMapping("jbzdZhubiao")
public class JbzdzhubiaoController extends BaseEntityController<BiaozhuZhubiao> {
    private static final Logger logger = LoggerFactory.getLogger(BaseEntityController.class);

    @Autowired
    JbzdZhubiaoService jbzdZhubiaoService;
    @Autowired
    BiaozhuZhubiaoRepService biaozhuZhubiaoRepService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UrlPropertiesConfig urlPropertiesConfig;

    @PostMapping("/list")
    public void list(HttpServletResponse response, @RequestBody String map) {
        Map params = (Map) JSONObject.parse(map);
        AtResponse resp = super.listData(params, biaozhuZhubiaoRepService, "createTime");
        wirte(response, resp);
    }

    @PostMapping("/findOne")
    public void findOne(HttpServletResponse response, @RequestBody String map) {
        JSONObject object = JSONObject.parseObject(map);
        Integer id = object.getInteger("id");
        BiaozhuZhubiao one = biaozhuZhubiaoRepService.findOne(id);
        wirte(response, one);
    }

    @PostMapping("/test")
    public void test(HttpServletResponse response, @RequestBody String map) {
        AtResponse resp = new AtResponse(System.currentTimeMillis());
        Iterable<BiaozhuZhubiao> all = biaozhuZhubiaoRepService.findAll();
//        String string = JSONObject.toJSONString(all);
        resp.setData(all);
        wirte(response, resp);
    }

    //    @PostMapping("/save")
//    public void save(HttpServletResponse response, @RequestBody String map) {
//        BiaozhuZhubiao biaozhuZhubiao = JSONObject.parseObject(map, BiaozhuZhubiao.class);
//        BiaozhuZhubiao save = biaozhuZhubiaoRepService.save(biaozhuZhubiao);
//        biaozhuZhubiaoRepService.save(save);
//
//        wirte(response, save);
//    }

    @PostMapping("/returnMainInitData")
    public void returnFirstInitData(HttpServletResponse response, @RequestBody String map) {
        Object parse = JSONObject.parse(map);
        logger.info("接受到的数据为：{}", map);
        String s = restTemplate.postForObject(urlPropertiesConfig.getTestToolUrl() + UrlConstants.getDataByPIdAndVId, parse, String.class);
        Rule rule = JSONObject.parseObject(s, Rule.class);
//        ZlfaZhubiao zhubiao = zlfaZhubiaoService.rule2ZlfaZhubiao(rule);
//        wirte(response, zhubiao);
    }


    @PostMapping("/save")
    public void saveAll(HttpServletResponse response, @RequestBody String map) {
        AtResponse resp = new AtResponse();
        BiaozhuZhubiao biaozhuZhubiaoResult = jbzdZhubiaoService.getBiaozhuZhubiaoResult(map);
        biaozhuZhubiaoResult.setCreateTime(new Date());
        try {
            biaozhuZhubiaoRepService.save(biaozhuZhubiaoResult);
            resp.setResponseCode(ResponseCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResponseCode(ResponseCode.INERERROR);

        }
        wirte(response, resp);
    }

    @PostMapping("/delete")
    public void delete(HttpServletResponse response, @RequestBody String map) {
        AtResponse resp = new AtResponse();
        JSONObject parse = JSONObject.parseObject(map);
        Integer id = parse.getInteger("id");

        try {
            biaozhuZhubiaoRepService.delete(id);
            resp.setResponseCode(ResponseCode.OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setResponseCode(ResponseCode.INERERROR);

        }
        wirte(response, resp);
    }


}