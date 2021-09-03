
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



-- 交易数据
DROP TABLE IF EXISTS mgt_trx_data;
CREATE TABLE mgt_trx_data  (
    mgt_trx_data_id integer primary key,
    mgt_status VARCHAR(10) DEFAULT NULL,--  COMMENT '迁移状态',
    loan_no VARCHAR(40) DEFAULT NULL,--  COMMENT '贷款编号',
    dd_flag VARCHAR(5) DEFAULT NULL,--  COMMENT '是否已代垫',
    bill_type VARCHAR(5) DEFAULT NULL,--  COMMENT '单据类型',
    sterm INT(10) DEFAULT 0,--  COMMENT '期次',
    pay_date DATE DEFAULT NULL,--  COMMENT '应还日期',
    acc_date DATE DEFAULT NULL,--  COMMENT '该期最后还款日期',
    pay_corp DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还本金',
    actual_corp DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还本金',
    pay_inte DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还利息',
    actual_inte DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还利息',
    pay_fine DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还融通罚息',
    actual_fine DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还融通罚息',
    pay_compound DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还融通复利',
    actual_compound DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还融通复利',
    pay_bank_fine DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还银行罚息',
    actual_bank_fine DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还银行罚息',
    pay_bank_compound DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还银行复利',
    actual_bank_compound DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还银行复利',

    dd_pay_fine DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还代垫罚息',
    dd_actual_fine DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还代垫罚息',
    dd_pay_compound DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还代垫复利',
    dd_actual_compound DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还代垫复利',
    fee_type VARCHAR(5) DEFAULT NULL,--  COMMENT '费用类型',
    pay_amount DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '应还费用',
    actual_amount DECIMAL(18,4) DEFAULT 0.0000,--  COMMENT '实还费用',
    acc_user VARCHAR(50) DEFAULT NULL,--  COMMENT '制单人',
    deduct_channel VARCHAR(50) DEFAULT NULL,--  COMMENT '扣款渠道',
    mgt_data_path VARCHAR(200) DEFAULT NULL);
CREATE INDEX mtd_ind_01 on mgt_trx_data (loan_no);



-- 本息还款计划
DROP TABLE IF EXISTS mgt_pay_plan;
CREATE TABLE mgt_pay_plan  (
    mgt_pay_plan_id integer primary key,
    mgt_status VARCHAR(10) ,--  COMMENT '迁移状态',
    loan_no VARCHAR(40) ,-- COMMENT '贷款编号',
    sterm INT(10) DEFAULT 0, --  COMMENT '期次',
    pay_date DATE DEFAULT NULL ,-- COMMENT '应还日期',
    acc_date DATE DEFAULT NULL ,-- COMMENT '该期最后还款日期',
    pay_corp DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '应还本金',
    actual_corp DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '实还本金',
    pay_inte DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '应还利息',
    actual_inte DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '实还利息',
    pay_bank_fine DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '应还银行罚息',
    actual_bank_fine DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '实还银行罚息',
    pay_bank_compound DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '应还银行复利',
    actual_bank_compound DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '实还银行复利',
    pay_fine DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '应还融通罚息',
    actual_fine DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '实还融通罚息',
    pay_compound DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '应还融通复利',
    actual_compound DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '实还融通复利',
    dd_corp DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '代垫本金',
    dd_inte DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '代垫利息',
    dd_fine DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '代垫罚息',
    dd_compound DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '代垫复利',
    fine_base DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '罚息基数',
    compound_base DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '复利基数',
    dd_flag VARCHAR(5) ,-- COMMENT '是否已代垫',
    fine_date DATE DEFAULT NULL ,-- COMMENT '罚复日期',
    month_pay DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '月供',
    owe_balance DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '剩余本金',
    business_sum DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '贷款金额',
    execute_rate DECIMAL(18,4) DEFAULT 0.0000 ,--  COMMENT '利率',
    loan_term INT(10) DEFAULT 0 ,--  COMMENT '期限',
    putout_date DATE DEFAULT NULL ,-- COMMENT '放款日期',
    mgt_data_path VARCHAR(200));
CREATE INDEX mpp_ind_01 on mgt_pay_plan (loan_no);


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
  element_seq integer default 0,
  element varchar(32),
  area varchar(32),
  sub_area varchar(32),
  element_type varchar(32),
  element_desc varchar(200) );
CREATE INDEX wei_ind_01 on web_element_info (menu,element_seq);
CREATE INDEX wei_ind_02 on web_element_info (element);
CREATE INDEX wei_ind_03 on web_element_info (area,sub_area);

-- 事件
-- menu	element	event_type	request_type	request_no	param
DROP TABLE IF EXISTS web_event_info;
CREATE TABLE IF NOT EXISTS web_event_info (
  web_event_info_id integer primary key,
  menu varchar(32) NOT NULL,
  element varchar(32),
  event_type varchar(32),
  request_type varchar(32),
  request_no varchar(32),
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