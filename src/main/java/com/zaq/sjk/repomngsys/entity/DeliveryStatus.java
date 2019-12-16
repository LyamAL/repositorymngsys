package com.zaq.sjk.repomngsys.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZAQ
 */

public enum EntryStatus {
    订单创建(0),
    部分完成(1),
    入库完成(2),
    订单取消(3);

    private static Map<Integer, EntryStatus> map = new HashMap<>();

    static {
        for (EntryStatus entryStatus : EntryStatus.values()) {
            map.put(entryStatus.value, entryStatus);
        }
    }

    private int value;


    EntryStatus(int value) {
        this.value = value;
    }

    public static EntryStatus valueOf(int val) {
        return map.get(val);
    }

    public int getValue() {
        return value;
    }

    }
