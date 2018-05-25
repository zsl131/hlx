$(function() {
    $(".filter-href").click(function() {
        var filterObj =$("#filter-div");
        var display = $(filterObj).css("display");
        if(display=='none') {
            displayFilter(true);
        } else {
            displayFilter(false);
        }
    });

    $("#win-wrapper").click(function() {displayFilter(false);});

    $("#filter-div").find('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
        setCategoryContent($(this).attr("locationType"), $(this).html());
    });

    setCategoryContent(1, $("#filter-div").find(".weui-bar__item_on").html());
})

function setCategoryContent(locationType, title) {
    var html = $(("#list"+locationType)).html();
    $("#filter-div").find(".weui-tab__panel").html(html);
}

function displayFilter(show) {
    var filterObj =$("#filter-div");
    var wrapperObj = $("#win-wrapper");
    if(show) {
        $(filterObj).slideDown(500);
        $(wrapperObj).fadeIn(400);
    } else {
        $(filterObj).slideUp(300);
        $(wrapperObj).fadeOut(400);
    }
}

function modifyGoods(obj) {
    if(containAuth("10")) {
        var objId = $(obj).attr("objId");
        var name = $(obj).attr("objName");
        var cateName = $(obj).attr("cateName");
        var locationType = $(obj).attr("locationType");
        var unit = $(obj).attr("unit");
        var warnAmount = $(obj).attr("warnAmount");
        var remark = $(obj).attr("remark");
        var status = $(obj).attr("status");

        var html = '<div class="dialog-html-div">'+
                                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">名称：</div><input name="goodsName" type="text" class="form-control" placeholder="输入物品名称" value="'+name+'"/></div></div>' +
                                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">单位：</div><input name="unit" type="text" class="form-control" placeholder="输入计量单位" value="'+unit+'"/></div></div>' +
                                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">预警：</div><input name="warnAmount" type="number" class="form-control" placeholder="输入预警数量" value="'+warnAmount+'"/></div></div>' +
                                '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">备注：</div><input name="remark" type="text" class="form-control" placeholder="输入备注信息" value="'+remark+'"/></div></div>' +
                                '<div class="form-group form-group-lg"><div class="form-control">'+
                                '<input type="radio" name="status" value="1" id="location_1" '+(status=='1'?'checked="checked"':'')+'/><label for="location_1">在用物品</label>&nbsp;&nbsp;'+
                                '<input type="radio" name="status" value="0" id="location_2" '+(status=='0'?'checked="checked"':'')+'"/><label for="location_2">不用物品</label>'
                                '</div></div>' +
                                '</div>';
        var modifyDialog = confirmDialog(html, "<b class='fa fa-pencil'></b> 修改物品【"+cateName+"-"+name+"】", function() {
            var name = $(modifyDialog).find("input[name='goodsName']").val();
            var unit = $(modifyDialog).find("input[name='unit']").val();
            var remark = $(modifyDialog).find("input[name='remark']").val();
            var warnAmount = $(modifyDialog).find("input[name='warnAmount']").val();
            var status = $(modifyDialog).find("input[name='status']:checked").val();
            //Integer id, String name, String unit, String remark, Integer warnAmount, String status//
            $.post("/wx/stock/stockGoods/update", {id: objId, name: name, unit: unit, remark: remark, warnAmount: warnAmount, status: status}, function(res) {
                if(res=='1') {
                    showToast("修改成功");
                    reloadWin();
                }
            }, "json");
            $(modifyDialog).remove();
        });
    } else {
        showDialog("无操作权限，不可修改", "<b class='fa fa-warning'></b> 系统提示");
    }
}

function addGoods() {
    if(containAuth("10")) {
        var cateId = $("input[name='category-input']").attr("cateId");
        var cateName = $("input[name='category-input']").attr("cateName");
        if(!cateId || cateId=='' || cateId<0) {
            showDialog("请先点击右上角“筛选”选择相关物品分类后才可添加物品哦！", "<b class='fa fa-warning'></b> 系统提示");
        } else {
            var html = '<div class="dialog-html-div">'+
                                    '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">名称：</div><input name="goodsName" type="text" class="form-control" placeholder="输入物品名称" /></div></div>' +
                                    '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">单位：</div><input name="unit" type="text" class="form-control" placeholder="输入计量单位" /></div></div>' +
                                    '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">预警：</div><input name="warnAmount" type="number" class="form-control" placeholder="输入预警数量" /></div></div>' +
                                    '<div class="form-group form-group-lg"><div class="input-group"><div class="input-group-addon">备注：</div><input name="remark" type="text" class="form-control" placeholder="输入备注信息"/></div></div>' +
                                    '</div>';
            var modifyDialog = confirmDialog(html, "<b class='fa fa-plus'></b> 添加物品【"+cateName+"】", function() {
                var name = $(modifyDialog).find("input[name='goodsName']").val();
                var unit = $(modifyDialog).find("input[name='unit']").val();
                var remark = $(modifyDialog).find("input[name='remark']").val();
                var warnAmount = $(modifyDialog).find("input[name='warnAmount']").val();
                var status = "1";
                //Integer cateId, String name, String unit, String remark, Integer warnAmount, String status
                $.post("/wx/stock/stockGoods/add", {cateId: cateId, name: name, unit: unit, remark: remark, warnAmount: warnAmount, status: status}, function(res) {
                    if(res=='1') {
                        showToast("修改成功");
                        reloadWin();
                    }
                }, "json");
                $(modifyDialog).remove();
            });
        }
    } else {
        showDialog("无操作权限，不可修改", "<b class='fa fa-warning'></b> 系统提示");
    }
}