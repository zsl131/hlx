$(function() {
    $("select[name='cateId']").change(function() {
        setCateName();
    });
    //setCateName();
//    console.log("===============")

    $("select[name='storeId']").change(function() {
        //console.log($(this).val())
        const name = $("select[name='storeId']").find("option:selected").text();
        const sn = $("select[name='storeId']").find("option:selected").attr("sn");
        $("input[name='storeName']").val(name);
        $('input[name="storeSn"]').val(sn);
        //console.log(sn+ "----------------->"+name);
        $.post("/admin/public/listCateByStore",{storeId: $(this).val()}, function(res) {
            let html = '<option value="0">选择分类</option>';
            for(let i=0;i<res.length;i++) {
                const obj = res[i];
                html += '<option value="'+obj.id+'">'+obj.name+'</option>';
            }
            //console.log(res);
            $("select[name='cateId']").html(html);
        });
    });
});

function checkData() {
    const cateId = $("select[name='cateId']").val();
    if(cateId && cateId>0) {return true;}
    else {alert("请选择店铺和分类"); return false;}
}

function setCateName() {
    $("input[name='cateName']").val($("select[name='cateId']").find("option:selected").text());
}