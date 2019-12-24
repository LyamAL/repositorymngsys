package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.*;
import com.zaq.sjk.repomngsys.repository.DeliveryRepository;
import com.zaq.sjk.repomngsys.repository.JdbcTemplate;
import com.zaq.sjk.repomngsys.utils.StringUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ZAQ
 */
@Repository
@Getter
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final String countByDateQuery = "select count(*) as count from deliverysheet where createtime > ? and createtime < ?";
    private final String countAllQuery = "select count(*) as count from deliverysheet";
    private JdbcTemplate jdbcTemplate;
    private String selectAllDlvrSheetsQuery = "select deliverysheet.id,createtime,closetime,repo.position,status,contact,destination,note from deliverysheet inner join repo on repo.id = deliverysheet.repoId";
    private String execInsertDlvrSheetProcedure = "{call insertDeliverySheet(?,?,?,?,?)}";
    private String insertDlvrGoodsQuery = "insert into deliverygoods values(?,?,?,?,?,?)";
    private String deleteDlvrSheetQuery = "delete from deliverysheet where id = convert(binary(16), ?)";
    private String selectDlvrSheetByIdQuery = "select s.*,r.position from deliverysheet s,repo r where s.id = convert(binary(16), ?) and r.id = s.repoId ";
    private String selectGoodsByDeliveryIdQuery = "select gs.*,g.name from deliverygoods gs inner join good g on deliveryId = convert(binary(16),?) and g.id = gs.gid";
    private String updateGoodsOnSheetQuery = "update deliverygoods set count=?,price = ?,sum=?,note=? where gid = ? and deliveryId = convert(binary(16),?)";
    private String deleteGoodsByGidQuery = "delete from deliverygoods where gid = ? and repoId = ?";
    private String updateSheetQuery = "update deliverysheet set status = ?, contact = ?, destination = ?, note = ?,closetime = ? where id = convert(binary(16),?)";

    public DeliveryRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int countByDate(String then, String now) {
        try {
            Map<String, List<Object>> map = jdbcTemplate.query(this.countByDateQuery, new String[]{then, now});
            if (map.get("count") == null || map.get("count").size() == 0) {
                return 0;
            }
            return (int) map.get("count").get(0);
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public int deleteById(String id) {
        byte[] bytes = StringUtil.stringToBytes(id);
        return this.getJdbcTemplate().update(this.deleteDlvrSheetQuery, new Object[]{bytes});
    }

    @Override
    public List<DeliveryGoods> selectGoodsByDeliveryId(String id) {
        List<DeliveryGoods> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectGoodsByDeliveryIdQuery, new Object[]{StringUtil.stringToBytes(id)});
            if (map.get("gid") == null || map.get("gid").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("gid").size(); i++) {
                DeliveryGoods goods = new DeliveryGoods();
                goods.setSum(Float.parseFloat(String.valueOf(map.get("sum").get(i))));
                goods.setNote(String.valueOf(map.get("note").get(i)));
                goods.setPrice(Float.parseFloat(String.valueOf(map.get("price").get(i))));
                goods.setCount((int) map.get("count").get(i));
                goods.setDeliveryId(StringUtil.bytesToString((byte[]) map.get("deliveryId").get(i)));
                goods.setGood(new Good((int) map.get("gid").get(i), (String) map.get("name").get(i)));
                res.add(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public DeliverySheet selectById(String id) {
        DeliverySheet dlvr = new DeliverySheet();
        byte[] bytes = StringUtil.stringToBytes(id);
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectDlvrSheetByIdQuery, new Object[]{bytes});
            if (map.get("id") != null && map.get("id").size() > 0) {
                dlvr.setId(StringUtil.bytesToString((byte[]) map.get("id").get(0)));
                dlvr.setCreateTime((Date) map.get("createtime").get(0));
                dlvr.setCloseTime((Date) map.get("closetime").get(0));
                dlvr.setContact((String) map.get("contact").get(0));
                dlvr.setRepoId(new Repo((String) map.get("position").get(0)));
                dlvr.setNote(String.valueOf(map.get("note").get(0)));
                dlvr.setDestination((String) map.get("destination").get(0));
                dlvr.setDeliveryStatus(DeliveryStatus.valueOf((short) map.get("status").get(0)));
            }
            return dlvr;
        } catch (SQLException e) {
            e.printStackTrace();
            return dlvr;
        }
    }

    @Override
    public int insertBatch(List<DeliveryGoods> deliveryGoods) {
        Object[][] argus = new Object[deliveryGoods.size()][6];
        for (int i = 0; i < deliveryGoods.size(); i++) {
            argus[i][0] = deliveryGoods.get(i).getGid();
            argus[i][1] = deliveryGoods.get(i).getCount();
            argus[i][2] = deliveryGoods.get(i).getPrice();
            argus[i][3] = deliveryGoods.get(i).getSum();
            argus[i][4] = deliveryGoods.get(i).getNote();
            argus[i][5] = StringUtil.stringToBytes(deliveryGoods.get(i).getDeliveryId());
        }
        this.getJdbcTemplate().updateBatch(this.insertDlvrGoodsQuery, argus);
        return 1;
    }

    @Override
    public String insert(DeliverySheet deliverySheet) {
        Object[] params = new Object[]{Integer.parseInt(deliverySheet.getRepoId().getPosition()), deliverySheet.getContact(), deliverySheet.getDestination(), deliverySheet.getNote()};
        try {
            Map<String, Object> res = this.getJdbcTemplate().call(this.execInsertDlvrSheetProcedure, params, new Integer[]{5}, new Integer[]{Types.BINARY});
            return StringUtil.bytesToString((byte[]) res.get("5"));
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int deleteGoodsByGid(int gid, String sheetId) {
        return this.getJdbcTemplate().update(this.deleteGoodsByGidQuery, new Object[]{gid, StringUtil.stringToBytes(sheetId)});
    }

    @Override
    public int updateSheet(DeliverySheet sheet) {
        Object[] params = new Object[]{sheet.getDeliveryStatus().getValue(), sheet.getContact(), sheet.getDestination(), sheet.getNote(), sheet.getCloseTime(),
                StringUtil.stringToBytes(sheet.getId())};
        return this.getJdbcTemplate().update(this.updateSheetQuery, params);
    }

    @Override
    public int updateGoodsOnSheet(DeliveryGoods goods) {
        Object[] params = new Object[]{goods.getCount(), goods.getPrice(), goods.getSum(), goods.getNote(), goods.getGid(), StringUtil.stringToBytes(goods.getDeliveryId())};
        return this.getJdbcTemplate().update(this.updateGoodsOnSheetQuery, params);
    }

    @Override
    public List<DeliverySheet> selectAll() {
        List<DeliverySheet> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectAllDlvrSheetsQuery, new Object[]{});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                DeliverySheet dlvr = new DeliverySheet();
                dlvr.setId(StringUtil.bytesToString((byte[]) map.get("id").get(i)));
                dlvr.setCreateTime((Date) map.get("createtime").get(i));
                dlvr.setCloseTime((Date) map.get("closetime").get(i));
                dlvr.setContact((String) map.get("contact").get(i));
                dlvr.setRepoId(new Repo((String) map.get("position").get(i)));
                dlvr.setNote(String.valueOf(map.get("note").get(i)));
                dlvr.setDestination((String) map.get("destination").get(i));
                dlvr.setDeliveryStatus(DeliveryStatus.valueOf((short) map.get("status").get(i)));
                res.add(dlvr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int count() {
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.countAllQuery, null);
            if (map.get("count") == null || map.get("count").size() == 0) {
                return 0;
            }
            return (int) map.get("count").get(0);
        } catch (SQLException e) {
            return 0;
        }
    }


}
