var curPersonalId = 0;
let image_array = [];
let current_url = '';
function initImageArray() {
    let index = 0;
    $(".single-voucher").each(function() {
        const objId = $(this).attr("voucherId");
        let url = $(this).css("background-image");
        //console.log(url)
        url = url.substring(5, url.length-2);
        // console.log("->"+url, objId)
        image_array.push({index: index, id: objId, url: url});
        index ++;
    });
}
$(function() {
    var detailId = $("input[name='detailId']").val();
    var detailTitle = $("input[name='detailTitle']").val();
    var storeName = $("input[name='storeName']").val();
    initImageArray(); //初始化图片
    $(".uploader-btn").change(function(e) {
        //console.log(e);
        var files = e.target.files;
        //console.log(files)
        uploadImage(files, detailId, storeName+"："+detailTitle, 0);
        /*for(var i=0;i<files.length; i++) {
            uploadImage(files[i], detailId, storeName+"："+detailTitle);
        }*/
        //alert("上传完成"); window.location.reload();
    })

    $(".cancel-btn").click(function() {
        cancelFinance(detailId);
    })

    //提交审核，提交到老板审核
    $(".submit-apply-btn").click(function() {
        var voucherLength = $(".single-voucher").length;
        var msg;
        if(voucherLength<=0) {
            msg = "目前没有上传任何凭证，确定<b style='color:#F00'>【无凭证】</b>提交审核吗？";
        } else {
            //submitApply(detailId);
            msg = "确定提交审核吗？当前<b style='color:#F00'>【"+voucherLength+"】</b>张凭证";
        }
        var submitDialog = confirmDialog(msg, "操作提示", function() {
            $(submitDialog).remove();//防止重复提交
            submitApply(detailId);
        }, "static");
    });

    $(".single-voucher").click(function() {
        var url = $(this).attr("path");
        var status = $(this).attr("status");
        var voucherStatus = $(this).attr("voucherStatus");
        var isOwn = $(this).attr("isOwn");
        //console.log(isOwn=='true')
        //console.log(voucherStatus)
        var voucherId = $(this).attr("voucherId");
        //console.log(url, status)
        var html = "<img src='"+url+"' style='width:100%'/>";
        let canDelete = false;
        if((voucherStatus=='0' || voucherStatus == '1' || voucherStatus=='3') && isOwn=='true') {
            //如果状态是等提交或驳回，可删除凭证
            html += '<p style="margin: 9px;"><button onClick="deleteVoucher('+voucherId+')" class="btn btn-danger">删除凭证</button></p>';
            canDelete = true;
        }

        /*weui.gallery(url, {
            className: 'custom-classname',
            onDelete: function(){
                deleteVoucher(voucherId);
            }
        })*/

        current_url = url; //当前图片地址
        showWeuiImg(0, canDelete);
        dragImg(canDelete);

        //showDialog(html, "凭证查阅");
    });


    $("#show-img-div").click(function() {
        $(this).css("display", "none")
    })

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

    //老板审核通过
    $(".pass-verify-btn").click(function() {
        var html = "<p>确定要通过此报账申请吗？此操作不可逆！</p>";
        var dialog = confirmDialog(html, "操作提示", function() {
            //console.log(reason)
            $.post("/wx/finance/updateStatus", {id: detailId, status: "2"}, function(res) {
                if(res=='1') {alert("提交成功，等待审核"); window.location.reload();}
            }, "json");
            $(dialog).remove();
        }, "static");
    });

    //未收到货
    $(".no-confirm-btn").click(function() {
        var html = "<p>确定没有收到货物吗？此操作不可逆！</p>"+
                "<p style='color:#F00'>如果不是已经确定无法收到货物，请不要点此操作</p>"+
                "<input class='form-control' name='reason' placeholder='请输入未收到货物的原因'/>";
        var rejectDialog = confirmDialog(html, "操作提示", function() {
            var reason = $(rejectDialog).find("input[name='reason']").val();
            //console.log(reason)
            $.post("/wx/finance/updateConfirm", {id: detailId, status: "3", reason: reason}, function(res) {
                if(res=='1') {alert("提交成功"); window.location.reload();}
            }, "json");
            $(rejectDialog).remove();
        }, "static");
    });

    //确认收到货物
    $(".confirm-btn").click(function() {
        var html = "<p>确定已经收到货物了吗？此操作不可逆！</p>";
        var dialog = confirmDialog(html, "操作提示", function() {
            //console.log(reason)
            $.post("/wx/finance/updateConfirm", {id: detailId, status: "2"}, function(res) {
                if(res=='1') {alert("提交成功"); window.location.reload();}
            }, "json");
            $(dialog).remove();
        }, "static");
    });

    //凭证不全，驳回
    $(".voucher-btn").click(function() {
        var html = "<p>确定凭证不全，需要退回补全凭证吗？</p>"+
                "<p style='color:#F00'>如果此报账确实无凭证，可直接通过</p>"+
                "<input class='form-control' name='reason' placeholder='请输入退回原因'/>";
        var rejectDialog = confirmDialog(html, "操作提示", function() {
            var reason = $(rejectDialog).find("input[name='reason']").val();
            //console.log(reason)
            $.post("/wx/finance/updateVoucher", {id: detailId, status: "3", reason: reason}, function(res) {
                if(res=='1') {alert("提交成功"); window.location.reload();}
            }, "json");
            $(rejectDialog).remove();
        }, "static");
    });

    //凭证齐全，存档
    $(".voucher-ok-btn").click(function() {
        var html = "<p>确定凭证齐全了吗？此操作不可逆！</p>";
        var dialog = confirmDialog(html, "操作提示", function() {
            //console.log(reason)
            $.post("/wx/finance/updateVoucher", {id: detailId, status: "2"}, function(res) {
                if(res=='1') {alert("提交成功"); window.location.reload();}
            }, "json");
            $(dialog).remove();
        }, "static");
    });

    $(".choice-personal-btn").click(function() {
        $.post("/wx/finance/listPersonal",{}, function(res) {
            //console.log(res)
            var html = '<p class="personal-list">';
            for(var i=0;i<res.length;i++) {
                var per = res[i];
                html += '  <button class="btn btn-default" style="margin:5px;" objId="'+per.id+'" onclick="setPersonal(this)">'+per.name+'</button>';
            }
            html += '</p>';
            html += '<p><button class="btn btn-info" onclick="addPersonal()">新增收货人员</button></p>';
            var perDialog = confirmDialog(html, "收货人员管理", function() {
                if(!curPersonalId) {
                    showDialog("请选择相应人员作为收货人员", "操作提示");
                } else {
                    var detailId = $("input[name='detailId']").val(); //detailID
                    $.post("/wx/finance/setPersonal", {personalId: curPersonalId, detailId: detailId}, function(code) {
                        if(code=='0') {
                            showDialog('不能设置自己为收货人员', "操作提示");
                        } else {
                            alert("设置成功"); window.location.reload();
                        }
                    }, "json");
                }
            }, "static");
        }, "json");
    })

    $(".modify-day").click(function() {
        const day = $(this).attr("targetDay");
        const objId = $(this).attr("objId");
        // console.log(day, objId)
        var html = '<div class="form-group">\n' +
            '                <div class="input-group">\n' +
            '                    <div class="input-group-addon">日期</div>\n' +
            '                    <div class="category-div">\n' +
            '                        <input name="targetDay" class="form-control modify-target-day" value="'+day+'" placeholder="所属日期，格式：yyyyMMdd"/>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>';
        var dayDialog = confirmDialog(html, "收货人员管理", function() {
            const targetDay = $(dayDialog).find("input[name='targetDay']").val();
            if(targetDay.length!==8) {alert("日期格式不对");}
            else {
                $.post("/wx/finance/modifyTargetDay", {day: targetDay, id: objId}, function(res) {
                    if(res===1) {
                        alert("修改成功");
                    } else {
                        alert("修改失败");
                    }
                    window.location.reload();
                }, "json");
            }
        }, "static");
        $(".modify-target-day").jeDate({
            isinitVal:true, //初始化日期
            festival: true, //显示农历
            isClear:false,
            maxDate: curDate(),
            //        skinCell:'jedatered',
            format: 'YYYYMMDD'
        });
    });
})

