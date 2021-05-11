$(function() {
    $(".notice-btn").click(function() {
        var objId = $(this).attr("objId");
        var html = "确定要再次提醒处理吗？<br/>他们做事不积极，我们是可以提醒一下的！";
        var dialog = confirmDialog(html, "操作提示", function() {
            //console.log(objId);
            $.post("/wx/finance/notice", {id: objId}, function(res) {
                if("1" == res) {showDialog("已经成功提醒，不要重复提醒哦！", "操作提示");}
                else if("0" == res) {showDialog("当前状态，不适合提醒哦！", "操作提示");}
            }, "json");
        });
    });
});