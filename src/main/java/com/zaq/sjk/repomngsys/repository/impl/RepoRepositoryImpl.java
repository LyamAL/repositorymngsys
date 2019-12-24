package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.Repo;
import com.zaq.sjk.repomngsys.exception.DuplicatePKException;
import com.zaq.sjk.repomngsys.repository.JdbcTemplate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZAQ
 */
@Repository
@Setter
@Getter
public class RepoRepositoryImpl implements RepoRepository {

    private JdbcTemplate jdbcTemplate;
    private String selectAllReposQuery = "select * from repo";
    private String selectReposPositionQuery = "select id,position from repo";
    private String deleteRepoQuery = "delete from repo where id = ?";
    private String execInsertRepoProcedure = "{call insertRepo(?,?,?)}";
    private String updateRepoQuery = "update repo set capacity = ?, used = ?, position = ? where id = ?";
    private String selectRepoByIdQuery = "select * from repo where id = ?";

    public RepoRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Repo> selectAll() {
        List<Repo> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectAllReposQuery, new Object[]{});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                Repo repo = new Repo();
                repo.setId((int)map.get("id").get(i));
                repo.setCapacity((int)map.get("capacity").get(i));
                repo.setPosition((String) map.get("position").get(i));
                repo.setUsed((int) map.get("used").get(i));
                res.add(repo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Repo> selectPositions() {
        List<Repo> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectReposPositionQuery, new Object[]{});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                Repo repo = new Repo();
                repo.setId((int) map.get("id").get(i));
                repo.setPosition((String) map.get("position").get(i));
                res.add(repo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int deleteById(int id) {
        return this.getJdbcTemplate().update(this.deleteRepoQuery, new Object[]{id});
    }


    @Override
    public int insert(Repo repo) {
        Object[] repoParams = new Object[]{null, null, null};
        repoParams[0] = repo.getCapacity();
        repoParams[1] = repo.getUsed();
        repoParams[2] = repo.getPosition();
        try {
            this.getJdbcTemplate().call(this.execInsertRepoProcedure, repoParams, null, null);
        } catch (DuplicatePKException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public Repo selectById(int repoId) {
        Repo repo = new Repo();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectRepoByIdQuery, new Object[]{repoId});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return repo;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                repo.setId((int) map.get("id").get(i));
                repo.setCapacity((int) map.get("capacity").get(i));
                repo.setPosition((String) map.get("position").get(i));
                repo.setUsed((int) map.get("used").get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return repo;
    }

    @Override
    public int update(Repo repo) {
        Object[] repoParams = new Object[]{null, null, null, null};

        repoParams[0] = repo.getCapacity();
        repoParams[1] = repo.getUsed();
        repoParams[2] = repo.getPosition();
        repoParams[3] = repo.getId();

        return this.getJdbcTemplate().update(this.updateRepoQuery, repoParams);
    }
}
