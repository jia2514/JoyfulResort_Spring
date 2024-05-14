CREATE DATABASE IF NOT EXISTS joyfulresort;
use joyfulresort;


DROP TABLE IF EXISTS room_schedule;          -- 訂房 -- 
DROP TABLE IF EXISTS room_order_item;
DROP TABLE IF EXISTS room_order;
DROP TABLE IF EXISTS room_type_photo;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS room_type;
DROP TABLE IF EXISTS activity_order;           -- 活動 --
DROP TABLE IF EXISTS activity_session;
DROP TABLE IF EXISTS activity_photo;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS activity_category;   
DROP TABLE IF EXISTS reserve_order;            -- 餐廳 --
DROP TABLE IF EXISTS reserve_session; 
DROP TABLE IF EXISTS reserve_content;    
DROP TABLE IF EXISTS meetingroom_photo;           -- 會議廳 --
DROP TABLE IF EXISTS meetingroom_order; 
DROP TABLE IF EXISTS meetingroom; 
DROP TABLE IF EXISTS member;                   -- 會員 -- 
DROP TABLE IF EXISTS position_authority;       -- 員工 --
DROP TABLE IF EXISTS emp;        
DROP TABLE IF EXISTS authority_function;    
DROP TABLE IF EXISTS position;           
DROP TABLE IF EXISTS spot_news_list;           -- 其他 --
DROP TABLE IF EXISTS news_list;
DROP TABLE IF EXISTS qa_list;



-- ===================================================== 會員 ===================================================== --
-- 創建會員資料表 --
CREATE TABLE member(
member_id INT AUTO_INCREMENT PRIMARY KEY,#會員編號為主鍵 會自動編號
member_name VARCHAR(10) NOT NULL,
member_account VARCHAR(10) UNIQUE KEY NOT NULL,#帳號為唯一鍵
member_password VARCHAR(10) NOT NULL,
member_email VARCHAR(50) UNIQUE KEY NOT NULL,#Email為唯一鍵
member_phone VARCHAR(10) UNIQUE KEY NOT NULL,#Phone為唯一鍵
member_address VARCHAR(225),
member_state TINYINT(2),
member_gender BOOLEAN,
member_birthday DATE,
member_img longBLOB); 
INSERT INTO member (member_name, member_account, member_password, member_email, member_phone, member_address, member_state, member_gender, member_birthday) 
VALUES('林小明', 'cia10101', 'cia10101', 'linxiaoming@example.com', '0912345678', '台北市中正區', '0', '0', '1990-03-15'),
	  ('陳小美', 'cia10102', 'cia10102', 'chenxiaomei@example.com', '0987654321', '新北市板橋區', '0', '1', '1992-07-25'),
	  ('王大力', 'cia10103', 'cia10103', 'wangdali@example.com', '0923456789', '台中市沙鹿區', '2', '0', '1985-11-30'),
	  ('黃小琳', 'cia10104', 'cia10104', 'huangxiaolin@example.com', '0934567890', '高雄市三民區', '1', '1', '1988-08-20'),
	  ('吳小華', 'cia10105', 'cia10105', 'wuxiaohua@example.com', '0956789012', '台南市仁德區', '1', '0', '1991-05-05'),
	  ('蔡小玲', 'cia10106', 'cia10106', 'caixiaoling@example.com', '0945678901', '桃園市中壢區', '2', '1', '1993-10-10'),
	  ('許小強', 'cia10107', 'cia10107', 'huxiaoqiang@example.com', '0978901234', '苗栗縣後龍鎮', '1', '0', '1994-12-25'),
	  ('劉小雪', 'cia10108', 'cia10108', 'liuxiaoxue@example.com', '0967890123', '嘉義市東區', '1', '1', '1989-04-18'),
	  ('郭小瑜', 'cia10109', 'cia10109', 'guoxiaoyu@example.com', '0990123456', '彰化縣鹿港鎮', '1', '1', '1995-09-01'),
	  ('林小龍', 'cia10110', 'cia10110', 'linxiaolong@example.com', '0901234567', '基隆市七堵區', '2', '0', '1987-02-20');



