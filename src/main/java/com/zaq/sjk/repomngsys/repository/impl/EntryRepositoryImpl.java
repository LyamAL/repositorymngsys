package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.*;
import com.zaq.sjk.repomngsys.repository.EntryRepository;
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
public class EntryRepositoryImpl implements EntryRepository {

    private final String countByDateQuery = "select count(*) as count from entrysheet where createtime > ? and createtime < ?";
    private final String countAllQuery = "select count(*) as count from entrysheet";
    private JdbcTemplate jdbcTemplate;
    private String selectGoodsByEntryIdQuery = "select eg.*,g.name from entrygoods eg inner join good g on entryId = convert(binary(16),?) and g.id = eg.gid";
    private String selectEntrSheetByIdQuery = "SELECT e.*,r.position FROM entrysheet e, repo r where e.id = convert(binary(16), ?) and r.id = e.repoId";
    private String selectAllEntrSheetsQuery = "select e.id,createtime,closetime,r.position,status,contact,producer,note from entrysheet e inner join repo r on r.id = e.repoId";
    private String deleteEntrSheetQuery = "delete from entrysheet where id = convert(binary(16), ?)";
    private String execInsertEntrSheetProcedure = "{call insertEntrySheet(?,?,?,?,?)}";
    private String insertEntrGoodsQuery = "insert into entrygoods values(?,?,?,?,?,?)";
    private String deleteGoodsByGidAndEntryIdQuery = "delete from entrygoods where gid = ? and entryId = convert(binary(16), ?)";
    private String updateSheetQuery = "update entrysheet set contact = ?, status = ?, note = ?,producer = ?,closetime = ? where id = convert(binary(16), ?)";
    private String updateGoodsOnSheetQuery = "update entrygoods set count = ?, price = ?, sum = ?, qualified = ? where entryId = convert(binary(16),?) and gid = ?";

    public EntryRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
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
    public int count() {
        try {
            Map<String, List<Object>> map = jdbcTemplate.query(this.countAllQuery, null);
            if (map.get("count") == null || map.get("count").size() == 0) {
                return 0;
            }
            return (int) map.get("count").get(0);
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public List<EntryGoods> selectGoodsBySheetId(String id) {
        List<EntryGoods> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectGoodsByEntryIdQuery, new Object[]{StringUtil.stringToBytes(id)});
            if (map.get("gid") == null || map.get("gid").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("gid").size(); i++) {
                EntryGoods goods = new EntryGoods();
                goods.setSum(Float.parseFloat(String.valueOf(map.get("sum").get(i))));
                goods.setQualified((int) map.get("qualified").get(i));
                goods.setPrice(Float.parseFloat(String.valueOf(map.get("price").get(i))));
                goods.setCount((int) map.get("count").get(i));
                goods.setEntryId(StringUtil.bytesToString((byte[]) map.get("entryId").get(i)));
                goods.setGood(new Good((int) map.get("gid").get(i), (String) map.get("name").get(i)));
                res.add(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public EntrySheet selectSheetById(String id) {
        EntrySheet entr = new EntrySheet();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectEntrSheetByIdQuery, new Object[]{StringUtil.stringToBytes(id)});
            if (map.get("id") != null && map.get("id").size() > 0) {
                entr.setId(StringUtil.bytesToString((byte[]) map.get("id").get(0)));
                entr.setCreateTime((Date) map.get("createtime").get(0));
                entr.setCloseTime((Date) map.get("closetime").get(0));
                entr.setContact((String) map.get("contact").get(0));
                entr.setRepoId(new Repo((String) map.get("position").get(0)));
                entr.setNote(String.valueOf(map.get("note").get(0)));
                entr.setProducer((String) map.get("producer").get(0));
                entr.setStatus(EntryStatus.valueOf((short) map.get("status").get(0)));
            }
            return entr;
        } catch (SQLException e) {
            e.printStackTrace();
            return entr;
        }
    }

    @Override
    public List<EntrySheet> selectAll() {
        List<EntrySheet> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectAllEntrSheetsQuery, new Object[]{});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                EntrySheet entr = new EntrySheet();
                entr.setId(StringUtil.bytesToString((byte[]) map.get("id").get(i)));
                entr.setCreateTime((Date) map.get("createtime").get(i));
                entr.setCloseTime((Date) map.get("closetime").get(i));
                entr.setContact((String) map.get("contact").get(i));
                entr.setRepoId(new Repo((String) map.get("position").get(i)));
                entr.setProducer((String) map.get("producer").get(i));
                entr.setNote(String.valueOf(map.get("note").get(i)));
                entr.setStatus(EntryStatus.valueOf((short) map.get("status").get(i)));
                res.add(entr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int deleteSheetById(String id) {
        return this.getJdbcTemplate().update(this.deleteEntrSheetQuery, new Object[]{StringUtil.stringToBytes(id)});
    }

    @Override
    public String insertSheet(EntrySheet entrySheet) {
        Object[] params = new Object[]{Integer.parseInt(entrySheet.getRepoId().getPosition()), entrySheet.getContact(), entrySheet.getNote(), entrySheet.getProducer()};
        try {
            Map<String, Object> res = this.getJdbcTemplate().call(this.execInsertEntrSheetProcedure, params, new Integer[]{5}, new Integer[]{Types.BINARY});
            return StringUtil.bytesToString((byte[]) res.get("5"));
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int insertBatch(List<EntryGoods> entryGoods) {
        Object[][] argus = new Object[entryGoods.size()][6];
        for (int i = 0; i < entryGoods.size(); i++) {
            argus[i][0] = entryGoods.get(i).getGid();
            argus[i][1] = entryGoods.get(i).getCount();
            argus[i][2] = entryGoods.get(i).getPrice();
            argus[i][3] = entryGoods.get(i).getSum();
            argus[i][4] = entryGoods.get(i).getQualified();
            argus[i][5] = StringUtil.stringToBytes(entryGoods.get(i).getEntryId());
        }
        this.getJdbcTemplate().updateBatch(this.insertEntrGoodsQuery, argus);
        return 1;
    }

    @Override
    public int deleteGoodsByGidAndSheetId(int gid, String sheetId) {
        return this.getJdbcTemplate().update(this.deleteGoodsByGidAndEntryIdQuery, new Object[]{gid, StringUtil.stringToBytes(sheetId)});

    }

    @Override
    public int update(EntrySheet sheet) {
        Object[] params = new Object[]{sheet.getContact(), sheet.getStatus().getValue(), sheet.getNote(), sheet.getProducer(), sheet.getCloseTime(),
                StringUtil.stringToBytes(sheet.getId())};
        return this.getJdbcTemplate().update(this.updateSheetQuery, params);
    }

    @Override
    public int updateGoods(EntryGoods goods) {
        Object[] params = new Object[]{goods.getCount(), goods.getPrice(), goods.getSum(), goods.getQualified(), StringUtil.stringToBytes(goods.getEntryId()), goods.getGid()};
        return this.getJdbcTemplate().update(this.updateGoodsOnSheetQuery, params);
    }

}
