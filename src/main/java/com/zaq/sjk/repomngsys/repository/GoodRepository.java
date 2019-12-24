package com.zaq.sjk.repomngsys.repository;

import com.zaq.sjk.repomngsys.entity.Good;
import com.zaq.sjk.repomngsys.entity.Sale;

import java.util.List;

public interface GoodRepository {
    List<Good> selectGoodNames();

    List<Good> selectAll();

    int deleteById(int id);

    int update(Good good);

    int insertGood(Good good);

    List<Good> selectByRepoId(int repoId);

    List<Sale> selectSaleById(int gid);

}
