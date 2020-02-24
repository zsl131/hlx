$(function() {
    $(".order-day").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        maxDate: curDate(),
//        skinCell:'jedatered',
        format: 'YYYY-MM-DD'
    });

    $('[data-toggle="tooltip"]').tooltip();
});
