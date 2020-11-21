function onUnfreeze(obj) {
    var objId = $(obj).attr("objId");

console.log(objId)
    var html =
            '<div class="form-group ">确定要手动解冻这笔会员充值吗？</div>';
    var myDialog = confirmDialog(html, "手动解冻", function() {
        $.post("/admin/moneybagDetail/unfreeze", {id: objId}, function() {
            window.location.reload();
        })
        $(myDialog).remove();
    });
}