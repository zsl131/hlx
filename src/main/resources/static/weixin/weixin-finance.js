$(function() {
    //console.log("--=-=-=---")
    $("input[name='storeSn']").change(function() {
        var name = $(this).parent("span").find("label").html();
        //console.log($(this).val())
        //console.log(name)
        $("input[name='storeName']").val(name);
    })

    $("input[name='cateName']").focus(function() {
        var personalType = $("input[name='personalType']").val(); //人员类型
        var html = '<div class="category-list-div">';
        $(".all-category").find("span").each(function() {
            html += '<button class="btn btn-default" style="margin:5px 3px;" onclick="clickCategory(this)" cateId="'+$(this).attr("cateId")+'">'+$(this).html()+'</button>';
        });
        html += '</div>';

        if(personalType=='2') {
            html += '<p><button class="btn btn-danger" onclick="addCategory()">添加新类别</button></p>';
        }


        var dialog = confirmDialog(html, "选择类别", function() {
            var choiceObj = $(dialog).find(".category-list-div").find("button.btn-primary")[0];
            //console.log(choiceObj);
            if(!choiceObj) {
                showDialog("请选择类别后再点确定");
            } else {
                choiceCate($(choiceObj));
                $(dialog).remove();
            }
        }, "static");
        //showDialog("-------");
    });

    $("input[name='cateName']").keyup(function() {
        var value = $(this).val();
        //console.log(value)
        findCate(value);
    })

    var checkSign = $("input[name='checkSign']").val();
    //console.log(checkSign)
    if(checkSign=='1') {
        var signPath = $("input[name='signPath']").val();
        //console.log(signPath)
        if(!signPath || signPath.indexOf("null")>=0 || signPath.indexOf("?")==0) {alert("请先设置电子签名"); window.location.href = "/wx/finance/sign";}
    }
})

function addCategory() {
    var html = '';
    html += '<div class="form-group">'+
                  '<div class="input-group">'+
                      '<div class="input-group-addon">类别名称</div>'+
                      '<input name="name" class="form-control" placeholder="输入类别名称"/>'+
                  '</div>'+
              '</div>';
    var dialog = confirmDialog(html, "添加新类别", function() {
        var name = $(dialog).find("input[name='name']").val();
        //console.log(choiceObj);
        if(!name) {
            showDialog("请输入类别名称");
        } else {
            $.post("/wx/finance/addCategory", {name: name}, function(res) {
                //console.log(res)
                if(!res.id || res.id == null) {showDialog("已经存在相似的名称，请更换后重新添加", "系统提示");}
                else {
                    //console.log(res);
                    var btnHtml = '<button class="btn btn-default" style="margin:5px 3px;" onclick="clickCategory(this)" cateId="'+res.id+'">'+res.name+'</button>';
                    $(".category-list-div").append(btnHtml);
                }
            }, "json");
            $(dialog).remove();
        }
    }, "static");
}

function clickCategory(obj) {
    $(obj).parents(".category-list-div").find("button.btn-primary").removeClass("btn-primary");
    $(obj).addClass("btn-primary");
}

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