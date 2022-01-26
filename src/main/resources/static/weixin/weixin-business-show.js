$(function() {
    $(".income-money").each(function() {
        var money = parseFloat($(this).html());
        $(this).html(money.toFixed(2) + " 元");
    })

    $(".income-show").click(function() {
        var path = $(this).attr("ticketPath");
        if(path) {
            showDialog("<img src='"+path+"' style='width: 100%'/>", "入账单")
        } else {
            showDialog("没有上传入账单", "入账单")
        }
    })

    $(".content-div").find("li.single-li").click(function() {
        var targetCls = $(this).attr("targetCls");
        $(".content-div").find("li.active").removeClass("active");
        $(this).addClass("active");

        $(".target-cls").css("display", "none");
        $((".")+targetCls).css("display", "block");
    })

    $(".upper-money-cny").each(function() {
        var money = parseFloat($(this).attr("money")).toFixed(2);
        $(this).html(numberToUpper(money));
    });
})