-- ===================================================== 訂房 ===================================================== --
-- 房型表格
CREATE TABLE room_type (
    room_type_id INT NOT NULL AUTO_INCREMENT,
    room_type_name VARCHAR(50) NOT NULL,
    number_of_people INT NOT NULL,
    room_type_amount INT NOT NULL DEFAULT 0,
    room_type_content VARCHAR(1000) NOT NULL,
    room_type_sale_state boolean NOT NULL DEFAULT 1,
    room_type_price INT NOT NULL,
    PRIMARY KEY (room_type_id)
);
INSERT INTO room_type (room_type_id, room_type_name,number_of_people, room_type_amount, room_type_content, room_type_sale_state, room_type_price)
 VALUES (1,'品趣客房',2, 3, '
·　人數：2 人
·　房型：一大床 (180cm * 188cm)
·　特色：單人椅、乾溼分離、免治馬桶
客房介紹
簡潔空間，好似置身田野溫室，在此睡下，旅程悠然美好。

設施提供
·　	客房設備：電視、無線上網、110V電源、冰箱、快煮壺、環保水瓶
·　	浴室設備：室內泡湯池、吹風機、毛巾、漱口杯
注意事項
·　	入住時間15：00，退房時間11：00。
·　	全客房採房客自助辦理入住與退房，請至1館1樓大廳「自助Check-in櫃台」辦理。
·　	兒童收費方式：0-5歲幼童免費；6歲(含)以上~12歲兒童，列為房型容納人數計算。
·　	為維護住宿品質，恕不提供加人加床服務。', 1, 4000);
INSERT INTO room_type (room_type_id, room_type_name,number_of_people, room_type_amount, room_type_content, room_type_sale_state, room_type_price) 
VALUES (2,'童旅主題房',4, 3, '
·　人數：2位成人、2位兒童
·　房型：兩大床 (180cm * 188cm)、沙發 (100cm * 175cm)
·　特色：挑高閣樓、室內泡湯池、乾溼分離、免治馬桶
客房介紹
孩子的秘密基地，就在客房裡！可讓孩子玩樂，是讓孩子驚喜連連的主題房。

設施提供
·　	客房設備：電視、無線上網、110V電源、冰箱、快煮壺、環保水瓶
·　	浴室設備：室內泡湯池、吹風機、毛巾、漱口杯
注意事項
·　	入住時間15：00，退房時間11：00。
·　	全客房採房客自助辦理入住與退房，請至1館1樓大廳「自助Check-in櫃台」辦理。
·　	兒童收費方式：0-5歲幼童免費；6歲(含)以上~12歲兒童，列為房型容納人數計算。
·　	為維護住宿品質，恕不提供加人加床服務。', 1, 16000);
INSERT INTO room_type (room_type_id, room_type_name,number_of_people, room_type_amount, room_type_content, room_type_sale_state, room_type_price) 
VALUES (3,'童漾家庭房',4, 3, '
·　人數：4 人
·　房型：兩中床 (150cm * 188cm)
·　特色：室內泡湯池、乾溼分離、免治馬桶
客房介紹
極簡風貌客房，無壓色系創造慵懶空間，緊湊日常步調在此自然慢下來，靜靜體會度假況味。

設施提供
·　	客房設備：電視、無線上網、110V電源、冰箱、快煮壺、環保水瓶
·　	浴室設備：室內泡湯池、吹風機、毛巾、漱口杯
注意事項
·　	入住時間15：00，退房時間11：00。
·　	全客房採房客自助辦理入住與退房，請至1館1樓大廳「自助Check-in櫃台」辦理。
·　	兒童收費方式：0-5歲幼童免費；6歲(含)以上~12歲兒童，列為房型容納人數計算。
·　	為維護住宿品質，恕不提供加人加床服務。', 1, 20000);


-- 房間表格
CREATE TABLE room (
  `room_id` INT NOT NULL AUTO_INCREMENT,
  `room_type_id` INT NOT NULL,
  `room_sale_state` boolean NOT NULL DEFAULT 1,
  `room_state` TINYINT(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`room_id`),
  CONSTRAINT `room_type_id_fk` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`room_type_id`)
);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (1,1,0);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (1,1,0);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (1,1,1);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (2,1,1);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (2,1,1);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (2,0,0);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (3,1,1);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (3,1,0);
INSERT INTO room (`room_type_id`, `room_sale_state`, `room_state`) VALUES (3,1,0);


-- 房型圖片表格
CREATE TABLE room_type_photo (
    room_type_photo_id INT NOT NULL AUTO_INCREMENT,
    room_type_id INT NOT NULL,
    room_type_photo LONGBLOB,
    room_type_photo_state boolean NOT NULL DEFAULT 1,
    PRIMARY KEY (room_type_photo_id),
    FOREIGN KEY (room_type_id) REFERENCES room_type(room_type_id)
);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (1,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (1,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (1,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (2,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (2,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (2,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (3,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (3,null,1);
INSERT INTO room_type_photo (room_type_id,room_type_photo,room_type_photo_state) VALUES (3,null,1);


-- 房間訂單表格
CREATE TABLE room_order (
    room_order_id INT NOT NULL AUTO_INCREMENT,
    member_id INT NOT NULL,
    order_date DATETIME NOT NULL,
    room_order_state TINYINT(2) Default 1,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    refund_state TINYINT(2) DEFAULT 0,
    complete_datetime datetime,
    CONSTRAINT room_order_member_id_fk FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT room_order_id_pk PRIMARY KEY (room_order_id)
);
INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state, complete_datetime) 
VALUES (1, DATE_SUB(NOW(), INTERVAL 35 DAY), 2, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 13 DAY), 0, DATE_SUB(NOW(), INTERVAL 13 DAY));

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (1, DATE_SUB(NOW(), INTERVAL 30 DAY), 4, DATE_SUB(CURDATE(), INTERVAL 10 DAY), CURDATE(), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (2, DATE_SUB(NOW(), INTERVAL 27 DAY), 4, DATE_SUB(CURDATE(), INTERVAL 7 DAY), CURDATE(), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (3, DATE_SUB(NOW(), INTERVAL 25 DAY), 4, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state, complete_datetime) 
VALUES (4, DATE_SUB(NOW(), INTERVAL 25 DAY), 0, DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 2, DATE_SUB(NOW(), INTERVAL 5 DAY));

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (5, DATE_SUB(NOW(), INTERVAL 23 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (6, DATE_SUB(NOW(), INTERVAL 23 DAY), 3, DATE_ADD(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 11 DAY), 1);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (7, DATE_SUB(NOW(), INTERVAL 20 DAY), 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 1);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (8, DATE_SUB(NOW(), INTERVAL 19 DAY), 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (9, DATE_SUB(NOW(), INTERVAL 19 DAY), 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (10, DATE_SUB(NOW(), INTERVAL 18 DAY), 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (9, DATE_SUB(NOW(), INTERVAL 17 DAY), 4, DATE_SUB(CURDATE(), INTERVAL 5 DAY), CURDATE(), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (4, DATE_SUB(NOW(), INTERVAL 17 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 11 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (5, DATE_SUB(NOW(), INTERVAL 17 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 18 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (6, DATE_SUB(NOW(), INTERVAL 15 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (7, DATE_SUB(NOW(), INTERVAL 13 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 18 DAY), DATE_ADD(CURDATE(), INTERVAL 25 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (8, DATE_SUB(NOW(), INTERVAL 10 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 22 DAY), DATE_ADD(CURDATE(), INTERVAL 30 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (9, DATE_SUB(NOW(), INTERVAL 5 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 25 DAY), DATE_ADD(CURDATE(), INTERVAL 32 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (2, DATE_SUB(NOW(), INTERVAL 3 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 38 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (3, DATE_SUB(NOW(), INTERVAL 5 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 40 DAY), DATE_ADD(CURDATE(), INTERVAL 45 DAY), 0);

INSERT INTO room_order (member_id, order_date, room_order_state, check_in_date, check_out_date, refund_state) 
VALUES (4, DATE_SUB(NOW(), INTERVAL 1 DAY), 1, DATE_ADD(CURDATE(), INTERVAL 42 DAY), DATE_ADD(CURDATE(), INTERVAL 49 DAY), 0);



-- 房間訂單明細表格
CREATE TABLE room_order_item (
    room_order_item_id INT NOT NULL AUTO_INCREMENT,
    room_id INT,
    room_order_id INT NOT NULL,
    room_type_id INT NOT NULL, 
    room_guest_name VARCHAR(50),
    room_guest_phone VARCHAR(10),
    number_of_people INT,
    special_req VARCHAR(50),
    room_price INT NOT NULL,
    room_order_item_state TINYINT(2) default 0,
    CONSTRAINT room_order_item_room_id_fk FOREIGN KEY (room_id) REFERENCES room (room_id),
    CONSTRAINT room_order_item_room_order_id_fk FOREIGN KEY (room_order_id) REFERENCES room_order (room_order_id),
    CONSTRAINT room_order_item_room_type_id_fk FOREIGN KEY (room_type_id) REFERENCES room_type (room_type_id),
    CONSTRAINT room_order_item_id_pk PRIMARY KEY (room_order_item_id)
);
INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state)
 VALUES (1,1,1,'林小明','0912345678',null,4000,2);
INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state)
 VALUES (1,2,1,'林大明','0912345789',null,4000,2);
INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state) 
VALUES (2,4,2,'林小明','0912345678',"嬰兒床及澡盆",16000,1);
INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state) 
VALUES (2,5,2,'林小明','0912345678',"嬰兒床及澡盆",16000,1);

INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state) 
VALUES (3,7,3,'陳小美','0987654321',"入住期間不需清潔",20000,1);

INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state) 
VALUES (4,1,1,'王大力','0923456789',null,4000,1);



INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (5,2,16000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (5,2,16000);

INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (6,1,4000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (7,2,16000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (8,1,4000);
INSERT INTO room_order_item (room_order_id, room_type_id, special_req, room_price) VALUES (9,2,"嬰兒床及澡盆",16000);
INSERT INTO room_order_item (room_order_id, room_type_id, special_req, room_price) VALUES (9,2,"嬰兒床及澡盆",16000);
INSERT INTO room_order_item (room_order_id, room_type_id, special_req, room_price) VALUES (10,3,"入住期間不需清潔",20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (11,3,20000);

INSERT INTO room_order_item (room_order_id, room_id, room_type_id, room_guest_name, room_guest_phone, special_req, room_price,room_order_item_state) 
VALUES (12,3,1,'郭小瑜','0990123456',null,4000,1);


INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (13,2,16000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (13,2,16000);

INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (14,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (15,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (15,1,4000);

INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (16,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (16,1,4000);




INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (17,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (17,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (18,1,4000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (18,1,4000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (19,2,16000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (19,2,16000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (20,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (20,3,20000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (21,1,4000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (21,1,4000);
INSERT INTO room_order_item (room_order_id, room_type_id, room_price) VALUES (21,2,16000);


-- 房型預定表格
CREATE TABLE room_schedule (
    room_schedule_id INT NOT NULL AUTO_INCREMENT,
    room_order_item_id INT NOT NULL,
    room_type_id INT NOT NULL, 
    room_schedule_date DATE NOT NULL,
    CONSTRAINT room_schedule_room_type_id_fk FOREIGN KEY (room_type_id) REFERENCES room_type (room_type_id),
    CONSTRAINT room_schedule_room_order_item_id_fk FOREIGN KEY (room_order_item_id) REFERENCES room_order_item (room_order_item_id),
    CONSTRAINT room_schedule_id_pk PRIMARY KEY (room_schedule_id)
);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (1,1,curdate()- INTERVAL 15 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (1,1,curdate()- INTERVAL 14 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (2,1,curdate()- INTERVAL 15 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (2,1,curdate()- INTERVAL 14 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 10 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 9 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (3,2,curdate()- INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 10 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 9 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (4,2,curdate()- INTERVAL 1 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (5,3,curdate()- INTERVAL 1 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 10 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 9 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate()- INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (6,1,curdate());

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (9,1,curdate()+ INTERVAL 9 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (10,2,curdate()+ INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (10,2,curdate()+ INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (10,2,curdate()+ INTERVAL 9 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (10,2,curdate()+ INTERVAL 10 DAY);


INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (11,1,curdate()+ INTERVAL 9 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate());
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate()+ INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate()+ INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate()+ INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate()+ INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate()+ INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (12,2,curdate()+ INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate());
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate()+ INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate()+ INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate()+ INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate()+ INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate()+ INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (13,2,curdate()+ INTERVAL 6 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate());
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (14,3,curdate()+ INTERVAL 9 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate());
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 1 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 2 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 3 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 4 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 5 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 6 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 7 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 8 DAY);
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (15,3,curdate()+ INTERVAL 9 DAY);

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (16,1,DATE_ADD(CURDATE(), INTERVAL -5 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (16,1,DATE_ADD(CURDATE(), INTERVAL -4 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (16,1,DATE_ADD(CURDATE(), INTERVAL -3 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (16,1,DATE_ADD(CURDATE(), INTERVAL -2 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (16,1,DATE_ADD(CURDATE(), INTERVAL -1 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 11 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 12 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 13 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 14 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 15 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 16 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 17 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 18 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (17,2,DATE_ADD(CURDATE(), INTERVAL 19 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 11 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 12 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 13 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 14 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 15 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 16 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 17 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 18 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (18,2,DATE_ADD(CURDATE(), INTERVAL 19 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 10 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 11 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 12 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 13 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 14 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 15 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 16 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (19,3,DATE_ADD(CURDATE(), INTERVAL 17 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (20,3,DATE_ADD(CURDATE(), INTERVAL 15 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (20,3,DATE_ADD(CURDATE(), INTERVAL 16 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (20,3,DATE_ADD(CURDATE(), INTERVAL 17 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (20,3,DATE_ADD(CURDATE(), INTERVAL 18 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (20,3,DATE_ADD(CURDATE(), INTERVAL 19 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (21,1,DATE_ADD(CURDATE(), INTERVAL 15 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (21,1,DATE_ADD(CURDATE(), INTERVAL 16 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (21,1,DATE_ADD(CURDATE(), INTERVAL 17 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (21,1,DATE_ADD(CURDATE(), INTERVAL 18 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (21,1,DATE_ADD(CURDATE(), INTERVAL 19 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (22,3,DATE_ADD(CURDATE(), INTERVAL 18 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (22,3,DATE_ADD(CURDATE(), INTERVAL 19 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (22,3,DATE_ADD(CURDATE(), INTERVAL 20 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (22,3,DATE_ADD(CURDATE(), INTERVAL 21 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (22,3,DATE_ADD(CURDATE(), INTERVAL 22 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (23,1,DATE_ADD(CURDATE(), INTERVAL 18 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (23,1,DATE_ADD(CURDATE(), INTERVAL 19 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (23,1,DATE_ADD(CURDATE(), INTERVAL 20 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (23,1,DATE_ADD(CURDATE(), INTERVAL 21 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (23,1,DATE_ADD(CURDATE(), INTERVAL 22 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 22 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 23 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 24 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 25 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 26 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 27 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 28 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (24,3,DATE_ADD(CURDATE(), INTERVAL 29 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 22 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 23 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 24 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 25 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 26 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 27 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 28 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (25,3,DATE_ADD(CURDATE(), INTERVAL 29 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 25 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 26 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 27 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 28 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 29 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 30 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (26,1,DATE_ADD(CURDATE(), INTERVAL 31 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 25 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 26 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 27 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 28 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 29 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 30 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (27,1,DATE_ADD(CURDATE(), INTERVAL 31 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 30 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 31 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 32 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 33 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 34 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 35 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 36 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (28,2,DATE_ADD(CURDATE(), INTERVAL 37 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 30 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 31 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 32 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 33 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 34 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 35 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 36 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (29,2,DATE_ADD(CURDATE(), INTERVAL 37 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (30,3,DATE_ADD(CURDATE(), INTERVAL 40 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (30,3,DATE_ADD(CURDATE(), INTERVAL 41 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (30,3,DATE_ADD(CURDATE(), INTERVAL 42 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (30,3,DATE_ADD(CURDATE(), INTERVAL 43 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (31,3,DATE_ADD(CURDATE(), INTERVAL 40 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (31,3,DATE_ADD(CURDATE(), INTERVAL 41 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (31,3,DATE_ADD(CURDATE(), INTERVAL 42 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (31,3,DATE_ADD(CURDATE(), INTERVAL 43 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 40 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 41 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 42 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 43 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 44 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 45 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 46 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 47 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (32,1,DATE_ADD(CURDATE(), INTERVAL 48 DAY));

INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 40 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 41 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 42 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 43 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 44 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 45 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 46 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 47 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (33,2,DATE_ADD(CURDATE(), INTERVAL 48 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 40 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 41 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 42 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 43 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 44 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 45 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 46 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 47 DAY));
INSERT INTO room_schedule (room_order_item_id, room_type_id, room_schedule_date) VALUES (34,2,DATE_ADD(CURDATE(), INTERVAL 48 DAY));




-- ===================================================== 活動 ===================================================== --
-- 活動類別表格 
CREATE TABLE activity_category(
	activity_category_id INT NOT NULL AUTO_INCREMENT,
    activity_category_name VARCHAR(20) NOT NULL,
    activity_category_info VARCHAR(100),
    CONSTRAINT activity_category_id_pk PRIMARY KEY (activity_category_id)
);

INSERT INTO activity_category(activity_category_name, activity_category_info) VALUES('手作系列', 
'  我們的活動範圍涵蓋各種手作領域，包括繪畫、手工藝等等。無論你是初學者還是有豐富經驗的專家，我們都有適合你的活動。無論你是單身、夫妻或家庭，我們都歡迎你加入我們的行列，一起享受手作的樂趣！');
INSERT INTO activity_category(activity_category_name, activity_category_info) VALUES('運動系列', 
'  我們的活動提供了一個放鬆身心、增強身體柔軟度和力量的完美場所。無論你是想擺脫壓力，還是保持身體健康，我們都歡迎你加入我們的活動，一起探索身心靈的平衡之道！');
INSERT INTO activity_category(activity_category_name) VALUES('其他');

-- 活動表格
 CREATE TABLE activity(
	activity_id INT NOT NULL AUTO_INCREMENT,
    activity_category_id INT NOT NULL,
    activity_name VARCHAR(20) NOT NULL,
    activity_price INT NOT NULL,
    activity_info VARCHAR(500),
    -- activity_notice VARCHAR(500),
    activity_status BOOLEAN NOT NULL,
    CONSTRAINT activity_id_activity_category_id_fk FOREIGN KEY (activity_category_id) REFERENCES activity_category (activity_category_id),
    CONSTRAINT activity_id_pk PRIMARY KEY (activity_id)
 );
 
INSERT INTO activity(activity_category_id, activity_name, activity_price, activity_status, activity_info) VALUES(1, "泡澡球", 200, 1, '  我們的活動將帶你探索製作泡澡球的樂趣和技巧。通過混合天然成分如植物精油、乳液或草本提取物，你可以為自己定制各種類型的泡澡球，讓你的泡澡體驗更加奢華和愉悅。無論你是喜歡花香、果味還是草本味道，我們都有適合你的配方和材料。參加我們的活動，你將學會如何製作泡澡球，並帶回家一些自製的泡澡球，讓你在家中隨時享受美好的泡澡時光。');
-- INSERT INTO activity(activity_category_id, activity_name, activity_price, activity_status) VALUES(1, "拓印", 150, 0);
INSERT INTO activity(activity_category_id, activity_name, activity_price, activity_status, activity_info) VALUES(2, "瑜珈", 250, 1, '無論你是瑜伽初學者還是經驗豐富的瑜伽愛好者，我們的課程都將適應你的需要。透過呼吸和姿勢的控制，你將有機會深入瞭解自己的身體和內心，從而實現身心靈的和諧。');
INSERT INTO activity(activity_category_id, activity_name, activity_price, activity_status) VALUES(3, "電影放映", 100, 1);

-- 活動圖片表格
CREATE TABLE activity_photo(
	activity_photo_id INT NOT NULL AUTO_INCREMENT,
    activity_id INT NOT NULL,
    activity_photo LONGBLOB,
    CONSTRAINT activity_photo_activity_id_fk FOREIGN KEY (activity_id) REFERENCES activity (activity_id),
    CONSTRAINT activity_photo_id_pk PRIMARY KEY (activity_photo_id)
);

INSERT INTO activity_photo(activity_id, activity_photo) VALUES(1, load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/activity/activitydiybath2.jpg'));
INSERT INTO activity_photo(activity_id, activity_photo) VALUES(2, load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/activity/activityyoga.jpg'));
INSERT INTO activity_photo(activity_id, activity_photo) VALUES(3, load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/activity/activitymovie1.jpg'));


-- 活動場次表格
CREATE TABLE activity_session(
	activity_session_id INT NOT NULL AUTO_INCREMENT,
    activity_id INT NOT NULL,
    activity_date DATE NOT NULL,
    activity_time TINYINT(2) NOT NULL,
    activity_max_part INT NOT NULL,
    -- activity_min_part INT NOT NULL,
    activity_entered_status TINYINT(2) NOT NULL,
    entered_total INT,
    entered_start DATE NOT NULL,
    entered_end DATE NOT NULL,
    activity_session_status BOOLEAN NOT NULL,
    CONSTRAINT activity_session_activity_id_fk FOREIGN KEY (activity_id) REFERENCES activity (activity_id),
    CONSTRAINT activity_session_id_pk PRIMARY KEY (activity_session_id)
);

INSERT INTO activity_session(activity_id, activity_date, activity_time, activity_max_part, activity_entered_status, entered_total, entered_start, entered_end, activity_session_status) 
VALUES(1, '2024-05-11', '0', 20, 0, 0, '2024-03-08', '2024-05-08', 0);
INSERT INTO activity_session(activity_id, activity_date, activity_time, activity_max_part, activity_entered_status, entered_total, entered_start, entered_end, activity_session_status) 
VALUES(2, '2024-05-11', '1', 20, 0, 0, '2024-03-08', '2024-05-08', 0);
INSERT INTO activity_session(activity_id, activity_date, activity_time, activity_max_part, activity_entered_status, entered_total, entered_start, entered_end, activity_session_status) 
VALUES(1, '2024-05-18', '0', 20, 1, 0, '2024-03-15', '2024-05-15', 1);
INSERT INTO activity_session(activity_id, activity_date, activity_time, activity_max_part, activity_entered_status, entered_total, entered_start, entered_end, activity_session_status) 
VALUES(3, '2024-05-18', '1', 20, 1, 0, '2024-03-15', '2024-05-15', 1);
INSERT INTO activity_session(activity_id, activity_date, activity_time, activity_max_part, activity_entered_status, entered_total, entered_start, entered_end, activity_session_status) 
VALUES(2, '2024-05-26', '0', 20, 1, 0, '2024-03-23', '2024-05-23', 1);

-- 活動訂單表格
CREATE TABLE activity_order(
	activity_order_id INT NOT NULL AUTO_INCREMENT,
    activity_session_id INT NOT NULL,
    -- member_id INT NOT NULL, 
    entered_number INT NOT NULL,
    -- entered_child_number INT,
    order_amount INT NOT NULL,
    order_status TINYINT NOT NULL,
    order_time DATETIME NOT NULL,
    order_note VARCHAR(50),
    refund_status TINYINT,
    CONSTRAINT activity_order_activity_session_id_fk FOREIGN KEY (activity_session_id) REFERENCES activity_session (activity_session_id),
    -- CONSTRAINT activity_order_member_id_fk FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT activity_order_id_pk PRIMARY KEY (activity_order_id)
);

-- INSERT INTO activity_order(activity_session_id, entered_number, order_amount, order_status, order_time) 
-- VALUES(1, 2, 300, 0, curtime());
-- INSERT INTO activity_order(activity_session_id, entered_number, order_amount, order_status, order_time) 
-- VALUES(1, 4, 600, 0, curtime());
-- INSERT INTO activity_order(activity_session_id, entered_number, order_amount, order_status, order_time) 
-- VALUES(2, 2, 400, 0, curtime());


-- ===================================================== 餐廳 ===================================================== --

CREATE TABLE reserve_content(
id INT AUTO_INCREMENT PRIMARY KEY,
    reserve_text TEXT,
    reserve_image LONGBLOB
);
insert into reserve_content(reserve_text,reserve_image)
values
('在JOYFUL餐廳，我們提供不僅是餐點，更是一場奢華的感官盛宴。餐廳設計結合了現代與傳統元素，創造出一個既舒適又富有藝術氛圍的空間。每一道菜都由我們的星級主廚精心烹製，從選料到擺盤，每一細節都力求完美，確保為您提供無與倫比的用餐體驗。'
,load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/reserveorder/restaurant1.jpg')),
('當您踏進「JOYFUL」的大門，即刻感受到奢華與溫暖交織的氛圍。從精緻的裝潢到周到的服務，每一處細節都彰顯出我們對完美的追求。我們的菜單融合了傳統食材和現代烹飪技術，每一道菜都是主廚用心打造的藝術品，讓您品味到獨一無二的美味。無論是與摯愛共度浪漫之夜，還是與親朋好友共享美好時光，「JOYFUL」都能為您締造難忘的用餐體驗。',load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/reserveorder/restaurant2.jpg')
);

-- 創建餐廳場次表格 RESERVE_SESSION
CREATE TABLE reserve_session (
    reserve_session_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reserve_max_part INT NOT NULL DEFAULT 100,
    session_time VARCHAR(20)
)AUTO_INCREMENT = 101;
-- 新增資料

INSERT INTO reserve_session(
 session_time)
values("白天"),("晚上"),("例外");


-- 創建餐廳訂單表格 RESERVE_ORDER
CREATE TABLE reserve_order (
    reserve_order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    reserve_session_id INT NOT NULL default 0,
    member_id INT  NOT NULL, 
    reserve_order_date DATE NOT NULL ,
    reserve_number INT NOT NULL,
    reserve_max	 int not null default 100,  -- 是否還要分場次，否則把人數上限直接加入
    reserve_order_state TINYINT default 1,
    booking_date DATETIME NOT NULL,
    order_note VARCHAR(50),
    FOREIGN KEY (reserve_session_id) REFERENCES reserve_session(reserve_session_id),  -- 是否為FK
    FOREIGN KEY (member_id) REFERENCES member(member_id)
)AUTO_INCREMENT = 2001;

-- 新增order資料
INSERT INTO reserve_order (reserve_session_id, member_id, reserve_order_date, reserve_number, booking_date, order_note)
VALUES
    (101, 1, '2024-03-30', 15, '2024-04-25 11:00:00', '特別要求：窗邊位置'),
    (101, 2, '2024-03-31', 20, '2024-04-25 12:00:00', NULL),
    (102, 4, '2024-04-01', 6,'2024-04-28 12:30:00', NULL),
    (102, 5, '2024-04-02', 10, '2024-04-29 16:30:00', '特殊要求：2位素食者'),
    (103, 3, '2024-04-03', 5,'2024-05-01 18:30:00', NULL);
 
 -- 沒綁FK用這個
 -- INSERT INTO reserve_order ( member_id, reserve_order_date, reserve_number, booking_date, order_note)
-- VALUES
--     ( 1001, '2024-03-30', 15, '2024-04-25 11:00:00', '特別要求：窗邊位置'),
--     ( 1003, '2024-03-31', 20, '2024-04-26 12:00:00', ''),
--     ( 1004, '2024-04-01', 6,'2024-04-28 12:30:00', ''),
--     ( 1005, '2024-04-02', 10, '2024-04-29 16:30:00', '特殊要求：2位素食者'),
--     ( 1007, '2024-04-03', 5,'2024-05-01 18:30:00', NULL);






-- ===================================================== 會議廳 ===================================================== --
create table meetingroom(
	meetingroom_id int not null primary key auto_increment,
    meetingroom_name varchar(30) not null,
    meetingroom_ename varchar(30) not null,
    meetingroom_content varchar(200) not null,
    meetingroom_sale_state boolean,
    meetingroom_price int not null,
    meetingroom_chair int,
    meetingroom_square_meter int,
    meetingroom_capacity varchar(30)
);
insert into meetingroom(meetingroom_name, meetingroom_ename, meetingroom_content, meetingroom_sale_state, meetingroom_price, meetingroom_chair, meetingroom_square_meter, meetingroom_capacity)
values('小型會議廳','Small Conference Room', '融合都會風情的獨家空中花園與無邊際泳池景觀，為您的會議打造出尊貴高雅的氛圍。', 0, 35000, 3, 25, '21~50'),
('中型會議廳','Medium Conference Room', '獨享空中花園與無邊際泳池景觀，為您的獨家會議注入與眾不同的體驗。', 0, 40000, 4, 35, '21~75'),
('大型會議廳','Large Conference Room', '鄰近寧靜花園，擁有獨家無邊際泳池景觀，為您的特殊活動及會議增添優雅氛圍。', 0, 50000, 5, 50, '21~100');


create table meetingroom_order(
	meetingroom_order_id int not null primary key auto_increment,
	member_id int not null,
	meetingroom_id int not null,
	meetingroom_order_date Date not null,
	booking_date Date not null,
	order_state Tinyint default 1,
	refund_state Tinyint default 0,
	order_note Varchar(50),
    foreign key (meetingroom_id) references meetingroom (meetingroom_id),
    CONSTRAINT meetroom_order_member_id_fk foreign key (member_id) references member (member_id)
    
);
insert into meetingroom_order(member_id, meetingroom_id, meetingroom_order_date, booking_date, order_state, refund_state, order_note)
values(1, 2, current_date(), '2024-5-22', 1, 1, 'eee'),
(2, 1, current_date(), '2024-5-24', 1, 1, 'eee'),
(1, 3, current_date(), '2024-6-7', 1, 1, 'eee'),
(3, 2, current_date(), '2024-7-8', 1, 1, 'eee'),
(4, 1, current_date(), '2024-6-9', 1, 1, 'eee'),
(3, 3, current_date(), '2024-7-10', 1, 1, 'eee');
insert into meetingroom_order(member_id, meetingroom_id, meetingroom_order_date, booking_date, order_state, refund_state, order_note)
values(1, 2, current_date(), '2024-5-11', 1, 1, 'eee');




create table meetingroom_photo(
	meetingroom_photo_id int not null primary key auto_increment,
    meetingroom_id int not null,
    photo_path varchar(200),
    meetingroom_image longblob,
    foreign key (meetingroom_id) references meetingroom (meetingroom_id)
);
insert into meetingroom_photo(meetingroom_photo_id, meetingroom_id, meetingroom_image)
values(1,1,load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/conference/small.jpg')),
(2,2,load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/conference/medium.jpg')),
(3,3,load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/conference/large.jpg'));

-- ===================================================== 員工 ===================================================== --
-- 員工的"職位"表格
CREATE TABLE `position` (
	`position_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `position_name` VARCHAR(50) NOT NULL 
);

INSERT INTO `position` VALUES(1, '經理');
INSERT INTO `position` VALUES(2, '客服人員');
INSERT INTO `position` VALUES(3, '行銷人員');
INSERT INTO `position` VALUES(4, '房務員');
INSERT INTO `position` VALUES(5, '櫃台接待人員');
INSERT INTO `position` VALUES(6, '活動人員');
INSERT INTO `position` VALUES(7, '會議廳人員');
INSERT INTO `position` VALUES(8, '餐廳人員');



-- 員工的"功能權限"表格 
CREATE TABLE `authority_function`(
	`function_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `function_name` VARCHAR(50) NOT NULL 
);



/* INSERT INTO `authority_function` VALUES(1, '員工登入');
INSERT INTO `authority_function` VALUES(2, '員工資料管理');
INSERT INTO `authority_function` VALUES(3, '員工權限管理');
INSERT INTO `authority_function` VALUES(4, '會員資料管理');
INSERT INTO `authority_function` VALUES(5, '最新消息管理');
INSERT INTO `authority_function` VALUES(6, '媒體報導管理');
INSERT INTO `authority_function` VALUES(7, '常見問題管理');
INSERT INTO `authority_function` VALUES(8, '房型管理');
INSERT INTO `authority_function` VALUES(9, '房間管理');
INSERT INTO `authority_function` VALUES(10, '房間訂單管理');
INSERT INTO `authority_function` VALUES(11, 'check-in');
INSERT INTO `authority_function` VALUES(12, 'check-out');
INSERT INTO `authority_function` VALUES(13, '餐廳訂單管理');
INSERT INTO `authority_function` VALUES(14, '餐廳資訊管理');
INSERT INTO `authority_function` VALUES(15, '會議廳訂單管理');
INSERT INTO `authority_function` VALUES(16, '會議廳資訊管理');
INSERT INTO `authority_function` VALUES(17, '活動訂單管理');
INSERT INTO `authority_function` VALUES(18, '活動場次管理');
INSERT INTO `authority_function` VALUES(19, '活動類型管理');  */


INSERT INTO `authority_function` (`function_name`) VALUES ('員工登入');
INSERT INTO `authority_function` (`function_name`) VALUES ('員工資料管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('員工權限管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('會員資料管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('最新消息管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('媒體報導管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('常見問題管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('房型管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('房間管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('房間訂單管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('check-in');
INSERT INTO `authority_function` (`function_name`) VALUES ('check-out');
INSERT INTO `authority_function` (`function_name`) VALUES ('餐廳訂單管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('餐廳資訊管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('會議廳訂單管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('會議廳資訊管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('活動訂單管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('活動場次管理');
INSERT INTO `authority_function` (`function_name`) VALUES ('活動類型管理');




-- 員工的"員工"表格 
CREATE TABLE `emp`(
	`empno` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `position_id` INT NOT NULL,
    `emp_name` VARCHAR(50) NOT NULL,
    `hire_date` DATE NOT NULL,
    `emp_state` BOOLEAN  NOT NULL,
    `emp_account` INT ,
	`emp_password` VARCHAR(20) NOT NULL,
    `image` LONGBLOB, 
    FOREIGN KEY(`position_id`) REFERENCES `position`(`position_id`) 
)AUTO_INCREMENT = 7001;

-- SELECT @@datadir;顯示mysql作用目錄區
INSERT INTO `emp` ( `position_id`,  `emp_name`, `hire_date`,`emp_state`, `emp_account`, `emp_password`, `image`)VALUES
( 1, '錢夫人', '2012-01-01', TRUE, 7001, '111111', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/28.jpg')),
( 2, '約翰喬', '2014-02-01', TRUE, 7002, 'password2', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/29.jpg')),
( 3, '沙龍巴斯', '2017-03-01', TRUE, 7003, 'password3', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/30.jpg')),
( 4, '忍太郎', '2020-04-01', TRUE, 7004, 'password4', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/31.jpg')),
( 5, '阿土伯', '2022-05-01', TRUE, 7005, 'password5', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/32.jpg')),
( 6, '莎拉公主', '2023-06-01', TRUE, 7006, 'password6', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/33.jpg')),
( 7, '宮本寶藏', '2011-07-01', TRUE, 7007, 'password7', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/36.jpg')),
( 8, '糖糖', '2010-08-01', TRUE, 7008, 'password8', load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/37.jpg')),
( 8, '烏咪', '2019-09-01', FALSE, 7009, 'password9',load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/38.jpg')),
( 1, '小丹尼', '2019-09-01', FALSE, 7010, 'password10',load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/40.jpg')),
( 2, '孫小美', '2019-09-01', FALSE, 7010, 'password10',load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/39.jpg')),
( 3, '金貝貝', '2019-09-01', FALSE, 7010, 'password10',load_file('C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/images/employee/41.jpg'));


-- 員工的"職位權限"表格 
CREATE TABLE `position_authority` (
    `position_id` INT  NOT NULL ,
    `function_id` INT NOT NULL,
    PRIMARY KEY (`position_id`, `function_id`),                                -- 複合主鍵
    FOREIGN KEY (`position_id`) REFERENCES `position` (`position_id`),
    FOREIGN KEY (`function_id`) REFERENCES `authority_function` (`function_id`)
);

INSERT INTO `position_authority` VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 19),
(2, 1),
(2, 2),
(2, 4),
(2, 5),
(2, 7),
(2, 9),
(2, 10),
(2, 11),
(2, 12),
(3, 1),
(3, 2),
(3, 5),
(3, 6),
(3, 8),
(3, 9),
(3, 10),
(3, 14),
(3, 16),
(3, 18),
(3, 19),
(4, 1),
(4, 2),
(4, 9),
(5, 1),
(5, 2),
(5, 9),
(5, 10),
(6, 1),
(6, 2),
(6, 5),
(6, 18),
(6, 19),
(7, 1),
(7, 2),
(7, 15),
(7, 16),
(8, 1),
(8, 2),
(8, 13),
(8, 14);



-- ===================================================== 其他 ===================================================== --
-- 最新消息資料表 
CREATE TABLE spot_news_list(
spot_news_id INT PRIMARY KEY AUTO_INCREMENT,
spot_news_title VARCHAR(20) NOT NULL,
spot_news_date DATETIME NOT NULL,
spot_news_abstract VARCHAR(500) NOT NULL,
spot_news_content VARCHAR(1000) NOT NULL,
spot_news_photo longblob,
news_photo_path varchar(200),
spot_news_state BOOLEAN
);

INSERT INTO spot_news_list(spot_news_title, spot_news_date, spot_news_abstract, spot_news_content, spot_news_photo, spot_news_state)
VALUE("暑假特惠：兒童免費住宿", "2024-06-01", "暑假期間的特惠活動。", "今年暑假，帶著您的孩子來我們度假飯店度假！我們提供兒童免費住宿的特惠，讓您可以與您的家人一起度過一個難忘的假期。", NULL, 1),
	 ("給孩子的特別驚喜！", "2024-04-10", "度假飯店為兒童提供的特別驚喜。", "我們為小朋友們準備了一個特別驚喜！在入住時，每位小朋友都將獲得一份小禮物和歡迎卡片，我們期待著您的孩子在度假期間度過快樂時光。", NULL, 1),
     ("親子DIY工作坊開辦了！", "2024-05-15", "度假飯店開辦的親子DIY工作坊活動。", "我們開辦了全新的親子DIY工作坊！在這個工作坊中，孩子們可以嘗試各種手工藝活動，並在專業指導下製作出獨一無二的手工作品。歡迎家庭一起來參加！", NULL, 1),
     ("專屬家庭活動：親子共遊樂趣無窮！", "2024-07-08", "度假飯店提供的專屬家庭活動。", "我們提供專屬的家庭活動，讓親子共度美好時光！從親子遊戲到兒童瑜伽，我們提供多種活動，讓家庭可以共同創造美好回憶。歡迎家庭一起加入我們的活動！",NULL ,1)
;


-- 媒體報導資料表 
CREATE TABLE news_list(
news_id INT PRIMARY KEY AUTO_INCREMENT,
news_title VARCHAR(20) NOT NULL,
news_date DATETIME NOT NULL,
news_abstract VARCHAR(500) NOT NULL,
news_content VARCHAR(1000) NOT NULL,
news_photo LONGBLOB,
news_from VARCHAR(50) NOT NULL,
news_state BOOLEAN
);
INSERT INTO news_list(news_title, news_date, news_abstract, news_content, news_photo, news_from, news_state)
VALUE("新推出的親子互動套餐", "2024-03-15", "這篇報導介紹了度假飯店最新推出的親子互動套餐。", "搭配兒童專屬導覽、主題遊戲和手工藝活動，讓親子一同享受樂趣，同時學習新知識。", NULL, "旅遊日報", 1),
     ("寓教於樂：親子旅遊的新趨勢", "2024-02-28", "這篇報導探討了寓教於樂的旅遊趨勢，專訪了度假飯店的經營者。", "文章描述了度假飯店如何通過提供具有教育意義的活動和遊戲，吸引了越來越多的親子旅客。", NULL, "旅遊周刊", 1),
     ("親子共遊：一家三口的快樂時光", "2024-01-10", "這篇報導分享了一家三口在度假飯店度過的快樂時光。", "家庭在度假期間參加了度假飯店提供的親子活動，孩子們在玩樂中學習到了許多新知識，一家人度過了美好的假期時光。", NULL, "旅遊雜誌", 1)
;


-- 常見問題資料表 
CREATE TABLE qa_list(
qa_id INT PRIMARY KEY AUTO_INCREMENT,
qa_question VARCHAR(100) NOT NULL,
qa_answer VARCHAR(500) NOT NULL,
qa_state BOOLEAN
);
INSERT INTO qa_list(qa_question, qa_answer, qa_state)
VALUE ("預訂程序和取消政策是怎樣的？", "預訂程序是線上預訂或通過電話預訂。取消政策視度假飯店的政策而定，一般會提前通知取消時限和可能產生的取消費用。" , 1),
	  ("有哪些客房類型和設施？", "我們提供單人房、雙人房、套房等不同客房類型，設施包括私人浴室、液晶電視、迷你冰箱、免費Wi-Fi等。", 1),
      ("餐飲選項有哪些？", "我們提供早餐、午餐和晚餐，餐廳內有各種美食選擇，也提供素食和特殊飲食要求的選擇。", 1),
      ("是否提供交通和接駁服務？", "我們提供從機場到度假飯店的接駁服務，也可以提供租車服務或安排其他交通方式", 1),
      ("有哪些額外費用需要注意？", "額外費用可能包括度假村費、停車費、設施使用費等，請在預訂時了解相關費用。",1),
      ("有關安全的措施是怎樣的？","我們有安全監控系統、24小時前臺服務、緊急應對計劃等措施，並提供基本的醫療服務，如急救箱、醫療護理等。",1)
;






