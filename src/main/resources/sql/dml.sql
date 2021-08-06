delete from web_config_element;
INSERT INTO web_config_element(element_parent, element_seq, element_id, element_name, element_area, element_type, element_class, element_prompt, element_value_key, element_req_name, element_req_type, element_width, element_height) VALUES
('M001', '1', 'E0001', 'localAddress',         'inputArea', 'dropDown', NULL, '本地地址',  'FilePath', NULL,NULL, 0, 0),
('M001', '2', 'E0002', 'selectFileType',       'inputArea', 'dropDown', NULL, '文件类型',  'FileType', NULL,NULL, 0, 0),
('M001', '3', 'E0003', 'queryButton',          'inputArea', 'button',   NULL, '查询',      NULL, 'displayDir', 'webReq',0, 0),

('M002', '1', 'E0013', 'dbName',               'inputArea', 'dropDown', NULL, 'DB名称',    'sql:select database_name from config_database_info', NULL, NULL,0, 0),
('M002', '2', 'E0004', 'libName',              'inputArea', 'dropDown', NULL, '数据库',    '', 'queryDatabase', 'baseDataReq',0, 0),
('M002', '3', 'E0005', 'tableName',            'inputArea', 'dropDown', NULL, '数据表',    'sql:select name from sqlite_master WHERE type = "table"', NULL,NULL, 0, 0),
('M002', '4', 'E0006', 'queryButton',          'inputArea', 'button',   NULL, '查询',      NULL, 'queryTable','webReq', 0, 0),

('M003', '1', 'E0007', 'httpAddress',          'inputArea', 'input',    NULL, '地址',      NULL, '',NULL, 800, 0),
('M003', '2', 'E0008', 'httpAddressSuffixBeg', 'inputArea', 'input',    NULL, '后缀开始',  NULL, NULL,NULL, 0, 0),
('M003', '3', 'E0009', 'httpAddressSuffixEnd', 'inputArea', 'input',    NULL, '后缀结束',  NULL, NULL,NULL, 0, 0),
('M003', '4', 'E0010', 'webCharset',           'inputArea', 'dropDown', NULL, '网页编码',  'Charset', NULL,NULL, 0, 0),
('M003', '5', 'E0011', 'regex',                'inputArea', 'input',    NULL, '正则表达式', NULL, NULL, NULL, 800, 0),
('M003', '6', 'E0012', 'query',                'inputArea', 'button',   NULL, '查询',       NULL, 'readHtml', "webReq",0, 0),
('M004', '1', 'E0013', 'dbName',               'inputArea', 'dropDown', NULL, 'DB名称',     'sql:select database_name from config_database_info', NULL, NULL,0, 0),
('M004', '2', 'E0014', 'tableName',            'inputArea', 'input',    NULL, '数据表',     '', NULL, NULL,200, 0),
('M004', '3', 'E0015', 'query',                'inputArea', 'button',   NULL, '查询',      NULL, "queryTableRecords","webReq", 0, 0),
('M004', '4', 'E0016', 'addRecord',            'inputArea', 'btn_user', NULL, '新增记录',  NULL, 'InsertRecord', 'webReq', 0, 0),

('Window', '1', 'M001', '文件夹查询', 'menuArea', 'menu', 'btn btn-light', '', NULL, NULL,NULL, 0, 0),
('Window', '2', 'M002', '数据表设计', 'menuArea', 'menu', 'btn btn-light', '', NULL, NULL,NULL, 0, 0),
('Window', '3', 'M003', 'http爬虫',   'menuArea', 'menu', 'btn btn-light', '', NULL, NULL,NULL, 0, 0),
('Window', '4', 'M004', '数据维护'  , 'menuArea', 'menu', 'btn btn-light', '', NULL, NULL,NULL, 0, 0);

delete from web_config_enum;
INSERT INTO web_config_enum(enum_key, enum_seq, enum_value, enum_text) VALUES
('Charset', 1, '1', 'utf-8'),
('Charset', 2, '2', 'gbk'),
('Charset', 3, '3', 'gb2312'),
('FilePath', 1, 'photo', 'E:\\照片'),
('FilePath', 2, 'ftpdir', 'E:\\ftp_dir'),
('FileType', 1, '0', 'all'),
('FileType', 2, '1', 'photo'),
('FileType', 3, '2', 'zip'),
('FileType', 4, '3', 'dir'),
('FileType', 5, '4', 'file'),
('LibName', 1, '1', 'hlhome');

delete from web_config_req;
INSERT INTO web_config_req(req_mapping, req_name, req_type, cur_menu, bean_name) VALUES
('clickReq', 'displayDir', 'webReq', 'M001', 'displayDir'),
('clickReq', 'queryTable', 'webReq', 'M002', 'queryTable'),
('clickReq', 'readHtml', 'webReq', 'M003', 'readHtml'),
('clickReq', 'queryTableRecords', 'webReq', 'M004', 'queryTableRecords'),
('clickReq', 'insertRecord', 'modalReq', 'M002', 'insertRecord'),
('clickReq', 'updateRecord', 'modalReq', 'M002', 'updateRecord'),
('clickReq', 'deleteRecord', 'modalReq', 'M002', 'deleteRecord');

