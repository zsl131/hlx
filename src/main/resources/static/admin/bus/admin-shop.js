function setHomePageHref() {
    var html = '<p style="color:#F00;">设置商家首页地址</p>'+
                '<p><input name="url" class="form-control" placeholder="请输入URL地址"/></p>';

    var myDialog = confirmDialog(html, "<i class='fa fa-cog'></i> 设置商家首页", function() {
        var url = $(myDialog).find("input[name='url']").val();
        $.post("/admin/shop/setHomePage",{homePage:url}, function(res) {
            if(res=='1') {
                alert("操作成功");
                window.location.reload();
            }
        }, "json");
    });
}

function setFirstPageHref() {
    var html = '<p style="color:#F00;">设置连接成功后的显示页面地址</p>'+
                '<p><input name="url" class="form-control" placeholder="请输入URL地址"/></p>';

    var myDialog = confirmDialog(html, "<i class='fa fa-cog'></i> 设置连接后的首页", function() {
        var url = $(myDialog).find("input[name='url']").val();
        $.post("/admin/shop/setFirstPage",{firstPage:url}, function(res) {
            if(res=='1') {
                alert("操作成功");
                window.location.reload();
            }
        }, "json");
    });
}

function addDeviceHref(obj) {
    var shopId = $(obj).attr("shopId");
    //alert(shopId);
    var html = '<p style="color:#F00;">添加Wifi设备</p>'+
                '<p><input name="ssid" class="form-control" placeholder="输入Wifi名称，必须以大写‘WX’开头"/></p>'+
                '<p><input name="password" class="form-control" placeholder="输入Wifi密码"/></p>';

    var myDialog = confirmDialog(html, "<i class='fa fa-plus'></i> 添加Wifi设备", function() {
        var ssid = $(myDialog).find("input[name='ssid']").val();
        var password = $(myDialog).find("input[name='password']").val();
        $.post("/admin/shop/add",{shopId:shopId, ssid:ssid, password:password}, function(res) {
            if(res=='1') {
                alert("操作成功");
                window.location.reload();
            }
        }, "json");
    });
}