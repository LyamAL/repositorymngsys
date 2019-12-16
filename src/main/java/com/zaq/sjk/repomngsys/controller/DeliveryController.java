package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.DeliverySheet;
import com.zaq.sjk.repomngsys.entity.EntrySheet;
import com.zaq.sjk.repomngsys.service.DeliveryService;
import com.zaq.sjk.repomngsys.service.EntryService;
import com.zaq.sjk.repomngsys.service.RepoService;
import com.zaq.sjk.repomngsys.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ZAQ
 */
@Controller
@RequestMapping("/sheet")
public class SheetController {

    private DeliveryService deliveryService;
    private EntryService entryService;

    private UserServiceImpl userService;
    private RepoService repoService;

    public SheetController(@Autowired DeliveryService deliveryService,@Autowired EntryService entryService,
                           @Autowired UserServiceImpl userService, @Autowired RepoService repoService) {
        this.deliveryService = deliveryService;
        this.entryService = entryService;
        this.userService = userService;
        this.repoService = repoService;
    }

    @RequestMapping(value = "/entr/add")
    public ModelAndView initEntryForm(ModelAndView modelAndView) {
        modelAndView.setViewName("addEntr");
        modelAndView.addObject("users", userService.listByRole("Purchaser"));
        modelAndView.addObject("repoPositions", repoService.listRepoPositions());
        return modelAndView;
    }

    @RequestMapping(value = "/dlvr/add")
    public ModelAndView initDeliveryForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addDlvr");
        modelAndView.addObject("repoPositions", repoService.listRepoPositions());
        modelAndView.addObject("users", userService.listByRole("Consignor"));
        return modelAndView;
    }

    @RequestMapping(value = "/dlvr/add",method = RequestMethod.POST)
    public ModelAndView addDelivery() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addDlvrGoods");
        modelAndView.addObject("goodNames",);
        return modelAndView;
    }

    @RequestMapping(value = "/dlvr")
    public ModelAndView listDelivery(ModelAndView modelAndView) {
        modelAndView.addObject("delivers", deliveryService.listAll());
        modelAndView.setViewName("dlvr");
        return modelAndView;
    }
    @RequestMapping(value = "/entr")
    public String initEntry() {
        return "entr";
    }

}
