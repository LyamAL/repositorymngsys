package com.zaq.sjk.repomngsys.repository;

import com.zaq.sjk.repomngsys.entity.DeliveryGoods;
import com.zaq.sjk.repomngsys.entity.DeliverySheet;

import java.util.List;

public interface DeliveryRepository {
    int countByDate(String then, String now);

    int count();

    List<DeliverySheet> selectAll();

    String insert(DeliverySheet deliverySheet);

    int insertBatch(List<DeliveryGoods> deliveryGoods);

    int deleteById(String id);

    DeliverySheet selectById(String id);

    List<DeliveryGoods> selectGoodsByDeliveryId(String id);

    int deleteGoodsByGid(int gid, String sheetId);

    int updateGoodsOnSheet(DeliveryGoods goods);

    int updateSheet(DeliverySheet sheet);
}
