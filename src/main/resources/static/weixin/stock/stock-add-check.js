
function addCheck(obj) {
    var objId = $(obj).attr("objId");
    var objName = $(obj).attr("objName");

    console.log(objId+"===="+objName);

    var html = '是否邀请【'+objName+'】作为本次盘点的复核员？';
    var myDialog = confirmDialog(html, '<b class="fa fa-info-circle"></b> 系统提示', function() {
        const curStoreSn = $("input[name='curStoreSn']").val();
        $.post("/wx/stock/stockCheck/postAddCheck", {workerId : objId, storeSn: curStoreSn}, function(res) {
            if("1"==res) {
                showToast("操作成功");
                window.location.href = '/wx/stock/stockCheck/list?storeSn='+curStoreSn;
            }
        }, "json");
    });
}