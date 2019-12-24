package com.zaq.sjk.repomngsys.service;

import com.zaq.sjk.repomngsys.entity.Good;
import com.zaq.sjk.repomngsys.entity.Sale;

import java.util.List;

public interface GoodService {
    List<Good> listGoods();

    List<Good> listGoodNames();

    int deleteGood(int id);

    int update(Good good);

    int addGood(Good good);

    List<Good> listGoodInRepo(int repoId);

    List<Sale> getSalesOfAllTime(int gid);
}
