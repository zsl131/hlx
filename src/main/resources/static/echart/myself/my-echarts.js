$(function() {
    var amount = $("#amount").val();
    //buildCountData(amount, "orderTypePie");

    $(".show-pie").each(function() {
        var href = $(this).attr("href")+"?amount="+amount;
        buildCountData(href, $(this).attr("id"));
    })

    $(".show-bar").each(function() {
        var href = $(this).attr("href")+"?amount="+amount;
        buildBarData(href, $(this).attr("id"));
    });
});

function buildBarData(href, id) {
    //alert(href);
    var myChart = echarts.init(document.getElementById(id));
    $.get(href).done(function (data) {
        var firstName = '';
        var showDatas = '[';

        for(var i=0;i<data.valList.length;i++) {
            var obj = data.valList[i];
            if(i==0) {firstName = obj.name;}
            showDatas += '{'+
                'name:"'+obj.name+'",'+
                'type:"bar",'+
                'data:['+obj.values+"],"+
                'stack: "'+firstName+'",'+
                'itemStyle:{normal:{label:{show: true,formatter: "{c}"}}}'+
            '},';
        }

        showDatas = showDatas.substring(0, showDatas.length-1);

        showDatas += "]";

//alert(showDatas);
//console.log(data.cateList);
//console.log(showDatas);

        myChart.setOption({
            title : {
                text: data.title,
                subtext: data.subTitle,
                x:'center'
            },
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            /*legend: {
                data:data.legend
            },*/
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : data.cateList
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : eval('(' + showDatas + ')')
        });
    });
}

function buildCountData(href, id) {
    var myChart = echarts.init(document.getElementById(id));
    $.get(href).done(function (data) {
        myChart.setOption({
            title : {
                text: data.title,
                subtext: data.subTitle,
                x:'center'
            },
            series : [
                {
                    type: 'pie',
                    radius: '50%',
                    data:data.data,
                    itemStyle:{
                    normal:{
                          label:{
                            show: true,
                            formatter: '{b}: \n{c} \n{d}%'
                          }
                        }
                    }
                }
            ]
        });
    });
}
