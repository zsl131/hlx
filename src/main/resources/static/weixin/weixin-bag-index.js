function onFilter(path) {
    var sn = $("input[name='sn']").val();
    var day = $("input[name='day']").val();

    if(!sn || sn.length<2) {alert("未检测到标记信息，请重新进入系统再试");}
    else if(!day || day.length!=8) {alert("请认真输入日期，格式如：yyyyMMdd");}
    else {
        window.location.href = "/weixin/moneybag/"+(path?path:"index")+"?sn="+sn+"&day="+day;
    }
}

