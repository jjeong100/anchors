select * from process.account
limit 1
;
select * from process.if_info_logs iil 
;
select * from process.asset a 
limit 1

;
select count(*) as cnt from information_schema.tables
where table_name = 'file_search'

;
select * from file_search