-- mem table
CREATE TABLE mem (
    nick_ip VARCHAR2(15) PRIMARY KEY,    -- IP �ּ�, PRIMARY KEY�� ����
    mem_nick VARCHAR2(50) NOT NULL       -- �г���
);

-- talk_room table
CREATE TABLE talk_room (
    talk_room_id NUMBER PRIMARY KEY,     -- ��ȭ�� ID, PRIMARY KEY�� ����
    talk_room_name VARCHAR2(100) NOT NULL -- ��ȭ�� �̸�
);

-- mem_talk table ����� ��ȭ���� ����
CREATE TABLE mem_talk (
    nick_ip VARCHAR2(15) REFERENCES mem(nick_ip),          -- `mem` ���̺��� �����ϴ� �ܷ� Ű
    talk_room_id NUMBER REFERENCES talk_room(talk_room_id), -- `talk_room` ���̺��� �����ϴ� �ܷ� Ű
    PRIMARY KEY (nick_ip, talk_room_id)                     -- ���� PRIMARY KEY
);

-- msg table �޼��� ��Ͽ�
CREATE TABLE msg (
    msg_id NUMBER PRIMARY KEY,                             -- �޽��� ID, PRIMARY KEY�� ����
    nick_ip VARCHAR2(15) REFERENCES mem(nick_ip),          -- ���� ����� IP, `mem` ���̺� ����
    talk_room_id NUMBER REFERENCES talk_room(talk_room_id), -- ��ȭ�� ID, `talk_room` ���̺� ����
    msg VARCHAR2(1000) NOT NULL,                           -- �޽��� ����
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP          -- �޽��� ���� �ð�
);



----------------------------------------------------------------------------


-- java�ڵ� �����׽�Ʈ �� ������
-- `mem` ���̺� ������ ����
INSERT INTO mem (nick_ip, mem_nick) VALUES ('192.168.1.1', 'user1');

INSERT INTO mem (nick_ip, mem_nick) VALUES ('192.168.1.2', 'user2');

-- `talk_room` ���̺� ������ ����
INSERT INTO talk_room (talk_room_id, talk_room_name) VALUES (1, 'Chat');

INSERT INTO talk_room (talk_room_id, talk_room_name) VALUES (2, 'Talk');

-- `mem_talk` ���̺� ������ ����
INSERT INTO mem_talk (nick_ip, talk_room_id) VALUES ('192.168.1.1', 1);

INSERT INTO mem_talk (nick_ip, talk_room_id) VALUES ('192.168.1.2', 1);

-- `msg` ���̺� ������ ����
INSERT INTO msg (msg_id, nick_ip, talk_room_id, msg, date_time) VALUES (1, '192.168.1.1', 1, 'Hello', SYSTIMESTAMP);

INSERT INTO msg (msg_id, nick_ip, talk_room_id, msg, date_time) VALUES (2, '192.168.1.2', 1, 'World', SYSTIMESTAMP);



------------------------------------------------------------
--���������� ������ Ȯ���ϱ�



-- `mem` ���̺� Ȯ��
SELECT * FROM mem;

-- `talk_room` ���̺� Ȯ��
SELECT * FROM talk_room;

-- `mem_talk` ���̺� Ȯ��
SELECT * FROM mem_talk;

-- `msg` ���̺��� Ư�� �г���(user2)�� ���� �޽��� Ȯ��
SELECT * FROM msg WHERE nick_ip = '192.168.1.2';
