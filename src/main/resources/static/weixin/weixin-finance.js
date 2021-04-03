$(function() {
    //console.log("--=-=-=---")
    $("input[name='storeSn']").change(function() {
        var name = $(this).parent("span").find("label").html();
        //console.log($(this).val())
        //console.log(name)
        $("input[name='storeName']").val(name);
    })

    $("input[name='cateName']").keyup(function() {
        var value = $(this).val();
        //console.log(value)
        findCate(value);
    })

    var checkSign = $("input[name='checkSign']").val();
//    console.log(checkSign)
    if(checkSign=='1') {
        var signPath = $("input[name='signPath']").val();
        if(!signPath) {alert("请先设置电子签名"); window.location.href = "/wx/finance/sign";}
    }
})

function findCate(key) {
    var html = '';
    $(".single-cate").each(function() {
        var firstSpell = $(this).attr("firstSpell");
        var longSpell = $(this).attr("longSpell");
        var cateId = $(this).attr("cateId");
        var name = $(this).html();
//        console.log(firstSpell)
//        console.log(longSpell)
//        console.log(name)
//        console.log("---------------------")
        if(firstSpell.indexOf(key)>=0 || longSpell.indexOf(key)>=0 || name.indexOf(key)>=0) {
            html += buildSingleCate(firstSpell, longSpell, name, cateId);
        }
    });
    if(html) {
        $(".show-category").html(html);
        $(".show-category").css("display", "block");
    }
}

function buildSingleCate(firstSpell, longSpell, name, cateId) {
    return '<p class="single-cate-p" onClick="choiceCate(this)" cateId="'+cateId+'" firstSpell="'+firstSpell+'" longSpell="'+longSpell+'">'+name+'</p>';
}

function choiceCate(obj) {
    $("input[name='cateName']").val($(obj).html());
    $("input[name='cateId']").val($(obj).attr("cateId"));
    $(".show-category").css("display", "none");
}

function buildMoney() {
    //console.log("-------")
    var price = parseFloat($("input[name='price']").val());
    var amount = parseFloat($("input[name='amount']").val());
    var money = price * amount;
    if(isNaN(money)) {money = 0;}
    $(".total-money").html(money.toFixed(2));
}

function checkForm() {
    var storeName = $("input[name='storeName']").val();
    var cateId = $("input[name='cateId']").val();
    var title = $("input[name='title']").val();
    var price = parseFloat($("input[name='price']").val());
    var amount = parseFloat($("input[name='amount']").val());

    if(!storeName) {showDialog("请选择店铺", "系统提示"); return false;}
    else if(!cateId) {showDialog("请选择类别", "系统提示"); return false;}
    else if(!title) {showDialog("请输入名称", "系统提示"); return false;}
    else if(!price || price<=0) {showDialog("请认真输入单价", "系统提示"); return false;}
    else if(!amount || amount<=0) {showDialog("请认真输入数量", "系统提示"); return false;}
    $(".submit-btn").attr("disabled", "disabled");
    return true;
}