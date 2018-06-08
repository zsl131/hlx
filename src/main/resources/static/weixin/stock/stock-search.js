$(function() {
    $(".search-input").keyup(function() {
        searchStockGoods(this);
    });
});

function searchStockGoods(obj) {
    //console.log("====in =======");
    var key = $(obj).val();
    filterStockGoods(key);

}

function filterStockGoods(key) {
    $(".single-goods-item").each(function() {
        var name = $(this).attr("objName");
        var nameShort = $(this).attr("nameShort");
        var nameFull = $(this).attr("nameFull");
        //console.log(key+"=="+name+"=="+nameShort+"=="+nameFull+"=="+(nameShort.indexOf(key)));
        if(name.indexOf(key)>=0 || nameShort.indexOf(key)>=0 || nameFull.indexOf(key)>=0) {
            $(this).css("display", "block");
        } else {
            $(this).css("display", "none");
        }
    });
}