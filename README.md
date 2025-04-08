# ApprovalProcessDemo  

게시글에 대한 승인 요청 및 처리 프로세스를 구현한 Spring Boot 기반 시스템입니다.
Salesforce의 Approval Process에서 영감을 받아, 템플릿 기반 승인 흐름을 설계하고 다양한 승인 유형(PARALLEL / SEQUENTIAL)을 지원합니다.

[ ERD ]  

![ApprovalProcess](https://github.com/user-attachments/assets/ae00ec80-574d-440b-a997-3ec0e6b5da7a)


📌 프로젝트 목적  

승인 프로세스가 적용된 게시글 작성/수정/승인 흐름을 설계 및 구현
Template 기반의 승인 프로세스 생성 기능 

🧱 주요 도메인  

📝 Post (게시글)
1. 게시글 등록/수정/조회 기능
2. 승인 완료 시 잠금 처리
3. 승인 요청을 통해 승인 프로세스와 연동

✅ ApprovalTemplate
1. 미리 정의된 승인 단계와 승인자를 포함하는 템플릿
2. 게시글 작성자가 승인 요청 시 해당 템플릿 기반으로 프로세스를 생성

🌀 ProcessInstance
1. 실제 실행되는 승인 프로세스 인스턴스
2. 게시글과 1:1 연관
3. 승인 상태 PENDING, COMPLETED, REJECTED 관리

🔁 ProcessInstanceNode
1. 단계별 승인 정의
2. 승인 방식: SEQUENTIAL, PARALLEL

👥 ProcessInstanceWorkItem
1. 개별 승인자 단위 작업 항목
2. 승인자별 승인/반려 상태 관리

🕓 ProcessInstanceStep
1. 승인 단계 완료 이력 기록

📌 주요 기능
 1. 게시글 등록 (Post)

 2. 게시글 수정 시 승인 상태 체크 (승인 완료 시 수정 불가 – AOP 적용)

 3. 승인 템플릿 등록 (ApprovalTemplate)

 3. 템플릿 기반 프로세스 생성 (ProcessInstance)

 4. SEQUENTIAL / PARALLEL 승인 방식 처리

 5. 승인자별 작업 항목 상태 관리 (WorkItem)

 6. 승인/반려 처리 및 이력 기록

 7. 승인 상태에 따라 게시글 상태 변

⚙️ 기술 스택  

영역 | 기술  
----|-----
Language |	Java 17
Framework |	Spring Boot 3.4
ORM |	Spring Data JPA + Hibernate
DB |	MariaDB
Build |	Gradle
기타 |	Lombok, AOP
