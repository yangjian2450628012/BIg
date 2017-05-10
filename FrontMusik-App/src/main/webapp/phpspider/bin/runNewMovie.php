<?php
ini_set("memory_limit", "1024M");
require dirname(__FILE__).'/../core/init.php';

/* Do NOT delete this comment */
/* 不要删除这段注释 */

$configs = array(
	'name' => '电影天堂',
	'log_show' => true, //显示爬区面板
	//'log_file' => data/movie.log,  //日志存放地址
	'log_type' => 'info,warn,debug,error' , //日志类型
	'input_encoding' => 'GB2312',
	'output_encoding' => 'GB2312', //输入输出编码
	'tasknum' => 5 , // 同时工作的爬虫任务数
	//'multiserver' => true, //多服务器处理
	'export' => array(
	    'type' => 'db',
	    'table' => 'movie_detail_splider' // 如果数据表没有数据新增请检查表结构和字段名是否匹配
	),
	'domains' => array(
	    "www.dy2018.com"
	),
	'scan_urls' => array(
        'http://www.dy2018.com/html/gndy/dyzz/index.html'
	),
	'list_url_regexes' => array(
        'http://www.dy2018.com/html/gndy/dyzz/index\d+.html'
    ),
    'content_url_regexes' => array(
        'http://www.dy2018.com/i/\d+.html'
    ),
    'fields' => array(
        // 片名
        array(
            'name' => "movie_name",
            'selector' => "//*[@id='Zoom']/p[3]",
            'required' => true,
        ),
        //图片
        array(
            'name' => "image",
            'selector' => "//*[@id='Zoom']/p[1]//@src",
            'required' => true,
        ),
        //译名
        array(
            'name' => "translation",
            'selector' => "//*[@id='Zoom']/p[2]",
            'required' => true,
        ),
         //上传时间
        array(
            'name' => "upload_time",
            'selector' => "//*[@id='header']/div/div[3]/div[2]/div[6]/div[2]/ul/div[1]/span[3]",
            'required' => true,
        ),
         //年代
        array(
            'name' => "years",
            'selector' => "//*[@id='Zoom']/p[4]",
            'required' => true,
        ),
         //国家       
        array(
            'name' => "country",
            'selector' => "//*[@id='Zoom']/p[5]",
            'required' => true,
        ),
        //影片分类       
        array(
            'name' => "classification_type",
            'selector' => "//*[@id='Zoom']/p[6]",
            'required' => true,
        ),
        //语言      
        array(
            'name' => "language",
            'selector' => "//*[@id='Zoom']/p[7]",
            'required' => true,
        ),
        //字幕    
        array(
            'name' => "subtitle",
            'selector' => "//*[@id='Zoom']/p[8]",
            'required' => true,
        ),
        //文件格式 
        array(
            'name' => "file_format",
            'selector' => "//*[@id='Zoom']/p[12]",
            'required' => true,
        ),
        //视频尺寸 
        array(
            'name' => "video_size",
            'selector' => "//*[@id='Zoom']/p[13]",
            'required' => true,
        ),
        //文件大小 
        array(
            'name' => "file_zise",
            'selector' => "//*[@id='Zoom']/p[14]",
            'required' => true,
        ),
        //片长
        array(
            'name' => "movie_long",
            'selector' => "//*[@id='Zoom']/p[15]",
            'required' => true,
        ),
        //导演
        array(
            'name' => "director",
            'selector' => "//*[@id='Zoom']/p[16]",
            'required' => true,
        ),
        //url
        array(
            'name' => "url",
            'selector' => "//*[@id='Zoom']//a[contains(@href,'ftp')]",
            'required' => true,
        ),
        //发布时间
        array(
            'name' => "release_time",
            'selector' => "//*[@id='Zoom']/p[9]",
            'required' => true,
        ),
        /*//主演
        array(
            'name' => "mainactor",
            'selector' => "//*[@id='Zoom']/p[17]/following-sibling::p",
            'required' => true,
        ),*/
        //简介
        array(
            'name' => "brief_introduction",
            'selector' => "//*[@id='Zoom']//p[text()='◎简　　介']/following-sibling::p[1]",
            'required' => true,
        ),
        
    ),
);


$spider = new phpspider($configs);
$spider->on_extract_field = function($fieldname, $data, $page) {
    if($fieldname == 'mainactor'){
        echo 'mainactor...................';
        $da = $data['mainactor'];
        for ($i=0; $i < count($da); $i++) { 
            echo $i;
        }
       /* $mainactor = '';
        for ($i=0; $i < count($data['mainactor']); $i++) { 
            $mainactor = $mainactor.$data['mainactor'][$i].',';
        }
        $data['mainactor'] = $mainactor;*/
    }
    return $data;
};
$spider->start();


