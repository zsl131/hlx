$(function() {
    var chargeId = $("#chargeId").val();
    var status = $("#chargeStatus").val();
    if(status!='1') {
        checkChargeStatus(chargeId);
    }

    $(".cancel-btn").click(function() {
        var myDialog = confirmDialog("确定要取消此次会员卡的办理吗？", "<i class='fa fa-info-circle'></i> 系统提示", function() {
            var toastId = showLoadToast();
            $.post("/wx/member/cancelCharge", {id:chargeId}, function(res) {
                if(res=='1') {
                    showToast("已成功取消!");
                    window.location.reload();
                } else {
                    showDialog("操作失败，请重试！", "系统提示");
                }
                $(myDialog).remove();
                hideLoadToast(toastId);
            }, "json");
        });
    });

    $(".verfiy-btn").click(function() {
        var money = $(this).find("b").html();
        var myDialog = confirmDialog("确定已收到顾客"+money+"元了吗？", "<i class='fa fa-info-circle'></i> 系统提示", function() {
            var toastId = showLoadToast();
            $.post("/wx/member/verifyCharge", {id:chargeId}, function(res) {
                if(res=='1') {
                    showToast("会员办理成功!");
                    window.location.reload();
                } else {
                    showDialog("操作失败，请重试！", "系统提示");
                }
                $(myDialog).remove();
                hideLoadToast(toastId);
            }, "json");
        });
    });
});

function checkChargeStatus(chargeId) {
    var checkStatusInterval = setInterval(function() {
        $.post("/wx/member/findChargeStatus", {id:chargeId}, function(res) {
            if(res=='1') {
                clearInterval(checkStatusInterval);
                verifyStatus();
            } else if(res=='-1') {
                clearInterval(checkStatusInterval);
                cancelStatus();
            }
        }, "json");
    }, 3000);
}

function cancelStatus() {
    $(".verfiy-btn").remove();
}

//审核通过
function verifyStatus() {
    $(".cancel-btn").remove();
    $(".wait-btn").html("已经确认，账户已充值完成");
    $(".wait-btn").attr("href", "/wx/account/me");
}