-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO k8s_thoughts (id, thought, author, day) VALUES 
(nextval('k8s_thoughts_seq'), 'The best way to predict the future is to create it.', 'Abraham Lincoln', '2024-05-01'),
(nextval('k8s_thoughts_seq'), 'Innovation distinguishes between a leader and a follower.', 'Steve Jobs', '2024-05-02'),
(nextval('k8s_thoughts_seq'), 'The only way to do great work is to love what you do.', 'Steve Jobs', '2024-05-03'),
(nextval('k8s_thoughts_seq'), 'Success is not final, failure is not fatal: it is the courage to continue that counts.', 'Winston Churchill', '2024-05-04'),
(nextval('k8s_thoughts_seq'), 'The future belongs to those who believe in the beauty of their dreams.', 'Eleanor Roosevelt', '2024-05-05');