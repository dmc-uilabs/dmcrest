 select gjr.request_date as event_date, concat('invited to project ', g.group_name, '(', g.group_id, ') by ', ur.realname) as message from groups as g, group_join_request as gjr, users as ur where gjr.user_id = 102 and g.group_id = gjr.group_id and gjr.reject_date is null and gjr.accept_date is null and gjr.requester_id = ur.user_id and g.group_id not in (select group_id from group_join_request                        where user_id = 102 and (accept_date is not null or reject_date is not null))  
 UNION 
 select gjr.accept_date as event_date, concat('joined project ', g.group_name, '(', g.group_id, ')') as message from groups as g, group_join_request as gjr where gjr.user_id = 102 and g.group_id = gjr.group_id and gjr.accept_date is not null  
 UNION 
 select gjr.reject_date as event_date, concat('declined project invitation for ', g.group_name, '(', g.group_id, ')') as message from groups as g, group_join_request as gjr where gjr.user_id = 102 and g.group_id = gjr.group_id and gjr.reject_date is not null 
 UNION
 select to_timestamp(to_number(d.created_at, '99999999999999999999')) as event_date, concat('started "', title, '" discussion in project ', g.group_name, ' [p', g.group_id, ' d',d.id,']') as message
from individual_discussions d, groups g
where d.project_id = g.group_id
and d.account_id = 550
union
 select to_timestamp(to_number(d.created_at, '99999999999999999999')) as event_date, concat('started "', title, '" public discussion [d',d.id,']') as message
from individual_discussions d
where d.project_id is null
and d.account_id = 550
union
 select to_timestamp(to_number(c.created_at, '99999999999999999999')) as event_date, concat('commented on "', title, '" ', g.group_name, ' project discussion [p', g.group_id, ' d',d.id, ' c',c.id,']') as message
from individual_discussions d, groups g, individual_discussions_comments c
where d.project_id = g.group_id
and c.individual_discussion_id = d.id
and d.account_id = 550
union
 select to_timestamp(to_number(c.created_at, '99999999999999999999')) as event_date, concat('commented on "', title, '" public discussion [d',d.id, ' c',c.id,']') as message
from individual_discussions d, individual_discussions_comments c
where d.project_id is null
and c.individual_discussion_id = d.id
and d.account_id = 550
union
select s.release_date + time '00:00' as event_date, concat('service \"', s.title, '\" released ') as message 
from service s 
where s.release_date is not null 
and owner_id = 102 
union
select sr.start_date + time '00:00' as event_date, concat('service \"', s.title, '\" run started [r',sr.run_id,']') as message 
from service s, service_run sr 
where s.service_id = sr.service_id 
and sr.start_date is not null 
and run_by = 550
union
select sr.stop_date + time '00:00' as event_date, concat('service \"', s.title, '\" run stopped [r',sr.run_id,']') as message 
from service s, service_run sr 
where s.service_id = sr.service_id 
and sr.stop_date is not null 
and run_by = 555

 ORDER BY message DESC
 