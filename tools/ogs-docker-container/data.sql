USE catalog_db;
SET NAMES utf8mb4;

INSERT INTO categories (name, description) VALUES
    ('Logic', 'Развивающие игры на логику'),
    ('Action', 'Игры на реакцию и быстрое мышление, на сообразительность'),
    ('Race', 'Гоночные игры'),
    ('Tools', 'Утилиты'),
    ('Other', 'Игры не подходящие ни под один из разделов');

insert into products (category_id, name, price, stock, description) values
    (2, 'Клад', 12.00, 1000, 'Собери все клады, оставь людоедов голодными!'),
    (2, 'Пилот', 800.00, 8, 'Разбомби вражеский город. Вгони его в каменный век!'),
    (2, 'Питон', 1150.00, 0, 'Проголодался? Самое время перекусить кроликами.'),
    (1, 'Угадай число', 600.00, 4, 'Старая-добрая игра "Быки и коровы"'),
    (2, 'Тяп-ляп', 1210.50, 0, 'Великий и Ужасный... да, это Тетрис'),
    (4, 'Тест', 1000.00, 22, 'Заболел компьютер? Тогда это именно то, что ему нужно, прежде чем отнести его на мусорку.'),
    (1, 'Минер', 980.10, 12, 'Пройди по пастбищу стада коров, но помни, что сапёр ошибается только один раз!'),
    (1, 'Морской бой', 820.00, 6, 'Не умеешь плавать? Тогда потопи врага раньше, чем он тебя'),
    (3, 'Автодром', 920.00, 4, 'Поговаривают, что сам Шумахер учился на этом авто-тренажере.'),
    (5, 'Сура', 400.00, 11, 'Сурских кущ не обещаем, но печатать научим.'),
    (5, 'Танец', 580.00, 3, 'Потанцуем?'),
    (1, 'Шахматы', 1200.00, 3, 'Новое слово в ИИ! Вкуси радость от победы над компьютером.'),
    (2, 'Пожарник', 1200.00, 1, 'Не дай погибнуть в огне домашним животным! Спаси от пожара всех мышек.'),
    (4, 'Тест-Хобби', 1000.00, 22, 'Заболел компьютер? Тогда это именно то, что ему нужно, прежде чем отнести его на мусорку.'),
    (2, 'Алибаба', 800.00, 2, 'Алибаба и много больше 40 разбойников.'),
    (2, 'Binary land', 1050.00, 4, 'Выведи персонажа и его зеркального двойника к выходу'),
    (1, 'Crux', 900.00, 8, 'Лучшая игра из семейства цвето-тетрисовых.'),
    (2, 'Кобра', 1300.00, 4, 'Почти легендарная "Кобра" в действии! Преврати пейзаж в лунный ландшафт.'),
    (2, 'Boulder dash', 1440.00, 2, 'Прадедушка Supaplex. Но в более "ламповом" 4-битном цвете.'),
    (2, 'Bomber man', 700.00, 4, 'Какая же подборка без минёра? Взорви их всех!'),
    (4, 'File manager', 1.00, 100, 'Лучший файловый менеджер для CP/M! Написан на ассемблере. Полная поддержка жестких дисков и пользовательских областей CP/M! К тому же вы всегда можете обратиться с вопросом непосредственно к автору - ко мне :)'),
    (4, 'Image view', 1.00, 100, 'Неплохой вьюнер картинок для ПК8000. Поддерживает все известные науке форматы для 8-битных машин, начиная от ZX и заканчивая MSX. Удобный интерфейс, поддержка жестких дисков и пользовательских областей CP/M.'),
    (5, 'STC Player', 1.00, 100, 'Единственный и неповторимый. Не, правда единственный плеер STC-файлов. Требует наличия AY-контроллера. Автор планировал подключить еще формат PT2(3), но пока у меня руки не доходят.'),
    (2, 'Питон', 100.00, 4, 'Классическая змейка. Главное не откусить свой хвост!'),
    (2, 'Двигалка', 1.00, 100, 'Порт игры с PC. Моя первая игра на PL/M.'),
    (2, 'Bolder dash', 1.00, 100, 'Порт знаменитой игрушки. Мелентьев писал её со слов знакомых и поэтому в название закралась неточность, а игровой процесс стал на порядок интереснее, начиная от сваливающихся на голову камней и заканчивая непредсказуемым поведением бомб и бабочек. Данный же порт идет с порта для PC и написан мной на PL/M.'),
    (3, 'High way', 1400, 4, 'Гонки по городу. Порт с MSX. Собираем списки, деньги, ищем заправки и не попадаемся плохим парням.')
;

