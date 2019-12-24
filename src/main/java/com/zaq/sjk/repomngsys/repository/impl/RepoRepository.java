package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.Repo;

import java.util.List;

/**
 * @author ZAQ
 */
public interface RepoRepository {

    List<Repo> selectAll();

    List<Repo> selectPositions();

    int deleteById(int id);

    int insert(Repo repo);

    int update(Repo repo);

    Repo selectById(int repoId);
}
