$(function() {
    $("#month").jeDate({
        isinitVal:true, //初始化日期
        festival: false, //显示农历
        isClear:false,
        maxDate: curDate(),
        format: 'YYYY-MM'
    });

    $("#submitClockinBtn").click(function() {
        var month = $("#month").val();
        window.location.href = "/admin/clockin/index?month="+month;
    });

    $(".data-ul li").hover(function() {
        $(this).addClass("hover");
    }, function() {
    $(this).removeClass("hover");
    })
});