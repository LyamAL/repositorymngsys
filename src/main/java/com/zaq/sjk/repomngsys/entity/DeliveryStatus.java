package com.zaq.sjk.repomngsys.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZAQ
 */

public enum DeliveryStatus {
    订单创建((short) 0),
    拣货完成((short) 1),
    关闭回传((short) 2),
    订单取消((short) 3);

    private static Map<Short, DeliveryStatus> map = new HashMap<>();

    static {
        for (DeliveryStatus entryStatus : DeliveryStatus.values()) {
            map.put(entryStatus.value, entryStatus);
        }
    }

    private Short value;


    DeliveryStatus(Short value) {
        this.value = value;
    }

    public static DeliveryStatus valueOf(Short val) {
        return map.get(val);
    }

    public Short getValue() {
        return value;
    }

}
