package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.entity.EntryGoods;
import com.zaq.sjk.repomngsys.entity.EntrySheet;
import com.zaq.sjk.repomngsys.repository.EntryRepository;
import com.zaq.sjk.repomngsys.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZAQ
 */
@Service
public class EntryServiceImpl implements EntryService {
    private EntryRepository entryRepository;

    public EntryServiceImpl(@Autowired EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public int getSheetCountsBetween(Date then, Date now) {
        return entryRepository.countByDate(then.toString(), now.toString());
    }

    @Override
    public int getSheetCounts() {
        return entryRepository.count();
    }

    @Override
    public List<EntrySheet> listAllSheet() {
        return entryRepository.selectAll();
    }

    @Override
    public EntrySheet getSheetById(String id) {
        return entryRepository.selectSheetById(id);
    }

    @Override
    public List<EntryGoods> getGoodsInSheet(String id) {
        return entryRepository.selectGoodsBySheetId(id);
    }

    @Override
    public int deleteById(String id) {
        return entryRepository.deleteSheetById(id);
    }

    @Override
    public String addEntrySheet(EntrySheet entrySheet) {
        return entryRepository.insertSheet(entrySheet);
    }

    @Override
    public int addGoodsToSheet(String sheetId, Integer[] gids, Float[] prices, Integer[] counts, Integer[] qualifieds, String[] notes, Integer[] add) {
        List<EntryGoods> entryGoods = new ArrayList<>();
        EntryGoods goods = new EntryGoods();
        goods.setEntryId(sheetId);
        if (add == null) {
            return 0;
        }
        for (int i = 0; i < add.length; i++) {
            if (!gids[i].equals(add[i])) {
                continue;
            }
            goods.setGid(gids[i]);
            goods.setCount(counts[i]);
            goods.setPrice(prices[i]);
            goods.setSum(prices[i] * counts[i]);
            goods.setQualified(qualifieds[i]);
            entryGoods.add(goods);
        }
        return entryRepository.insertBatch(entryGoods);
    }

    @Override
    public int updateSheet(EntrySheet sheet) {
        return entryRepository.update(sheet);
    }

    @Override
    public int updateGoodsOnSheet(EntryGoods goods) {
        return entryRepository.updateGoods(goods);
    }

    @Override
    public int deleteGoodsOnSheet(int gid, String sheetId) {
        return entryRepository.deleteGoodsByGidAndSheetId(gid, sheetId);
    }
}
