$(function() {
    $(".single-box").change(function() {
        onChangeBox(this);
    })

    $(".print-btn").click(function() {
        onPrint();
    });
    //console.log(getUrl());
})

function onPrint() {
    var list = $(".single-box[type='checkbox']:checked");
    if(list.length<=0) {
        showDialog("请先选择要打印的数据项");
    } else {
        var html = '<p>是否确认打印选择的这<b style="color:#F00;">【'+list.length+'】</b>条数据？</p><p>点击确定后将修改所选数据为[已打印]</p>';
        var dialog = confirmDialog(html, "操作提示", function() {
            var ids = "0,";
            for(var i=0;i<list.length;i++) {
                ids += ($(list[i]).attr("detailId")+',');
            }
            ids += '0';

            var newWin = window.open('_blank');
            newWin.location.href = getUrl()+"/admin/financeDetail/printDetail?ids="+ids;
            //$.post("/admin/financeDetail/printDetail", {ids: ids}, function)
            console.log(ids);
        }, "static");
    }
}

function getUrl() {
    return window.location.protocol+"//"+
        window.location.host;
}

function onChangeBox(obj) {
    var storeSn = $(obj).attr("storeSn");
    var detailId = $(obj).attr("detailId");
    var userOpenid = $(obj).attr("userOpenid");
    var checked = $(obj).prop("checked");
    //console.log(storeSn, detailId);
    //console.log(checked)
    setCheckbox();
    setBtnStyle();
}

function setBtnStyle() {
    var list = $(".single-box[type='checkbox']:checked");
    var btnObj = $(".print-btn");
    if(list.length<=0) {
        $(btnObj).prop("disabled", true);
        $(btnObj).html("不可打印");
        $(btnObj).removeClass("btn-danger");
    } else {
        $(btnObj).prop("disabled", false);
        $(btnObj).html("打印["+list.length+"]条");
        $(btnObj).addClass("btn-danger");
    }
}

/** 控制不同店铺不能同时选择 */
function setCheckbox(checked) {
    var list = $(".single-box[type='checkbox']:checked");
    var obj = list[0];
    var storeSn = $(obj).attr("storeSn");
    var userOpenid = $(obj).attr("userOpenid");
    //console.log(storeSn+"===========")
    $(".single-box").each(function() {
        var thisSn = $(this).attr("storeSn");
        var thisOpenid = $(this).attr("userOpenid");
        if(storeSn && (thisSn!=storeSn || thisOpenid!=userOpenid)) {
            $(this).prop("disabled", true);
            $(this).attr("title", "只能选择同一店铺且同一报账人的数据进行打印");
        } else {
            $(this).prop("disabled", false);
            $(this).attr("title", "可选择多条合并打印");
        }

        if(list.length>=6) {
            $(this).prop("disabled", true);
            $(this).attr("title", "一次最多只能选择6条数据进行打印");
        }
    });
}
