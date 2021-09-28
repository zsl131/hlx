$(function() {
    $(".single-store-box").click(function() {
        var objId = $(this).attr("id"); //角色Id

        objId = objId.split("_")[1];
        var userId = $("#curUserId").val(); //用户Id
        var query = {storeId: objId, userId: userId};
        $.post("/admin/stockUser/addOrDeleteUserStore", query, function(datas) {
            if(datas=='1') {
                alert("设置成功！");
            }
        }, "json");
    });
    var curAuth_array = $("#curStores").val().split(",");
    for(var i=0; i<curAuth_array.length; i++) {
        $(("#store_"+curAuth_array[i])).attr("checked", true);
    }
});