delete from config_database_info;
INSERT INTO config_database_info(database_name, database_type, database_driver,database_addr,database_attr, database_label, login_name, login_password) VALUES
('local_sqlite', 'sqlite', 'org.sqlite.JDBC','jdbc:sqlite', '', ':resource:sqlitedb/hlhome.db', '', ''),
('local_mysql', 'mysql', 'com.mysql.cj.jdbc.Driver','jdbc:mysql://localhost:3306','useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC', 'hlhome', 'root', 'Root#98e'),
('local_oracle', 'oracle', 'oracle.jdbc.driver.OracleDriver','jdbc:oracle:thin:@localhost:1521:hlhome', '', 'hlhome', 'root', 'Root#98e'),
('dev_caes', 'mysql', 'com.mysql.cj.jdbc.Driver','jdbc:mysql://10.20.16.9:6100','useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai', 'caes', 'deployop', 'Kkccf#24kk');







-- 迁移excel数据表模板配置
DELETE FROM template_define WHERE template_name in ('mgt_loan_info');
insert into template_define (template_name,template_desc,def_type,sheet_no,pos_row,pos_col,data_masking,field_name,field_desc,field_type) values
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  1, null, 'loan_no', '贷款编号', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  2, null, 'product_id', '产品ID', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  3, null, 'product_version', '产品版本号', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  4, null, 'customer_name', '姓名', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  5, null, 'putout_date', '放款日期', 'date'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  6, null, 'business_channel', '进件渠道', 'string'), -- G
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  7, null, 'area_code', '区域', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  8, null, 'business_sum', '放款金额', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4,  9, null, 'customer_irr', '客户IRR', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 10, null, 'loan_term', '贷款期限', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 11, null, 'maturity_date', '到期日', 'date'), -- 第12/M列为空列
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 13, null, 'return_method', '还款方式', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 14, null, 'pay_day', '还款日', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 15, null, 'execute_rate', '执行利率', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 16, null, 'account_owner_cd', '归属（自营/非自营）', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 17, null, 'fund_org', '资金方', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 18, null, 'line_id', '合作方', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 19, null, 'product_type', '产品类型', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 20, null, 'collaborate', '合作模式', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 21, null, 'first_product', '一级产品', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 22, null, 'fee_string', '费用收取情况', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 23, null, 'prepayment_fee_string', '提前还款违约金收取情况', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 24, null, 'deduct_type', '扣款类型', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 25, null, 'deduct_channel', '扣款渠道', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 26, null, 'deduct_accno', '扣款账号', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 27, null, 'cert_id', '证件号', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 28, null, 'phone', '预留手机', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 29, null, 'bank_code', '总行行号', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 30, null, 'deduct_account_cd', '账号类型', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 31, null, 'loan_status', '贷款状态', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 32, null, 'common_flag', '代偿标识', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 33, null, 'security_fund', '保证金', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 34, null, 'lawsuit_status', '诉讼状态', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 35, null, 'normal_balance', '正常余额', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 36, null, 'overdue_balance', '逾期余额', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 37, null, 'normal_inte', '正常利息', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 38, null, 'fine_inte', '罚息', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 39, null, 'compound', '复利', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 40, null, 'corp_current_overdue_days', '本息当前逾期天数', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 41, null, 'corp_current_overdue_date', '本息当前逾期日期', 'date'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 42, null, 'corp_first_overdue_date', '本息首次逾期日期', 'date'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 43, null, 'fee_current_overdue_days', '费用当前逾期天数', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 44, null, 'fee_current_overdue_date', '费用当前逾期日期', 'date'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 45, null, 'fee_first_overdue_date', '费用首次逾期日期', 'date'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 46, null, 'fine_rate', '罚息利率(年率)', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 47, null, 'inte_rate', '复利利率(年率)', 'number'), -- 第48列为空列
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 49, null, 'cert_type', '证件类型', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 50, null, 'cert_no', '证件号码', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 51, null, 'birthday', '出生年月日', 'date'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 52, null, 'mobile', '手机号', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 53, null, 'home_addr', '家庭地址', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 54, null, 'comp_addr', '单位地址', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 55, null, 'marriage_sts', '婚姻状况', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 56, null, 'career', '职业', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 57, null, 'education', '学历', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 58, null, 'is_farmer', '是否农户(0-否；1-是)', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 59, null, 'guarantee_desc', '抵押物', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 60, null, 'guarantee_rate', '抵押比例', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 61, null, 'appraisal', '评估价', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 62, null, 'customer_manager', '客户经理', 'string'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 64, null, 'business_source', '业务来源', 'string'), -- 客户来源 BM

