$(function() {
    var detailId = $("input[name='detailId']").val();

    $(".uploader-btn").change(function(e) {
        //console.log(e);
        var files = e.target.files;
        //console.log(files)
        for(var i=0;i<files.length; i++) {
            uploadImage(files[i], detailId);
        }
    })

    $(".cancel-btn").click(function() {
        cancelFinance(detailId);
    });

    //提交审核
    $(".submit-apply-btn").click(function() {
        var voucherLength = $(".single-voucher").length;
        var msg;
        if(voucherLength<=0) {
            msg = "目前没有上传任何凭证，确定<b style='color:#F00'>【无凭证】</b>提交审核吗？";
        } else {
            //submitApply(detailId);
            msg = "确定提交审核吗？当前<b style='color:#F00'>【"+voucherLength+"】</b>张凭证";
        }
        confirmDialog(msg, "操作提示", function() {
            submitApply(detailId);
        }, "static");
    });

    $(".single-voucher").click(function() {
        var url = $(this).attr("path");
        var status = $(this).attr("status");
        var voucherId = $(this).attr("voucherId");
        //console.log(url, status)
        var html = "<img src='"+url+"' style='width:100%'/>";
        if(status=='0' || status=='3') {
            //如果状态是等提交或驳回，可删除凭证
            html += '<p style="margin: 9px;"><button onClick="deleteVoucher('+voucherId+')" class="btn btn-danger">删除凭证</button></p>';
        }
        showDialog(html, "凭证查阅");
    });

    //驳回申请
    $(".reject-btn").click(function() {
        var html = "<p>确定要驳回此报账申请吗？此操作不可逆！</p>"+
                "<input class='form-control' name='reason' placeholder='请输入驳回原因'/>";
        var rejectDialog = confirmDialog(html, "操作提示", function() {
            var reason = $(rejectDialog).find("input[name='reason']").val();
            //console.log(reason)
            $.post("/wx/finance/updateStatus", {id: detailId, status: "3", reason: reason}, function(res) {
                if(res=='1') {alert("提交成功，等待审核"); window.location.reload();}
            }, "json");
            $(rejectDialog).remove();
        }, "static");
    });

    $(".pass-verify-btn").click(function() {
        var html = "<p>确定要通过此报账申请吗？此操作不可逆！</p>";
        confirmDialog(html, "操作提示", function() {
            //console.log(reason)
            $.post("/wx/finance/updateStatus", {id: detailId, status: "2"}, function(res) {
                if(res=='1') {alert("提交成功，等待审核"); window.location.reload();}
            }, "json");
            $(rejectDialog).remove();
        }, "static");
    });
})

//删除凭证
function deleteVoucher(vid) {
    confirmDialog("此操作不可逆，确定要删除吗？", "操作提示", function() {
        $.post("/wx/finance/deleteVoucher", {id: vid}, function(res) {
            if(res=='1') {alert("删除成功"); window.location.reload();}
        }, "json");
    }, "static");
}

//提交审核
function submitApply(detailId) {
    $.post("/wx/finance/updateStatus", {id: detailId, status: "1"}, function(res) {
        if(res=='1') {alert("提交成功，等待审核"); window.location.reload();}
    }, "json");
}

function uploadImage(file, detailId) {
//console.log(file)
    var formData = new FormData();
    formData.append("file", file);
    formData.append("params", "123");
    formData.append("objId", detailId);
    $.ajax({
        url: "/wx/finance/upload",
        data: formData,
        type: "POST",
        dataType: "json",
        cache: false,
        processData: false,
        contentType: false,
        success: function(res) {
            if(res=='1') {
                alert("上传成功"); window.location.reload();
            }
            //console.log("suc", res);
        },
        complete: function(res) {
            console.log("complete", res);
        }
    })
}

function cancelFinance(id) {
    confirmDialog("确定要取消报账吗？取消后此申请将全部作废", "系统提示", function() {
        $.post("/wx/finance/cancel", {id: id}, function(res) {alert("操作成功！"); window.location.reload();})
    }, "static");
}