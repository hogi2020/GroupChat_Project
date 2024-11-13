-- mem table
CREATE TABLE mem (
    nick_ip VARCHAR2(15) PRIMARY KEY,    -- IP 주소, PRIMARY KEY로 설정
    mem_nick VARCHAR2(50) NOT NULL       -- 닉네임
);

-- talk_room table
CREATE TABLE talk_room (
    talk_room_id NUMBER PRIMARY KEY,     -- 대화방 ID, PRIMARY KEY로 설정
    talk_room_name VARCHAR2(100) NOT NULL -- 대화방 이름
);

-- mem_talk table 멤버와 대화방의 관계
CREATE TABLE mem_talk (
    nick_ip VARCHAR2(15) REFERENCES mem(nick_ip),          -- `mem` 테이블을 참조하는 외래 키
    talk_room_id NUMBER REFERENCES talk_room(talk_room_id), -- `talk_room` 테이블을 참조하는 외래 키
    PRIMARY KEY (nick_ip, talk_room_id)                     -- 복합 PRIMARY KEY
);

-- msg table 메세지 기록용
CREATE TABLE msg (
    msg_id NUMBER PRIMARY KEY,                             -- 메시지 ID, PRIMARY KEY로 설정
    nick_ip VARCHAR2(15) REFERENCES mem(nick_ip),          -- 보낸 사람의 IP, `mem` 테이블 참조
    talk_room_id NUMBER REFERENCES talk_room(talk_room_id), -- 대화방 ID, `talk_room` 테이블 참조
    msg VARCHAR2(1000) NOT NULL,                           -- 메시지 내용
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP          -- 메시지 전송 시간
);



----------------------------------------------------------------------------


-- java코드 단위테스트 용 데이터
-- `mem` 테이블에 데이터 삽입
INSERT INTO mem (nick_ip, mem_nick) VALUES ('192.168.1.1', 'user1');

INSERT INTO mem (nick_ip, mem_nick) VALUES ('192.168.1.2', 'user2');

-- `talk_room` 테이블에 데이터 삽입
INSERT INTO talk_room (talk_room_id, talk_room_name) VALUES (1, 'Chat');

INSERT INTO talk_room (talk_room_id, talk_room_name) VALUES (2, 'Talk');

-- `mem_talk` 테이블에 데이터 삽입
INSERT INTO mem_talk (nick_ip, talk_room_id) VALUES ('192.168.1.1', 1);

INSERT INTO mem_talk (nick_ip, talk_room_id) VALUES ('192.168.1.2', 1);

-- `msg` 테이블에 데이터 삽입
INSERT INTO msg (msg_id, nick_ip, talk_room_id, msg, date_time) VALUES (1, '192.168.1.1', 1, 'Hello', SYSTIMESTAMP);

INSERT INTO msg (msg_id, nick_ip, talk_room_id, msg, date_time) VALUES (2, '192.168.1.2', 1, 'World', SYSTIMESTAMP);



------------------------------------------------------------
--쿼리문으로 데이터 확인하기



-- `mem` 테이블 확인
SELECT * FROM mem;

-- `talk_room` 테이블 확인
SELECT * FROM talk_room;

-- `mem_talk` 테이블 확인
SELECT * FROM mem_talk;

-- `msg` 테이블에서 특정 닉네임(user2)이 받은 메시지 확인
SELECT * FROM msg WHERE nick_ip = '192.168.1.2';
