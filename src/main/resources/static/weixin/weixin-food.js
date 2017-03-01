$(function() {
    $(".plus-good").click(function() {
        var thisObj = $(this);
        var id = $(this).attr("foodId");

        var toastId = showLoadToast();

        $.post("/wx/food/goodFood", {id:id}, function(res) {
            if(res=='1') {
                var valObj = $(thisObj).parents("li").find("b.good");
                var oldVal = parseInt($(valObj).html());
                $(valObj).html(oldVal+1);
                showToast("点赞成功!");
            }
            hideLoadToast(toastId);
        }, "json");
    });
});