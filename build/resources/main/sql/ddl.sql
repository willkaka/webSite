
-- drop table if exists web_config_element;
create table If Not Exists web_config_element
(element_parent varchar(20) comment '父元素',
 element_seq varchar(20) NOT NULL DEFAULT '0' COMMENT '序号',
 element_id varchar(20) not null comment 'id',
 element_name varchar(30) comment '名称',
 element_area varchar(30) comment '放置区域',
 element_type varchar(10) comment '元素类型',
 element_class varchar(100) comment '外部样式',
 element_prompt varchar(100) comment '提示内容',
 element_value_key varchar(200) comment '可选值key',
 element_req_name varchar(20) COMMENT '请求名称',
 element_req_type varchar(20) COMMENT '请求类型',
 element_width int(8) DEFAULT 0 COMMENT '宽度',
 element_height int(8) DEFAULT 0 COMMENT '高度',
 primary key (element_parent,element_seq) USING BTREE,
 UNIQUE KEY index_unique (element_id) USING BTREE) COMMENT '页面元素配置表';

-- drop table if exists web_config_enum;
create table If Not Exists web_config_enum
(enum_key varchar(20) not null COMMENT '关键字',
 enum_seq int default 0 COMMENT '序号',
 enum_value varchar(20) COMMENT '值',
 enum_text varchar(255) COMMENT '文本',
primary key (enum_key,enum_seq) USING BTREE) COMMENT '枚举配置表';

-- drop table if exists web_config_req;
CREATE TABLE If Not Exists web_config_req (
  req_mapping varchar(20) NOT NULL COMMENT '处理mapping',
  req_name varchar(20) NOT NULL COMMENT '请求名称',
  req_type varchar(10) not null comment '请求类型',
  cur_menu varchar(20) not null comment '当前菜单',
  bean_name varchar(20) not null comment '处理该事件bean',
PRIMARY KEY (req_mapping,req_name,req_type) USING BTREE) COMMENT '页面请求配置表';

-- drop table if exists config_database_info;
CREATE TABLE If Not Exists config_database_info (
  database_name varchar(20) NOT NULL COMMENT '数据库名称',
  database_type varchar(10) not null comment '类型',
  database_driver varchar(100) not null comment '驱动',
  database_addr varchar(200) not null comment '地址',
  database_label varchar(20) comment '子标识(lib/file)',
  login_name varchar(20) comment '登录用户',
  login_password varchar(20) comment '登录密码',
PRIMARY KEY (database_name) USING BTREE) COMMENT '数据源信息表';

