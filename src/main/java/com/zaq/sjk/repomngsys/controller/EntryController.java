package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.EntryGoods;
import com.zaq.sjk.repomngsys.entity.EntrySheet;
import com.zaq.sjk.repomngsys.entity.Repo;
import com.zaq.sjk.repomngsys.service.EntryService;
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
@RequestMapping("/sheet/entr")
public class EntryController {
    private RepoService repoService;
    private UserServiceImpl userService;
    private EntryService entryService;
    private GoodService goodService;

    public EntryController(RepoService repoService, UserServiceImpl userService, EntryService entryService, GoodService goodService) {
        this.repoService = repoService;
        this.userService = userService;
        this.entryService = entryService;
        this.goodService = goodService;
    }

    @RequestMapping("/update")
    public ModelAndView initDetail(@RequestParam("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("entrDetail");
        List<Repo> repos = repoService.listRepoPositions();
        EntrySheet sheet = entryService.getSheetById(id);

        modelAndView.addObject("entrSheet", sheet);
        modelAndView.addObject("users", userService.listByRole("Purchaser"));
        modelAndView.addObject("goodsInEntr", entryService.getGoodsInSheet(id));
        modelAndView.addObject("repoPositions", repos);
        return modelAndView;
    }


    @RequestMapping(value = "/update/sub", method = PUT)
    @ResponseBody
    public String submitUpdateSheet(@RequestBody EntrySheet sheet) {
        int res = entryService.updateSheet(sheet);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/goods/update", method = PUT)
    @ResponseBody
    public String updateGoods(@RequestBody EntryGoods goods) {
        int res = entryService.updateGoodsOnSheet(goods);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/goods/delete", method = DELETE)
    @ResponseBody
    public String deleteGoods(@RequestParam("id") int gid, @RequestParam("sheetId") String sheetId) {
        int res = entryService.deleteGoodsOnSheet(gid, sheetId);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteSheet(@RequestParam("id") String id) {
        int res = entryService.deleteById(id);
        if (res > 0) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping(value = "/add")
    public ModelAndView initEntryForm(ModelAndView modelAndView) {
        modelAndView.setViewName("addEntr");
        modelAndView.addObject("repoPositions", repoService.listRepoPositions());
        modelAndView.addObject("users", userService.listByRole("Purchaser"));
        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addEntry(EntrySheet entrySheet) {
        String id = entryService.addEntrySheet(entrySheet);
        if (id != null && !"".equals(id)) {
            return "/sheet/entr/addGoods?id=" + id;
        } else {
            return "fail";
        }
    }

    @RequestMapping(value = "/addGoods")
    public ModelAndView initAddEntryGoods(@RequestParam("id") String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addEntrGoods");
        modelAndView.addObject("goods", goodService.listGoods());
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @RequestMapping(value = "/addGoods", method = RequestMethod.POST)
    @ResponseBody
    public String addDeliveryGoods(@RequestParam("entryId") String sheetId,
                                   @RequestParam("gid") Integer[] gids,
                                   @RequestParam("price") Float[] prices,
                                   @RequestParam("count") Integer[] counts,
                                   @RequestParam("qualified") Integer[] qualifieds,
                                   @RequestParam("note") String[] notes,
                                   @RequestParam("add") Integer[] add) {
        int res = entryService.addGoodsToSheet(sheetId, gids, prices, counts, qualifieds, notes, add);
        if (res > 0) {
            return "/sheet/entr";
        }
        return "fail";
    }


    @RequestMapping(value = "")
    public ModelAndView initEntry() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("entr");
        modelAndView.addObject("entrySheets", entryService.listAllSheet());
        return modelAndView;
    }


}
