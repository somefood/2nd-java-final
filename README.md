# STARTERS 취업부트캠프 최종평가(Java)

### 평가 안내 ([노션 링크](https://www.notion.so/flearnerhq/STARTERS-Java-8b1d1e267df64ff6921e24f966e2ea0e))

---

- 아래의 정보를 참고하여 지원서 접수 서비스를 제작합니다.
- 평가일정
    - 시험 시작 ~ 오후 6시 (점심시간인 오후 12시 30분 ~ 2시에는 반드시 퇴실)
- 평가방법
    - 평가배점표에 따라 기본점수에서 가감하여 총합된 점수로 평가
- 제출방법
    - 제출은 Github Repository로 제출하고, 시간 내에 Pull Request를 진행합니다.
        - API 명세서는 README 파일에 작성합니다. (프로젝트 폴더 내 새로 생성하여 구성)
    - [구글폼](https://forms.gle/quAHq1QUvnHubZHw5)을 통해 이미지 파일, AWS EC2 인스턴스의 퍼블릭 IP 및 Github 아이디를 제출합니다.
        - Class Diagram, E-R Diagram은 이미지 파일로 제출합니다.
    - 본 README파일을 활용하여 본인이 구현한 사항에 대해 진행 여부를 표기하여 제출합니다.

### 평가 전 주의사항

---

- 스택 별로 권장되는 코딩 표준 스타일을 활용합니다.
    - [JavaScript 코딩 표준](https://www.notion.so/JavaScript-4931f74d49054eaa9a574a230946fecb)
- 인터넷 상에서 구현에 필요한 사항을 검색/활용할 수 있습니다.
- 기타 부정행위로 간주될만한 사항이 적발될 경우 강제퇴실 처리

### 평가배점

---

| 구분 | 내용 |
| --- | --- |
| 기본점수 | 200점 |
| 코딩 표준 스타일 | 미준수 시 건당 -1점 |
| 전제사항 | 충족 시 +20점, 미 충족시 -10점 |
| 기능 요구사항  | 구현 시 +20점, 미 구현시 -20점
| | 단, 추가점수에 해당하는 항목은 미 구현 감점없음 |
| 부분 점수 | 작성 및 구현된 내용에 따라 부분 점수 가감 |

### 전제사항

---

- 로그인/회원가입은 따로 구현하지 않으며, 이미 확보된 회원데이터가 존재하는 것을 가정하고 구현합니다.
    - [X]  스프링 프로젝트 실행 시 프로젝트에서 기본적으로 참조하고 있는 데이터베이스 생성 혹은 제공하여야합니다.
    - [X]  데이터베이스는 내/외장 데이터베이스를 활용합니다.
    - [X]  회원은 교수자와 학생으로 구분합니다.
    - [X]  회원 정보는 아래의 표와 같이구성하며, 추가데이터 및 항목 별 변수타입은 올바른 값으로 자율 구성하여도 무관합니다.
      (단, 회원 데이터 구성은 최소 10개 이상 구성)

        | 회원정보 | 아이디 (이메일) | 비밀번호 | 회원 이름 | 회원 구분 | 학번 (사번) |
        | --- | --- | --- | --- | --- | --- |

- 강의 생성은 따로 구현하지 않으며, 이미 확보된 데이터가 존재하는 것을 가정하고 구현합니다.
    - [X]  스프링 프로젝트 실행 시 프로젝트에서 기본적으로 참조하고 있는 데이터베이스 생성 혹은 제공하여야합니다.
    - [X]  데이터베이스는 내장 데이터베이스인 H2를 활용합니다.
    - [X]  강의 정보는 아래의 표와 같이구성하며, 추가데이터 및 항목 별 변수타입은 올바른 값으로 자율 구성하여도 무관합니다.
      (단, 강의 당 정원은 5명, 최대 학점은 3점으로 한정하며 데이터 구성은 최소 10개 이상 구성)

        | 강의정보 | 강의명 | 담당교수 | 강의요일 | 강의시간 | 정원 | 학점 |
        | --- | --- | --- | --- | --- | --- | --- |

- [X]  강의 장바구니 기간은 2023년 1월 9일 오후 2시부터 1월 10일 오후 6시까지이며, 강의 신청 가능 기간은 2023년 1월 11일 오후 2시부터 오후 6시까지로 설정합니다.

### 기능 요구사항


# 파일 내 ANSWER.md 파일에 API와 이미지 적어두었습니다! 꼭 확인 부탁드립니다!

---

- **공통**
    - [X]  Class Diagram 작성
    - [X]  E-R Diagram 작성
    - [X]  API 명세서 작성
- **사전 수강 신청 (장바구니 기능)**
    - [X]  장바구니 기능을 활용하여 강의를 사전 신청할 수 있습니다.
        - [X]  사전 신청자가 강의의 정원을 넘지 않은 경우 강의 신청이 자동으로 이루어집니다.
        - [x]  사전 신청자가 강의의 정원을 넘은 경우 강의 신청이 이루어지지 않습니다.
    - [X]  사전 수강 신청이 이루어지지 않은 경우 찜 상태로 상태값을 변환합니다.
    - [X]  신청한 강의는 요일/시간이 겹치도록 신청할 수 없습니다.
- **본 수강 신청**
    - [X]  회원은 최대 21학점까지 강의를 신청할 수 있습니다.
    - [X]  강의 신청 기간에 정원이내의 인원은 선착순으로 신청이 가능합니다.
    - [X]  신청한 강의는 요일/시간이 겹치도록 신청할 수 없습니다. (장바구니로 기 신청된 강의가 있을 경우 모두 포함)
- **수강 신청 상태 관리**
    - [X]  강의 신청의 상태는 장바구니, 신청완료, 찜 총 3가지 상태로 관리합니다.
    - [X]  수강 신청에 실패하거나 취소할 경우 별도의 데이터를 생성 및 저장하지 않고 삭제합니다.
- 교수자
    - [X]  교수자가 담당하고 있는 강의의 신청 현황을 조회할 수 있습니다.
- 배포 (추가점수)
    - [X]  AWS 개인 계정을 활용하여 EC2 인스턴스(t2.micro)에 배포를 진행합니다.
        - 인스턴스명은 본인영문이름_final-test로 구성 (예 woosublee_final-test)
    - [X]  배포된 EC2 인스턴스에 RDS를 연결합니다.
