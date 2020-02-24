$(function() {
    $(".sendHref").click(function() {
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
        var articleId = $(this).parents("ul").attr("articleId");
        var type = $(this).attr("val");
        var val = $(this).html();

        var html = '<div style="font-size:20px;"><i class="fa fa-question" style="font-size:30px;"></i> 确定将该文章'+val+'吗？</div>';
        var myDialog = confirmDialog(html, "推送文章", function() {
            $.post("/admin/article/sendArticle", {id:articleId, type:type}, function(res) {
                if(res=='1') {
                    showDialog("推送成功！");
                    //$(thisObj).parents(".set-type-div").find("span").html(val.replace("设为", ""));
                }
            }, "json");
            $(myDialog).remove();
        });
    });
})