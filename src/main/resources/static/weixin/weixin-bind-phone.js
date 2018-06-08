var canSendCode = true;
var curPhone = '';
var myCodeInterval;
function openPhoneDialog(str) {
    var html = '<div class="dialog-html-div"><div class="form-group form-group-lg">'+
            '<p>'+str+'</p>'+
            '<input name="phone" type="number" pattern="[0-9]*" maxlength="11" class="form-control" placeholder="输入手机号码"/>' +
            '</div><div class="form-group form-group-lg">'+
            '<div class="input-group">'+
            '<input name="code" type="number" pattern="[0-9]*" maxlength="4" class="form-control" placeholder="验证码"/>' +
            '<span class="input-group-addon get-code-btn" onclick="sendCode(this)">获取验证码</span>'+
            '</div></div></div>';
    var myDialog = confirmDialog(html, "<i class='fa fa-info'></i> 绑定手机号码", function() {
        var code = $(myDialog).find("input[name='code']").val();
        code = $.trim(code); //去空格
        if(code=='' || code.length!=4) {
            showDialog("请输入手机上收到的验证码", "<i class='fa fa-info-circle'></i>系统提示");
        } else {
            var toastId = showLoadToast();
            $.post("/wx/account/modifyPhone", {phone:curPhone, code:code}, function(res) {
                if(res=='1') {
                    showToast("手机绑定成功!");
                    $("#phone").html(curPhone);
                    $(myDialog).remove();
                } else {
                    showDialog("验证码错误，请重新输入", "<i class='fa fa-info-circle'></i>系统提示");
                }
                hideLoadToast(toastId);
            }, "json");
        }
    });
}

function sendCode(obj) {
    var phoneObj = $(obj).parents(".dialog-html-div").find("input[name='phone']");
    var phone= $(phoneObj).val();
    if(isPhone(phone)) {
        if(canSendCode) {
            setPhoneStyle(phoneObj, true); //不可修改
            setGetCodeBtnCon(obj);
            curPhone = phone;
            $.post("/wx/account/sendCode", {phone:phone}, function(res) {
                if("-1"==res) {
                    showDialog("手机号码已被其他用户绑定", "<i class='fa fa-info-circle'></i>系统提示");
                    errorCode(obj);
                }
            }, "json");
        }
    } else {
        showDialog("请输入手机号码", "<i class='fa fa-info-circle'></i>系统提示");
    }
}

function setGetCodeBtnCon(obj) {
    canSendCode = false;
    var time = 60;
    myCodeInterval = setInterval(function() {
        if(time>0) {
            $(obj).html((time--)+" 秒");
        } else {
            canSendCode = true;
            var phoneObj = $(obj).parents(".dialog-html-div").find("input[name='phone']");
            setPhoneStyle(phoneObj, false);
            clearInterval(myCodeInterval);
            $(obj).html("重获验证码");
        }
    }, 1000);
}

function errorCode(obj) {
    canSendCode = true;
    var phoneObj = $(obj).parents(".dialog-html-div").find("input[name='phone']");
    setPhoneStyle(phoneObj, false);
    clearInterval(myCodeInterval);
    $(obj).html("获取验证码");
}

function setPhoneStyle(obj, readonly) {
    if(readonly) {
        $(obj).attr("readonly", "readonly");
    } else {
        $(obj).removeAttr("readonly");
    }
}

function setPhone(phone) {
    $("#phone").val(phone);
}

function isPhone(sMobile) {
    if((/^1[3|4|5|7|8|9][0-9]\d{8}$/.test(sMobile))) {
        return true;
    } else {return false;}
}