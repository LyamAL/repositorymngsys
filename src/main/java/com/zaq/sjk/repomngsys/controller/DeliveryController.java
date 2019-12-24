package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.DeliveryGoods;
import com.zaq.sjk.repomngsys.entity.DeliverySheet;
import com.zaq.sjk.repomngsys.entity.Repo;
import com.zaq.sjk.repomngsys.service.DeliveryService;
import com.zaq.sjk.repomngsys.service.GoodService;
import com.zaq.sjk.repomngsys.service.RepoService;
import com.zaq.sjk.repomngsys.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author ZAQ
 */
@Controller
@RequestMapping("/sheet/dlvr")
public class DeliveryController {

    private DeliveryService deliveryService;
    private GoodService goodService;
    private UserServiceImpl userService;
    private RepoService repoService;

    public DeliveryController(DeliveryService deliveryService, GoodService goodService, UserServiceImpl userService, RepoService repoService) {
        this.deliveryService = deliveryService;
        this.goodService = goodService;
        this.userService = userService;
        this.repoService = repoService;
    }

    @RequestMapping(value = "")
    public ModelAndView listDelivery(ModelAndView modelAndView) {
        modelAndView.addObject("delivers", deliveryService.listAll());
        modelAndView.setViewName("dlvr");
        return modelAndView;
    }

    @RequestMapping(value = "/add")
    public ModelAndView initDeliveryForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addDlvr");
        modelAndView.addObject("repoPositions", repoService.listRepoPositions());
        modelAndView.addObject("users", userService.listByRole("Consignor"));
        return modelAndView;
    }

    @RequestMapping("/update")
    public ModelAndView initDetail(@RequestParam("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dlvrDetail");
        List<Repo> repos = repoService.listRepoPositions();
        DeliverySheet sheet = deliveryService.getSheetById(id);

        modelAndView.addObject("dlvrSheet", sheet);
        modelAndView.addObject("users", userService.listByRole("Consignor"));
        modelAndView.addObject("goodsInDlvr", deliveryService.getGoodsInSheet(id));
        modelAndView.addObject("repoPositions", repos);
        return modelAndView;
    }

    @RequestMapping(value = "/update/sub", method = PUT)
    @ResponseBody
    public String submitUpdateSheet(@RequestBody DeliverySheet sheet) {
        int res = deliveryService.updateSheet(sheet);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/goods/update", method = PUT)
    @ResponseBody
    public String updateGoods(@RequestBody DeliveryGoods goods) {
        int res = deliveryService.updateGoodsOnSheet(goods);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/goods/delete", method = DELETE)
    @ResponseBody
    public String deleteGoods(@RequestParam("id") int gid, @RequestParam("sheetId") String sheetId) {
        int res = deliveryService.deleteGoodsOnSheet(gid, sheetId);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteSheet(@RequestParam("id") String id) {
        int res = deliveryService.deleteById(id);
        if (res > 0) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDelivery(DeliverySheet deliverySheet) {
        String id = deliveryService.addDelivery(deliverySheet);
        int repoId = Integer.parseInt(deliverySheet.getRepoId().getPosition());
        if (id != null && !"".equals(id)) {
            return "/sheet/dlvr/addGoods?id=" + id + "&repoId=" + repoId;
        } else {
            return "fail";
        }
    }

    @RequestMapping(value = "/addGoods")
    public ModelAndView initAddDeliveryGoods(@RequestParam("id") String id, @RequestParam("repoId") int repoId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addDlvrGoods");
        modelAndView.addObject("goodsInRepo", goodService.listGoodInRepo(repoId));
        modelAndView.addObject("id", id);
        modelAndView.addObject("repo", repoService.getRepoById(repoId));
        return modelAndView;
    }


    @RequestMapping(value = "/addGoods", method = RequestMethod.POST)
    @ResponseBody
    public String addDeliveryGoods(@RequestParam("repoId") int repoId, @RequestParam("deliveryId") String deliveryId, @RequestParam("gid") Integer[] gids,
                                   @RequestParam("price") Float[] prices, @RequestParam("count") Integer[] counts, @RequestParam("note") String[] notes, @RequestParam("add") Integer[] add) {
        int res = deliveryService.addGoodsToSheet(repoId, deliveryId, gids, prices, counts, notes, add);
        if (res > 0) {
            return "/sheet/dlvr";
        }
        return "fail";
    }
}
