-- 配置修改mysql的配置文件 my.ini 计算机\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\MySQL57(找到imagePath里面的文件路径 )


drop table if EXISTS `movie_type`;
create table `movie_type` (
`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '表主键',  
 `name` varchar(20) not null COMMENT '影片类型',
 `type` int(2) not null COMMENT '类型',
`create_time` datetime not null COMMENT '创建时间',
 PRIMARY KEY (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


drop table if EXISTS `movie_classification`;
create table `movie_classification` (
`id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '表主键',  
 `name` varchar(20) not null COMMENT '影片分类(比如，新片，热片)',
 `classification_type` int(2) not null COMMENT '类型',
`create_time` datetime not null COMMENT '创建时间',
 PRIMARY KEY (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `my_orders`;  
CREATE TABLE `my_orders` (    ---创建订单表
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '表主键',  
  `pid` int(10) unsigned NOT NULL COMMENT '产品ID',  
  `price` decimal(15,2) NOT NULL COMMENT '单价',  
  `num` int(11) NOT NULL COMMENT '购买数量',  
  `uid` int(10) unsigned NOT NULL COMMENT '客户ID',  
  `atime` datetime NOT NULL COMMENT '下单时间',  
  `utime` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '修改时间',  
  `isdel` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除标识',  
  PRIMARY KEY (`id`,`atime`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8  
  
/*********分区信息**************/  
PARTITION BY RANGE (YEAR(atime))  
(  
   PARTITION p0 VALUES LESS THAN (2016),  
   PARTITION p1 VALUES LESS THAN (2017),  
   PARTITION p2 VALUES LESS THAN MAXVALUE  
);  
EXPLAIN PARTITIONS SELECT * FROM `my_orders`  --- 执行创建表语句过程执行计划

--爬虫后信息
drop table if EXISTS `movie_detail_splider`;
create table `movie_detail_splider`(
`id` int(10) unsigned not null AUTO_INCREMENT comment '主键id',
`movie_name` VARCHAR (50) not null comment '影片名',
`image` VARCHAR (50) not null comment '影片图片路径',
`translation` VARCHAR (50) comment '译名',
`upload_time` datetime not null comment '发布时间',
`years` datetime not null comment '年代',
`country` VARCHAR (10) not null comment '国家',
`language` VARCHAR (10) not null comment '语言',
`subtitle` VARCHAR (10) comment '字幕',
`file_format` VARCHAR (10) comment '文件格式',
`video_size` VARCHAR (10) comment '视频尺寸',
 `file_zise` VARCHAR (6) comment '文件大小',
  `movie_long` VARCHAR (5) comment '片长',
  `director` VARCHAR (10) not null comment '导演',
  `brief_introduction` VARCHAR (150) not null comment '简介',
  `url` VARCHAR (100) not null comment '对应ftp下载路径',
   `release_time` datetime not null comment '上映时间',
   `success_status` char(2) not null comment '是否下载成功',
   PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;
/* 影片下载成功后保存信息 */
drop table if EXISTS  `movie_detail`;
create table `movie_detail`(
`id` int(10) unsigned not null AUTO_INCREMENT comment '主键id',
`movie_name` VARCHAR (50) not null comment '影片名',
`image` VARCHAR (50) not null comment '影片图片路径',
`translation` VARCHAR (50) comment '译名',
`movie_type` VARCHAR (10) not null comment '影片类型',
`classification_type` VARCHAR (10) comment '影片分类',
`upload_time` datetime not null comment '发布时间',
`years` datetime not null comment '年代',
`country` VARCHAR (10) not null comment '国家',
`language` VARCHAR (10) not null comment '语言',
`subtitle` VARCHAR (10) comment '字幕',
`file_format` VARCHAR (10) comment '文件格式',
`video_size` VARCHAR (10) comment '视频尺寸',
 `file_zise` VARCHAR (6) comment '文件大小',
  `movie_long` VARCHAR (5) comment '片长',
  `director` VARCHAR (10) not null comment '导演',
  `brief_introduction` VARCHAR (150) not null comment '简介',
  `url` VARCHAR (100) not null comment '影片存放位置',
   `release_time` datetime not null comment '上映时间',
    PRIMARY KEY (`id`,`years`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
PARTITION BY RANGE (YEAR(years))
(
  partition p0 values less than (2014),
  partition p1 VALUES less than (2015),
   PARTITION p2 VALUES LESS THAN (2016),
   PARTITION p3 VALUES LESS THAN (2017),
   PARTITION p4 VALUES LESS THAN MAXVALUE
);

DROP TABLE IF EXISTS `my_orders_notpart`;  
-- 创建为分区表
CREATE TABLE `my_orders_notpart` (    
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '表主键',  
  `pid` int(10) unsigned NOT NULL COMMENT '产品ID',  
  `price` decimal(15,2) NOT NULL COMMENT '单价',  
  `num` int(11) NOT NULL COMMENT '购买数量',  
  `uid` int(10) unsigned NOT NULL COMMENT '客户ID',  
  `atime` datetime NOT NULL COMMENT '下单时间',  
  `utime` int(10) unsigned NOT NULL DEFAULT 0 COMMENT '修改时间',  
  `isdel` tinyint(4) NOT NULL DEFAULT '0' COMMENT '软删除标识',  
  PRIMARY KEY (`id`,`atime`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;  

/**************************向分区表插入数据****************************/  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,CURRENT_TIMESTAMP());  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,'2016-05-01 00:00:00');  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,'2017-05-01 00:00:00');  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,'2018-05-01 00:00:00');  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2015-05-01 00:00:00');  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2016-05-01 00:00:00');  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2017-05-01 00:00:00');  
INSERT INTO my_orders(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2018-05-01 00:00:00');  

/**************************向未分区表插入数据****************************/  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,CURRENT_TIMESTAMP());  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,'2016-05-01 00:00:00');  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,'2017-05-01 00:00:00');  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89757,'2018-05-01 00:00:00');  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2015-05-01 00:00:00');  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2016-05-01 00:00:00');  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2017-05-01 00:00:00');  
INSERT INTO my_orders_notpart(`pid`,`price`,`num`,`uid`,`atime`) VALUES(1,12.23,1,89756,'2018-05-01 00:00:00');  

/**********************************主从复制大量数据******************************/  
INSERT INTO `my_orders`(`pid`,`price`,`num`,`uid`,`atime`) SELECT `pid`,`price`,`num`,`uid`,`atime` FROM `my_orders`;  
INSERT INTO `my_orders_notpart`(`pid`,`price`,`num`,`uid`,`atime`) SELECT `pid`,`price`,`num`,`uid`,`atime` FROM `my_orders_notpart`; 

 ---插入2097152条数据

/***************************查询性能分析**************************************/  
SELECT * FROM `my_orders` PARTITION(p1) WHERE `uid`=89757 AND `atime`< CURRENT_TIMESTAMP();  ---指定分区 用时 2.489s 
select * from my_orders where atime < STR_TO_DATE('2017-1-1','%Y-%m-%d %H:%i:%s') 
	and atime > STR_TO_DATE('2015-12-31','%Y-%m-%d %H:%i:%s');  --- 用普通的方式查询
  
SELECT * FROM `my_orders_notpart` WHERE `uid`=89757 AND `atime`< CURRENT_TIMESTAMP();   --- 用时2.283s

---- 查看分区情况
EXPLAIN PARTITIONS SELECT * FROM `my_orders` WHERE `uid`=89757 AND `atime`< CURRENT_TIMESTAMP();



