$(function() {
    jeDate('#income-day',{
        isinitVal:true, //初始化日期
        festival: true, //显示农历
        isClear:false,
        maxDate: curDate(),
        format: 'YYYYMMDD',
        donefun: function(obj){
            //this    而this指向的都是当前实例
//            console.log(obj.elem);     //得到当前输入框的ID
//            console.log(obj.val);      //得到日期生成的值，如：2017-06-16
            var storeSn = $("input[name='curStoreSn']").val();
//            console.log(storeSn)
            window.location.href = "/wx/income/add?storeSn="+storeSn+"&day="+obj.val;
        }
    })

    $(".money-input").keyup(function() {
        this.value=this.value.replace(/[^\d.]/g,'');
        resetTotalMoney();
    });
    resetTotalMoney();

    $(".uploader-btn").change(function(e) {
        //console.log(e);
        var objId = $("input[name='objId']").val();
        var token = $("input[name='comeToken']").val();
        var title = $("#income-day").val();
//        console.log(objId, token);
//        console.log("========", title)
        var files = e.target.files;
//        console.log(files)
        for(var i=0;i<files.length; i++) {
            uploadImage(files[i], objId, title, token);
        }
    })

    //showImg("http://img.zslin.com/income_1773_1643104076061.jpg");

    $(".show-img-div").click(function() {
        var targetObj = $(this).find(".weui-uploader__file");
//        console.log(targetObj)
        if(targetObj.length>0) {
            var url = $(targetObj).attr("path");
            //console.log(url)
            showDialog("<image src='"+url+"' style='width: 100%'/>", "凭证")
        }
    })
});

function resetTotalMoney() {
    var totalMoney = 0;
    $(".money-input").each(function() {
        totalMoney += parseFloat($(this).val());
    });

    $("input[name='totalMoney']").val(totalMoney+" 元");
}

function uploadImage(file, detailId, title, token) {
//console.log(file)
    var formData = new FormData();
    formData.append("file", file);
    formData.append("objId", detailId);
    formData.append("title", title);
    formData.append("token", token);
    //选择文件后，需要显示提示信息
    $(".upload-remind-div").css("display", "block");
    $.ajax({
        url: "/wx/income/upload",
        data: formData,
        type: "POST",
        dataType: "json",
        cache: false,
        processData: false,
        contentType: false,
        success: function(res) {
            var code = res.code;
            if(code=='1') {
                //alert("上传成功"); window.location.reload();
                showImg(res.msg);
            } else if(code=='-5') {
                alert(res.msg+"。你太坏了，居然想重复上传"); window.location.reload();
            }
            //console.log("suc", res);
        },
        complete: function(res) {
            //console.log("complete", res);
            $(".upload-remind-div").css("display", "none");
        }
    })
}

function showImg(url) {
    var targetObj = $(".show-img-div").find(".weui-uploader__files");
    if(targetObj.length<=0) { //不存在
        $(".show-img-div").html('<ul class="weui-uploader__files"><li class="weui-uploader__file single-voucher" path="'+url+'" style="background-image: url(\''+url+'\');"></li></ul>')
    } else {
        $(targetObj).css({"background-image": url})
    }
//    console.log(targetObj.length)
}