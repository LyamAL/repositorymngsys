package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.entity.Good;
import com.zaq.sjk.repomngsys.entity.Sale;
import com.zaq.sjk.repomngsys.repository.GoodRepository;
import com.zaq.sjk.repomngsys.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodServiceImpl implements GoodService {
    private GoodRepository goodRepository;

    public GoodServiceImpl(@Autowired GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }

    @Override
    public List<Good> listGoods() {
        return goodRepository.selectAll();
    }

    @Override
    public List<Good> listGoodNames() {
        return goodRepository.selectGoodNames();
    }

    @Override
    public List<Good> listGoodInRepo(int repoId) {
        return goodRepository.selectByRepoId(repoId);
    }

    @Override
    public List<Sale> getSalesOfAllTime(int gid) {
        return goodRepository.selectSaleById(gid);
    }

    @Override
    public int addGood(Good good) {
        return goodRepository.insertGood(good);
    }

    @Override
    public int update(Good good) {
        return goodRepository.update(good);
    }

    @Override
    public int deleteGood(int id) {
        return goodRepository.deleteById(id);
    }
}
