package com.zaq.sjk.repomngsys.repository;

import com.zaq.sjk.repomngsys.entity.Storage;

import java.util.List;

/**
 * @author ZAQ
 */
public interface StorageRepository {
    List<Storage> listStorageByRepo(int rid);

    List<Storage> listStorageByGood(int gid);

    int delete(int gid, int repoId);

    int update(Storage storage);
}
