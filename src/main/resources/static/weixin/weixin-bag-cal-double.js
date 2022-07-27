function onCal() {
    var html = '<div class="dialog-html-div charge-div"><div class="form-group form-group-lg">'+
            '<input name="phone" type="number" pattern="[0-9]*" class="form-control" onKeyup="changeMoney(this)" placeholder="输入消费金额"/>' +
            '</div><div class="form-group form-group-lg">'+
            '</div>'+
            '<div class="row">'+
            '<div class="col-md-6 col-sm-6 col-xs-6"><p>充<b>两倍</b>享半价：</p><div class="charge2"><p>充值金额：<b style="color:#F00" class="value1">0</b></p><p>餐费金额：<b style="color:#F00" class="value2">0</b></p><p>合计需支付：<b style="color:#F00" class="value3">0</b></p></div></div>'+
            '<div class="col-md-6 col-sm-6 col-xs-6"><p>充<b>四倍</b>享免费：</p><div class="charge4"><p>充值金额：<b style="color:#F00" class="value1">0</b></p><p>餐费金额：<b style="color:#F00" class="value2">0</b></p><p>合计需支付：<b style="color:#F00" class="value3">0</b></p></div></div>'+
            '</div>'+
            '<div>注意：消费金额<span style="color:#F00">不包含押金</span>等金额</div>'+
            '</div>';
    var myDialog = confirmDialog(html, "<i class='fa fa-info'></i> 会员计算器", function() {

    });
}

function changeMoney(obj) {
    var money = parseFloat($(obj).val());
    var obj2 = $(obj).parents(".charge-div").find(".charge2");
    $(obj2).find(".value1").html((money*2).toFixed(2));
    $(obj2).find(".value2").html((money/2).toFixed(2));
    $(obj2).find(".value3").html((money*2.5).toFixed(2));

    var obj4 = $(obj).parents(".charge-div").find(".charge4");
    $(obj4).find(".value1").html((money*4).toFixed(2));
    $(obj4).find(".value2").html(0);
    $(obj4).find(".value3").html((money*4).toFixed(2));
    //console.log(money)
}