function dragImg(canDelete) {
    const imgObj = $("#show-img-div");
    let startX = 0;
    let startY = 0;
    $(imgObj).on("touchstart", function (e) {
        startX = e.originalEvent.changedTouches[0].pageX
        startY = e.originalEvent.changedTouches[0].pageY
    });

    $(imgObj).on("touchend", function(e) {
        const moveEndX = e.originalEvent.changedTouches[0].pageX;
        const moveEndY = e.originalEvent.changedTouches[0].pageY;

        const amountY = Math.abs(moveEndY-startY);
        // console.log("Y值 "+amountY)
        // console.log(moveEndX, startX)
        // console.log(moveEndX - startX)
        if(amountY<30) { //如果上下移动小于30，则表示是左右移动
            const flagAmount = 30;
            if(moveEndX - startX>flagAmount) {//往右滑动，获取前一条数据
                //console.log("========")
                showWeuiImg(-1, canDelete);
            } else if(startX-moveEndX>flagAmount) { //往左
                //console.log("+++++++++++")
                showWeuiImg(1, canDelete);
            }
        }
    })
}
// let current_gallery ;
function showWeuiImg(flag, canDelete) {
    // console.log(canDelete)
    const imgObj = buildImgObj(flag);
    //console.log(imgObj)
    current_url = imgObj.url;

    const showObj = $("#show-img-div");
    $(showObj).css("display", "block")
    let html = '<img src="'+current_url+'" style="width: 100%"/>';
    if(canDelete) {
        html += '<p><span class="delete-btn delete-voucher-btn" objId="'+imgObj.id+'"><i class="fa fa-trash"></i></span></p>'
    }
    $(showObj).find(".img-span").html(html);

    $(".delete-voucher-btn").click(function(e) {
        const voucherId = $(this).attr("objId");
        // alert(voucherId)
        deleteVoucher(voucherId);
        e.stopPropagation();
    })
}

