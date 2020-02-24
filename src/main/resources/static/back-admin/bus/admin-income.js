$(function() {
    $(".income-day").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        maxDate: curDate(),
//        skinCell:'jedatered',
        format: 'YYYYMMDD'
    });

    $(".money-input").keyup(function() {
        this.value=this.value.replace(/[^\d.]/g,'');
        resetTotalMoney();
    });
    resetTotalMoney();
});

function resetTotalMoney() {
    var totalMoney = 0;
    $(".money-input").each(function() {
        totalMoney += parseFloat($(this).val());
    });

    $("input[name='totalMoney']").val(totalMoney+" 元");
}
