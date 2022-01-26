$(function() {
    $(".search-btn").click(function() {
        var keyword = $("input[name='keyword']").val();
//        console.log(keyword)
        if(!keyword) {
            showDialog("请输入昵称或电话", "系统提示")
        } else {
            $.post("/admin/financePersonal/queryAccount", {query: keyword}, function(res) {
                console.log(res);
                showAccount(res);
            }, "json")
        }
    })

//console.log("--------------")
    $("input[name='storeSn']").change(function() {
        var name = $(this).parent("span").find("label").html();
        $("input[name='storeName']").val(name);
    })

    $("input[name='partStore']").change(function() {
//        console.log($(this).val());
        /*var name = $(this).parent("span").find("label").html();
        $("input[name='storeName']").val(name);*/
        rebuildPartStore();
    })
    $("input[name='cashStore']").change(function() {
    //        console.log($(this).val());
            /*var name = $(this).parent("span").find("label").html();
            $("input[name='storeName']").val(name);*/
            rebuildCashStore();
        })
    initPartStore();
})

function initPartStore() {
    var sns = $("input[name='partStores']").val();
    sns.split(";").map((item) => {
//        console.log(item)
        $("input[name='partStore'][value='"+item+"']").attr("checked", true);
    })

    var csns = $("input[name='cashStores']").val();
        csns.split(";").map((item) => {
    //        console.log(item)
            $("input[name='cashStore'][value='"+item+"']").attr("checked", true);
        })
}

function rebuildCashStore() {
    var res = "";
    $("input[name='cashStore']:checked").each(function() {
        var storeSn = $(this).val();
        res += storeSn+";";
        console.log($(this).val());
//        console.log($(this).attr("checked"))
        var name = $(this).parent("span").find("label").html();
        $("input[name='storeName']").val(name);
    })
    $("input[name='cashStores']").val(res);
}

function rebuildPartStore() {
    var res = "";
    $("input[name='partStore']:checked").each(function() {
        var storeSn = $(this).val();
        res += storeSn+";";
        console.log($(this).val());
//        console.log($(this).attr("checked"))
        var name = $(this).parent("span").find("label").html();
        $("input[name='storeName']").val(name);
    })
    $("input[name='partStores']").val(res);
}

function checkData() {
    var name = $("input[name='name']").val();
    var phone = $("input[name='phone']").val();
    var type = $("input[name='type']:checked").val();
    var accountId = $("input[name='accountId']").val();

    if(!name) {showDialog("请输入姓名", "系统提示"); return false;}
    else if(!phone) {showDialog("请输入电话", "系统提示"); return false;}
    else if(!type) {showDialog("请选择类别", "系统提示"); return false;}
    else if(!accountId) {showDialog("请选择对应微信用户", "系统提示"); return false;}
    else {return true;}

//    console.log("name", name)
//    console.log("phone", phone)
//    console.log("type", type)
//    console.log("account", accountId)
}

function showAccount(list) {
    if(!list || list.length<=0) {
        $(".show-account").html("<b class='red'>未检索到微信用户信息，请更换关键词</b>")
    } else {
        var html = '';
        for(var i=0;i<list.length; i++) {
            var account = list[i];
            html += '<span class="btn btn-default" onclick="choiceAccount(this)" openid="'+account.openid+'" accountId="'+account.id+'" nickname="'+account.nickname+'" style="margin:8px;"><img style="width:18px;" src="'+account.headimgurl+'"/>'+account.nickname+'</span>';
        }
        $(".show-account").html(html);
    }
}

function choiceAccount(obj) {
    $(".show-account").find(".btn-primary").removeClass("btn-primary");
    $(obj).addClass("btn-primary");

    var accountId = $(obj).attr("accountId");
    var openid = $(obj).attr("openid");
    var nickname = $(obj).attr("nickname");
    $("input[name='accountId']").val(accountId);
    $("input[name='openid']").val(openid);
    $("input[name='nickname']").val(nickname);
}