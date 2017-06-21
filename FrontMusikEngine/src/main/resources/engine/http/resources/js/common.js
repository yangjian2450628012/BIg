/* engine-v1.0.0 MIT License By http://www.yobbo.tech */
var engine = layui.use(['layer','tree'],function(){
	engine.jquery('.openTemplate-a,.openJavaBase-a').on('click',function(){
		var othis = engine.jquery(this),options = othis.data();
		if(typeof options.options == "string"){
			var v = "{"+options.options+"}",options = eval('('+ v +')');
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
                    //data.jar_path?contentUrl = contentUrl+"&jar_path="+data.jar_path : null;
                    data.prefix?contentUrl = contentUrl+"&prefix="+data.prefix : null;
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
                            var callback = options['callback'];
                            callback ? engine[callback].call(this,options) : null;
                            alert("确认生成");
                        }
                        , btn2: function(){
                            layer.close(index);
                        }
                        , zIndex: layer.zIndex
                        , success: function(layero){
                            // 此success回调方法，每次加载ifream都会执行一次
                            //getTree.call(layero,options);
                            engine.jquery(layero).find('#templateIfream iframe').css({width:'84%'});
                            if(engine.jquery("#freemarker_ul").html() == undefined){
                                engine.jquery(layero).find('#templateIfream')
                                    .prepend('<div style="display: inline-block;width: 14%;padding: 10px;overflow: auto;float: left;">' +
                                        '<ul id="freemarker_ul"></ul></div>');
                                engine.tree({
                                    elem: '#freemarker_ul'
                                    , skin: 'templateTree'
                                    , click: function (item) {
                                        if(item.children == undefined && typeof item.params == "string"){
                                            var params = item.params,prefix = item.prefix;
                                            contentUrl = contentUrl.replace(new RegExp("templatePath=[A-Za-z0-9_/]+.ftl"),"templatePath="+params);
                                            contentUrl = contentUrl.replace(new RegExp("prefix=[A-Za-z0-9/]+"),"prefix="+prefix);
                                            engine.jquery(layero).find('#templateIfream iframe').attr("src",contentUrl);
                                        }
                                    }
                                    , nodes: treeDatas
                                });
                            }
                        }
                        , end : function(){
                            layer.close(index);
                        }
                    });
                });
			},
            openJavaBase : function (options) {
                var treeDatas,contentUrl;
                engine.jquery.getJSON(options.treeUrl,function (r) {
                    if(r.ResultCode !=1 )return;
                    var data = JSON.parse(r.Content);
                    treeDatas = data.tree;
                    contentUrl = options.url + "?v=1.0.0";
                    data.firstJavaBase?contentUrl=contentUrl + "&java_base_path="+data.firstJavaBase:null;
                    options.readOnly?contentUrl=contentUrl+"&readOnly=true":null;

                    var index = engine.layer.open({
                        type: 2
                        , id: 'javaBaseIfream'
                        , title: options['windowTitle'] ? options['windowTitle'] : '窗口'
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
                            engine.jquery(layero).find('#javaBaseIfream iframe').css({width:'81%'});
                            if(engine.jquery("#javaBaseTree_ul").html() == undefined){
                                engine.jquery(layero).find('#javaBaseIfream')
                                    .prepend('<div style="display: inline-block;width: 17%;padding: 10px;overflow: auto;float: left;">' +
                                        '<ul id="javaBaseTree_ul"></ul></div>');
                                engine.tree({
                                    elem: '#javaBaseTree_ul'
                                    , skin: 'templateTree'
                                    , click: function (item) {
                                        if(item.children == undefined && typeof item.path == "string"){
                                            contentUrl = contentUrl.replace(new RegExp("java_base_path=[A-Za-z0-9_/:\u4E00-\u9FA5]+.java"),"java_base_path="+item.path);
                                            engine.jquery(layero).find('#javaBaseIfream iframe').attr("src",contentUrl);
                                        }
                                    }
                                    , nodes: treeDatas
                                });
                            }
                        }
                        , end : function(){
                            layer.close(index);
                        }
                    });
                });
            }
		}
}();
