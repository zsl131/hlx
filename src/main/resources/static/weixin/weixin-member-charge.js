
$(function() {
    checkPhone();
});

function checkPhone() {
    var phone = $("#phone").html();
    if(phone==null || phone=='') {
        openPhoneDialog("绑定手机号码才能与家人共享使用：");
    }
}

function submitCharge(obj) {
    var phone = $("#phone").html();
    if(phone==null || phone=='') {
        openPhoneDialog("绑定手机号码才能与家人共享使用：");
    } else {
        var levelId = $(obj).attr("levelId");
        var toastId = showLoadToast();
        $.post("/wx/member/addCharge", {id:levelId}, function(res) {
            hideLoadToast(toastId);
            if(res!='0') {
                window.location.href = "/wx/member/showCharge?id="+res;
            }
        }, "json");
        alert(href);
    }
}

