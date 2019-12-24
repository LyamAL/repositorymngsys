package com.zaq.sjk.repomngsys.service;

import com.zaq.sjk.repomngsys.entity.Storage;

import java.util.List;

/**
 * @author ZAQ
 */
public interface StorageService {
    List<Storage> listStorageByRepo(int id);

    List<Storage> listStorageByGood(int id);

    int updateStorage(Storage storage);

    int deleteStorage(int gid, int repoId);
}
