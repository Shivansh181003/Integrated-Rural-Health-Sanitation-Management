INSERT INTO appointment (id, resident_id, resident_name, centre_id, centre_name, village, worker_id, worker_name, appointment_date, time_slot, token_no, reason, status) VALUES
(1,  1, 'Lakshmi R',    2, 'PHC Gudiyatham',  'Gudiyatham',  1, 'Dr. Priya Raman',  '2026-10-09', '09:30', 1, 'Fever for 3 days',          'COMPLETED'),
(2,  2, 'Murugan K',    2, 'PHC Gudiyatham',  'Gudiyatham',  1, 'Dr. Priya Raman',  '2026-10-09', '10:00', 2, 'Cough and cold',            'COMPLETED'),
(3,  3, 'Selvi M',      1, 'AAM Katpadi',     'Katpadi',     3, 'Lakshmi Devi',     '2026-10-10', '09:00', 1, 'Antenatal check-up',        'COMPLETED'),
(4,  4, 'Ramesh P',     1, 'AAM Katpadi',     'Katpadi',     2, 'Karthik S',        '2026-10-10', '11:00', 2, 'BP and sugar screening',    'CANCELLED'),
(5,  5, 'Kavitha S',    3, 'AAM Anaicut',     'Anaicut',     4, 'Ravi Kumar',       '2026-10-11', '09:00', 1, 'Skin rash',                 'CHECKED_IN'),
(6,  6, 'Arjun V',      4, 'CHC Pernambut',   'Pernambut',   5, 'Dr. Arun Prakash', '2026-10-11', '08:30', 1, 'Chest pain, referred',      'BOOKED'),
(7,  7, 'Meenakshi T',  2, 'PHC Gudiyatham',  'Gudiyatham',  1, 'Dr. Priya Raman',  '2026-10-11', '09:30', 1, 'Diarrhoea, 2 days',         'BOOKED'),
(8,  8, 'Ganesh B',     5, 'AAM Kaniyambadi', 'Kaniyambadi', 7, 'Suresh M',         '2026-10-11', '10:00', 1, 'Follow-up after camp',      'BOOKED'),
(9,  1, 'Lakshmi R',    2, 'PHC Gudiyatham',  'Gudiyatham',  1, 'Dr. Priya Raman',  '2026-10-12', '09:30', 1, 'Fever follow-up',           'BOOKED'),
(10, 3, 'Selvi M',      1, 'AAM Katpadi',     'Katpadi',     3, 'Lakshmi Devi',     '2026-10-13', '09:00', 1, 'Immunisation for newborn',  'BOOKED')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('appointment', 'id'), (SELECT MAX(id) FROM appointment));