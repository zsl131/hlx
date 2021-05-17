var byMonth;
$(function() {
    byMonth = $("input[name='byMonth']").val();
    var format = byMonth=='1'?"YYYY-MM":"YYYY-MM-DD";
    //console.log(format)

    $(".finance-day").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        maxDate: curDate(),
//        skinCell:'jedatered',
        format: format,
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
        if(byMonth=='1') {
            window.location.href = "/wx/finance/summaryByMonth?storeSn="+curSn+"&month="+day;
        } else {
            window.location.href = "/wx/finance/summary?storeSn="+curSn+"&day="+day;
        }
    })

    var totalMoneyAll = 0;
    $(".single-money").each(function() {
        var money = parseFloat($(this).html());
        totalMoneyAll += money;
    })
    totalMoneyAll = totalMoneyAll.toFixed(2);
//    console.log(numberToUpper(totalMoneyAll));
//    console.log(toThousands(totalMoneyAll));
console.log(totalMoneyAll)
    $(".total-money").html(totalMoneyAll);
    $(".total-money-cny").html(numberToUpper(totalMoneyAll));
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

function showDetailByMonth(obj) {
    var curSn = $("input[name='storeSn']").val();
    var month = $("input[name='financeDay']").val();
    var cateId = $(obj).attr("cateId");

    //console.log(curSn)
//    console.log(month)
//    console.log(cateId)

    $.post("/wx/finance/queryDetailByMonth", {month: month, storeSn: curSn, cateId: cateId}, function(res) {
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
        html += '<div style="width:100%; border-bottom: 1px #ddd solid; margin-top: 8px;">'+
            '['+detail.cateName+']<b>'+detail.title+'</b>（<b>'+detail.price+'</b>*<b>'+detail.amount+'</b>=<b>'+detail.totalMoney+' 元</b>）';

        if(byMonth=='1') {
            html += '<p style="color:#888; font-size:12px; margin-top: 2px; padding-bottom:0px;">'+detail.username+' 申请于 '+detail.createDay+'</p>';
        }

        html += '</div>';

        total += detail.totalMoney;
    }

    html += '<p style="margin-top:10px;">合计：￥<b style="color:#F00;">'+total+'</b> 元</p>';

    html += '</div>';

    showDialog(html, "报账明细（"+data.length+"条）");
}