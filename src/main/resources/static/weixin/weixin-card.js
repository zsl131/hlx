var canSubmitCard = false;
$(function() {
    $(".card-shop-href").click(function() {
        var html ='<div class="form-group form-group-lg">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">类型：</div>'+
                        '<div class="btn-group form-control" role="group" aria-label="...">'+
                            '<button type="button" class="btn btn-default card-type-btn" typeValue="1">10元券</button>'+
                            '<button type="button" class="btn btn-default card-type-btn" typeValue="2">45元券</button>'+
                            '<button type="button" class="btn btn-default card-type-btn" typeValue="3">55元券</button>'+
                        '</div>'+
                        '<input name="type" type="hidden"/>'+
                    '</div>'+
                '</div>'+
                '<div class="form-group">'+
                        '<div class="input-group">'+
                            '<div class="input-group-addon">数量：</div>'+
                            '<input name="count" type="number" class="form-control" onkeyup="value=value.replace(/[^\\d]/g,\'\')" max="3" placeholder="输入数量"/>'+
                        '</div>'+
                    '</div>'+
                    '<div class="form-group">'+
                        '<button type="button" class="btn btn-primary check-nos-btn" >检索卡券</button>'+
                    '</div><div class="form-group show-nos" style="width:100%;float:left;"></div>';
        var myDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 卡券店店', function() {
            if(canSubmitCard) {
                var count = parseInt($(myDialog).find("input[name='count']").val());
                var type = $(myDialog).find('input[name="type"]').val();
                $.post("/wx/card/card2Shop", {type: type, count:count }, function(res) {
                    if(res=='1') {
                        alert("操作成功");
                        window.location.reload();
                    } else {
                        showDialog("操作失败："+res, "系统提示");
                    }
                }, "json");
                $(myDialog).remove();
            } else {
                showDialog("先检索到卡券再点确定", "系统提示");
            }
        });

        $(".check-nos-btn").click(function() {
            var count = parseInt($(myDialog).find("input[name='count']").val());
            var type = $(myDialog).find('input[name="type"]').val();
            if(!type) {showDialog("请选择类型", "系统提示");} else if(!count) {showDialog("请输入数量", "系统提示");} else {
                $.post("/wx/card/queryCard2ShopNos", {type:type, count: count}, function(res) {
                    var html = '';
                    if(!res || res.length<=0) {html = '未检索到任何卡信息'; canSubmitCard = false;} else {
                        for(var i=0;i<res.length;i++) {
                            html += '<span style="padding:5px; float:left;">'+res[i]+'</span>'
                        }
                        canSubmitCard = true;
                    }
                    $(myDialog).find(".show-nos").html(html);
                }, "json");
            }
        })

        $(".card-type-btn").click(function() {
            $(".card-type-btn").removeClass("btn-primary");
            $(this).addClass("btn-primary");
            var val = $(this).attr("typeValue");
            $(myDialog).find("input[name='type']").val(val);
        });
    });

    $(".apply-status-btn").click(function() {
        var status = $(this).attr("status"); var cardNo = $(this).attr("cardNo"); var objId = $(this).attr("objId");
        if(status!='0') {showDialog("不可再审核", "系统提示");} else {
            var html = '确定通过【'+cardNo+'】的申请吗？';
            var cancelDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
                $.post("/wx/card/applyCard", {cardNo:cardNo, status:'1'}, function(res) {
                    if(res=='1') {
                        window.location.reload();
                    } else {
                        showDialog(res, "系统提示");
                    }
                }, "json");
                $(cancelDialog).remove();
            });
        }
    })
})