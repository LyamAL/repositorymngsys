package com.zaq.sjk.repomngsys.service;

import com.zaq.sjk.repomngsys.entity.Repo;

import java.util.List;

/**
 * @author ZAQ
 */
public interface RepoService {
    List<Repo> listRepos();

    int deleteRepo(int id);

    int addRepo(Repo repo);

    int updateRepo(Repo repo);

    List<Repo> listRepoPositions();

    Repo getRepoById(int repoId);
}
