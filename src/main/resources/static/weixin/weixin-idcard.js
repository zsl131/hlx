function uploadIdPic(obj) {
    //console.log("++++++++++")
    var oldVal = $(obj).attr("birthday");
    var phone = $(obj).attr("phone");
    if(oldVal) {
        showDialog("生日："+oldVal+"，不可修改", "系统提示"); return false;
    }
    var html = '<div class="form-group form-group-lg">'+
                       '<div class="input-group">'+
                           '<div class="input-group-addon">输入生日：</div>'+
                           '<input name="birthday" type="number" maxlength="8" class="form-control birthday-input"  placeholder="输入生日"/>'+
                       '</div>'+
                       '<p style="color:#F00">设置之后不可修改，在享受生日特权时需要与身份证或户口本进行核对！请认真对待哦！！</p>'+
                   '</div>';
    var dialog = confirmDialog(html, "设置生日", function() {
        var val = $(dialog).find("input[name='birthday']").val();
        console.log(val)
        if(val.length!=8) {
            showDialog("请认真设置日期", "系统提示");
        } else {
            //console.log("=-=--=")
            $.post("/wx/account/setBirthday", {birthday: val, phone: phone}, function(res) {
                if(res=='1') {
                    alert("保存成功"); window.location.reload();
                }
            }, "json");
        }
    })

    $(".birthday-input").jeDate({
        isinitVal:false, //初始化日期
        festival: false, //显示农历
        isClear:false,
        maxDate: curDate(),
//        skinCell:'jedatered',
        format: 'YYYYMMDD'
    });
}