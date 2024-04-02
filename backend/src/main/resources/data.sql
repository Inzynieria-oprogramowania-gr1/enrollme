INSERT INTO mydb.students(email, role)
VALUES ('zarzitski@student.agh.edu.pl',0),
       ('zolszowka@student.agh.edu.pl',0),
       ('pkuchta@student.agh.edu.pl',0),
       ('Asobiesiak@student.agh.edu.pl',0),
       ('amezydlo@student.agh.edu.pl',0),
       ('test@test.com',0),
       ('test2@test.com',0),
       ('miidzik@agh.edu.pl',1);


INSERT INTO mydb.enrollments VALUES ();
SET @enrollment_id := LAST_INSERT_ID();

INSERT INTO mydb.timeslots(weekday, start_time, end_time, is_selected, enrollment_id)
SELECT w.weekday,
       t.start_time,
       t.end_time,
       false,
       @enrollment_id
FROM (SELECT 0 AS weekday UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) w
         CROSS JOIN
     (SELECT '08:00:00' AS start_time, '09:30:00' AS end_time
      UNION ALL
      SELECT '09:45:00', '11:15:00'
      UNION ALL
      SELECT '11:30:00', '13:00:00'
      UNION ALL
      SELECT '13:15:00', '14:45:00'
      UNION ALL
      SELECT '15:00:00', '16:30:00'
      UNION ALL
      SELECT '16:45:00', '18:15:00'
      UNION ALL
      SELECT '18:30:00', '20:00:00') t;


