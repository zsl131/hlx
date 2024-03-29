$(function() {
    $("input[name='keyword']").blur(function() {onSearch(this)});

    $(".rebuild-obj").blur(function() {
        let money = parseFloat($(this).val());
        if(isNaN(money)) {money = 0;}
        const storeSn = $("input[name='curStoreSn']").val();
        const month = $("input[name='targetMonth']").val();
        //var preMonthMoney = $("input[name='preMonthMoney']").val();
        window.location.href = "/wx/business/showByBoss?storeSn="+storeSn+"&month="+month+"&preMonthMoney="+money;
    });
});

function onSearch(obj) {
    const key = $(obj).val();
    // doSearch(obj)
    // console.log(key);
    $(".single-detail-div").removeAttr("hasKey");

    if(key) {
        $(".income-div").each(function() {
            $(this).css("display", "none");
        })
        $(".no-data").parents(".single-detail-div").css("display", "none");
    } else {
        $(".income-div").each(function() {
            $(this).css("display", "block");
        })
        $(".no-data").parents(".single-detail-div").css("display", "block");
        $(".single-detail-div").css("display", "block");
        $(".single-paid").css("display", "block");
    }

    let amount = 0;
    $(".single-paid").each(function () {
        const titleObj = $(this).find(".paid-title");
        const parentObj = $(this).parents(".single-detail-div");
        const title = $(titleObj).attr("fdTitle");
        if(key && title.indexOf(key)>=0) {
            $(titleObj).html(buildTitle(title, key));
            amount ++;
            $(this).css("display", "block");
            $(parentObj).css("display", "block");
            $(parentObj).attr("hasKey", "1");
        } else {
            if(key) {
                $(titleObj).html(title);
                $(this).css("display", "none");
                if(parentObj.attr("hasKey")!=='1') {
                    $(parentObj).css("display", "none")
                }
            }
        }
        if(!key) {
            $(titleObj).html(title);
        } else {

        }
    })
    $(".search-amount-b").html(amount);
}

function buildTitle(title, key) {
    //const array = title.split(key);
    return title.substring(0, title.indexOf(key)) + "<span style='color:#FFF; background:#F00; padding: 0 3px;'>"+key+"</span>" + title.substring(title.indexOf(key)+key.length, title.length);
}

/**
 * 保存
 * @param flag 0-只保存；1-保存并公布
 */
function saveData(flag) {
    console.log(flag)
    const dialog = confirmDialog("确定要"+(flag==='0'?"保存":"保存并公布")+"吗？", "操作提示", function() {
        //console.log("==========")
        const storeSn = $("input[name='curStoreSn']").val();
        const month = $("input[name='targetMonth']").val();
        let preMonthMoney = parseFloat($("input[name='preMonthMoney']").val());
        if(isNaN(preMonthMoney)) {preMonthMoney = 0;}
        //console.log(storeSn+"=="+month+"==="+preMonthMoney)
        $.post("/wx/business/saveDetail", {storeSn: storeSn, month: month, preMonthMoney: preMonthMoney, publishFlag: flag}, function(res) {
            //console.log(res);
            //alert(res)
            if(res=="1") {
                alert("提交成功");
                window.location.reload();
            } else {
                alert(res);
            }
        }, "json");
        $(dialog).remove();
        showDialog("数据正在提交...这可能要花半分钟", "操作提示");
    });
}