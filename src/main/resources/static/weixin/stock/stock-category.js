$(function() {
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
        setCategoryContent($(this).attr("locationType"), $(this).html());
    });

    setCategoryContent(1, $(".weui-bar__item_on").html());

});

function setCategoryContent(locationType, title) {
    var html = $(("#list"+locationType)).html();
    $(".weui-tab__panel").html(html);
    title = '<div class="container-fluid">'+title + (containAuth("10")?('<span class="text-right" style="float:right;"><button class="btn btn-small btn-primary" onclick="addCategory('+locationType+')"><b class="fa fa-plus"></b></button></span>'):"")+"</div>";
    $(".weui-tab__panel").find(".weui-cells__title").html(title);
}

function addCategory(locationType) {
    var locationTypeStr = "店内";
    if(locationType=='1') {
        locationTypeStr = "冻库";
    } else if(locationType=='2') {
        locationTypeStr = "仓库";
    }
    var storeSn = sessionStorage.getItem("curStoreSn");
//    console.log("======>"+storeSn)

    var html = '<div class="dialog-html-div"><div class="form-group form-group-lg">'+
                '<input name="categoryName" type="text" class="form-control" placeholder="输入分类名称"/>' +
                '</div>'+
                '</div>';
    var addDialog = confirmDialog(html, "<b class='fa fa-plus'></b> 添加分类到："+locationTypeStr, function() {
        var categoryName = $(addDialog).find("input[name='categoryName']").val();
        $.post("/wx/stock/stockCategory/add", {locationType: locationType, storeSn: storeSn, categoryName:categoryName}, function(res) {
            if(res=='1') {
                showToast("添加成功");
                reloadWin();
            }
        }, "json");
        $(addDialog).remove();
    });
}

function modifyCategory(obj) {
    if(containAuth("10")) {
        var name = $(obj).find(".categoryName").html();
        var id = $(obj).find(".categoryName").attr("objId");
        var html = '<div class="dialog-html-div"><div class="form-group form-group-lg">'+
                        '<input name="categoryName" type="text" class="form-control" placeholder="输入分类名称" value="'+name+'"/>' +
                        '</div>'+
                        '<div><button class="btn btn-danger" onclick="deleteCategory('+id+', \''+name+'\')"><b class="fa fa-remove"></b> 删除分类</button></div>'+
                        '</div>';
        var addDialog = confirmDialog(html, "<b class='fa fa-pencil'></b> 修改分类【"+name+"】", function() {
            var categoryName = $(addDialog).find("input[name='categoryName']").val();
            $.post("/wx/stock/stockCategory/update", {id: id, categoryName:categoryName}, function(res) {
                if(res=='1') {
                    showToast("修改成功");
                    reloadWin();
                }
            }, "json");
            $(addDialog).remove();
        });
    } else {
        showDialog("无操作权限，不可修改", "<b class='fa fa-warning'></b> 系统提示");
    }
}

function deleteCategory(id, name) {
    var html = "<b class='fa fa-warning' style='color:#F00; font-size:24px;'></b>  确定删除分类【"+name+"】吗？";
    var deleteDialog = confirmDialog(html, "<b class='fa fa-remove'></b> 删除分类【"+name+"】", function() {
            $.post("/wx/stock/stockCategory/delete", {id: id}, function(res) {
                if(res=='1') {
                    showToast("删除成功");
                    reloadWin();
                }
            }, "json");
            $(deleteDialog).remove();
        });
}