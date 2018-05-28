var batchNo, isAdd;
var dataCookieName = "stock-check-cookie-name", showFlagCookieName="hidden-ok-cookie-name";
$(function() {
    batchNo = $("#batchNo").val();
    initShowFlag();
    var curStatus = $("input[name='curStatus']").val();
    isAdd = (curStatus=='1'); //是否为添加
    checkStatus();
    $(".single-location-div").click(function() {
        setShowStatus(this);
    });
    console.log(getCookie(dataCookieName));
    resetDatas(); //重新打开时自动取Cookie中的数据加载
});

function goodsFull(obj ) {
    var pObj = $(obj).parents(".dialog-html-div");
    var allowAmount = $(pObj).find("input[name='amount']").val();
    $(pObj).find("input[name='amountNow']").val(allowAmount);
}

function addApply(obj) {
    var goodsId = $(obj).attr("goodsId");
    var goodsName = $(obj).attr("goodsName"); var unit = $(obj).attr("unit");
    var amount = $(obj).parents(".weui-panel__hd").find(".cur-amount").html();

    var applyCount = $(obj).find(".applyCount").html();

    var html = '<div class="dialog-html-div">'+
                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">应有：</div><input readonly="readonly" name="amount" type="text" class="form-control" value="'+amount+'" /><div class="input-group-addon">'+unit+'</div></div></div>'+

                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">现有：</div>'+
                '<input name="amountNow" type="number" class="form-control" placeholder="输入'+goodsName+'实际数量" value="'+applyCount+'"/>' +
                '<div class="input-group-addon">'+unit+'</div></div></div>'+
                '<div><button class="btn btn-danger" onclick="goodsFull(this)"><b class="fa fa-check-circle"></b> '+goodsName+'齐全，就点这里</button></div>'+
                '</div>';
    var applyDialog = confirmDialog(html, "<b class='fa fa-pencil'></b> 现有库存【"+goodsName+"】", function() {
        var amount = parseInt($(applyDialog).find("input[name='amountNow']").val());
        amount = (isNaN(amount) || amount==''||amount<=0)?0:amount;
        modifyBtnStyle(obj, amount);
        $(obj).find(".applyCount").html(amount);
        rebuildCount();
        $(applyDialog).remove();

        buildApplyData();
    });

    $(applyDialog).find("input[name='amountNow']").focus();
}

function modifyBtnStyle(obj, amount) {
    var pObj = $(obj).parents(".weui-panel__hd");
    var amountOld = parseInt($(pObj).find(".cur-amount").html());
    var showFlag = getCookie(showFlagCookieName);
    showFlag = (showFlag==null || isNaN(showFlag) || showFlag =='')?"1":showFlag; //默认为1
    if(amount == amountOld) {
        $(obj).removeClass("btn-info").removeClass("btn-danger").addClass("btn-success");
        $(obj).find("b.fa-pencil").removeClass("fa-pencil").addClass("fa-check");
        if(showFlag == '1') {
            $(pObj).css("display", "none");
        } else {
            $(pObj).css("display", "block");
        }
    } else {
        $(obj).removeClass("btn-info").removeClass("btn-success").addClass("btn-danger");
        $(obj).find("b.fa-check").removeClass("fa-check").addClass("fa-pencil");
    }
    $(obj).find(".applyCount").html(amount);
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

function resetDatas() {
    var datas = getCookie(dataCookieName);
    if(!isAdd) {datas = $("input[name='batch-no']").attr("datas");}
//    console.log("==-=-=-=="+datas);
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
        rebuildCount();
    }
}

function checkStatus() {
    var status = $("input[name='curStatus']").val();
    var verifyOpenid = $("input[name='verifyOpenid']").val();
    var loginOpenid = $("input[name='loginOpenid']").val();
    if("0"==status && verifyOpenid == loginOpenid) {
        var html = '<h4>确定成为此次盘点的复核员并开始填单！</h4>';
        var myDialog = confirmDialog(html, '<b class="fa fa-info"></b> 系统提示', function() {
            $.post("/wx/stock/stockCheck/updateStatus", {batchNo: batchNo, status: '1'}, function(res) {
                if(res=='1') {
                    showToast("操作成功");
                    reloadWin();
                }
            }, "json");
        });
    }
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
        showDialog("未设置任何物品，不可提交盘点信息 ", "<b class='fa fa-warning'></b> 系统提示");
    } else {
        var isCheck = $("input[name='is-check']").val();
        var html = '<h4>此次盘点总数量为：<b style="color:#F00">'+totalCount+'</b></h4>'+
                    '<p>持平物种数：<b style="color:#F00">'+calCheckCount(0)+"</b> 种</p> "+
                    '<p>盘亏物种数：<b style="color:#F00">'+calCheckCount(-1)+"</b> 种</p> "+
                    '<p>盘赢物种数：<b style="color:#F00">'+calCheckCount(1)+"</b> 种</p> "+
                    '是否已盘点完成并确定提交盘点信息呢？';
        var applyDialog = confirmDialog(html, "<b class='fa fa-question-circle'></b> 系统提示", function() {
            $.post("/wx/stock/stockCheck/postVerify", {datas: datas, batchNo: batchNo}, function(res) {
                if(res == '1') {
                    delCookie(dataCookieName);
                    showToast("提交成功");
                    window.location.href = '/wx/stock/stockCheck/show?batchNo='+batchNo;
                }
            }, "json");
            applyDialog.remove();
        });
    }
}

function calCheckCount(flag) {
    var count = 0;
    $(".goods-list-div").each(function() {
        $(this).find(".weui-panel__hd").each(function() {
            var curAmount = parseInt($(this).find(".cur-amount").html());
            var amount = parseInt($(this).find(".applyCount").html());
            if(flag==0) {
                if(curAmount == amount) {count ++;}
            } else if(flag==-1) {
                if(curAmount>amount) {count++ ;}
            } else if(flag == 1) {
                if(curAmount<amount) {count++;}
            }
        })
    })
    return count;
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

/** 关闭返回 */
function cancelApply() {
    var html = '确定取消并返回吗？此操作将清空此次所有您已设置的物品信息';
    var myDialog = confirmDialog(html, "<b class='fa fa-warning'></b> 系统提示", function() {
        delCookie(dataCookieName);
        window.history.back(-1);
    });
}

function initShowFlag() {
    var flag = getCookie(showFlagCookieName);
    flag = (flag==null || flag=='1' || isNaN(flag))?"1":flag;
    var btnObj = $(".show-flag-btn");
    if(flag=='0') {
        $(btnObj).find("b").removeClass("fa-eye-slash").addClass("fa-eye");
        $(btnObj).find("span").html("显示全部");
    } else {
        setCookie(showFlagCookieName, "1", 60*6);
        $(btnObj).find("b").removeClass("fa-eye").addClass("fa-eye-slash");
        $(btnObj).find("span").html("自动隐藏");
    }
}

function setShowFlag(obj) {
    var flag = getCookie(showFlagCookieName);
    flag = (flag==null || flag=='1' || isNaN(flag))?"1":flag;
    if(flag=='1') {
        setCookie(showFlagCookieName, "0", 60*6);
        $(obj).find("b").removeClass("fa-eye-slash").addClass("fa-eye");
        $(obj).find("span").html("显示全部");
    } else {
        setCookie(showFlagCookieName, "1", 60*6);
        $(obj).find("b").removeClass("fa-eye").addClass("fa-eye-slash");
        $(obj).find("span").html("自动隐藏");
    }
    resetDatas();
}