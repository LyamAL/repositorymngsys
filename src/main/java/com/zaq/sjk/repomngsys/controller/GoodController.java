package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.Good;
import com.zaq.sjk.repomngsys.entity.Sale;
import com.zaq.sjk.repomngsys.service.GoodService;
import com.zaq.sjk.repomngsys.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/good")
public class GoodController {
    private GoodService goodService;

    private RepoService repoService;

    public GoodController(@Autowired GoodService goodService, @Autowired RepoService repoService) {
        this.goodService = goodService;
        this.repoService = repoService;
    }

    @RequestMapping("")
    public String init(Model model) {
        List<Good> list = goodService.listGoods();
        model.addAttribute("goods", list);
        model.addAttribute("repoPositions", repoService.listRepoPositions());
        return "goods";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delete(@RequestParam("id") int id) {
        int res = goodService.deleteGood(id);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String update(Good good) {
        int res = goodService.update(good);
        return res >= 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Good good) {
        int res = goodService.addGood(good);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/soat")
    @ResponseBody
    public List<Sale> saleOfAllTime(@RequestParam("id") int gid) {
        return goodService.getSalesOfAllTime(gid);
    }
}
