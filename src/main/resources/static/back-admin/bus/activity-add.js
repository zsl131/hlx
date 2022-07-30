$(function() {
    $("#startTime").jeDate({
        isinitVal:true, //初始化日期
        festival: false, //显示农历
        isClear:false,
        minDate: curDate(),
//        skinCell:'jedatered',
        format: 'YYYY-MM-DD hh:mm:ss'
    });

    $("#endTime").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        initAddVal:[1],
        minDate: $.nowDate(1),
        format: 'YYYY-MM-DD hh:mm:ss'
    });
});