function buildImgObj(flag) {
    // console.log(image_array)
    const curIndex = image_array.findIndex((item) => item.url===current_url);
    let targetIndex = curIndex + flag;
    if(targetIndex<0) {targetIndex = 0; showDialog("已经是第一张了");}
    else if(targetIndex>image_array.length-1) {targetIndex = image_array.length - 1; showDialog("已经是最后张了");}
    return image_array[targetIndex];
}

function setPersonal(obj) {
    var objId = $(obj).attr("objId");
    //console.log(objId);
    $(obj).parents(".personal-list").find(".btn-primary").removeClass("btn-primary");
    $(obj).addClass("btn-primary");
    curPersonalId = objId;
}

function addPersonal() {
    var html = '<div id="addPersonalDiv"><div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">姓名</div>'+
                        '<input name="name" class="form-control" placeholder="收货人姓名"/>'+
                    '</div>'+
                '</div>'+
                '<div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">电话</div>'+
                        '<input name="phone" class="form-control" placeholder="收货人电话"/>'+
                    '</div>'+
                '</div>'+
                '<div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">人员</div>'+
                        '<input name="query" class="form-control" placeholder="输入昵称或电话后点查询按钮" />'+
                        '<span class="input-group-addon" onclick="searchAccount()" style="cursor:pointer;">查询用户</span>'+
                    '</div>'+
                    '<div class="alert alert-info">被添加人员需要关注公众号</div>'+
                '</div>'+
                '<div class="showAccounts"></div>'+
                '<input name="openid" type="hidden"/>'+
                '</div>';
    var addDialog = confirmDialog(html, "新增收货人员", function() {
        var name = $(addDialog).find("input[name='name']").val();
        var phone = $(addDialog).find("input[name='phone']").val();
        var openid = $(addDialog).find("input[name='openid']").val();

//        console.log(name)
//        console.log(phone)
//        console.log(openid)
        if(!name) {showDialog("请输入姓名", "操作提示");}
        else if(!phone) {showDialog("请输入电话", "操作提示");}
        else if(!openid) {showDialog("请选择人员", "操作提示");}
        else {
            $.post("/wx/finance/savePersonal", {name: name, phone: phone, openid: openid}, function(res) {
                if("0"==res) {
                    alert("该微信用户已被添加");
                } else if("1"==res) {
                    alert("添加成功！"); window.location.reload();
                }
            }, "json");
        }
    }, "static");
}

function searchAccount() {
    var query = $("#addPersonalDiv").find("input[name='query']").val();
    console.log(query)
    if(!query) {
        showDialog("请输入被添加人员的微信昵称或电话号码（需要绑定）查询", "操作提示");
    } else {
        $.post("/wx/finance/queryAccount", {query: query}, function(res) {
            if(!res || res.length<=0) {
                $("#addPersonalDiv").find(".showAccounts").html("未检索到相应微信用户");
            } else {
                var html = '';
                for(var i=0;i<res.length;i++) {
                    var account = res[i];
                    html += '<button onclick="choicePersonal(this)" class="btn btn-default" openid="'+account.openid+'" style="margin:4px;"><img src="'+account.headimgurl+'" style="width:26px"/>   '+account.nickname+'</button>';
                }
                $("#addPersonalDiv").find(".showAccounts").html(html);
            }
            console.log(res);
        }, "json");
    }
}

function choicePersonal(obj) {
    var target = $("#addPersonalDiv").find(".showAccounts");
    $(target).find(".btn-primary").removeClass("btn-primary");
    $(obj).addClass("btn-primary");
    $("#addPersonalDiv").find("input[name='openid']").val($(obj).attr("openid"));
}

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

function uploadImage(files, detailId, title, index) {
//console.log(file)
    var file = files[index];
    var formData = new FormData();
    formData.append("file", file);
    formData.append("objId", detailId);
    formData.append("title", title);
    //选择文件后，需要显示提示信息
    $(".upload-remind-div").css("display", "block");
    $(".upload-remind-div").html("正在上传"+(index+1)+"/"+files.length+"，请稍候...");
    $.ajax({
        url: "/wx/finance/upload",
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
            } else if(code=='-5') {
                alert(res.msg+"。你太坏了，居然想重复上传"); //window.location.reload();
            }
            //console.log("suc", res);
        },
        complete: function(res) {
            //console.log("complete", res);
            index ++;
            if(files.length>index) {
                uploadImage(files, detailId, title, index);
            } else {
                alert("上传完成"); window.location.reload();
            }
        }
    })
}

function cancelFinance(id) {
    confirmDialog("确定要取消报账吗？取消后此申请将全部作废", "系统提示", function() {
        $.post("/wx/finance/cancel", {id: id}, function(res) {alert("操作成功！"); window.location.reload();})
    }, "static");
}