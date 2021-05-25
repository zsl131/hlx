$(function() {
    /*console.log("------------")
    $(".template-ul").find("li").click(function() {
        setActive($(this))
    })*/

    $(".config-btn").click(function() {
        onConfig($(this));
    });

    $(".delete-btn").click(function() {
        var objId = $(this).attr("objId");
        var objName = $(this).attr("objName");
        console.log(objId, objName)
        var dialog = confirmDialog("确定删除【"+objName+"】吗？删除后可重新配置", "操作提示", function() {
            $.post("/admin/templateMessageRelation/delete/"+objId, {}, function(res) {
                if("1"==res) {alert("删除成功"); window.location.reload();}
            }, "json");
        });
    });
});

function onConfig(obj) {
    var objName=$(obj).attr("objName");
    var rules = $(obj).attr("rules");
    //console.log(objName)
    //console.log(rules)

    var html = '<div class="form-group form-group-lg">'+
                '<div class="input-group">'+
                    '<div class="input-group-addon">模板ID：</div>'+
                    '<input name="tempId" type="text" class="form-control"  placeholder="请输入模板ID，公众号中查找" />'+
                '</div>'+
            '</div>';
    var array = rules.split("-");
    for(var i=0;i<array.length;i++) {
        var n = array[i];
        if(n) {
            html += '<div class="form-group form-group">'+
                        '<div class="input-group">'+
                            '<div class="input-group-addon">'+n+'：</div>'+
                            '<input name="tempId" type="text" class="form-control key-value" key="'+n+'" placeholder="对应键，在模板消息中查找，如：keyword'+(i+1)+'" />'+
                        '</div>'+
                    '</div>';
        }
    }
    var dialog = confirmDialog(html, "模板配置【"+objName+"】", function() {
        var tempId = $(dialog).find("input[name='tempId']").val();
        if(!tempId) {showDialog("请输入模板ID，在微信公众号中查找", "操作提示");}
        else {
            var keyValues = buildKeyValues(dialog);
            $(dialog).remove();
            $.post("/admin/templateMessageRelation/add", {name: objName, tempId: tempId, kv: keyValues}, function(res) {
                if(res=='1') {alert("保存成功！"); window.location.reload();}
            }, "json");
        }
    }, "static");

}

function buildKeyValues(obj) {
    var array = $(obj).find(".key-value");
    var res = '';
    for(var i=0;i<array.length;i++) {
        var that = array[i];
        res += $(that).attr("key")+'='+$(that).val()+"-";
    }
    console.log(res);
    return res;
}
