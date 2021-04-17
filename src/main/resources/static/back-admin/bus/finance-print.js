$(function() {
    $(".single-box").change(function() {
        onChangeBox(this);
    })
})

function onChangeBox(obj) {
    var storeSn = $(obj).attr("storeSn");
    var detailId = $(obj).attr("detailId");
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
    //console.log(storeSn+"===========")
    $(".single-box").each(function() {
        var thisSn = $(this).attr("storeSn");
        if(storeSn && thisSn!=storeSn) {
            $(this).prop("disabled", true);
        } else {
            $(this).prop("disabled", false);
        }

        if(list.length>=6) {
            $(this).prop("disabled", true);
        }
    });
}

function setCheckboxLength(list) {
    $(".single-box").each(function() {
        var detailId = $(this).attr("detailId");
        if(storeSn && thisSn!=storeSn) {
            $(this).prop("disabled", true);
        } else {
            $(this).prop("disabled", false);
        }

        if(list.length>=6) {
            $(this).prop("disabled", true);
        }
    });
}

//function