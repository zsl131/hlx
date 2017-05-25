$(function() {
    var isAdmin = $("input[name='isAdmin']").val();
    $(".single-feedback").click(function() {
        var thisObj = $(this);
        var objId = $(thisObj).attr("objId");
        if(isAdmin=='1') {
            text = $(thisObj).html();
            var html = text+
                    '<div><textarea class="form-control" style="height:100px; width:100%; resize:none" name="reply"></textarea></div>';
            var myDialog = confirmDialog(html, '<i class="fa fa-pencil"></i> 回复', function() {
                var reply = $(myDialog).find("textarea").val();
                $.post("/wx/feedback/update", {id:objId, reply:reply}, function(res) {
                    if(res=='1') {
                        if(!$(thisObj).find(".rep-con").html()) {
                            window.location.reload();
                        }
                        $(thisObj).find(".rep-con").html(reply);
                    }
                }, "json");
                $(myDialog).remove();
            });

            $(myDialog).find("textarea").val($(myDialog).find(".reply").find(".rep-con").html());
            $(myDialog).find(".reply").remove();
        }
    });
});