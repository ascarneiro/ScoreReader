/*
 * query for computing statistics of testset
 */

-- view for prefiltering
create view musicsample_view as 
    select * from musicsample where filename not like 'morestuff/%';

select sum(m.staffs) as "staffs", 
    round(sum(100*cast(m.staffs as numeric)/s.x),2) as "percentage", 
    t.name as "type"from musicsample_view m, notationtype t, 
    (select sum(staffs) as x from musicsample_view) s 
    where m.typenr=t.nr group by 3
union all
select sum(staffs), '100.00', 'total sum' from musicsample_view
order by 2 desc;

select sum(m.staffs) as "staffs", 
    round(sum(100*cast(m.staffs as numeric)/s.x),2) as "percentage", 
    p.name as "program"from musicsample_view m, notationprogram p, 
    (select sum(staffs) as x from musicsample_view) s 
    where m.programnr=p.nr group by 3
union all
select sum(staffs), '100.00', 'total sum' from musicsample_view 
order by 2 desc;

drop view musicsample_view;
