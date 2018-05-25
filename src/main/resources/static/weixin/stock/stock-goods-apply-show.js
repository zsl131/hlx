$(function() {
    setCurrentAmount();
});
function setCurrentAmount() {
    $(".goods-info").each(function() {
        var goodsId = $(this).attr("goodsId");
        var amount = $(this).val();
        $(".now-amount[goodsId='"+goodsId+"']").html(amount);
    });
}

function updateStatus(status) {
    if(containAuth("2")) { //如果是采购员才可以驳回
        var batchNo = $("#batchNo").val();
        console.log(batchNo+"===="+status);
        var html = '<b class="fa fa-question"></b> 确定<b style="color:#F00">驳回</b>此次的申购申请吗？此操作不可逆！';
        if(status=='1') {
            html = '<b class="fa fa-question"></b> 确定<b style="color:blue">通过</b>此次的申购申请吗？此操作不可逆！';
        }
        var rejectDialog = confirmDialog(html, "<b class='fa fa-info-circle'></b> 系统提示", function() {
            $.post("/wx/stock/goodsApply/updateStatus", {batchNo: batchNo, status: status}, function(res) {
                if(res == '1') {
                    showToast("驳回成功");
                    reloadWin();
                }
            }, "json");
            rejectDialog.remove();
        });
    } else {
        showDialog("<b class='fa fa-warning'></b> 无权限操作", "<b class='fa fa-info-circle'></b> 系统提示");
    }
}
