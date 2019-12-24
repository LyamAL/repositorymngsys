package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.entity.Storage;
import com.zaq.sjk.repomngsys.repository.StorageRepository;
import com.zaq.sjk.repomngsys.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZAQ
 */
@Service
public class StorageServiceImpl implements StorageService {

    private StorageRepository storageRepository;

    public StorageServiceImpl(@Autowired StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @Override
    public List<Storage> listStorageByRepo(int id) {
        return storageRepository.listStorageByRepo(id);
    }

    @Override
    public List<Storage> listStorageByGood(int id) {
        return storageRepository.listStorageByGood(id);
    }

    @Override
    public int updateStorage(Storage storage) {
        return storageRepository.update(storage);
    }

    @Override
    public int deleteStorage(int gid, int repoId) {
        return storageRepository.delete(gid, repoId);
    }
}