('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 65, null, 'total_return_rate', '总返点比例', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 66, null, 'return_cash_rate', '现返比例', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 67, null, 'follow_interest_rate', '跟息比例', 'number'),
('mgt_loan_info', '迁移_贷款信息_模板', 'Detail', 0, 4, 68, null, 'follow_interest_terms', '跟息期限', 'number');

DELETE FROM template_define WHERE template_name in ('mgt_pay_plan');
insert into template_define (template_name,template_desc,def_type,sheet_no,pos_row,pos_col,data_masking,field_name,field_desc,field_type) values
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  0, null, 'loan_no', '贷款编号', 'string'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  1, null, 'sterm', '期次', 'string'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  2, null, 'pay_date', '应还日期', 'date'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  3, null, 'acc_date', '记账日期', 'date'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  4, null, 'pay_corp', '应还本金', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  5, null, 'actual_corp', '实还本金', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  6, null, 'pay_inte', '应还利息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  7, null, 'actual_inte', '实还利息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  8, null, 'pay_bank_fine', '应还银行罚息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1,  9, null, 'actual_bank_fine', '实还银行罚息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 10, null, 'pay_bank_compound', '应还银行复利', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 11, null, 'actual_bank_compound', '实还银行复利', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 12, null, 'pay_fine', '应还罚息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 13, null, 'actual_fine', '实还罚息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 14, null, 'pay_compound', '应还复利', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 15, null, 'actual_compound', '实还复利', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 16, null, 'dd_corp', '代垫本金', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 17, null, 'dd_inte', '代垫利息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 18, null, 'dd_fine', '代垫罚息', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 19, null, 'dd_compound', '代垫复利', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 20, null, 'fine_base', '罚息基数', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 21, null, 'compound_base', '复利基数', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 22, null, 'dd_flag', '代垫标志', 'string'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 23, null, 'fine_date', '罚复日期', 'date'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 24, null, 'month_pay', '期供', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 25, null, 'owe_balance', '逾期余额', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 26, null, 'business_sum', '放款金额', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 27, null, 'execute_rate', '执行利率', 'number'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 28, null, 'loan_term', '贷款期限', 'string'),
('mgt_pay_plan', '迁移_本息计划_模板', 'Detail', 0, 1, 29, null, 'putout_date', '放款日期', 'date');

DELETE FROM template_define WHERE template_name in ('mgt_fee_plan');
insert into template_define (template_name,template_desc,def_type,sheet_no,pos_row,pos_col,data_masking,field_name,field_desc,field_type) values
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  0, null, 'loan_no', '贷款编号', 'string'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  1, null, 'sterm', '期次', 'number'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  2, null, 'pay_date', '应还日期', 'date'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  3, null, 'acc_date', '记账日期', 'date'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  4, null, 'fee_type', '费用类型', 'string'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  5, null, 'pay_amount', '应还费用', 'number'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  6, null, 'actual_amount', '实还费用', 'number'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  7, null, 'pay_fine', '应还逾期违约金', 'number'),
('mgt_fee_plan', '迁移_费用计划_模板', 'Detail', 1,  8, null, 'actual_fine', '实还逾期违约金', 'number');

DELETE FROM template_define WHERE template_name in ('mgt_trx_data');
insert into template_define (template_name,template_desc,def_type,sheet_no,pos_row,pos_col,data_masking,field_name,field_desc,field_type) values
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  0, null, 'loan_no' ,'贷款编号', 'string'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  1, null, 'dd_flag' ,'代垫标志', 'string'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  2, null, 'bill_type' ,'单据类型', 'string'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  3, null, 'sterm' ,'期次', 'string'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  4, null, 'pay_date' ,'应还日期', 'date'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  5, null, 'acc_date' ,'记账日期', 'date'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  6, null, 'pay_corp' ,'应还本金', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  7, null, 'actual_corp' ,'实还本金', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  8, null, 'pay_inte' ,'应还利息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1,  9, null, 'actual_inte' ,'实还利息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 10, null, 'pay_fine' ,'应还罚息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 11, null, 'actual_fine' ,'实还罚息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 12, null, 'pay_compound' ,'应还复利', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 13, null, 'actual_compound' ,'实还复利', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 14, null, 'pay_bank_fine' ,'应还银行罚息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 15, null, 'actual_bank_fine' ,'实还银行罚息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 16, null, 'pay_bank_compound' ,'应还银行复利', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 17, null, 'actual_bank_compound' ,'实还银行复利', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 18, null, 'dd_pay_fine' ,'应还代垫罚息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 19, null, 'dd_actual_fine' ,'实还代垫罚息', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 20, null, 'dd_pay_compound' ,'应还代垫复利', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 21, null, 'dd_actual_compound' ,'实还代垫复利', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 22, null, 'fee_type' ,'费用类型', 'string'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 23, null, 'pay_amount' ,'应还费用', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 24, null, 'actual_amount' ,'实还费用', 'number'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 25, null, 'acc_user' ,'制单人', 'string'),
('mgt_trx_data', '迁移_交易信息_模板', 'Detail', 1, 26, null, 'deduct_channel' ,'扣款渠道', 'string');


