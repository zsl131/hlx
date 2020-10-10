function checkBag() {
    var sn = $("input[name='sn']").val();
    var phone = $("input[name='phone']").val();
    //var password = $("input[name='password']").val();

    if(!sn || sn.length<2) {alert("未检测到标记信息，请重新进入系统再试");}
    else if(!phone || phone.length!=11) {alert("请认真输入顾客手机号码");}
    //else if(!password || password.length!=4) {alert("请认真输入密码，4位纯数字");}
    else {
        $.post("/weixin/moneybag/queryBag", {phone: phone}, function(bag) {
            if(!bag.id) {alert(bag.name);}
            else {
                var name = bag.name; var surplus = bag.money; $("#bagName").html("<p class='old'>顾客姓名： "+name+"，剩余：<b style='color:#F00'>"+surplus+"</b> 元。</p>");
                $("input[name='bagId']").val(bag.id);
                $("input[name='surplus']").val(surplus);
                //$(".submit-btn").removeAttr("disabled");
                $("input[name='money']").removeAttr("disabled");
            }
        }, "json");
    }
}

function setMoney() {
    var surplus = parseFloat($("input[name='surplus']").val());
    var money = parseFloat($("input[name='money']").val());
    if(isNaN(money) || money<=0) {alert("请输入大于0的金额")}
    else if(money>surplus) {alert("消费金额不能大于账户余额【"+surplus+"】");}
    else {
        var html = $("#bagName").find("p.old").html();
        $("#bagName").html("<p class='old'>"+html+"</p>消费<b style='color:#00F;'>"+money+"</b>元后，余<b style='color:#F00'>"+(surplus-money)+"</b> 元。");
        $("input[name='password']").removeAttr("disabled");
        $("input[name='realMoney']").val(money);
    }
}

function changePwd() {
    var pwd = $("input[name='password']").val();
    //console.log(pwd)
    if(pwd.length==4) {
        $(".submit-btn").removeAttr("disabled");
    } else {
        $(".submit-btn").attr("disabled", "disabled");
    }
}

function submitDetail() {
    var sn = $("input[name='sn']").val();
    var bagId = $("input[name='bagId']").val();
    var surplus = parseFloat($("input[name='surplus']").val());
    var money = parseFloat($("input[name='realMoney']").val()); //真实金额
    var pwd = $("input[name='password']").val();
    console.log(money)
    if(!bagId) {alert("请先检索会员信息");}
    else if(isNaN(money) || money<=0) {alert("请输入大于0的金额")}
    else if(money>surplus) {alert("消费金额不能大于账户余额【"+surplus+"】");}
    else if(pwd.length!=4) {alert("请输入密码");}
    else {
        $(".submit-btn").attr("disabled", "disabled");
        $.post("/weixin/moneybag/addDetail", {sn: sn, bagId: bagId, money: money, password: pwd}, function(res) {
            if(res=='-1') {alert("没有找到会员信息"); setDisabled(false); }
            else if(res=='-2') {alert("没有找到店铺信息"); setDisabled(false); }
            else if(res=='-3') {alert("账户余额不足"); setDisabled(false); }
            else if(res=='-10') {alert("密码不正确"); setDisabled(false); }
            else {alert("扣款成功"); window.location.href = "/weixin/moneybag/index?sn="+sn;}
        }, "json");
    }
}

function setDisabled(disabled) {
    if(disabled) {
        $(".submit-btn").attr("disabled", "disabled");
    } else {
        $(".submit-btn").removeAttr("disabled");
    }
}