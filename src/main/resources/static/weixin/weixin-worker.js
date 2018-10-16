$(function() {
    $(".status-btn").click(function() {
        var status = $(this).attr("status"); var objId = $(this).attr("objId"); var objName = $(this).attr("objName");
        //console.log(status, objId);
        var html = '确定设置【'+objName+'】的状态为：'+(status=='1'?'离职':'在职')+" 吗？";
        var cancelDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
            $.post("/wx/worker/updateStatus", {id:objId, status:status=='1'?'2':'1'}, function(res) {
                window.location.reload();
            }, "json");
            $(cancelDialog).remove();
        });
    })
});