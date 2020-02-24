$(function() {
    $("input[type='radio'][name='locationType']").change(function() {
        var val = $(this).val();
        setOptions(val);
        setCategory("", "");
    });

    $(".category-select").change(function() {
        var val = $(".category-select option:selected").val();
        var text = $(".category-select option:selected").text();
        if(val!=-1) {
            setCategory(val, text);
        }
    });
})

function checkData() {
    var canSubmit = true;
    $(".not-null").each(function() {
        var val = $(this).val();
        if(val==null || val == '') {canSubmit = false;}
    })
    if(!canSubmit) {
        showDialog("信息不全，无法提交", "系统提示");
    }
    return canSubmit;
}

function setOptions(locationType) {
    var html = '<option value="-1">==请选择==</option>';
    $("#categoryList").find("span").each(function() {
        var lt = $(this).attr("locationType");
        if(lt == locationType) {
            var cateId = $(this).attr("cateId");
            var cateName = $(this).attr("cateName");
            html += '<option value="'+cateId+'">'+cateName+'</option>';
        }
    });
    $(".category-select").html(html);
}

function setCategory(cateId, cateName) {
    $("input[name='cateId']").val(cateId);
    $("input[name='cateName']").val(cateName);
}