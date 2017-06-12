/* engine-v1.0.0 MIT License By http://www.yobbo.tech */
var engine = layui.use('layer',function(){
	engine.jquery('.openTemplate-a').on('click',function(){
		var othis = engine.jquery(this),options = othis.data();
		if(typeof options.options == "string"){
			var _v = "{"+options.options+"}",options = eval('('+ _v +')');
		}
		engine.commonMethod[options.method] ? engine.commonMethod[options.method].call(this,options) : null;
	});
});

;!function(){"use strict"
	engine.commonMethod = {
			openTemplate : function(options){
				engine.layer.open({
					type: 2,
					title: options['windowTitle'] ? options['windowTitle'] : '模板窗口',
					area: ['1200px', '500px'],
					shade: 0,
			        maxmin: true,
			        offset: 'auto',
			        content: options.url,
			        btn: ['确认生成', '全部关闭'],
			        yes: function(){
			        	// TODO 获取IFREAM中的内容，回调
			        	var callback = options['callback'];
			        	callback ? engine[callback].call(this,options) : null;
			            alert("确认生成");
			        },
			        btn2: function(){
			            layer.closeAll();
			        },
			        zIndex: layer.zIndex,
			        success: function(layero){
			        	// TODO 加载树形菜单
			        	console.log("加载成功");
			        }
				});
			}
		}
}();
