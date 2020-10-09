$(function() {
    $("select[name='storeId']").change(function() {
        var option = $(this).find("option:selected");
        var sn = $(option).attr("sn"); var name = $(option).html();
        $("input[name='storeName']").val(name); $("input[name='storeSn']").val(sn);
    })
})

function checkData() {
    var storeId = $("select[name='storeId']").val();
    var name = $("input[name='name']").val();
    var phone = $("input[name='phone']").val();
    if(!storeId || storeId<=0) {showDialog("请选择店铺", "系统提示"); return false; }
    else if(!name) {showDialog("请输入顾客姓名", "系统提示"); return false; }
    else if(!phone) {showDialog("请输入顾客手机号码", "系统提示"); return false;}
    else {return true;}
}