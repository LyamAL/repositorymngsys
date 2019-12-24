package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.Storage;
import com.zaq.sjk.repomngsys.service.GoodService;
import com.zaq.sjk.repomngsys.service.RepoService;
import com.zaq.sjk.repomngsys.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author ZAQ
 */
@Controller
@RequestMapping("/storage")
public class StorageController {

    private StorageService storageService;
    private RepoService repoService;

    private GoodService goodService;

    public StorageController(@Autowired StorageService storageService, @Autowired RepoService repoService, @Autowired GoodService goodService) {
        this.storageService = storageService;
        this.repoService = repoService;
        this.goodService = goodService;
    }

    @RequestMapping("/repo")
    public ModelAndView initByRepo() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("storage-repo");
        modelAndView.addObject("repos", repoService.listRepoPositions());
        return modelAndView;
    }

    @RequestMapping("/repo/data")
    public ModelAndView initByRepo(@RequestParam("id") int id, @RequestParam("position") String position) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("storage-repo-data");
        modelAndView.addObject("repoId", id);
        modelAndView.addObject("position", position);
        modelAndView.addObject("storageByRepo", storageService.listStorageByRepo(id));
        return modelAndView;
    }

    @RequestMapping("/goods/data")
    public ModelAndView initByGoods(@RequestParam("id") int id, @RequestParam("name") String name) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("storage-goods-data");
        modelAndView.addObject("gid", id);
        modelAndView.addObject("name", name);
        modelAndView.addObject("storageByGood", storageService.listStorageByGood(id));
        return modelAndView;
    }

    @RequestMapping("/repo/select")
    @ResponseBody
    public String selectPos(@RequestParam(value = "id") int id, @RequestParam("position") String position) {
        return "/storage/repo/data?id=" + id + "&position=" + position;
    }

    @RequestMapping("/goods/select")
    @ResponseBody
    public String selectName(@RequestParam(value = "id") int id, @RequestParam("name") String name) {
        return "/storage/goods/data?id=" + id + "&name=" + name;
    }

    @RequestMapping(value = "/repo/good/delete", method = DELETE)
    @ResponseBody
    public String deleteGoods(@RequestParam("id") int gid, @RequestParam("repoId") int repoId) {
        int res = storageService.deleteStorage(gid, repoId);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/repo/good/update", method = PUT)
    @ResponseBody
    public String updateGoods(@RequestBody Storage storage) {
        int res = storageService.updateStorage(storage);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/goods/good/delete", method = DELETE)
    @ResponseBody
    public String deleteGoodsByGoods(@RequestParam("id") int gid, @RequestParam("repoId") int repoId) {
        int res = storageService.deleteStorage(gid, repoId);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping(value = "/goods/good/update", method = PUT)
    @ResponseBody
    public String updateGoodsByGoods(@RequestBody Storage storage) {
        int res = storageService.updateStorage(storage);
        return res > 0 ? "success" : "fail";
    }

    @RequestMapping("/goods")
    public ModelAndView initByGoods() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("storage-goods");
        modelAndView.addObject("goods", goodService.listGoodNames());
        return modelAndView;
    }


}
