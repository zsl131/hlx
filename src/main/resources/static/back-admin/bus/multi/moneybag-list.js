function onCharge(obj) {
    var objId = $(obj).attr("objId");
    var objName = $(obj).attr("objName");
    var money = parseFloat($(obj).attr("money"));

    var html = '<p>顾客姓名：<b>'+objName+'</b>，当前余额：<b>'+money+'</b> &nbsp;元</p>'+
            '<div class="form-group ">'+
                '<div class="input-group ">'+
                    '<div class="input-group-addon">充值数量</div>'+
                    '<input type="text" name="amount" class="form-control" placeholder="请输入充值金额，单位元"/>'+
                '</div>'+
            '</div>'+
            '<div class="form-group ">'+
                '<div class="input-group ">'+
                    '<div class="input-group-addon">充值原因</div>'+
                    '<input type="text" name="reason" class="form-control" placeholder="请输入原因"/>'+
                '</div>'+
            '</div>';
    var myDialog = confirmDialog(html, "会员充值", function() {
        var amount = parseFloat($(myDialog).find("input[name='amount']").val());
        var reason = $(myDialog).find("input[name='reason']").val();

        console.log(money, amount);
        console.log(money<amount)

        if(isNaN(amount) || amount==0 || (money+amount)<0) {
            showDialog("请输入充值金额，负数则为扣款，扣除后不可为负数", '<i class="fa fa-info-circle"></i> 系统提示');
        } else if($.trim(reason)=='') {
            showDialog("请输入充值原因", '<i class="fa fa-info-circle"></i> 系统提示');
        } else {
            $.post("/admin/moneybag/plus", {amount:amount, reason:reason, bagId: objId}, function(res) {
                if(res=='1') {
                    showDialog("<i class='fa fa-check'></i> 充值已完成", '<i class="fa fa-info-circle"></i> 系统提示');
                    window.location.reload();
                } else {
                    showDialog("<i class='fa fa-close'></i> 充值失败", '<i class="fa fa-info-circle"></i> 系统提示');
                }
            }, "json");
            $(myDialog).remove();
        }
    });
}