"use strict"
var engine = layui.use(['layer','element','form','laytpl','tree'], function() {
    var $ = layui.jquery, layer = layui.layer;
    //触发事件
    var active = {
        setTop: function () {
            // var that = this;
            //多窗口模式，层叠置顶
            layer.open({
                type: 2 //此处以iframe举例
                ,title: '生成代码'
                ,area: ['1000px', '500px']
                ,shade: 0
                ,maxmin: true
                ,offset: 'auto'
                ,content: 'code.html'
                ,btn: ['确认生成', '全部关闭']
                ,yes: function(){
                    alert("确认生成");
                }
                ,btn2: function(){
                    layer.closeAll();
                }
                ,zIndex: layer.zIndex //重点1
                ,success: function(layero){
                    layer.setTop(layero); //重点2
                }
            });
        }
    }

    $('.site-engine-button .layui-btn').on('click', function(){
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });
    
});

