$(function() {
    $("select[name='cateId']").change(function() {
        setCateName();
    });
    setCateName();
});

function setCateName() {
    $("input[name='cateName']").val($("select[name='cateId']").find("option:selected").text());
}