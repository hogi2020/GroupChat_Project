SELECT * FROM mem;

SELECT * FROM mem_talk;

SELECT * FROM msg;

SELECT * FROM talk_room;

SELECT * FROM chat_messages;

------------------------------------------------

-- �г��� ���� Ȯ��
SELECT COUNT(1) AS isExist FROM mem WHERE mem_nick=?;

-- �г��Ӱ�IP�� ��ġ�ϴ��� Ȯ��
SELECT mem_nick FROM mem WHERE mem_nick=? AND nick_ip=?;

-- ���ο� �޽��� ����
INSERT INTO chat_messages (sender, receiver, message)
    VALUES (?, ?, ?);

-- ���� �޽��� ��ȸ �� ��ȯ
SELECT sender, message, timestamp FROM chat_messages
    WHERE receiver=? ORDER BY timestamp DESC;

--------------------------------------------------



---------------------------------------------------

SELECT sender, receiver, message, timestamp
    FROM chat_messages
    WHERE sender = :x;

---------------------------------------------------

-- chat messages �����ϱ�

CREATE TABLE chat_messages (
    sender VARCHAR2(50) NOT NULL,  -- �޽��� �߽���
    receiver VARCHAR2(50) NOT NULL,  -- �޽��� ������
    message VARCHAR2(4000) NOT NULL,  -- �޽��� ����
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- �޽��� ���� �ð� (�⺻���� ���� �ð�)
);

-- message varchar�� ���� �ڵ�
ALTER TABLE chat_messages
MODIFY message VARCHAR2(4000);

