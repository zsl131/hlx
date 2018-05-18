$(function() {

})
var showPasswordDialog;
function showPassword() {
    /*$.post("/wx/account/getPassword", {}, function(res) {
        var showStr = '消费密码：'+res;
        if(res=="-2") {showStr = "未初始化钱包";}
        else if(res=='-1') {showStr = "未设置密码";}
        var html = '<div class="dialog-html-div"><div class="form-group form-group-lg">'+
                    '<h4 style="color:#F00;padding-bottom:12px;">'+showStr+'</h4>' +
                    '<button class="btn btn-primary" onclick="updatePassword()"><b class="fa fa-pencil"></b> 修改密码</button>'+
                    '</div></div>';
        showPasswordDialog = showDialog(html, "<i class='fa fa-info'></i> 消费密码");
    }, "json");*/

    updatePassword();
}

function updatePassword() {
    var html = '<div class="dialog-html-div"><div class="form-group form-group-lg">'+
            '<input name="password" type="password" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" maxlength="4" class="form-control" placeholder="输入4位纯数字密码"/>' +
            '</div><div class="form-group form-group-lg">'+
            '<input name="rePwd" type="password" onkeyup="this.value=this.value.replace(/\\D/g,\'\')"  maxlength="4" class="form-control" placeholder="再次输入"/>' +
            '</div></div>';
    var myDialog = confirmDialog(html, "<i class='fa fa-info'></i> 设置新密码", function() {
        var password = $(myDialog).find("input[name='password']").val();
        var rePwd =$(myDialog).find("input[name='rePwd']").val();
        if(password=='' || password.length!=4 || password=='0000') {
            showDialog("输入4位纯数字密码，且不能为0000", "<i class='fa fa-info-circle'></i>系统提示");
        } else if(password != rePwd) {
            showDialog("密码两次输入不一致，请重新输入", "<i class='fa fa-info-circle'></i>系统提示");
        } else {
            var toastId = showLoadToast();
            $.post("/wx/account/setPassword", {password: password}, function(res) {
                if(res=='1') {
                    showToast("密码设置成功!");
                    $(myDialog).remove();
                    $(showPasswordDialog).remove();
                }
                hideLoadToast(toastId);
            }, "json");
        }
    });
}