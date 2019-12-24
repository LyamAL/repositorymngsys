package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.service.DeliveryService;
import com.zaq.sjk.repomngsys.service.EntryService;
import com.zaq.sjk.repomngsys.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZAQ
 */
@Controller
public class IndexController {
    private static final String USERS_COUNT = "usersCount";
    private static final String ENTRIES_COUNT = "entriesCount";
    private static final String DELIVERY_COUNT = "deliveryCount";
    private UserServiceImpl userService;
    private DeliveryService deliveryService;
    private EntryService entryService;

    public IndexController(@Autowired UserServiceImpl userService, @Autowired DeliveryService deliveryService, @Autowired EntryService entryService) {
        this.userService = userService;
        this.deliveryService = deliveryService;
        this.entryService = entryService;
    }

    @RequestMapping(value = "/index")
    public String init(Model model) {
        Map<String, Object> initialData = new HashMap<>();
        initialData.put(USERS_COUNT, userService.countUsers());
        initialData.put(DELIVERY_COUNT, deliveryService.getSheetCounts());
        initialData.put(ENTRIES_COUNT, entryService.getSheetCounts());
        model.addAllAttributes(initialData);
        return "index";
    }


}
