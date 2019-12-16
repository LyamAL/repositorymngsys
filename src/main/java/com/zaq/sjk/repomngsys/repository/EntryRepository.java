package com.zaq.sjk.repomngsys.repository;

public interface DeliveryRepository {
    int countByDate(String then, String now);

    int count();

}
