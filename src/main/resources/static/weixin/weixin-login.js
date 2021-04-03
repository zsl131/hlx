function submitLogin() {
    //console.log("------------")
    var phone = $("input[name='phone']").val();
    var password = $("input[name='password']").val();
    if(!phone) {
        showDialog("请输入用户名，即手机号码", "提示")
    } else if(!password) {
        showDialog("请输入密码", "提示")
    } else {
        $.post("/weixin/login/onSubmit", {phone: phone, password: password}, function(res) {
            //alert("=======>"+res)
            if(res=='-1') {showDialog("用户不存在", "提示") }
            else if(res=='-2') {showDialog("密码不正确", "提示") }
            else if(res=='-10') {showDialog("系统错误", "提示") }
            else {
                //正常跳转
                window.location.href = "/weixin/waitTable/index";
            }
        }, "json");
    }
    //console.log(phone, password)
}