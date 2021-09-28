$(function() {
    autoRedirect();
    setStoreSn();
    $("select[name='storeSn']").change(function() {
        setStoreSn();
        var sn = $(this).val();
        window.location.href = "/wx/stock/index?storeSn="+sn;
    })
});

function setStoreSn() {
    var sn = $("select[name='storeSn']").find("option:selected").val();
//    console.log("===========>"+sn);
    sessionStorage.setItem("curStoreSn", sn);
}

function autoRedirect() {
    $(".auto-redirect").each(function() {
        var href = $(this).attr("href");
        var time = parseInt($(this).attr("time"));
        time = time<=0?3:time;
        var domain = getDomain();
        //alert(domain+"=="+href+"=="+time);

        setTimeout(function() {
            window.location.href = domain+href;
        }, time*1000);
    });
}

function getDomain() {
    var res = location.protocol + "//" + location.hostname + ":" +location.port;
    return res;
}