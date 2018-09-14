function checkFinish(obj){
    var batchNo = $(obj).attr("batchNo");
    var html = '<div class="dialog-html-div">'+
                    '<p>确定此次申购货物已全部收完，不会再次到货了吗？</p>'+
                    '<p>此操作不可逆，确定提交吗？</p></div>';
    var applyDialog = confirmDialog(html, "<b class='fa fa-question-circle'></b> 收货完成提示", function() {
            $.post("/wx/stock/goodsApply/checkFinish", {batchNo: batchNo}, function(res) {
                if(res=='1') {
                    showToast("提交成功");
                    reloadWin();
                    delCookie(datasCookieName);
                }
            }, "json");
            applyDialog.remove();
        });
}