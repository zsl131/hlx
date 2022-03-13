$(function() {
    $("input[name='keyword']").blur(function() {onSearch(this)});

    $(".rebuild-obj").blur(function() {
        let money = parseFloat($(this).val());
        console.log(money, "----------")
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
    //console.log(key);

    let amount = 0;
    $(".paid-title").each(function () {
        const title = $(this).attr("fdTitle");
        if(key && title.indexOf(key)>=0) {
            $(this).html(buildTitle(title, key));
            amount ++;
        } else {
            $(this).html(title);
        }
        if(!key) {
            $(this).html(title);
        }
    })
    $(".search-amount-b").html(amount);
}

function buildTitle(title, key) {
    //const array = title.split(key);
    return title.substring(0, title.indexOf(key)) + "<span style='color:#F00; font-weight: bold;'>"+key+"</span>" + title.substring(title.indexOf(key)+key.length, title.length);
}

/*
function doSearch(obj) {

    lockFlag = 1;
    const keyword = $(obj).val();
    console.log(keyword);
    lockFlag = 0;
}*/
