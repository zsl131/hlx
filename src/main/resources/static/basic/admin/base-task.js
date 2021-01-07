$(function() {
    $("input[type='radio'][name='type']").click(function() {
        var val = $(this).val();

        showRule(val);
        //console.log(val, "----------------")
    });

    /*$("input[name='startTime']").jeDate({
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        //maxDate: curDate(),
//        skinCell:'jedatered',
        format: 'YYYY-MM-DD'
    });*/
    /*jeDate("input[name='startTime']",{
        isinitVal:true,
        festival: true,
        format: 'YYYY-MM-DD hh:mm:ss'
    });*/
});

function showRule(type) {
    //console.log("---------")
    $(".need-hide").css("display", "none");
    $((".type-"+type)).each(function() {
        $(this).css("display", "block");
    })
}