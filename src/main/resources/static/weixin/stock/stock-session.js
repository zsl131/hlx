$(function() {
    var sn = sessionStorage.getItem("curStoreSn");
//    console.log("=========>"+sn)
    if(!sn) {
        window.location.href = "/wx/stock/index";
    } else {
        $(".show-current-store").html("&nbsp;店铺："+sn);
    }
})