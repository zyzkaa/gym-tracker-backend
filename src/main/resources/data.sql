insert into weekdays (name) values
    ('Monday'),
    ('Tuesday'),
    ('Wednesday'),
    ('Thursday'),
    ('Friday'),
    ('Saturday'),
    ('Sunday');

COPY public.muscles (id, name)
    FROM 'D:\studia\inzynierka\demo\src\database\muscles.csv'
    WITH (FORMAT csv, HEADER true, DELIMITER ';', NULL '', ENCODING 'UTF8');

COPY public.exercises (id, name)
    FROM 'D:\studia\inzynierka\demo\src\database\exercises.csv'
    WITH (FORMAT csv, HEADER true, DELIMITER ';', NULL '', ENCODING 'UTF8');

COPY public.muscles_exercises (muscle_id, exercise_id)
    FROM 'D:\studia\inzynierka\demo\src\database\muscles_exercises.csv'
    WITH (FORMAT csv, HEADER true, DELIMITER ';', NULL '', ENCODING 'UTF8');