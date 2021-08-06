
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