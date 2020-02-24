$(function() {
    $(".show-money").each(function() {
        var money = parseInt($(this).html());
        $(this).html(money/100+" 元");
    });
});