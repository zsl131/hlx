$(function() {
    wxConfig(["getLocation", "openLocation", "onMenuShareTimeline","onMenuShareAppMessage"]);
    setShares();
    var array = ["超值", "值", "爽", "划算", "赞", "便宜"];
    var random = parseInt(Math.random()*array.length);

    $(".show-info").find("b").html(array[random]);

    $(".address").click(function() {
        var latitude = $(this).attr("latitude");
        var longitude = $(this).attr("longitude");
        var address = $(this).attr("address");
        var title = $(this).attr("appName");
        openLocation(latitude, longitude, title, address);
    });

    $(".update-name-href").click(function() {
        var tarObj = $(this).parents("p.name").find("b");
        var oldName = $(tarObj).html();

        var html = '<div class="form-group form-group-lg">'+
                        '<p>输入真实姓名更有说服力(最多6个字)：</p>'+
                        '<input name="name" type="text" class="form-control" maxLength="6" placeholder="请输入点评内容，不能为空哦~" value="'+oldName+'"/>' +
                        '</div>';
        var myDialog = confirmDialog(html, "<i class='fa fa-pencil'></i> 修改显示名称", function() {
            var name = $(myDialog).find("input[name='name']").val();
            if($.trim(name)=='') {showDialog("请认真输入姓名", "系统提示");}
            else {
                $.post("/wx/account/updateQrName", {name:name}, function(res) {
                    $(tarObj).html(name);
                    $(myDialog).remove();
                }, "json");
            }
        });
    });
});

function setShares() {
    var title = $("title").html();
    var href = window.location.href;

    var appName = $(".head-part").find(".info").find(".title").html();
    var shareDesc = "在"+appName+"自助餐厅用餐真“"+buildShareTitle()+"”";

//alert(shareDesc);
//alert(href);

    share2Cirlce(title, href, "http://zthlx.zslin.com/logo-200.png", function() {
        //postScore("SHARE");
    }, function() {
        alert("取消分享不会加分哦");
    });

    share2Friend(title, shareDesc, href, "http://zthlx.zslin.com/logo-200.png", function() {
        //postScore("SHARE-FRIEND");
    }, function() {
        alert("取消分享不会加分哦");
    })
}

function buildShareTitle() {
    var array = ["超值", "值", "爽", "划算", "赞", "便宜"];
    var random = parseInt(Math.random()*array.length);

    return array[random];
}
