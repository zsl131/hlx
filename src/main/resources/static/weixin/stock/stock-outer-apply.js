var dataCookieName = "apply-outer-cookie-name";
var isAdd;
var batchNo;
$(function() {
    batchNo = $("input[name='batch-no']").val();
    isAdd = (batchNo==null || batchNo==''); //是否为添加
    $(".single-location-div").click(function() {
        setShowStatus(this);
    });
//    console.log(getCookie(dataCookieName));
    resetDatas(); //重新打开时自动取Cookie中的数据加载
})

function addApply(obj) {
    var goodsId = $(obj).attr("goodsId");
    var goodsName = $(obj).attr("goodsName"); var unit = $(obj).attr("unit");
    var applyCount = $(obj).find(".applyCount").html();

    var html = '<div class="dialog-html-div">'+
                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">出库</div><input name="amount" type="number" class="form-control text-center" placeholder="输入数量" value="'+applyCount+'"/><div class="input-group-addon">'+unit+'</div></div></div>' +
                '</div>';
    var applyDialog = confirmDialog(html, "<b class='fa fa-plus'></b> 出库【"+goodsName+"】", function() {
        var amount = parseInt($(applyDialog).find("input[name='amount']").val());
        var curAmount = parseInt($(obj).parents(".weui-panel__hd").find(".cur-amount").html());
        amount = (isNaN(amount) || amount==''||amount<=0)?0:amount;
        if(amount>curAmount) {
            amount = curAmount;
            $(applyDialog).find("input[name='amount']").val(curAmount);
            showDialog("<b class='fa fa-warning'></b> 当前库存为【"+curAmount+"】"+unit+"，出库数量最多只能为【"+curAmount+"】"+unit, "<b class='fa fa-warning'></b> 系统提示");
        }
        modifyBtnStyle(obj, amount);
        $(obj).find(".applyCount").html(amount);
        rebuildCount();
        $(applyDialog).remove();

        buildApplyData();
    });

    $(applyDialog).find("input[name='amount']").focus();
}

function modifyBtnStyle(obj, amount) {
    if(amount>0) {
        $(obj).removeClass("btn-info").addClass("btn-danger");
        $(obj).find("b.fa-plus").removeClass("fa-plus").addClass("fa-pencil");
        $(obj).find(".applyCount").html(amount);
    } else {
        $(obj).removeClass("btn-danger").addClass("btn-info");
        $(obj).find("b.fa-pencil").removeClass("fa-pencil").addClass("fa-plus");
    }
}

function setShowStatus(obj) {
    var show = $(obj).attr("show");
    var locationType = $(obj).attr("locationType");
    if(show=='1') { //如果是1则修改为0
        $(obj).attr("show", "0");
        $(obj).find(".operator-b").addClass("fa-angle-right").removeClass("fa-angle-down");
        $((".goods-list-"+locationType)).slideUp(300);
    } else {
        $(obj).attr("show", "1");
        $(obj).find(".operator-b").addClass("fa-angle-down").removeClass("fa-angle-right");
        $((".goods-list-"+locationType)).slideDown(300);
    }
}

function rebuildCount() {
    var totalCount = 0;
    $(".single-location-div").each(function() {
        var thisObj = $(this);
        var targetId = "goods-list-"+$(this).attr("locationType");
        //alert(targetId);
        var singleCount = 0;
        $(("."+targetId)).find(".applyCount").each(function() {
            singleCount += parseInt($(this).html());
        });
        $(thisObj).find(".category-count").html(singleCount);
        totalCount += singleCount;
    });

    $(".total-count").html(totalCount);
}

function buildApplyData() {
    var datas = "";
    $(".single-location-div").each(function() {
        var thisObj = $(this);
        var locationType = $(thisObj).attr("locationType");
        //datas += locationSpe+locationType+locationSpe;
        //goods-list-1
        $((".goods-list-"+locationType)).find(".apply-href").each(function() {
            var amount = parseInt($(this).find(".applyCount").html());
//            if(amount>0) {
                datas += $(this).attr("goodsId")+"-"+amount+"_";
//            }
        });
//        datas+="|";
    });

    console.log(datas);
    setCookie(dataCookieName, datas, 5*60); //存5小时
}

function resetDatas() {
    var datas = getCookie(dataCookieName);
    if(!isAdd) {datas = $("input[name='batch-no']").attr("datas");}
    if(datas != null && datas != '') {
        var array = datas.split("_");
        var total = 0, total1 = 0, total2 = 0, total3 = 0;
        for(var i=0;i<array.length;i++) {
            if(array[i] != null && array[i] != '') {
                var goodsId = array[i].split("-")[0];
                var amount = parseInt(array[i].split("-")[1]);
                modifyBtnStyle($("a.apply-href[goodsId='"+goodsId+"']"), amount);
            }
        }
        buildCount();
    }
}

function buildCount() {
    var amount1=0, amount2=0,amount3=0;
    $(".goods-list-1").find(".applyCount").each(function() {
        amount1 += parseInt($(this).html());
    });
    $(".goods-list-2").find(".applyCount").each(function() {
        amount2 += parseInt($(this).html());
    });
    $(".goods-list-3").find(".applyCount").each(function() {
        amount3 += parseInt($(this).html());
    });
    $(".single-location-div[locationType='1']").find(".category-count").html(amount1);
    $(".single-location-div[locationType='2']").find(".category-count").html(amount2);
    $(".single-location-div[locationType='3']").find(".category-count").html(amount3);
    $(".total-count").html(amount1+amount2+amount3);
}

// 提交申购数据
function submitApplyDatas() {
    var datas = getCookie(dataCookieName);
    if(datas==null || datas == '') {
        buildApplyData();
    }
    datas = getCookie(dataCookieName);
    var totalCount = parseInt($(".total-count").html());
    if(datas == null || datas == '' || isNaN(totalCount) || totalCount<=0) {
        showDialog("未选任何物品，不可提交出库", "<b class='fa fa-warning'></b> 系统提示");
    } else {
        var isCheck = $("input[name='is-check']").val();
        var html = '<h4>此次出库总数量为：<b style="color:#F00">'+totalCount+'</b></h4>是否已选择完成并确定提交出库申请呢？';
        var applyDialog = confirmDialog(html, "<b class='fa fa-question-circle'></b> 系统提示", function() {
            $.post("/wx/stock/outerApply/applyPost", {datas: datas, batchNo: batchNo, isCheck: isCheck}, function(res) {
                if(res == '1') {
                    delCookie(dataCookieName);
                    showToast("提交出库申请成功，等待核对");
                    window.location.href = '/wx/stock/outerApply/listApply';
                } else {
                    showToast(res);
                }
            }, "json");
            applyDialog.remove();
        });
    }
}

/** 关闭返回 */
function cancelApply() {
    var html = '确定取消出库并返回吗？此操作将清空此次所有您已选择的所有物品';
    var myDialog = confirmDialog(html, "<b class='fa fa-warning'></b> 系统提示", function() {
        delCookie(dataCookieName);
        window.history.back(-1);
    });
}