insert into product_images (product_id, image_file) values
    (1, 'klad-title.png'),
    (1, 'klad-1.png'),
    (1, 'klad-2.png'),
    (1, 'klad-3.png'),
    (1, 'klad-4.png'),
    (2, 'pilot-title.png'),
    (2, 'pilot-1.png'),
    (2, 'pilot-2.png'),
    (2, 'pilot-3.png'),
    (2, 'pilot-4.png'),
    (2, 'pilot-5.png'),
    (3, 'piton-foton-title.png'),
    (3, 'piton-foton-1.png'),
    (3, 'piton-foton-2.png'),
    (3, 'piton-foton-3.png'),
    (4, 'chislo-title.png'),
    (4, 'chislo-1.png'),
    (4, 'chislo-2.png'),
    (4, 'chislo-3.png'),
    (5, 'typ-lyp-title.png'),
    (5, 'typ-lyp-1.png'),
    (6, 'tect-title.png'),
    (6, 'tect-1.png'),
    (6, 'tect-2.png'),
    (7, 'miner-title.png'),
    (7, 'miner-0.png'),
    (7, 'miner-1.png'),
    (7, 'miner-2.png'),
    (8, 'mor-boy-title.png'),
    (8, 'mor-boy-1.png'),
    (8, 'mor-boy-2.png'),
    (8, 'mor-boy-3.png'),
    (9, 'avto-title.png'),
    (9, 'avto-1.png'),
    (9, 'avto-2.png'),
    (10, 'sura-title.png'),
    (10, 'sura-1.png'),
    (10, 'sura-2.png'),
    (11, 'tanec-3.png'),
    (11, 'tanec-1.png'),
    (11, 'tanec-2.png'),
    (12, 'chess-title.png'),
    (12, 'chess-1.png'),
    (12, 'chess-2.png'),
    (12, 'chess-3.png'),
    (13, 'fire-title.png'),
    (13, 'fire-1.png'),
    (13, 'fire-2.png'),
    (13, 'fire-3.png'),
    (13, 'fire-4.png'),
    (14, 'test-title.png'),
    (14, 'test-1.png'),
    (14, 'test-2.png'),
    (14, 'test-3.png'),
    (14, 'test-4.png'),
    (15, 'alibab-title.png'),
    (15, 'alibab-1.png'),
    (16, 'binary-title.png'),
    (16, 'binary-1.png'),
    (16, 'binary-2.png'),
    (16, 'binary-3.png'),
    (17, 'crux-title.png'),
    (17, 'crux-1.png'),
    (17, 'crux-2.png'),
    (18, 'cobra-1.png'),
    (18, 'cobra-2.png'),
    (19, 'boulder-dash-title.png'),
    (19, 'boulder-dash-1.png'),
    (19, 'boulder-dash-2.png'),
    (20, 'bomb-title-1.png'),
    (20, 'bomb-title.png'),
    (20, 'bobm-1.png'),
    (20, 'bobm-2.png'),
    (21, 'fm-1.png'),
    (21, 'fm-2.png'),
    (21, 'fm-3.png'),
    (21, 'fm-4.png'),
    (21, 'fm-5.png'),
    (21, 'fm-6.png'),
    (21, 'fm-7.png'),
    (22, 'iview-1.png'),
    (22, 'iview-2.png'),
    (22, 'iview-3.png'),
    (22, 'iview-4.png'),
    (22, 'iview-5.png'),
    (22, 'iview-6.png'),
    (23, 'stcpl-1.png'),
    (23, 'stcpl-2.png'),
    (23, 'stcpl-3.png'),
    (23, 'stcpl-4.png'),
    (23, 'stcpl-5.png'),
    (23, 'stcpl-6.png'),
    (23, 'stcpl-7.png'),
    (24, 'piton-title.png'),
    (24, 'piton-1.png'),
    (24, 'piton-2.png'),
    (24, 'piton-3.png'),
    (25, 'dvig-title.png'),
    (25, 'dvig-1.png'),
    (25, 'dvig-2.png'),
    (25, 'dvig-3.png'),
    (25, 'dvig-4.png'),
    (26, 'bolder-5.png'),
    (26, 'bolder-title.png'),
    (26, 'bolder-1.png'),
    (26, 'bolder-2.png'),
    (26, 'bolder-3.png'),
    (26, 'bolder-4.png'),
    (26, 'bolder-6.png'),
    (27, 'higway-1.png'),
    (27, 'higway-title.png'),
    (27, 'higway-title-2.png'),
    (27, 'higway-2.png'),
    (27, 'higway-3.png')
;


USE payment_db;

INSERT INTO payment_method (name, supports_cards) VALUES
    ('Visa/MasterCard', true),
    ('Мир', true),
    ('PayPal', false),
    ('SberPay', false),
    ('Быстрый платеж', false);
