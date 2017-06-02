$(function() {
    $(".add-comment-btn").click(function() {
        var foodId = $(this).attr("foodId");
        //alert(foodId);

        var html = '<div class="form-group form-group-lg">' +
                '<input type="checkbox" class="form-control" isGood="1" onclick="modifyAttr(this)" checked="checked" style="width:24px;height:24px; float:left;" id="my-checkbox"/>'+
                '<label for="my-checkbox" style="font-weight:bold; line-height:35px; padding-left:8px; font-weight:normal">觉得这道菜不错</label>' +
                '</div><div class="form-group form-group-lg">'+
                '<textarea name="content" type="text" style="height:80px; resize:none;" class="form-control" placeholder="请输入点评内容，不能为空哦~"></textarea>' +
                '</div>';
        var myDialog = confirmDialog(html, "<i class='fa fa-commenting'></i> 点评食品", function() {
            var content = $(myDialog).find("textarea[name='content']").val();
            var isGood = $(myDialog).find("#my-checkbox").attr("isGood");
            //alert(content);
            if($.trim(content)=='') {
                showDialog("点评内容不能为空!", "系统提示");
                return false;
            } else {
                var toastId = showLoadToast();
                $.post("/wx/food/add", {foodId:foodId, content:content, isGood:isGood}, function(res) {
                    if(res=='1') {
                        showToast("点评成功!");
                    }
                    hideLoadToast(toastId);
                    $(myDialog).remove();
                }, "json");
                $(myDialog).remove(); //提交后就移除
            }
        });
    });
});

function modifyAttr(obj) {
    var isGood = $(obj).attr("isGood");
    $(obj).attr("isGood", isGood=='1'?'0':'1');
}