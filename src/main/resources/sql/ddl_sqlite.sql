
-- drop table if exists web_config_element;
-- create table If Not Exists web_config_element
--(element_parent varchar(20) , -- comment '父元素',
-- element_seq varchar(20) NOT NULL DEFAULT '0' , -- comment '序号',
-- element_id varchar(20) not null , -- comment 'id',
-- element_name varchar(30) , -- comment '名称',
-- element_area varchar(30) , -- comment '放置区域',
-- element_type varchar(10) , -- comment '元素类型',
-- element_class varchar(100) , -- comment '外部样式',
-- element_prompt varchar(100) , -- comment '提示内容',
-- element_value_key varchar(200) , -- comment '可选值key',
-- element_req_name varchar(20) , -- comment '请求ID',
-- element_req_type varchar(20) , -- comment '请求ID',
-- element_width int(8) DEFAULT 0 , -- comment '宽度',
-- element_height int(8) DEFAULT 0  -- comment '高度',
-- ); -- comment '页面元素配置表';

 drop table if exists web_element;
 create table If Not Exists web_element
 (id varchar(10) not null ,-- comment 'id',
  name varchar(50) ,-- comment '显示名称',
  function varchar(30) ,-- comment '所属功能',
  area varchar(20) ,-- comment '放置区域',
  type varchar(10), -- comment '元素类型',
  data varchar(200), -- comment '数据',
  attr varchar(200) ,-- comment '属性',
  event varchar(200), -- comment '事件',
 primary key (id) ) ; -- COMMENT '页面元素配置表';

-- drop table if exists web_config_enum;
create table If Not Exists web_config_enum
(enum_key varchar(20) not null , -- comment '关键字',
 enum_seq int default 0 , -- comment '序号',
 enum_value varchar(20) , -- comment '值',
 enum_text varchar(255)  -- comment '文本',
); -- comment '枚举配置表';

-- drop table if exists web_config_req;
CREATE TABLE If Not Exists web_config_req (
  req_mapping varchar(20) NOT NULL , -- COMMENT '处理mapping',
  req_name varchar(20) NOT NULL , -- COMMENT '请求名称',
  req_type varchar(10) not null , -- comment '请求类型',
  cur_menu varchar(20) not null , -- comment '当前菜单',
  bean_name varchar(20) not null ); -- comment '处理该事件bean',
--PRIMARY KEY (req_mapping,req_name,req_type) USING BTREE) COMMENT '页面请求配置表';

-- drop table if exists config_database_info;
CREATE TABLE If Not Exists config_database_info (
  database_name varchar(20) NOT NULL , -- comment '数据库名称',
	database_type varchar(10) not null , -- comment '类型',
	database_driver varchar(100) not null,
	database_addr varchar(200) not null , -- comment '地址',
	database_attr varchar(200) not null , -- comment '连接属性',
	database_label varchar(20) , -- comment '子标识(lib/file)',
	login_name varchar(20) , -- comment '登录用户',
	login_password varchar(20)  -- comment '登录密码',
); -- comment '数据源信息表';



-- 模板定义
DROP TABLE IF EXISTS template_define;
CREATE TABLE IF NOT EXISTS template_define (
  template_define_id integer primary key,
  template_name varchar(32) NOT NULL,
  template_desc varchar(200),
  def_type varchar(10) DEFAULT NULL,
  sheet_no int(11) DEFAULT '0',
  pos_row int(11) DEFAULT '0',
  pos_col int(11) DEFAULT '0',
  field_name varchar(50) DEFAULT NULL,
  field_desc varchar(100) DEFAULT NULL,
  field_type varchar(50) DEFAULT NULL,
  data_masking varchar(10) DEFAULT NULL);
CREATE INDEX td_ind_01 on template_define (template_name,def_type);



-- 菜单组
-- menu_group	group_desc
DROP TABLE IF EXISTS web_menu_group_info;
CREATE TABLE IF NOT EXISTS web_menu_group_info (
  web_menu_group_info_id integer primary key,
  menu_group varchar(32) NOT NULL,
  group_desc varchar(200));
CREATE INDEX wmg_ind_01 on web_menu_group_info (menu_group);

