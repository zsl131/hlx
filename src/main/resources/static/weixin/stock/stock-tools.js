/**
* 获取当前用户的操作权限
* 1-采购员；2-出库员；3-入库员；4-审核员；10-管理员
*/
function getOperatorAuth() {
    var operator = $("input[name='loginWorker']").val();
    var array = operator.split("-");
    return array;
}

/**
* 判断当前用户是否包含某个权限
* 1-采购员；2-出库员；3-入库员；4-审核员；10-管理员
*/
function containAuth(authCode) {
    var res = false;
    var array = getOperatorAuth();
    for(var i=0;i<array.length;i++) {
        if(authCode == array[i]) {
            res = true;
        }
    }
    return res;
}