var dataCookieName = "apply-cookie-name";
var locationSpe = "-_-";
var isAdd;
var batchNo;
$(function() {
    batchNo = $("input[name='batch-no']").val();
    isAdd = (batchNo==null || batchNo==''); //是否为添加
    $(".single-location-div").click(function() {
        setShowStatus(this);
    });
    resetDatas(); //重新打开时自动取Cookie中的数据加载
})

function addApply(obj) {
    var goodsId = $(obj).attr("goodsId");
    var goodsName = $(obj).attr("goodsName"); var unit = $(obj).attr("unit");
    var applyCount = $(obj).find(".applyCount").html();

    var html = '<div class="dialog-html-div">'+
                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">申购</div><input name="amount" type="number" class="form-control text-center" placeholder="输入数量" value="'+applyCount+'"/><div class="input-group-addon">'+unit+'</div></div></div>' +
                '</div>';
    var applyDialog = confirmDialog(html, "<b class='fa fa-plus'></b> 申购【"+goodsName+"】", function() {
        var amount = parseInt($(applyDialog).find("input[name='amount']").val());
        amount = (isNaN(amount) || amount==''||amount<=0)?0:amount;
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
        datas += locationSpe+locationType+locationSpe;
        //goods-list-1
        $((".goods-list-"+locationType)).find(".apply-href").each(function() {
            var amount = parseInt($(this).find(".applyCount").html());
//            if(amount>0) {
                datas += $(this).attr("goodsId")+"_"+amount+"-";
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
        var locationArray = datas.split(locationSpe);
        var data1 = locationArray[2];
        var data2 = locationArray[4];
        var data3 = locationArray[6];

        var array1 = data1.split("-");
        var array2 = data2.split("-");
        var array3 = data3.split("-");
        var total = 0, total1 = 0, total2 = 0, total3 = 0;
        for(var i=0;i<array1.length;i++) {
            if(array1[i] != null && array1[i] != '') {
                var goodsId = array1[i].split("_")[0];
                var amount = parseInt(array1[i].split("_")[1]);
                total1 += amount;
                modifyBtnStyle($("a.apply-href[goodsId='"+goodsId+"']"), amount);
            }
        }
        $(".single-location-div[locationType='1']").find(".category-count").html(total1);

        for(var i=0;i<array2.length;i++) {
            if(array2[i] != null && array2[i] != '') {
                var goodsId = array2[i].split("_")[0];
                var amount = parseInt(array2[i].split("_")[1]);
                total2 += amount;
                modifyBtnStyle($("a.apply-href[goodsId='"+goodsId+"']"), amount);
            }
        }
        $(".single-location-div[locationType='2']").find(".category-count").html(total2);

        for(var i=0;i<array3.length;i++) {
            if(array3[i] != null && array3[i] != '') {
                var goodsId = array3[i].split("_")[0];
                var amount = parseInt(array3[i].split("_")[1]);
                total3 += amount;
                modifyBtnStyle($("a.apply-href[goodsId='"+goodsId+"']"), amount);
            }
        }
        $(".single-location-div[locationType='3']").find(".category-count").html(total3);

        total = total1 + total2 + total3;

        $(".total-count").html(total);
    }
}

// 提交申购数据
function submitApplyDatas() {
    var datas = getCookie(dataCookieName);
    var totalCount = parseInt($(".total-count").html());
    if(datas == null || datas == '' || isNaN(totalCount) || totalCount<=0) {
        showDialog("未选任何物品，不可提交申购", "<b class='fa fa-warning'></b> 系统提示");
    } else {
        var isVerify = $("input[name='is-verify']").val();
        var html = '<h4>此次申购总数量为：<b style="color:#F00">'+totalCount+'</b></h4>是否已选择完成并确定提交申购申请呢？';
        var applyDialog = confirmDialog(html, "<b class='fa fa-question-circle'></b> 系统提示", function() {
            $.post("/wx/stock/goodsApply/applyPost", {datas: datas, batchNo: batchNo, isVerify: isVerify}, function(res) {
                if(res == '1') {
                    delCookie(dataCookieName);
                    showToast("申购成功，等待审核");
                    window.location.href = '/wx/stock/goodsApply/listApply';
                }
            }, "json");
            applyDialog.remove();
        });
    }
}

/** 关闭返回 */
function cancelApply() {
    var html = '确定取消申购并返回吗？此操作将清空此次所有您已申购的物品';
    var myDialog = confirmDialog(html, "<b class='fa fa-warning'></b> 系统提示", function() {
        delCookie(dataCookieName);
        window.history.back(-1);
    });
}