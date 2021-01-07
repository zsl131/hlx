function optTask(obj, flag) {
    var objId = $(obj).attr("objId");
    var objName = $(obj).attr("objName");
    var uuid = $(obj).attr("uuid");
    console.log(objId, flag+"---"+uuid)
    confirmDialog("确定要【"+(flag=='1'?"运行":"停止")+"】定时器【"+objName+"】吗？", "系统提示", function() {
        if(flag==='1') {
            $.post("/admin/baseTask/start", {id: objId}, function(res) {
                if(res==='1') {alert("运行成功"); }
                window.location.reload();
            }, "json")
        } else {
            $.post("/admin/baseTask/stopRun", {uuid: uuid}, function(res) {
                if(res==='1') {alert("停止成功"); }
                window.location.reload();
            }, "json");
        }
    });
}