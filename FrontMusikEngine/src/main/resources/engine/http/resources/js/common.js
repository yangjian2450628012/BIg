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
				var treeDatas,contentUrl;
                engine.jquery.getJSON(options.treeUrl,function(r){
                    if(r.ResultCode !=1 )return;
                    var data = JSON.parse(r.Content);

					treeDatas = data.tree;
                    contentUrl = options.url + "?v=1.0.0";
                    data.firstTemplate?contentUrl = contentUrl + "&templatePath="+data.firstTemplate : null;
                    data.jar_path?contentUrl = contentUrl+"&jar_path="+data.jar_path : null;
                    options.readOnly?contentUrl=contentUrl+"&readOnly=true":null;

                    var index = engine.layer.open({
                        type: 2
                        , id: 'templateIfream'
                        , title: options['windowTitle'] ? options['windowTitle'] : '模板窗口'
                        , area: ['1200px', '500px']
                        , shade: 0
                        , maxmin: true
                        , offset: 'auto'
                        , content: contentUrl
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
                            //getTree.call(layero,options);
                            engine.jquery(layero).find('#templateIfream iframe').css({width:'83%'});
                            engine.jquery(layero).find('#templateIfream')
                                .prepend('<div style="display: inline-block;width: 15%;padding: 10px;overflow: auto;float: left;">' +
									'<ul id="freemarker_ul"></ul></div>');
                            engine.tree({
                                elem: '#freemarker_ul'
                                //, target: '_blank'
                                , skin: 'templateTree'
                                , click: function (item) {
                                    if(item.children == undefined && typeof item.params == "string"){
                                        var params = item.params;
                                        console.log(ifream);
                                    }
                                }
                                , nodes: treeDatas
                            });
                            console.log("加载成功");
                        }
                        , end : function(){
                            layer.close(index);
                        }
                    });
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
                            , skin: 'templateTree'
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
