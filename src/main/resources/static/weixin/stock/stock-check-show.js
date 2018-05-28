function submitResult() {
    var count1 = $(".single-data[flag='0']").length; //持平
    var count2 = $(".single-data[flag='-1']").length; //盘亏
    var count3 = $(".single-data[flag='1']").length; //盘赢
    console.log(count1+"=="+count2+"==="+count3);
    var html = '<p>持平物种：<b>'+count1+'</b> 种</p>'+
                '<p>盘亏物种：<b style="color:#F00">'+count2+'</b> 种</p>'+
                '<p>盘赢物种：<b style="color:#00F">'+count3+'</b> 种</p>'+
                '<p>是否对本次的盘点库存信息确认无误？</p>';

    var myDialog = confirmDialog(html, "<b class='fa fa-info'></b> 系统提示", function() {
        var batchNo = $("#batchNo").val();
        $.post("/wx/stock/stockCheck/updateStatus", {batchNo: batchNo, status:'3'}, function(res) {
            if(res == '1') {
                showToast("操作成功");
                reloadWin();
            }
            myDialog.remove();
        }, "json");
    });
}