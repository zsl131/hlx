$(function() {
    resetDuty();
    setStockDuty();
})

function resetDuty() {
    var res = $("input[name='operator']").val();
    var array = res.split("-");
    for(var i=0;i<array.length;i++) {
        $("input[type='checkbox'][name='duty'][value='"+array[i]+"']").attr("checked", true);
    }
}

function setStockDuty() {
    $("input[type='checkbox'][name='duty']").click(function() {
        $("input[name='operator']").val(rebuildDuty());
    })
}

function rebuildDuty() {
    var res = '-';
    $("input[type='checkbox'][name='duty']").each(function() {
        var check = this.checked;
        if(check) {
            res+=$(this).val()+'-';
        }
    });
    if(res=='-') {res = '';}
    return res;
}