-- 菜单
-- menu	menu_desc	menu_group
DROP TABLE IF EXISTS web_menu_info;
CREATE TABLE IF NOT EXISTS web_menu_info (
  web_menu_info_id integer primary key,
  menu_group varchar(32),
  menu_seq integer default 0,
  menu varchar(32) NOT NULL,
  menu_desc varchar(32) );
CREATE INDEX wmi_ind_01 on web_menu_info (menu_group,menu_seq);
CREATE INDEX wmi_ind_02 on web_menu_info (menu);

-- 页面元素
-- menu	element_seq	element_id	area	sub_area	element_type	element_desc
DROP TABLE IF EXISTS web_element_info;
CREATE TABLE IF NOT EXISTS web_element_info (
  web_element_info_id integer primary key,
  menu varchar(32) NOT NULL,
  page varchar(32) NOT NULL,
  element_seq integer default 0,
  element varchar(32),
  element_type varchar(32),
  element_desc varchar(200) );
CREATE INDEX wei_ind_01 on web_element_info (menu,element_seq);
CREATE INDEX wei_ind_02 on web_element_info (element);

-- 事件
-- menu	element	event_type	request_type	request_no	param
DROP TABLE IF EXISTS web_event_info;
CREATE TABLE IF NOT EXISTS web_event_info (
  web_event_info_id integer primary key,
  menu varchar(32) NOT NULL,
  page varchar(32) NOT NULL,
  element varchar(32),
  event_type varchar(32),
  request_type varchar(32),
  request_bean varchar(32),
  trigger_type varchar(32),
  trigger_element varchar(32),
  trigger_element_type varchar(32),
  param varchar(500) );
CREATE INDEX wev_ind_01 on web_event_info (menu,element);



-- 数据字典
DROP TABLE IF EXISTS sys_code_library;
CREATE TABLE IF NOT EXISTS sys_code_library (
  sys_code_library_id integer primary key,
  code_no varchar(100) NOT NULL,
  sub_code varchar(100),
  item_no varchar(500),
  item_name varchar(500),
  item_desc varchar(500));
CREATE INDEX scl_ind_01 on sys_code_library (code_no,sub_code);

-- 系统参数
DROP TABLE IF EXISTS sys_param;
CREATE TABLE IF NOT EXISTS sys_param (
  sys_param_id integer primary key,
  param_level1 varchar(100) NOT NULL,
  param_level2 varchar(500),
  param_level3 varchar(500),
  param_name varchar(500),
  param_value1 varchar(500),
  param_value2 varchar(500),
  param_value3 varchar(500),
  param_desc varchar(500));
CREATE INDEX sp_ind_01 on sys_param (param_level1,param_level2,param_level3);




-- 20211208 backup
CREATE TABLE web_config_enum
(enum_key varchar(20) not null , -- comment '关键字',
 enum_seq int default 0 , -- comment '序号',
 enum_value varchar(20) , -- comment '值',
 enum_text varchar(255)
)
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('Charset', 1, '1', 'utf-8');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('Charset', 2, '2', 'gbk');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('Charset', 3, '3', 'gb2312');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FilePath', 1, 'photo', 'E:\\照片');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FilePath', 2, 'ftpdir', 'E:\\ftp_dir');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FileType', 1, '0', 'all');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FileType', 2, '1', 'photo');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FileType', 3, '2', 'zip');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FileType', 4, '3', 'dir');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('FileType', 5, '4', 'file');
INSERT INTO "main"."web_config_enum" ("enum_key", "enum_seq", "enum_value", "enum_text") VALUES ('LibName', 1, '1', 'hlhome');


CREATE TABLE "web_config_req" (
  "web_config_req_id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "menu" varchar(20) NOT NULL,
  "req_type" varchar(10) NOT NULL,
  "req_name" varchar(20) NOT NULL,
  "bean_name" varchar(20) NOT NULL
)
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (1, 'M001', 'webReq', 'displayDir', 'displayDir');
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (2, 'M002', 'webReq', 'queryTable', 'queryTable');
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (3, 'M003', 'webReq', 'readHtml', 'readHtml');
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (4, 'M004', 'webReq', 'queryTableRecords', 'queryTableRecords');
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (5, 'M002', 'modalReq', 'insertRecord', 'insertRecord');
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (6, 'M002', 'modalReq', 'updateRecord', 'updateRecord');
INSERT INTO "main"."web_config_req" ("web_config_req_id", "menu", "req_type", "req_name", "bean_name") VALUES (7, 'M002', 'modalReq', 'deleteRecord', 'deleteRecord');

