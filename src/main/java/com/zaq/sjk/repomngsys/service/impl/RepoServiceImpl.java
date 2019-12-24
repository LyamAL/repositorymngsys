package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.entity.Repo;
import com.zaq.sjk.repomngsys.repository.impl.RepoRepository;
import com.zaq.sjk.repomngsys.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZAQ
 */
@Service
public class RepoServiceImpl implements RepoService {
    private RepoRepository repoRepository;

    public RepoServiceImpl(@Autowired RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    @Override
    public List<Repo> listRepos() {
        return repoRepository.selectAll();
    }

    @Override
    public int deleteRepo(int id) {
        return repoRepository.deleteById(id);
    }

    @Override
    public int addRepo(Repo repo) {
        return repoRepository.insert(repo);
    }

    @Override
    public int updateRepo(Repo repo) {
        return repoRepository.update(repo);
    }

    @Override
    public List<Repo> listRepoPositions() {
        return repoRepository.selectPositions();
    }

    @Override
    public Repo getRepoById(int repoId) {
        return repoRepository.selectById(repoId);
    }
}
