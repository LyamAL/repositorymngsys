package com.zaq.sjk.repomngsys.controller;

import com.zaq.sjk.repomngsys.entity.Repo;
import com.zaq.sjk.repomngsys.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ZAQ
 */
@Controller
@RequestMapping("/repo")
public class RepositoryController {

    private RepoService repoService;

    public RepositoryController(@Autowired RepoService repoService) {
        this.repoService = repoService;
    }

    @RequestMapping("")
    public String init(Model model) {
        model.addAttribute("repos", repoService.listRepos());
        return "repos";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestParam("id") int id) {
        if (repoService.deleteRepo(id) > 0) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public String update(Repo repo) {
        if (repoService.updateRepo(repo) > 0) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(Repo repo) {
        if (repoService.addRepo(repo) > 0) {
            return "success";
        }
        return "fail";
    }
}
