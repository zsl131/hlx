
function cancelFriend() {
    confirmFriend('确定取消该订单吗？取消即表示不给予顾客折扣', "0");
}

function verifyFriend() {
    confirmFriend('确定给予折扣吗？', "1");
}

function confirmFriend(html, res) {
    var orderNo = $("#ordersNo").val(); //订单编号
    var cancelDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
        $.post("/wx/orders/confirmFriend", {no:orderNo, res:res}, function(res) {
            window.location.reload();
        }, "json");
        $(cancelDialog).remove();
    });
}