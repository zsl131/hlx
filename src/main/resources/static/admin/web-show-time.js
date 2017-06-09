$(function() {
    $(".show-seconds").each(function() {
        var seconds = parseInt(parseInt($(this).html())/1000);
        $(this).html(buildTime(seconds));
    });

    $(".show-seconds-now").each(function() {
        //var seconds = parseInt($(this).html());
        //$(this).html(buildTime(parseInt((new Date().getTime()-seconds)/1000)));
        rebuildCon($(this));
    });

    $(".show-money").each(function() {
        var money = parseInt($(this).html());
        $(this).html(money/100 + " 元");
    });
});

function rebuildCon(obj) {
    var seconds = parseInt($(obj).html());
    $(obj).html(buildTime(parseInt((new Date().getTime()-seconds)/1000)));
    setInterval(function() {
        $(obj).html(buildTime(parseInt((new Date().getTime()-seconds)/1000)));
    }, "1000");
}

function buildTime(seconds) {
    if(seconds<60) {
        return seconds + "秒";
    } else if(seconds>=60 && seconds<3600){
        var min = parseInt(seconds/60);
        return min+"分"+(seconds%60)+"秒";
    } else {
        var hour = parseInt(seconds/3600);
        var surplus = seconds%3600;
        var min = parseInt(surplus/60);
        return hour+"时"+min+"分"+(surplus%60)+"秒";
    }
}