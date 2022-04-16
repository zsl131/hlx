function publishBusiness(obj) {
    const objId = $(obj).attr("objId");
    //console.log(objId)
    confirmDialog("确定公布此经营数据吗？此操作不可逆", "操作提示", function() {
        //alert(objId);
        $.post("/wx/business/publish", {id: objId}, function(res) {
            if(res=="1") {alert("操作成功"); window.location.reload();}
            else {
                alert(res);
            }
        }, "json");
    })
}