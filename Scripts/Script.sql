select attach_file,url,c.* from process."case" c 
where 1=1
--and attach_file notnull 
and protocol__c = 'HMB8739974017'
;
update process."case" set attach_file = null
where 1=1
--and attach_file notnull 
and protocol__c = 'HMB8739974017'

;
select * from process.if_info_logs iil 
order by created_at desc

