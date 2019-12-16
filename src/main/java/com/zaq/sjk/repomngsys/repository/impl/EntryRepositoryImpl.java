package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.repository.BaseRepository;
import com.zaq.sjk.repomngsys.repository.DeliveryRepository;
import com.zaq.sjk.repomngsys.repository.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author ZAQ
 */
@Repository
public class DeliveryRepositoryImpl extends BaseRepository implements DeliveryRepository {

    private final String countByDateQuery = "select count(*) as " + COUNT + " from deliverysheet where createtime > ? and createtime < ?";
    private final String countAllQuery = "select count(*) as " + COUNT + " from deliverysheet";
    private JdbcTemplate jdbcTemplate;

    public DeliveryRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int countByDate(String then, String now) {
        try {
            Map<String, List<Object>> map = jdbcTemplate.query(this.countByDateQuery, new String[]{then, now});
            return (int)this.fetch(map, COUNT, false);
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public int count() {
        try {
            Map<String, List<Object>> map = jdbcTemplate.query(this.countAllQuery, null);
            return (int) this.fetch(map, COUNT,false);
        } catch (SQLException e) {
            return 0;
        }
    }


}
