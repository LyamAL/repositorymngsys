package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.entity.DeliveryGoods;
import com.zaq.sjk.repomngsys.entity.DeliverySheet;
import com.zaq.sjk.repomngsys.repository.DeliveryRepository;
import com.zaq.sjk.repomngsys.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZAQ
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private DeliveryRepository deliveryRepository;


    public DeliveryServiceImpl(@Autowired DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public int deleteById(String id) {
        return deliveryRepository.deleteById(id);
    }

    @Override
    public DeliverySheet getSheetById(String id) {
        return deliveryRepository.selectById(id);
    }

    @Override
    public List<DeliveryGoods> getGoodsInSheet(String id) {
        return deliveryRepository.selectGoodsByDeliveryId(id);
    }

    @Override
    public List<DeliverySheet> listAll() {
        return this.deliveryRepository.selectAll();
    }

    @Override
    public int addGoodsToSheet(int repoId, String deliveryId, Integer[] gids, Float[] prices, Integer[] counts, String[] notes, Integer[] add) {
        List<DeliveryGoods> deliveryGoods = new ArrayList<>();
        DeliveryGoods goods = new DeliveryGoods();
        goods.setDeliveryId(deliveryId);
        if (add == null) {
            return 0;
        }
        for (int i = 0; i < add.length; i++) {
            if (!add[i].equals(gids[i])) {
                continue;
            }
            goods.setGid(gids[i]);
            goods.setCount(counts[i]);
            goods.setPrice(prices[i]);
            goods.setSum(prices[i] * counts[i]);
            goods.setNote(notes[i]);
            deliveryGoods.add(goods);
        }
        return deliveryRepository.insertBatch(deliveryGoods);
    }

    @Override
    public int updateSheet(DeliverySheet sheet) {
        return deliveryRepository.updateSheet(sheet);
    }

    @Override
    public int deleteGoodsOnSheet(int gid, String sheetId) {
        return deliveryRepository.deleteGoodsByGid(gid, sheetId);
    }

    @Override
    public int updateGoodsOnSheet(DeliveryGoods goods) {
        return deliveryRepository.updateGoodsOnSheet(goods);
    }

    @Override
    public String addDelivery(DeliverySheet deliverySheet) {
        return this.deliveryRepository.insert(deliverySheet);
    }

    @Override
    public int getSheetCountsBetween(Date then, Date now) {
        return deliveryRepository.countByDate(then.toString(), now.toString());
    }

    @Override
    public int getSheetCounts() {
        return deliveryRepository.count();
    }
}
