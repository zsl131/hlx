function submitBag() {
    var sn = $("input[name='sn']").val();
    var name = $("input[name='name']").val();
    var phone = $("input[name='phone']").val();
    var money = parseFloat($("input[name='money']").val());

    if(!sn || sn.length<2) {alert("未检测到标记信息，请重新进入系统再试");}
    else if(!name || name.length<2) {alert("请认真输入顾客姓名");}
    else if(!phone || phone.length!=11) {alert("请认真输入顾客手机号码");}
    else if(!money || money<=0) {alert("请认真输入充值金额，不得小于0");}
    else {
        $(".submit-btn").attr("disabled", "disabled");
        $.post("/wx/moneybag/addBag", {sn: sn, name: name, phone: phone, money: money}, function(res) {
            if(res=='-1') {alert("无法创建会员信息，可能店铺信息出错");}
            else if(res=='1') {alert("操作成功"); window.location.href = "/wx/moneybag/index?sn="+sn;}
        }, "json")
    }
}

