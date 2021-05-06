$(function() {
    $(".finance-day").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        maxDate: curDate(),
//        skinCell:'jedatered',
        format: 'YYYY-MM-DD',
    });

    var curSn = $("input[name='storeSn']").val();
    $(".store-head").each(function() {
        var storeSn = $(this).attr("storeSn");
        //console.log(storeSn)
        if(storeSn==curSn) {$(this).addClass("weui-bar__item_on");}
    })

    $(".search-btn").click(function() {
        var day = $("input[name='financeDay']").val();
        console.log(day)
        window.location.href = "/wx/finance/summary?storeSn="+curSn+"&day="+day;
    })
})

function showDetail(obj) {
    var curSn = $("input[name='storeSn']").val();
    var day = $("input[name='financeDay']").val();
    var openid = $(obj).attr("openid");

    //console.log(curSn)
    //console.log(day)
    //console.log(openid)

    $.post("/wx/finance/queryDetail", {day: day, storeSn: curSn, openid: openid}, function(res) {
        //console.log(res);
        showContent(res);
    }, "json");
}

function showContent(data) {
    /*confirmDialog(html, "报账明细", function() {
    })*/
    var html = '<div>';
    var total = 0;
    for(var i=0;i<data.length;i++) {
        var detail = data[i];
        html += '<p style="width:100%; border-bottom: 1px #ddd solid; padding-bottom: 6px; margin-top: 8px;">['+detail.cateName+']<b>'+detail.title+'</b>（<b>'+detail.price+'</b>*<b>'+detail.amount+'</b>=<b>'+detail.totalMoney+' 元</b>）</p>';
        total += detail.totalMoney;
    }

    html += '<div>合计：￥<b style="color:#F00;">'+total+'</b> 元</div>';

    html += '</div>';

    showDialog(html, "报账明细（"+data.length+"条）");
}