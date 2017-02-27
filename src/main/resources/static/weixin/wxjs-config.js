function wxConfig(apiList) {
    if(apiList && apiList.length>0) {
        var url = window.location.href;
        $.post("/weixin/globalConfig/config", {url:url}, function(res) {
            wxConfigGlobal(res, apiList);
        }, "json");
    }
}

wx.error(function(res) {
//    alert("error:"+res);
});

function wxConfigGlobal(cfg, apiList) {
    //alert(cfg.appid+",time:"+cfg.timestamp+",nonce:"+cfg.nonceStr+",sign:"+cfg.signature);
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: cfg.appid, // 必填，公众号的唯一标识
        timestamp: cfg.timestamp, // 必填，生成签名的时间戳
        nonceStr: cfg.nonceStr, // 必填，生成签名的随机串
        signature: cfg.signature,// 必填，签名，见附录1
//        jsApiList: ['menuItem:share:timeline'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        jsApiList: apiList
    });
}