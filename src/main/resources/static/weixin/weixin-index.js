$(function() {

    wxConfig(["menuItem:share:timeline", "chooseImage", "getLocation", "openLocation", "scanQRCode", "onMenuShareTimeline","onMenuShareAppMessage"]);

    setShares();

    $(".address-map").click(function() {
        var latitude = $(this).attr("latitude");
        var longitude = $(this).attr("longitude");
        var address = $(this).attr("address");
        var title = $(this).attr("appName");
        openLocation(latitude, longitude, title, address);
    });

    $("#qrCode").click(function() {
        scanQRCode("1", function(res) {
            alert(res.resultStr);
        });
    });
});

function setShares() {
    var appName = $(".address-map").attr("appName");
    var shareDesc = "在"+appName+"自助餐厅用餐真“"+buildShareTitle()+"”";

    share2Cirlce(shareDesc, "http://zthlx.zslin.com/weixin/index", "http://zthlx.zslin.com/logo-200.png", function() {
        postScore("SHARE");
    }, function() {
        alert("取消分享不会加分哦");
    });

    share2Friend(appName, shareDesc, "http://zthlx.zslin.com/weixin/index", "http://zthlx.zslin.com/logo-200.png", function() {
        postScore("SHARE-FRIEND");
    }, function() {
        alert("取消分享不会加分哦");
    })
}

function postScore(type) {
    $.post("/wx/account/sharePage", {type:type}, function(res) {
        if(res!='1') {
            alert(res);
        }
    }, "json");
}

function buildShareTitle() {
    var array = ["超值", "值", "爽", "划算", "赞", "便宜"];
    var random = parseInt(Math.random()*array.length);

    return array[random];
}

