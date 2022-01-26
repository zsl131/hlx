$(function () {
    $(".rebuild-obj").keyup(function() {
        rebuildMoney();
    });
    rebuildMoney();

    $(".submit-btn").click(function() {
        checkForm();
    })
})

function checkForm() {
    var dialog = confirmDialog("确定提交吗？提交成功后将展示给各股东！", "操作提示", function() {
        $("#dataForm").submit();
    })
}

function rebuildMoney() {
//    console.log("===========")
    var gotMoney = parseFloat($("input[name='gotMoney']").val()); //本月营收
    var payMoney = parseFloat($("input[name='paidMoney']").val()); //本月支出
//    var surplusMoney = parseFloat($("."))
    var settleMoney = parseFloat($("input[name='settleMoney']").val()); //本月分账
    var preMonthMoney = parseFloat($("input[name='preMonthMoney']").val()); //上月结余
    if(isNaN(gotMoney)) {gotMoney = 0;}
    if(isNaN(payMoney)) {payMoney = 0;}
    if(isNaN(settleMoney)) {settleMoney = 0;}
    if(isNaN(preMonthMoney)) {preMonthMoney = 0;}
//    console.log(gotMoney, payMoney)
//    console.log(settleMoney, preMonthMoney)
    //本月结余=本月营收-本月支出-本月分账+上月结余
    var curSurplusMoney = (gotMoney - payMoney - settleMoney + preMonthMoney).toFixed(2); //本月结余即账面余额
    var showObj = $(".cur-surplus-money");
    if(curSurplusMoney==0) {
        $(showObj).css("color", "#000");
    } else if(curSurplusMoney>0) {
        $(showObj).css("color", "blue");
    } else if(curSurplusMoney<0) {
        $(showObj).css("color", "#F00");
    }
    $(".cur-surplus-money").html(curSurplusMoney);
}