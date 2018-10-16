$(function() {
    $(".allot-btn").click(function() {
        var objId = $(this).attr("objId"); var objName = $(this).attr("objName"); var canSubmitCard = false;
        console.log(objId, objName)

        var html = '<div class="form-group">'+
                        '<div class="input-group">'+
                            '<div class="input-group-addon">数量：</div>'+
                            '<input name="count" type="number" class="form-control" onkeyup="value=value.replace(/[^\\d]/g,\'\')" max="3" placeholder="输入数量"/>'+
                        '</div>'+
                    '</div>'+
                    '<div class="form-group">'+
                        '<button type="button" class="btn btn-primary check-nos-btn" >检索卡券</button>'+
                    '</div><div class="form-group show-nos" style="width:100%;float:left;"></div>';
        var myDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 分发10元券给：'+objName, function() {
            if(canSubmitCard) {
                var count = parseInt($(myDialog).find("input[name='count']").val());
                $.post("/wx/card/setCardUnder", {type: '1', workerId: objId, count:count }, function(res) {
                    if(res=='1') {
                        alert("分发成功");
                        window.location.reload();
                    } else {
                        showDialog("分发失败", "系统提示");
                    }
                }, "json");
                $(myDialog).remove();
            } else {
                showDialog("先检索到卡券再点确定", "系统提示");
            }
        });

        $(".check-nos-btn").click(function() {
            var count = parseInt($(myDialog).find("input[name='count']").val());
            if(!count) {showDialog("请输入数量", "系统提示");} else {
                $.post("/wx/card/queryNos", {type:'1', count: count}, function(res) {
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
    });


});