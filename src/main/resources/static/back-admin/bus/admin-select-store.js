$(function() {
    $("select[name='storeId']").change(function() {
        //console.log($(this).val())
        const name = $("select[name='storeId']").find("option:selected").text();
        const sn = $("select[name='storeId']").find("option:selected").attr("sn");
        $("input[name='storeName']").val(name);
        $('input[name="storeSn"]').val(sn);
    });
});

function checkStoreData() {
    const storeId = $("select[name='storeId']").val();
    if(storeId && storeId>0) {return true;}
    else {alert("请选择所属店铺"); return false;}
}