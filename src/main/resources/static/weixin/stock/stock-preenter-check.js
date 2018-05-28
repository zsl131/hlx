var datasCookieName = "checkPreenterDatas";
$(function() {
    $(".single-goods").click(function() {
        checkSingleGoods(this);
    });

    reloadDatasFromCookie();
    checkAmountStyle();

    $(".match-span").click(function() {
        $(".has-matched").each(function() {
            var display = $(this).css("display");
            if(display == 'none') {
                $(this).css("display", "block");
            } else {
                $(this).css("display", "none");
            }
        });
    })
});

function checkSingleGoods(obj) {
    var goodsId = $(obj).attr("goodsId");
    var goodsName = $(obj).find(".goodsName").html();
    var allowAmount = $(obj).find(".allow-amount").html();
    var amountTrueOld = $(obj).find(".amount-true").html();
    var unit = $(obj).attr("unit");

    var html = '<div class="dialog-html-div">'+
            '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">采购：</div><input readonly="readonly" name="allowAmount" type="text" class="form-control" value="'+allowAmount+'" /><div class="input-group-addon">'+unit+'</div></div></div>'+

            '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">实到：</div>'+
            '<input name="amountTrue" type="number" class="form-control" placeholder="输入'+goodsName+'实到数量" value="'+amountTrueOld+'"/>' +
            '<div class="input-group-addon">'+unit+'</div></div></div>'+
            '<div><button class="btn btn-danger" onclick="goodsFull(this)"><b class="fa fa-check-circle"></b> '+goodsName+'到齐，就点这里</button></div>'+
            '</div>';
    var applyDialog = confirmDialog(html, "<b class='fa fa-eye'></b> 收货核对【"+goodsName+"】", function() {
        var amountTrue = parseInt($(applyDialog).find("input[name='amountTrue']").val()); //实到
        amountTrue = (amountTrue<0 || isNaN(amountTrue))?0:amountTrue;
        $(obj).find(".amount-true").html(amountTrue);
        //showToast("申购成功，等待审核");
        applyDialog.remove();
        checkAmountStyle();
    });
}

function reloadDatasFromCookie() {
    var datas = getCookie(datasCookieName);
    //console.log("======="+datas);
    if(datas != null && datas !== '') {
        var array = datas.split("_");
        for(var i=0;i<array.length;i++) {
            if(array[i] != '') {
                var goodsId = array[i].split("-")[0];
                var amount = array[i].split("-")[1];
                $(".single-goods[goodsId='"+goodsId+"']").find(".amount-true").html(amount);
            }
        }
    }
}

function checkAmountStyle() {
    var matchCount = 0;
    var datas = '';
    $(".single-goods").each(function() {
        var allowAmount = parseInt($(this).find(".allow-amount").html());
        allowAmount = (allowAmount<0 || isNaN(allowAmount))?0:allowAmount;
        var amountTrue = parseInt($(this).find(".amount-true").html());
        amountTrue = (amountTrue<0 || isNaN(amountTrue))?0:amountTrue;
        var goodsId = $(this).attr("goodsId");
        if(allowAmount!=amountTrue) {
            $(this).addClass("no-match").removeClass("has-matched");
        } else {
            $(this).removeClass("no-match").addClass("has-matched");
            $(this).css("display", "none");
            matchCount ++;
        }
        datas+= goodsId+"-"+amountTrue+"_";
    })
    //console.log(datas);
    setCookie(datasCookieName, datas, 60*8); //存8小时
    $(".match-ok-b").html(matchCount);
}

function goodsFull(obj ) {
    var pObj = $(obj).parents(".dialog-html-div");
    var allowAmount = $(pObj).find("input[name='allowAmount']").val();
    $(pObj).find("input[name='amountTrue']").val(allowAmount);
}

function submitDatas() {
    var datas = getCookie(datasCookieName);
    var totalKindCount = parseInt($(".total-kind-count").html());
    var matchedKindCount = parseInt($(".match-ok-b").html());
    var lastKindCount = totalKindCount - matchedKindCount;

    var batchNo = $("#batchNo").val();
//    console.log(batchNo+"===="+totalKindCount+"===="+matchedKindCount+"====="+lastKindCount);
//    console.log(datas);

    var html = '<div class="dialog-html-div">'+
                ((lastKindCount==0)?'<h4><b class="fa fa-check"></b> 物品已全部核对完成！</h4>':'<h4><b class="fa fa-warning"></b> 还有<b style="color:#F00">【'+lastKindCount+'】</b>种物品未核对或存在数量不一致！</h4>')+
                '<p>此操作不可逆，确定提交吗？</p></div>';
    var applyDialog = confirmDialog(html, "<b class='fa fa-question-circle'></b> 核对完成，提交入库", function() {
        //showToast("申购成功，等待审核");
        $.post("/wx/stock/preenter/postCheckGoods", {batchNo: batchNo, datas: datas}, function(res) {
            if(res=='1') {
                showToast("提交成功");
                reloadWin();
                delCookie(datasCookieName);
            }
        }, "json");
        applyDialog.remove();
    });
}