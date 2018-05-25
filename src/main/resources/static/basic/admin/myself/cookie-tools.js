
/**
* 设置Cookie
* key：键
* value：值
* minutes：有效时长，分钟
*/
function setCookie(key, value, minutes) {
    var expire = "";
    if (minutes != null && minutes>0) {
        expire = new Date ((new Date ()).getTime () + minutes * 60000);
        expire = "; expires=" + expire.toGMTString ();
    }
    document.cookie = key + "=" + escape (value) + expire;
}

/**
* 获取Cookie内容
* key：键
*/
function getCookie(key) {
    var cookieValue = "";
    var search = key + "=";
    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf (search);
        if (offset != -1) {
            offset += search.length;
            end = document.cookie.indexOf (";", offset);
            if (end == -1)
                end = document.cookie.length;
            cookieValue = unescape (document.cookie.substring (offset, end))
        }
    }
    return cookieValue;
}

function delCookie(key) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 60 * 60 * 1000);
    var cval = getCookie(key);
    if (cval != null)
        document.cookie = key + "=" + cval + ";expires=" + exp.toGMTString();
}