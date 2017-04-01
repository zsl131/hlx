$(function() {
    $(".set-type-div").click(function() {
        var ulObj = $(this).find(".type-ul");
        var display = $(ulObj).css("display");
        if(display=='none') {
            $(".type-ul").css("display", "none");
            $(ulObj).css("display","block");
        } else {
            $(ulObj).css("display","none");
        }
    });

    $(".type-ul li").click(function() {
        var thisObj = $(this);
        var accountId = $(this).parents("ul").attr("accountId");
        var type = $(this).attr("val");
        var val = $(this).html();

        var html = '<div style="font-size:20px;"><i class="fa fa-question" style="font-size:30px;"></i> 确定将用户类型'+val+'吗？</div>';
        var myDialog = confirmDialog(html, "设置用户类型", function() {
            $.post("/admin/account/updateType", {id:accountId, type:type}, function(res) {
                if(res=='1') {
                    showDialog("设置成功！");
                    $(thisObj).parents(".set-type-div").find("span").html(val.replace("设为", ""));
                    $(myDialog).remove();
                }
            }, "json");
        });
    });
});

