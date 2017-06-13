/* engine-v1.0.0 MIT License By http://www.yobbo.tech */
var engine = layui.use(['layer','tree'],function(){
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
				options.readOnly?options.url=options.url+"?readOnly=true":null;
				var index = engine.layer.open({
					type: 2
					, id: 'templateIfream'
					, title: options['windowTitle'] ? options['windowTitle'] : '模板窗口'
					, area: ['1200px', '500px']
					, shade: 0
			        , maxmin: true
			        , offset: 'auto'
			        , content: options.url
			        , btn: ['确认生成', '全部关闭']
			        , yes: function(){
			        	// TODO 获取IFREAM中的内容，回调
			        	var callback = options['callback'];
			        	callback ? engine[callback].call(this,options) : null;
			            alert("确认生成");
			        }
			        , btn2: function(){
			        	layer.close(index);
			        }
			        , zIndex: layer.zIndex
			        , success: function(layero){
			        	getTree.call(layero,options);
			        	// TODO load left tree menu
			        	console.log("加载成功");
			        }
			        , end : function(){
			        	layer.close(index);
			        }
				});
				/* build template left tree*/
				function getTree(options){
					if(!options.treeUrl) return;
					var clazz = this;
					engine.jquery.getJSON(options.treeUrl,function(r){
						if(r.ResultCode !=1 )return;
						var ifream = engine.jquery(clazz).find('.layui-layer-content iframe');
						var ifreamId = ifream.attr("id");
						ifream.css({width:'83%'});
						engine.jquery(clazz).find('.layui-layer-content')
						.prepend('<div style="display: inline-block;width: 15%;padding: 10px;overflow: auto;float: left;"><ul id="freemarker_ul"></ul></div>');
						engine.tree({
                            elem: '#freemarker_ul'
                            //, target: '_blank'
                            , skin: 'shihuang' 
                            , click: function (item) {
                            	if(item.children == undefined && typeof item.params == "string"){
                            		var params = item.params;
                            		console.log(ifream);
                            	}
                            }
                            , nodes: JSON.parse(r.Content)
                        });
					});
				}
			}
		}
}();