CREATE TABLE "web_element" (
  "web_element_id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  "function" varchar(30) NOT NULL,
  "seq" varchar(10),
  "id" varchar(20) NOT NULL,
  "area" varchar(20),
	"window" varchar(20),
  "type" varchar(10),
  "prompt" varchar(50),
  "data" varchar(200),
  "event" varchar(200),
	"attr" varchar(200)
)
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (11, 'root', '1', 'menu_mainPage', 'menuArea', NULL, 'menu', '主页', NULL, '{"eventList":[{"event":"click","type":"initPageInfo","id":""}]}', 'class="menuDropButton"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (13, 'root', '2', 'menu_group_table', 'menuArea', NULL, 'menu', '数据表操作', NULL, NULL, 'class="menuDropButton"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (14, 'root', '3', 'menu_group_caes', 'menuArea', NULL, 'menu', '核算数据查询', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (18, 'root', '98', 'menu_grp_oth', 'menuArea', NULL, 'menu', '其它', NULL, NULL, 'class="menuDropButton"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (19, 'root', '99', 'menu_aboutPage', 'menuArea', NULL, 'menu', '关于', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"about"}]}', 'class="menuDropButton"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (21, 'menu_group_table', '1', 'maintineTable', 'menuArea', NULL, 'menu', '维护数据表', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"maintineTable"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (22, 'menu_group_table', '2', 'queryTable', 'menuArea', NULL, 'menu', '查询数据表', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"queryTable"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (23, 'menu_group_table', '3', 'genClass', 'menuArea', NULL, 'menu', '类代码生成', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"genClass"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (24, 'menu_group_table', '4', 'getTableCreateDdl', 'menuArea', NULL, 'menu', '数据表创建ddl', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"getTableCreateDdl"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (25, 'menu_group_table', '5', 'queryTableWithCond', 'menuArea', NULL, 'menu', '条件查询数据表', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"queryTableWithCond"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (31, 'menu_group_caes', '1', 'codeLibraryQuery', 'menuArea', NULL, 'menu', '核算参数表查询', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"codeLibraryQuery"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (32, 'menu_group_caes', '2', 'subjectQuery', 'menuArea', NULL, 'menu', '科目查询', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"querySubject"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (81, 'menu_grp_oth', '1', 'queryDir', 'menuArea', NULL, 'menu', '浏览文件夹', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"queryDir"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (82, 'menu_grp_oth', '2', 'httpCrawler', 'menuArea', NULL, 'menu', '网络爬虫', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"httpCrawler"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (83, 'menu_grp_oth', '3', 'queryGit', 'menuArea', NULL, 'menu', 'Git历史查询', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"gitHistoryQuery"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (84, 'menu_grp_oth', '4', 'queryHomeIp', 'menuArea', NULL, 'menu', '查询IP', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"queryHomeIp"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (85, 'menu_grp_oth', '5', 'prcStringUtil', 'menuArea', NULL, 'menu', '处理字符串', '', '{"eventList":[{"event":"click","type":"menuReq","id":"prcStringUtil"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (86, 'menu_grp_oth', '6', 'uploadFile', 'menuArea', NULL, 'menu', '上传文件', NULL, '{"eventList":[{"event":"click","type":"menuReq","id":"uploadFile"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (87, 'menu_grp_oth', '7', 'spdbClaimOverdue', 'menuArea', '', 'menu', '浦发理赔逾期天数处理', '', '{"eventList":[{"event":"click","type":"menuReq","id":"spdbClaimOverdue"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2101, 'maintineTable', '1', 'dbName', 'inputArea', NULL, 'inputDataList', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2102, 'maintineTable', '2', 'libName', 'inputArea', NULL, 'inputDataList', '库名', '', '{"eventList":[{"event":"change","type":"webDataReq","id":"getTabFromLib","relEleId":"tableName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:200px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2103, 'maintineTable', '3', 'tableName', 'inputArea', NULL, 'inputDataList', '表名', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2104, 'maintineTable', '4', 'queryButton', 'inputArea', NULL, 'button', '查询', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"queryTable"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2105, 'maintineTable', '1', 'editButton', 'outputArea', NULL, 'button', 'edit', NULL, '{"eventList":[{"event":"click","type":"webButtonShowModal","id":"editRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2106, 'maintineTable', '1', 'delButton', 'modalArea', NULL, 'button', '删除', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"deleteRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2107, 'maintineTable', '2', 'updButton', 'modalArea', NULL, 'button', '更新', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"updateTableField"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2201, 'queryTable', '1', 'dbName', 'inputArea', NULL, 'inputDataList', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2202, 'queryTable', '2', 'libName', 'inputArea', NULL, 'inputDataList', '库名', '', '{"eventList":[{"event":"change","type":"webDataReq","id":"getTabFromLib","relEleId":"tableName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:200px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2203, 'queryTable', '3', 'tableName', 'inputArea', NULL, 'inputDataList', '表名', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2204, 'queryTable', '4', 'query', 'inputArea', NULL, 'button', '查询', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"queryTableRecords","withPage":true}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2205, 'queryTable', '5', 'addRecord', 'inputArea', NULL, 'button', '新增记录', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"addNewRecord"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2206, 'queryTable', '1', 'editButton', 'outputArea', NULL, 'button', 'edit', NULL, '{"eventList":[{"event":"click","type":"webButtonShowModal","id":"editRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2207, 'queryTable', '1', 'delButton', 'modalArea', 'editWindow', 'button', '删除', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"deleteRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2208, 'queryTable', '2', 'updButton', 'modalArea', 'editWindow', 'button', '更新', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"updateRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2209, 'queryTable', '3', 'addButton', 'modalArea', 'addWindow', 'button', '新增', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"addRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2301, 'genClass', '1', 'dbName', 'inputArea', NULL, 'inputDataList', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2302, 'genClass', '2', 'libName', 'inputArea', NULL, 'inputDataList', '库名', '', '{"eventList":[{"event":"change","type":"webDataReq","id":"getTabFromLib","relEleId":"tableName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:200px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2303, 'genClass', '3', 'tableName', 'inputArea', NULL, 'inputDataList', '表名', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2399, 'genClass', '99', 'queryButton', 'inputArea', NULL, 'button', '生成代码', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"genClass"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2401, 'getTableCreateDdl', '1', 'dbName', 'inputArea', NULL, 'inputDataList', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2402, 'getTableCreateDdl', '2', 'libName', 'inputArea', NULL, 'inputDataList', '库名', '', '{"eventList":[{"event":"change","type":"webDataReq","id":"getTabFromLib","relEleId":"tableName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:200px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2403, 'getTableCreateDdl', '3', 'tableName', 'inputArea', NULL, 'inputDataList', '表名', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2499, 'getTableCreateDdl', '99', 'getButton', 'inputArea', NULL, 'button', '取数据表创建ddl', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"getTableCreateDdl"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2501, 'queryTableWithCond', '1', 'dbName', 'inputArea', NULL, 'inputDataList', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2502, 'queryTableWithCond', '2', 'libName', 'inputArea', NULL, 'inputDataList', '库名', '', '{"eventList":[{"event":"change","type":"webDataReq","id":"getTabFromLib","relEleId":"tableName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:200px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2503, 'queryTableWithCond', '3', 'tableName', 'inputArea', NULL, 'inputDataList', '表名', NULL, '{"eventList":[{"event":"change","type":"webDataReq","id":"getFieldFromTab","relEleId":"showFields","relEleType":"select","relEleChgType":"multipleSelect"},{"event":"change","type":"webDataReq","id":"getFieldFromTab","relEleId":"selectField","relEleType":"select","relEleChgType":"selectOption"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2504, 'queryTableWithCond', '4', 'showFields', 'inputArea', NULL, 'multipleSelect', '显示字段', NULL, NULL, 'style=width:180px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2505, 'queryTableWithCond', '5', 'selectField', 'inputArea', NULL, 'selectOption', '筛选条件：字段名', NULL, NULL, 'style=width:180px,display=block');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2506, 'queryTableWithCond', '6', 'operationType', 'inputArea', NULL, 'inputDataList', '连接类型', '{"dataType":"optionList","optionList":[{"value":"=","text":"="},{"value":"LIKE","text":"LIKE"},{"value":">","text":">"},{"value":"<","text":"<"}]}', NULL, 'style=width:80px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2507, 'queryTableWithCond', '7', 'fieldValue', 'inputArea', NULL, 'input', '值', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2511, 'queryTableWithCond', '11', 'editButton', 'outputArea', NULL, 'button', 'edit', NULL, '{"eventList":[{"event":"click","type":"webButtonShowModal","id":"editRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2521, 'queryTableWithCond', '21', 'delButton', 'modalArea', 'editWindow', 'button', '删除', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"deleteRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2522, 'queryTableWithCond', '22', 'updButton', 'modalArea', 'editWindow', 'button', '更新', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"updateRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2523, 'queryTableWithCond', '23', 'addButton', 'modalArea', 'addWindow', 'button', '新增', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"addRecord"}]}', 'class="button button1"');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2598, 'queryTableWithCond', '98', 'addRecord', 'inputArea', NULL, 'button', '新增记录', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"addNewRecord"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (2599, 'queryTableWithCond', '99', 'query', 'inputArea', NULL, 'button', '查询', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"queryTableWithCond","withPage":true}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3101, 'codeLibraryQuery', '1', 'dbName', 'inputArea', NULL, 'inputDataList', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3102, 'codeLibraryQuery', '2', 'codeNo', 'inputArea', NULL, 'dropDown', 'CodeNo', '{"dataType":"optionList","optionList":[{"value":"LineNo","text":"合作机构"},{"value":"CollaborateMode","text":"合作模式"},{"value":"LoanStatus","text":"贷款状态"},{"value":"BillType","text":"单据类型"},{"value":"TransId","text":"交易类型"},{"value":"BranchName","text":"分公司"},{"value":"BankCode","text":"银行代码"},{"value":"TopBusinessType","text":"一级产品"},{"value":"AccountOwner","text":"归属类型"},{"value":"TransChannel","text":"交易渠道"},{"value":"LoanFeeType","text":"费用类型"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3199, 'codeLibraryQuery', '99', 'queryButton', 'inputArea', NULL, 'button', '查询', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"queryCodeLibrary"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3201, 'querySubject', '1', 'dbName', 'inputArea', NULL, 'selectOption', '数据库', '{"dataType":"sql","sql":"select database_name value,database_name show from config_database_info order by config_database_info_id"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getLibFromDb","relEleId":"libName","relEleType":"select","relEleChgType":"inputDataList"}]}', 'style=width:120px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3202, 'querySubject', '2', 'libName', 'inputArea', NULL, 'inputDataList', '库名', '', '{"eventList":[{"event":"change","type":"webDataReq","id":"getDataWithSelectDb","relEleId":"modelId","relEleType":"select","relEleChgType":"selectOption","paramMap":{"sql":"select model_id value,name from acct_model_organization where record_ind=''A''"}},{"event":"change","type":"webDataReq","id":"getDataWithSelectDb","relEleId":"orgId","relEleType":"select","relEleChgType":"selectOption","paramMap":{"sql":"select item_no value,item_name name from code_library where code_no=''BranchName'' and record_ind=''A''"}},{"event":"change","type":"webDataReq","id":"getDataWithSelectDb","relEleId":"owner","relEleType":"select","relEleChgType":"selectOption","paramMap":{"sql":"select item_no value,item_name name from code_library where code_no=''AccountOwner'' and record_ind=''A''"}},{"event":"change","type":"webDataReq","id":"getDataWithSelectDb","relEleId":"channel","relEleType":"select","relEleChgType":"selectOption","paramMap":{"sql":"select item_no value,item_name name from code_library where code_no=''TransChannel'' and record_ind=''A''"}}]}', 'style=width:200px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3203, 'querySubject', '3', 'modelId', 'inputArea', NULL, 'selectOption', '核算办法id', '{"dataType":"sql","sql":"select item_no value,item_name text from code_library where code_no=''LineNo'' and record_ind=''A''","dataBase":"#dbName","libName":"#libName"}', '{"eventList":[{"event":"change","type":"webDataReq","id":"getDataWithSelectDb","relEleId":"transId","relEleType":"select","relEleChgType":"selectOption","paramMap":{"sql":"select te.trans_id value,cl.item_name name from trans_entry te,code_library cl where te.trans_id=cl.item_no and cl.code_no=''TransId''","sqlParam":{"te.model_id":"#modelId"},"sqlOrder":"order by te.trans_id","sqlGroup":"group by te.trans_id,cl.item_name","specialValue":{"all":"所有"}}}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3204, 'querySubject', '4', 'orgId', 'inputArea', NULL, 'selectOption', '分公司', '{"dataType":"sql","sql":"select item_no value,item_name text from code_library where code_no=''LineNo'' and record_ind=''A''","dataBase":"#dbName","libName":"#libName"}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3205, 'querySubject', '5', 'owner', 'inputArea', NULL, 'selectOption', '归属', '{"dataType":"sql","sql":"select item_no value,item_name text from code_library where code_no=''LineNo'' and record_ind=''A''","dataBase":"#dbName","libName":"#libName"}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3206, 'querySubject', '6', 'channel', 'inputArea', NULL, 'selectOption', '交易渠道', '{"dataType":"sql","sql":"select item_no value,item_name text from code_library where code_no=''LineNo'' and record_ind=''A''","dataBase":"#dbName","libName":"#libName"}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3207, 'querySubject', '7', 'freeTax', 'inputArea', NULL, 'selectOption', '免税', '{"dataType":"optionList","optionList":[{"value":"Y","text":"是"},{"value":"N","text":"否"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3208, 'querySubject', '8', 'transId', 'inputArea', NULL, 'selectOption', '交易类型', '{"dataType":"optionList","optionList":[{"value":"all","text":"所有"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (3299, 'querySubject', '99', 'queryButton', 'inputArea', NULL, 'button', '查询', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"querySubject"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8101, 'queryDir', '1', 'localAddress', 'inputArea', NULL, 'inputDataList', '文件夹位置', '{"dataType":"optionList","optionList":[{"value":"D:\\test_img","text":"test_img"},{"value":"D:\\test_img2","text":"test_img2"},{"value":"E:\\t","text":"E:\\t"},{"value":"F:\\娱乐\\movies","text":"F:\\娱乐\\movies"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8102, 'queryDir', '2', 'selectFileType', 'inputArea', NULL, 'dropDown', '筛选类型', '{"dataType":"optionList","optionList":[{"value":"all","text":"显示所有"},{"value":"photo","text":"图片"},{"value":"video","text":"视频"},{"value":"file","text":"文件"},{"value":"dir","text":"文件夹"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8199, 'queryDir', '99', 'queryButton', 'inputArea', NULL, 'button', '查询', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"displayDir"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8201, 'httpCrawler', '1', 'httpAddress', 'inputArea', NULL, 'input', 'http地址', NULL, NULL, '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8202, 'httpCrawler', '2', 'httpAddressSuffixBeg', 'inputArea', NULL, 'input', 'http地址前缀', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8203, 'httpCrawler', '3', 'httpAddressSuffixEnd', 'inputArea', NULL, 'input', 'http地址后缀', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8204, 'httpCrawler', '4', 'webCharset', 'inputArea', NULL, 'dropDown', '编码', '{"dataType":"optionList","optionList":[{"value":"GBK","text":"GBK"},{"value":"UTF-8","text":"UTF-8"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8205, 'httpCrawler', '5', 'regex', 'inputArea', NULL, 'inputDataList', '正则表达式', '{"dataType":"inputDataList","optionList":[{"value":"","text":"p"},{"value":"","text":"td"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8299, 'httpCrawler', '99', 'query', 'inputArea', NULL, 'button', '执行', '', '{"eventList":[{"event":"click","type":"buttonReq","id":"readHtml"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8301, 'gitHistoryQuery', '1', 'dir', 'inputArea', NULL, 'selectOption', 'Git仓库本地路径', '{"dataType":"fun","funBean":"getGitDirList"}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8302, 'gitHistoryQuery', '2', 'ignoreMerge', 'inputArea', NULL, 'selectOption', '是否忽略Merge记录', '{"dataType":"optionList","optionList":[{"value":"Y","text":"是"},{"value":"N","text":"否"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8303, 'gitHistoryQuery', '3', 'user', 'inputArea', NULL, 'input', '用户', '{"defaultValueObject":{"type":"value","value":"huangyuanwei"}}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8304, 'gitHistoryQuery', '4', 'begTime', 'inputArea', NULL, 'input', '开始时间', '{"defaultValueObject":{"type":"QLExpress","express":"java.time.LocalDateTime.now().plusDays(-15).format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\"))"}}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8305, 'gitHistoryQuery', '5', 'endTime', 'inputArea', NULL, 'input', '结束时间', '{"defaultValueObject":{"type":"QLExpress","express":"java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\"))"}}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8306, 'gitHistoryQuery', '6', 'groupBy', 'inputArea', NULL, 'selectOption', '统计方式', '{"dataType":"optionList","optionList":[{"value":"commitList","text":"提交的事务"},{"value":"fileList","text":"以文件统计"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8399, 'gitHistoryQuery', '99', 'queryButton', 'inputArea', NULL, 'button', '查询', '', '{"eventList":[{"event":"click","type":"buttonReq","id":"queryGitHistory"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8499, 'queryHomeIp', '99', 'queryButton', 'inputArea', NULL, 'button', '查询', '', '{"eventList":[{"event":"click","type":"buttonReq","id":"queryHomeIp"}]}', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8501, 'prcStringUtil', '1', 'inputStr', 'inputArea', NULL, 'input', '字符', '', NULL, 'style=width:600px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8502, 'prcStringUtil', '2', 'addString', 'inputArea', NULL, 'selectOption', '加', '{"dataType":"optionList","optionList":[{"value":"single","text":"单引号"},{"value":"double","text":"双引号"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8503, 'prcStringUtil', '3', 'separator', 'inputArea', NULL, 'selectOption', '分隔符', '{"dataType":"optionList","optionList":[{"value":"comma","text":"逗号"},{"value":"semicolon","text":"分号"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8504, 'prcStringUtil', '4', 'colNum', 'inputArea', NULL, 'input', '分成几列', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8599, 'prcStringUtil', '99', 'process', 'inputArea', NULL, 'button', '处理', NULL, '{"eventList":[{"event":"click","type":"buttonReq","id":"prcStringUtil"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8601, 'uploadFile', '1', 'file', 'inputArea', NULL, 'inputFile', '选择文件', NULL, NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8602, 'uploadFile', '2', 'fileType', 'inputArea', NULL, 'selectOption', '文件类型', '{"dataType":"optionList","optionList":[{"value":"spd_claim","text":"浦发理赔"},{"value":"yapi2postman","text":"yapiJSON转postmanJSON"}]}', NULL, NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8603, 'uploadFile', '3', 'sheetNo', 'inputArea', '', 'input', 'sheetNo', '', '', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8699, 'uploadFile', '99', 'uploadFileButton', 'inputArea', NULL, 'button', '确认上传', NULL, '{"eventList":[{"event":"click","type":"fileReq","id":"uploadFile"}]}', NULL);
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8701, 'spdbClaimOverdue', '1', 'file', 'inputArea', '', 'inputFile', '选择文件', '', '', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8702, 'spdbClaimOverdue', '2', 'sheetNo', 'inputArea', '', 'input', 'sheetNo', '', '', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8703, 'spdbClaimOverdue', '3', 'begLineNo', 'inputArea', '', 'input', '开始行号', '', '', '');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8704, 'spdbClaimOverdue', '4', 'fieldInfoList', 'inputArea', '', 'input', '字段信息', '', '', 'style=width:1800px');
INSERT INTO "main"."web_element" ("web_element_id", "function", "seq", "id", "area", "window", "type", "prompt", "data", "event", "attr") VALUES (8799, 'spdbClaimOverdue', '99', 'prcButton', 'inputArea', '', 'button', '确认上传', '', '{"eventList":[{"event":"click","type":"fileReq","id":"spdbClaimOverdue"}]}', '');
