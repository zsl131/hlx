var canSubmitCard = false;
$(function() {
    $(".card-type-btn").click(function() {
        $(".card-type-btn").removeClass("btn-primary");
        $(this).addClass("btn-primary");
        var val = $(this).attr("typeValue");
        $("input[name='type']").val(val);
    })
});

function checkNos() {
    return canSubmitCard;
}

function queryCheckNos() {
    var type = $("input[name='type']").val();
    var count = parseInt($("input[name='count']").val());
    if(!type) {
        alert("请选择类型");
        return false;
    } else if(!count) {
        alert("请输入要入库的数量");
        return false;
    } else {
        $.post("/admin/grantCard/queryNos", {type:type, count: count}, function(res) {
            var html = '';
            for(var i=0;i<res.length;i++) {
                html += '<span style="padding:8px; float:left;">'+res[i]+'</span>'
            }
            $(".nos").html(html);
            $("button[type='submit']").removeAttr("disabled");
            canSubmitCard = true;
        }, "json");
    }
}
