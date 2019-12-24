package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.Good;
import com.zaq.sjk.repomngsys.entity.Repo;
import com.zaq.sjk.repomngsys.entity.Storage;
import com.zaq.sjk.repomngsys.repository.JdbcTemplate;
import com.zaq.sjk.repomngsys.repository.StorageRepository;
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
@Getter
@Setter
public class StorageRepositoryImpl implements StorageRepository {
    private JdbcTemplate jdbcTemplate;
    private String selectStorageByRidQuery = "select gid, count, qualified, name from storage inner join good on repoId = ? and storage.gid = good.id";
    private String selectStorageByGidQuery = "select s.count, s.qualified, r.id, r.position from storage s inner join repo r on gid = ? and  s.repoId = r.id ";
    private String deleteStorageQuery = "delete from storage where gid = ? and repoId = ?";
    private String updateStorageQuery = "update storage set  qualified = ? ,count = ? where gid = ? and repoId = ?";

    public StorageRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Storage> listStorageByRepo(int rid) {
        List<Storage> storageList;
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectStorageByRidQuery, new Object[]{rid});
            int size = map.get("gid").size();
            storageList = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Storage storage = new Storage();
                storage.setGid((Integer) map.get("gid").get(i));
                Good good = new Good();
                good.setId((Integer) map.get("gid").get(i));
                good.setName((String) map.get("name").get(i));
                storage.setGood(good);
                storage.setCount((Integer) map.get("count").get(i));
                storage.setQualified((Integer) map.get("qualified").get(i));
                storageList.add(storage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            storageList = new ArrayList<>();
        }
        return storageList;
    }

    @Override
    public int delete(int gid, int repoId) {
        return this.getJdbcTemplate().update(this.deleteStorageQuery, new Object[]{gid, repoId});
    }

    @Override
    public int update(Storage storage) {
        Object[] params = new Object[]{storage.getQualified(), storage.getCount(), storage.getGid(), storage.getRepoId()};
        return this.getJdbcTemplate().update(this.updateStorageQuery, params);
    }

    @Override
    public List<Storage> listStorageByGood(int gid) {
        List<Storage> storageList = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectStorageByGidQuery, new Object[]{gid});
            if (map.get("id") == null) {
                return storageList;
            }
            int size = map.get("id").size();
            storageList = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Storage storage = new Storage();
                storage.setRepoId((int) map.get("id").get(i));
                Repo repo = new Repo();
                repo.setId((int) map.get("id").get(i));
                repo.setPosition((String) map.get("position").get(i));
                storage.setRepo(repo);
                storage.setCount((Integer) map.get("count").get(i));
                storage.setQualified(Integer.parseInt(String.valueOf(map.get("qualified").get(i))));
                storageList.add(storage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            storageList = new ArrayList<>();
        }
        return storageList;
    }
}
