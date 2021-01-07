package com.zslin.basic.tools;

import com.zslin.wx.tools.JsonTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestAutoTools {

    public void run(String params) {
        String name = JsonTools.getJsonParam(params, "name");
        String msg = JsonTools.getJsonParam(params, "msg");
        System.out.println("-----TestAutoTools run -----name:"+name+"---> msg: "+msg);
    }
}
