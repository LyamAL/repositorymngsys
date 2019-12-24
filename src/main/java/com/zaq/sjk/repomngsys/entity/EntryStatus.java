package com.zaq.sjk.repomngsys.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZAQ
 */

public enum EntryStatus {
    订单创建((short) 0),
    部分完成((short) 1),
    入库完成((short) 2),
    订单取消((short) 3);

    private static Map<Short, EntryStatus> map = new HashMap<>();

    static {
        for (EntryStatus entryStatus : EntryStatus.values()) {
            map.put(entryStatus.value, entryStatus);
        }
    }

    private Short value;


    EntryStatus(Short value) {
        this.value = value;
    }

    public static EntryStatus valueOf(Short val) {
        return map.get(val);
    }

    public Short getValue() {
        return value;
    }

}
