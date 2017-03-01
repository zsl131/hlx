
//显示Toast，time秒后，自动关闭
function showToast(str, time) {
    var idStr = "myToast_"+parseInt(Math.random()*100000000);
    var html = '<div id="'+idStr+'" style="">' +
                    '<div class="weui-mask_transparent"></div>' +
                    '<div class="weui-toast">' +
                        '<i class="weui-icon-success-no-circle weui-icon_toast"></i>' +
                        '<p class="weui-toast__content">'+str+'</p>' +
                    '</div>' +
                '</div>';


    $(html).appendTo("body");
    var timeCount = parseInt(time);
    timeCount = (!timeCount || timeCount<=0 || isNaN(timeCount))?3:timeCount;
    var myInterval = setInterval(function() {
        if(timeCount--<=0) {
            //alert($(("#"+idStr)).html());
            $(("#"+idStr)).css("display", "none");
            $(("#"+idStr)).remove();
            clearInterval("myInterval");
        }
    }, 1000);
}

//显示加载Toast，返回该Toast的id
function showLoadToast(str) {
    var idStr = "myToast_"+parseInt(Math.random()*100000000);
    str = !str?"数据加载中":str;
    var html = '<div id="'+idStr+'" style="display:none;">'+
                    '<div class="weui-mask_transparent"></div>' +
                    '<div class="weui-toast">' +
                        '<i class="weui-loading weui-icon_toast"></i>' +
                        '<p class="weui-toast__content">'+str+'</p>' +
                    '</div>' +
                '</div>';

    $(html).appendTo("body");
    return idStr;
}

//关闭加载的Toast
function hideLoadToast(idStr) {
    $(("#"+idStr)).remove();
}