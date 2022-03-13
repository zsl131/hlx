$(function () {
    rebuildMoney();
})

function rebuildMoney() {
    $(".need-rebuild-money").each(function() {
        const thisObj = $(this);
        const money = parseFloat($(thisObj).attr("money"));
        const len = $(thisObj).attr("len");
        // console.log(money, len)
        const res = money.toFixed(len?len:2);
        // console.log(res)
        $(thisObj).html(res);
    })
}