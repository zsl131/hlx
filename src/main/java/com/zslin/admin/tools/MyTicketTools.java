package com.zslin.admin.tools;

import com.zslin.admin.dto.MyTicketDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/12 10:32.
 */
public class MyTicketTools {
    private Map<MyTicketDto, Integer> map;
    public MyTicketTools(){
        this.map = new HashMap<>();
    }

    public void add(Integer prizeId, String prizeName, Integer amount) {
        MyTicketDto dto = new MyTicketDto(prizeId, prizeName);
        if(map.containsKey(dto)) {
            map.put(dto, map.get(dto)+amount);
        } else {
            map.put(dto, amount);
        }
    }

    public Map<MyTicketDto, Integer> getDatas() {
        return this.map;
    }
}
