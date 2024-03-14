-- INSERT INTO mydb.students(email)
-- VALUES ('zarzitski@student.agh.edu.pl'),
-- ('zolszowka@student.agh.edu.pl'), 
-- ('pkuchta@student.agh.edu.pl'), 
-- ('Asobiesiak@student.agh.edu.pl'),
-- ('amezydlo@student.agh.edu.pl');

INSERT INTO mydb.students(email)
VALUES ('zarzitski@student.agh.edu.pl'),
       ('zolszowka@student.agh.edu.pl'),
       ('pkuchta@student.agh.edu.pl'),
       ('Asobiesiak@student.agh.edu.pl'),
       ('amezydlo@student.agh.edu.pl'),
       ('test@test.com');


INSERT INTO mydb.timetable(start_time, end_time, is_selected, week_day)
SELECT start_time, end_time, false, week_day
FROM (
         SELECT '8:00:00' AS start_time, '9:30:00' AS end_time
         UNION ALL SELECT '9:45:00', '11:15:00'
         UNION ALL SELECT '11:30:00', '13:00:00'
         UNION ALL SELECT '13:15:00', '14:45:00'
         UNION ALL SELECT '15:00:00', '16:30:00'
         UNION ALL SELECT '16:45:00', '18:15:00'
         UNION ALL SELECT '18:30:00', '20:00:00'
     ) AS time_slots
         CROSS JOIN (
    SELECT 'pn' AS week_day
    UNION ALL SELECT 'wt'
    UNION ALL SELECT 'sr'
    UNION ALL SELECT 'czw'
    UNION ALL SELECT 'pt'
) AS days_of_week;



--
-- INSERT INTO mydb.timetable(start_time, end_time, is_selected, week_day)
-- VALUES ('8:00:00', '9:30:00', false, 'pn'),
--        ('8:00:00', '9:30:00', false, 'wt'),
--        ('8:00:00', '9:30:00', false, 'sr'),
--        ('8:00:00', '9:30:00', false, 'czw'),
--        ('8:00:00', '9:30:00', false, 'pt');

INSERT INTO mydb.timetable(start_time, end_time, is_selected, week_day)
SELECT start_time, end_time, false, week_day
FROM (
         SELECT '8:00:00' AS start_time, '9:30:00' AS end_time
         UNION ALL SELECT '9:45:00', '11:15:00'
         UNION ALL SELECT '11:30:00', '13:00:00'
         UNION ALL SELECT '13:15:00', '14:45:00'
         UNION ALL SELECT '15:00:00', '16:30:00'
         UNION ALL SELECT '16:45:00', '18:15:00'
         UNION ALL SELECT '18:30:00', '20:00:00'
     ) AS time_slots
         CROSS JOIN (
    SELECT 'pn' AS week_day
    UNION ALL SELECT 'wt'
    UNION ALL SELECT 'sr'
    UNION ALL SELECT 'czw'
    UNION ALL SELECT 'pt'
) AS days_of_week;