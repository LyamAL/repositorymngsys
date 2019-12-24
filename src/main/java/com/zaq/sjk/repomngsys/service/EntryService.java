package com.zaq.sjk.repomngsys.service;

import com.zaq.sjk.repomngsys.entity.EntryGoods;
import com.zaq.sjk.repomngsys.entity.EntrySheet;

import java.util.Date;
import java.util.List;

/**
 * @author ZAQ
 */
public interface EntryService {
    int getSheetCountsBetween(Date then, Date now);

    int getSheetCounts();

    List<EntrySheet> listAllSheet();

    EntrySheet getSheetById(String id);

    List<EntryGoods> getGoodsInSheet(String id);

    int deleteById(String id);

    String addEntrySheet(EntrySheet entrySheet);

    int addGoodsToSheet(String sheetId, Integer[] gids, Float[] prices, Integer[] counts, Integer[] qualifieds, String[] notes, Integer[] add);

    int updateSheet(EntrySheet sheet);

    int updateGoodsOnSheet(EntryGoods goods);

    int deleteGoodsOnSheet(int gid, String sheetId);

}
