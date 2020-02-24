$(function() {
    var myEditormd = editormd("my-editormd", {
        width           : "100%",
        dialogMaskBgColor : "#000",
        height          : "600px",
        //autoHeight      : true,
        path            : "/editor.md-master/lib/",
        htmlDecode      : "style,script,iframe",
        placeholder     : "请输入文章内容，是以Markdown富文本形式书写哦~~",
        imageUpload : true,
        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        saveHTMLToTextarea : true,    // 保存 HTML 到 Textarea
        tocm            : true,         // Using [TOCM]
        tex             : true,
        emoji           : true,
        taskList        : true,
        flowChart       : true,
        sequenceDiagram : true,
        toolbar         : false
    });

    $("#dataForm").submit(function() {
        var title = $("input[name='title']").val();
        if(title==null || $.trim(title)=='') {showDialog("请输入文章标题！"); return false;}

        var content = myEditormd.getHTML();
        var mdCon = myEditormd.getMarkdown();
        if(content==null || $.trim(content)=='') {
            showDialog("请认真输入文章内容！"); return false;
        }

        $("textarea[name='content']").text(content);
        $("textarea[name='mdContent']").text(mdCon);

        return true;
    });
});