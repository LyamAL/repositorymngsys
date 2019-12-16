package com.zaq.sjk.repomngsys.service.impl;

import com.zaq.sjk.repomngsys.repository.DeliveryRepository;
import com.zaq.sjk.repomngsys.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public int getSheetCountsBetween(Date then, Date now) {
        return deliveryRepository.countByDate(then.toString(), now.toString());
    }

    @Override
    public int getSheetCounts() {
        return deliveryRepository.count();
    }
}
