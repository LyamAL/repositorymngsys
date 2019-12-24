package com.zaq.sjk.repomngsys.repository.impl;

import com.zaq.sjk.repomngsys.entity.Good;
import com.zaq.sjk.repomngsys.entity.Sale;
import com.zaq.sjk.repomngsys.exception.DuplicatePKException;
import com.zaq.sjk.repomngsys.repository.GoodRepository;
import com.zaq.sjk.repomngsys.repository.JdbcTemplate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZAQ
 */
@Repository
@Getter
@Setter
public class GoodRepositoryImpl implements GoodRepository {
    private JdbcTemplate jdbcTemplate;
    private String selectAllGoodsQuery = "select *  from good";
    private String selectAllGoodNamesQuery = "select id,name from good";
    private String deleteGoodById = "delete from good where id = ?";
    private String updateGoodByIdQuery = "update good set name = ?, origin = ?, price = ?, unit = ? where id = ?";
    private String execInsertGoodProcedure = "{call insertGood(?,?,?,?,?)}";
    private String insertStorageQuery = "insert into Storage values(?,?,?,?)";
    private String selectGoodsByRepoId = "SELECT * FROM GOOD WHERE ID IN (SELECT GID FROM storage S WHERE S.repoId =?)";
    private String execSelectSaleProcedure = "{call goodSaleOfAllTime(?)}";

    public GoodRepositoryImpl(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Good> selectByRepoId(int repoId) {
        List<Good> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectGoodsByRepoId, new Object[]{repoId});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                Good good = new Good();
                good.setId((int) map.get("id").get(i));
                good.setName((String) map.get("name").get(i));
                good.setPrice(Float.parseFloat(String.valueOf(map.get("price").get(i))));
                good.setUnit((String) map.get("unit").get(i));
                res.add(good);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Sale> selectSaleById(int gid) {
        List<Sale> res = new ArrayList<>();
        Map<String, List<Object>> resMap = this.getJdbcTemplate().call(this.execSelectSaleProcedure, new Object[]{gid});
        if (resMap == null || resMap.get("sale") == null) {
            return res;
        }
        for (int i = 0; i < resMap.get("sale").size(); i++) {
            Sale sale = new Sale();
            sale.setGid(gid);
            sale.setCount((Integer) resMap.get("sale").get(i));
            sale.setSum(Float.parseFloat(String.valueOf(resMap.get("sum").get(i))));
            sale.setDate(String.valueOf(resMap.get("month").get(i)));
            res.add(sale);
        }
        return res;
    }

    @Override
    public List<Good> selectGoodNames() {
        List<Good> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectAllGoodNamesQuery, new Object[]{});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                Good good = new Good();
                good.setId((int) map.get("id").get(i));
                good.setName((String) map.get("name").get(i));
                res.add(good);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int insertGood(Good good) {
        Object[] goodPrams = new Object[]{good.getOrigin(), good.getName(), good.getPrice(), good.getUnit()};
        try {
            Map<String, Object> resMap = this.getJdbcTemplate().call(this.execInsertGoodProcedure, goodPrams,
                    new Integer[]{5}, new Integer[]{Types.INTEGER});
            return (int) resMap.get("5");
        } catch (DuplicatePKException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(Good good) {
        Object[] goodParams = new Object[]{good.getName(), good.getOrigin(), good.getPrice(), good.getUnit(), good.getId()};
        return this.getJdbcTemplate().update(this.updateGoodByIdQuery, goodParams);
    }

    @Override
    public int deleteById(int id) {
        return this.getJdbcTemplate().update(this.deleteGoodById, new Object[]{id});
    }

    @Override
    public List<Good> selectAll() {
        List<Good> res = new ArrayList<>();
        try {
            Map<String, List<Object>> map = this.getJdbcTemplate().query(this.selectAllGoodsQuery, new Object[]{});
            if (map.get("id") == null || map.get("id").size() == 0) {
                return res;
            }
            for (int i = 0; i < map.get("id").size(); i++) {
                Good good = new Good();
                good.setId((int) map.get("id").get(i));
                good.setName((String) map.get("name").get(i));
                good.setOrigin((String) map.get("origin").get(i));
                good.setPrice(Float.parseFloat(String.valueOf(map.get("price").get(i))));
                good.setUnit((String) map.get("unit").get(i));
                res.add(good);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
