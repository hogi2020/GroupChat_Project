# GroupChat_Project

**Project Management Link :** [Chat Project TimeLine Link | Click!!!](https://docs.google.com/spreadsheets/d/1IYySTJvefOVfoQwdsIm4xq4j5l2pxkdAC0cG4n1HPgk/edit?usp=sharing)

<br />


## **채팅 프로그램 개발 계획서**

### 1. **프로젝트 개요**
- **프로젝트명** : 클라이언트-서버 기반 채팅 프로그램
- **목표** : 
  > 다수의 사용자가 로그인 후,  
  실시간으로 서버를 통해 메시지를 주고받을 수 있는 채팅 프로그램을 개발.
- **주요 기능** :
  - 서버-클라이언트 간 실시간 메시지 전송
  - 멀티 클라이언트 지원 (다중 사용자 접속)
  - 메시지 브로드캐스팅 (서버가 받은 메시지를 모든 클라이언트에 전송)

---

### 2. **개발 목표**
- **기본 목표**: 
  - 여러 클라이언트가 서버에 접속하여 실시간으로 채팅하는 시스템 개발.  
  - 채팅 메시지는 서버를 통해 중계되며, 모든 참여 클라이언트에게 전송.  
  - DB를 통해 메시지 기록 저장 및 불러오기.
  
- **추가 목표**:
  - 사용자 로그인 기능 추가.
  - 복수의 단체창 생성 및 창별 클라이언트 관리.
  - 사용자 친화적인 그래픽 인터페이스 (GUI) 개발.
  - 이미지, 파일 첨부 기능 개발.
  - 서버 성능 평가 및 개선.

---

### 3. **개발 과정**

| **단계**               | **주요 작업**                    | **기간(일정)** |
|------------------------|--------------------------------------------|------|
| **1. 개발계획서 작성** | 프로젝트 및 기능 정의, 일정 예측           | 2일  |
| **2. 소프트웨어 설계** | 화면정의서 작성, 인터페이스 및 시스템 설계 | 2일  |
| **2. 프로젝트 준비**   | 프로젝트 셋팅, 역할 분담, 코드 이해        | 3일  |
| **4. 소프트웨어 구현** | 프로젝트 관리, UI, 클라이언트, 서버, DB    | 10일 |
| **5. 통합 및 테스트**  | 서버-클라이언트 통합, 메세지 송수신 테스트 | 4일  |
| **6. 버그 수정 및 최적화** | 발생한 문제 해결, 성능 안정성 최적화   | 3일  |
| **7. 선택: 추가 개발** | 추가 목표 달성을 위한 개발                 | 5일  |

---

### 4. **주요 개발 세부 사항**

#### **1) UI/UX**
- **기능** : 
  > 사용자가 서비스를 조작하기 위한 View 및 인터페이스 구현  
- **구현 계획** : 
  > 채팅창에 적합하고 사용자 친화적인 UI 설계.
- **사용 기술** : 
  > Java, Swing, JFrame.


#### **2) Protocol 설계**
- **기능** : 
  > 사용자가 조작한 서비스에 해당하는 동작을 위한 규약 설계  
- **구현 계획** : 
  > 각 기능별 프로토콜 설계
- **사용 기술** : 
  > Java, Protocol 관련 기술


#### **3) 클라이언트 사이드**
- **기능** : 
  > 클라이언트는 채팅창과 서버가 메세지를 주고 받도록 중계 역할 수행
- **구현 계획** :
  > 클라이언트에서 서버로 연결 요청을 보내고, 연결이 완료되면 채팅 가능.  
  사용자가 입력한 메시지를 서버로 전송하고, 서버로부터 메시지를 수신하여 출력  
  서버로부터 수신되는 메시지를 쓰레드를 통해 비동기적으로 처리.
- **사용 기술** :  
  > Java, 소켓 프로그래밍, 단일스레드.


#### **4) 서버 개발**
- **기능** : 
  > 클라이언트의 접속 요청을 처리하고, 메시지를 다른 클라이언트에 전송하는 역할.
- **구현 계획**:
  > **접속 처리**: 다중 클라이언트의 접속을 지원하기 위해 멀티스레드 환경을 구축.  
  **메시지 중계**: 서버에서 받은 메시지를 다른 클라이언트에게 브로드캐스팅.  
  **예외 처리**: 클라이언트 연결 끊김, 메시지 전송 실패 등의 예외 상황 처리.
- **사용 기술**: 
  > Java, 소켓 프로그래밍, 브로드캐스팅, 멀티스레드.


#### **5) DB 구축**
- **기능** : 
  > 회원정보와 메세지를 저장하는 DB구축 & DB와 서버를 연동하는 클래스 설계
- **구현 계획**:
  > **DB구축**: 오라클 데이터 베이스를 통한 DB구축.  
  > **메시지 중계**: 서버로부터 받은 메시지를 DB에 저장하고 중계하는 역할.  
  > **회원 관리**: 서버로부터 받은 클라이언트 정보 저장 및 관리.
- **사용 기술**: 
  > Java, Oracle DB.


#### **6) 통합 및 테스트**
- **테스트 계획**:
  > **단위 테스트**: 각 기능이 독립적으로 잘 작동하는지 확인 (메시지 송수신).  
  **통합 테스트**: 클라이언트와 서버 간의 통신이 원활한지 전체 흐름을 테스트.  
  **다중 사용자 테스트**: 클라이언트가 동시에 접속해도 문제없는지 확인.
- **테스트 도구**: 
  > JUnit 또는 수동 테스트, 콘솔 출력 확인.

---

### 5. **필요 기술 및 도구**
- **Java** : 클라이언트, 서버, UI 등 채팅 앱 기능 구현
- **JDBC** : 서버와 데이터베이스 연동.
- **소켓 프로그래밍** : 서버-클라이언트 간 통신 구현.
- **멀티스레드** : 서버와 클라이언트에서 비동기 처리를 위한 쓰레드 관리.
- **IDE** : IntelliJ, eclipse, Toad

---

### 6. **예상 위험 요소 및 해결 방안**
- **동시 접속 문제**:
  > 여러 클라이언트가 접속할 때, 서버 성능 저하가 발생할 수 있음.  
  멀티스레드 환경에서 각 클라이언트의 연결을 적절히 처리하고,  
  연결 상태를 모니터링하여 문제를 해결할 수 있도록 계획.
- **데이터베이스 처리 오류**: 
  > 데이터베이스 연결 실패 또는 인증 오류가 발생할 수 있음.  
  > 예외 처리를 통해 연결 오류를 처리하고, 적절한 메시지를 반환하는 로직을 작성.
