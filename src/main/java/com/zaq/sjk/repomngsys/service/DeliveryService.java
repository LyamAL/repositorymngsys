package com.zaq.sjk.repomngsys.service;

import com.zaq.sjk.repomngsys.entity.DeliveryGoods;
import com.zaq.sjk.repomngsys.entity.DeliverySheet;

import java.util.Date;
import java.util.List;

/**
 * @author ZAQ
 */
public interface DeliveryService {
    int getSheetCountsBetween(Date then, Date now);

    int getSheetCounts();

    List<DeliverySheet> listAll();

    String addDelivery(DeliverySheet deliverySheet);

    int addGoodsToSheet(int repoId, String deliveryId, Integer[] gids, Float[] prices, Integer[] counts, String[] notes, Integer[] add);

    int deleteById(String id);

    DeliverySheet getSheetById(String id);

    List<DeliveryGoods> getGoodsInSheet(String id);

    int deleteGoodsOnSheet(int gid, String sheetId);

    int updateGoodsOnSheet(DeliveryGoods goods);

    int updateSheet(DeliverySheet sheet);
}
