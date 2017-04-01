$(function() {

    wxConfig(["menuItem:share:timeline", "chooseImage", "getLocation", "openLocation", "scanQRCode"]);

    $("#abc").click(function() {
share2Cirlce("测试分享标题", "http://www.zslin.com", "https://www.baidu.com/s?wd=%E5%BE%AE%E7%9B%9F&rsv_idx=2&tn=baiduhome_pg&usm=1&ie=utf-8&rsv_cq=wx+jsapi+invalid+signature&rsv_dl=0_right_recommends_merge_28335&euri=10960559", function() {
            alert("====success");
        }, function() {
            alert("cancel");
        });
    });

    $("#getPic").click(function() {
        chooseImages(6, function(res) {
            alert(res.localIds);
        });
    });

    $("#getLocation").click(function() {
        getLocation(true, function(res) {
            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
            var speed = res.speed; // 速度，以米/每秒计
            var accuracy = res.accuracy; // 位置精度

            alert(JSON.stringify(res));
        });
    });

    $("#openLocation").click(function() {
        getLocation(false, function(res) {
            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
            var speed = res.speed; // 速度，以米/每秒计
            var accuracy = res.accuracy; // 位置精度
alert(latitude+"=="+longitude);
            openLocation(latitude, longitude, "这里是我家", "云南省昭通市昭阳区二环西路荷花蒂斯", "http://www.zslin.com");
        });
    });

    $("#qrCode").click(function() {
        scanQRCode("1", function(res) {
            alert(res.resultStr);
        });
    });
});

