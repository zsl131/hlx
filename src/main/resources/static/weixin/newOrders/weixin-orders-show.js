$(function() {
    var orderDiscountReason = $(".orderDiscountReason").html();
    var myArray = orderDiscountReason.split(",");
    var html = '';
    for(var i=0;i<myArray.length;i++) {
        html += '<p>'+myArray[i]+'</p>';
    }
    $(".orderDiscountReason").html(html);
});