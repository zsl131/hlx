$(function() {
    if(containAuth("1")) { //采购员审核或管理员
        $(".verify-apply-href").each(function() {
            $(this).css("display", "block");
        });
    }
});