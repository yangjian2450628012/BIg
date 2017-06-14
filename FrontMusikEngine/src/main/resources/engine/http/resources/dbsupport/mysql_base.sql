CREATE TABLE  If Not Exists `ENGINE_SUPPORT_BASE` (
`id`  int NOT NULL AUTO_INCREMENT COMMENT 'major key' ,
`java_output_path`  varchar(100) NOT NULL COMMENT 'engine java output path' ,
`package_path`  varchar(50) NOT NULL COMMENT 'engine package path' ,
`version`  varchar(20) NOT NULL COMMENT 'engine config version' ,
PRIMARY KEY (`id`)
)
;

CREATE TABLE IF NOT TXISTS `ENGINE_BULID_HISTORY`(

);