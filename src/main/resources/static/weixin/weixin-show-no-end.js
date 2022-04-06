$(function() {
    $("input[name='keyword']").blur(function() {onSearch(this)});

    $(".cancel-finance").click(function() {
       const objId = $(this).attr("objId");
       const objTitle = $(this).attr("objTitle");
       console.log(objId)
        const dialog = confirmDialog("确定要取消报账吗？【"+objTitle+"】", "操作提示", function() {
            $.post("/wx/finance/cancel", {id: objId}, function(res) {
                if(res==='1') {
                    alert("取消成功");
                }
                window.location.reload();
            }, "json");
        });
    });
});

function onSearch(obj) {
    const key = $(obj).val();
    // doSearch(obj)
    // console.log(key);
    $(".fin-list").css("display", "block");

    let amount = 0;
    $(".obj-title").each(function () {
        const parentObj = $(this).parents(".fin-list");
        const title = $(this).attr("fdTitle");
        if(key && title.indexOf(key)>=0) {
            $(this).html(buildTitle(title, key));
            amount ++;
            $(parentObj).css("display", "block");
        } else {
            $(this).html(title);
            if(key) {
                $(parentObj).css("display", "none")
            }
        }
        if(!key) {
            $(this).html(title);
        }
    })

    $(".search-amount-b").html(amount);
}

function buildTitle(title, key) {
    //const array = title.split(key);
    return title.substring(0, title.indexOf(key)) + "<span style='color:#FFF; background:#F00; padding: 0 3px;'>"+key+"</span>" + title.substring(title.indexOf(key)+key.length, title.length);
}