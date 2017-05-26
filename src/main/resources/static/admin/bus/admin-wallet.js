$(function() {
    $(".single-wallet").click(function() {
        var phone = $(this).attr("phone");
        var openid = $(this).attr("openid");
        if(!phone || phone=='') {
            showDialog("未绑定手机号码，不能手动充值", '<i class="fa fa-info-circle"></i> 系统提示');
        } else {
            var html = '<div class="form-group ">'+
                            '<div class="input-group ">'+
                                '<div class="input-group-addon">充值类型</div>'+
                                '<div class="form-control">'+
                                    '<input type="radio" name="type" value="1" id="status_1" /><label for="status_1">积分&nbsp;&nbsp;</label>'+
                                    '<input type="radio" name="type" value="2" id="status_0" /><label for="status_0">现金&nbsp;&nbsp;</label>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group ">'+
                            '<div class="input-group ">'+
                                '<div class="input-group-addon">充值数量</div>'+
                                '<input type="text" name="amount" class="form-control" placeholder="请输入充值数量，现金单位元"/>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group ">'+
                            '<div class="input-group ">'+
                                '<div class="input-group-addon">充值原因</div>'+
                                '<input type="text" name="reason" class="form-control" placeholder="请输入原因"/>'+
                            '</div>'+
                        '</div>';
            var myDialog = confirmDialog(html, '<i class="fa fa-plug"></i> 手动充值', function() {
                var type = $(myDialog).find("input[name='type']:checked").val();
                var amount = parseFloat($(myDialog).find("input[name='amount']").val());
                var reason = $(myDialog).find("input[name='reason']").val();

                if(!type) {
                    showDialog("请选择充值类型", '<i class="fa fa-info-circle"></i> 系统提示');
                } else if(isNaN(amount) || amount<=0) {
                    showDialog("请输入充值数量", '<i class="fa fa-info-circle"></i> 系统提示');
                } else if($.trim(reason)=='') {
                    showDialog("请输入充值原因", '<i class="fa fa-info-circle"></i> 系统提示');
                } else {
                    $.post("/admin/wallet/plus", {type:type, amount:amount, reason:reason, phone:("1"==type)?openid:phone}, function(res) {
                        if(res=='1') {
                            showDialog("<i class='fa fa-check'></i> 充值已完成", '<i class="fa fa-info-circle"></i> 系统提示');
                        } else {
                            showDialog("<i class='fa fa-close'></i> 充值失败", '<i class="fa fa-info-circle"></i> 系统提示');
                        }
                    }, "json");
                    $(myDialog).remove();
                }
            });
        }
    });
});