$(function() {
    var array = ["超值", "值", "爽", "划算", "赞", "便宜"];
    var random = parseInt(Math.random()*array.length);

    $(".show-info").find("b").html(array[random]);

    $(".update-name-href").click(function() {
        var tarObj = $(this).parents("p.name").find("b");
        var oldName = $(tarObj).html();

        var html = '<div class="form-group form-group-lg">'+
                        '<p>输入真实姓名更有说服力(最多6个字)：</p>'+
                        '<input name="name" type="text" class="form-control" maxLength="6" placeholder="请输入点评内容，不能为空哦~" value="'+oldName+'"/>' +
                        '</div>';
        var myDialog = confirmDialog(html, "<i class='fa fa-pencil'></i> 修改显示名称", function() {
            var name = $(myDialog).find("input[name='name']").val();
            if($.trim(name)=='') {showDialog("请认真输入姓名", "系统提示");}
            else {
                $.post("/wx/account/updateQrName", {name:name}, function(res) {
                    $(tarObj).html(name);
                    $(myDialog).remove();
                }, "json");
            }
        });
    });
});