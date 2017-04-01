//分享到朋友圈
function share2Cirlce(title, link, imgUrl, sucFun, cancelFun) {
    wx.ready(function() {
        wx.onMenuShareTimeline({
            title: title,
            link: link,
            imgUrl: imgUrl,
            success: sucFun,
            cancel: cancelFun
        });
    });
}

//分享到朋友圈
function share2Friend(title, desc, link, imgUrl, sucFun, cancelFun) {
    wx.ready(function() {
        wx.onMenuShareAppMessage({
            title: title,
            desc:desc,
            link: link,
            imgUrl: imgUrl,
            success: sucFun,
            cancel: cancelFun
        });
    });
}

//选择图片
function chooseImages(count, sucFun) {
    wx.ready(function() {
        wx.chooseImage({
            count: count, // 默认9
            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
            success: sucFun
            /*success: function (res) {
                var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            }*/
        });
    });
}

function getLocation(isGPS, sucFun) {
    var type = isGPS?"wgs84":"gcj02";
    wx.getLocation({
        type: type, // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
        success: sucFun
        /*success: function (res) {
            var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
            var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
            var speed = res.speed; // 速度，以米/每秒计
            var accuracy = res.accuracy; // 位置精度
        }*/
    });
}

function openLocation(latitude, longitude, name, address, infoUrl) {
    wx.openLocation({
        latitude: latitude, // 纬度，浮点数，范围为90 ~ -90
        longitude: longitude, // 经度，浮点数，范围为180 ~ -180。
        name: name, // 位置名
        address: address, // 地址详情说明
        scale: 28, // 地图缩放级别,整形值,范围从1~28。默认为最大
        infoUrl: infoUrl // 在查看位置界面底部显示的超链接,可点击跳转
    });
}

function scanQRCode(needResult, sucFun) {
    wx.scanQRCode({
        needResult: needResult, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: sucFun
        /*success: function (res) {
            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
        }*/
    });
}