package com.zaq.sjk.repomngsys.repository;

import com.zaq.sjk.repomngsys.entity.EntryGoods;
import com.zaq.sjk.repomngsys.entity.EntrySheet;

import java.util.List;

public interface EntryRepository {
    int countByDate(String then, String now);

    int count();

    List<EntryGoods> selectGoodsBySheetId(String id);

    EntrySheet selectSheetById(String id);

    List<EntrySheet> selectAll();

    int deleteSheetById(String id);

    String insertSheet(EntrySheet entrySheet);

    int insertBatch(List<EntryGoods> entryGoods);

    int deleteGoodsByGidAndSheetId(int gid, String sheetId);

    int update(EntrySheet sheet);

    int updateGoods(EntryGoods goods);
}
