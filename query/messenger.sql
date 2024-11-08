SELECT * FROM mem;

SELECT * FROM mem_talk;

SELECT * FROM msg;

SELECT * FROM talk_room;

SELECT * FROM chat_messages;

------------------------------------------------

-- 닉네임 존재 확인
SELECT COUNT(1) AS isExist FROM mem WHERE mem_nick=?;

-- 닉네임과IP가 일치하는지 확인
SELECT mem_nick FROM mem WHERE mem_nick=? AND nick_ip=?;

-- 새로운 메시지 전송
INSERT INTO chat_messages (sender, receiver, message)
    VALUES (?, ?, ?);

-- 받은 메시지 조회 및 반환
SELECT sender, message, timestamp FROM chat_messages
    WHERE receiver=? ORDER BY timestamp DESC;

--------------------------------------------------



---------------------------------------------------

SELECT sender, receiver, message, timestamp
    FROM chat_messages
    WHERE sender = :x;

---------------------------------------------------

-- chat messages 생성하기

CREATE TABLE chat_messages (
    sender VARCHAR2(50) NOT NULL,  -- 메시지 발신자
    receiver VARCHAR2(50) NOT NULL,  -- 메시지 수신자
    message VARCHAR2(4000) NOT NULL,  -- 메시지 내용
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 메시지 전송 시간 (기본값은 현재 시간)
);

-- message varchar값 변경 코드
ALTER TABLE chat_messages
MODIFY message VARCHAR2(4000);

