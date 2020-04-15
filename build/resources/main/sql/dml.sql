delete from web_config_element;
INSERT INTO web_config_element(element_parent, element_seq, element_id, element_name, element_area, element_type, element_class, element_prompt, element_value_key, element_req_name, element_req_type, element_width, element_height) VALUES
('M001', '1', 'E0001', 'localAddress',         'inputArea', 'dropDown', NULL, '本地地址',  'FilePath', NULL,NULL, 0, 0),
('M001', '2', 'E0002', 'selectFileType',       'inputArea', 'dropDown', NULL, '文件类型',  'FileType', NULL,NULL, 0, 0),
('M001', '3', 'E0003', 'queryButton',          'inputArea', 'button',   NULL, '查询',      NULL, 'displayDir', 'webReq',0, 0),
('M002', '1', 'E0004', 'libName',              'inputArea', 'dropDown', NULL, '数据库',    'LibName', NULL, NULL,0, 0),
('M002', '2', 'E0005', 'tableName',            'inputArea', 'dropDown', NULL, '数据表',    'sql:select name from sqlite_master WHERE type = "table"', NULL,NULL, 0, 0),
('M002', '3', 'E0006', 'queryButton',          'inputArea', 'button',   NULL, '查询',      NULL, 'queryTable','webReq', 0, 0),
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
INSERT INTO config_database_info(database_name, database_type, database_driver,database_addr, database_label, login_name, login_password) VALUES
('local_sqlite', 'sqlite', 'org.sqlite.JDBC','jdbc:sqlite::resource:sqlitedb/hlhome.db', '', '', ''),
('local_mysql', 'mysql', 'com.mysql.cj.jdbc.Driver','jdbc:mysql://localhost:3306/hlhome?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC', 'hlhome', 'root', 'Root#98e'),
('dev_caes', 'mysql', 'com.mysql.cj.jdbc.Driver','jdbc:mysql://10.20.16.9:6100/caes?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai', 'caes', 'deployop', 'Kkccf#24kk');