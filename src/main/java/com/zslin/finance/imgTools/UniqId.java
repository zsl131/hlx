package com.zslin.finance.imgTools;

import java.util.UUID;

public class UniqId {

    public static String getUid() {
        String id = UUID.randomUUID().toString().replace("-", "");
        return id;
    }
}
