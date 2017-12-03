이 파일은 카카오 2차 온라인 코딩테스트 소스에 대한 설명을 포함합니다.

1. 클래스별 설명
 1) Main.java
  - main 메서드를 포함하는 클래스.
  - Token API와 Seed API를 이용하여 Document API를 사용할 수 있도록 준비한다.
  - API 전달 받은 데이터를 이용하여 호스트에 Service.java를 구동한다.
 2) TokenMng.java
  - 제출용 토큰을 관리하는 클래스 (싱글톤 패턴)
  - Token API의 응답이 200일 경우, 토큰을 [res/token] 파일에 저장하여 다시 활용할 수 있도록 구현하였다.
  - Token API의 응답이 403일 경우, [res/token]파일을 읽어들여 이미 생성된 유효한 토큰을 가져오도록 구현하였다.
 3) Doc.java
  - Document API를 통해 전달받은 값을 모델링한 클래스.
 3) Image.java
  - Document API를 통해 전달받은 값중 이미지에 대한 정보를 모델링한 클래스.
 4) GlobalData.java
  - 전역 상수로 쓰인 변수를 관리하는 클래스. 각 변수는 URL이 저장되었있다.
 5) JSONParser.java
  - JSON 데이터를 파싱하는 클래스.
  - Feature Extraction API, Document API를 이용했을때 전달받은 JSON 데이터를 파싱하는데 사용한다.
 6) Connection.java 
  - URL을 Connection하는 클래스.(싱글톤 패턴)
 7) Service.java
  - 데이터를 API를 통해 실제로 이미지 관련 처리를 하는 클래스.
  - JSON을 파싱하며, Connection.java를 통해 API를 호출한다.
  - Runnable를 상속받아 스레드로 구동한다.
  
 2. 동작 원리
  1) Main.java에 main메서드를 시작으로 한다.
  2) TokenMng.java에서 토큰을 생성한다.
  3) Seed API를 통해 카테고리별로 시작 데이터를 가져온다.
  4) 각 Seed별로  Service 스레드를 생성하고, 생성된 스레드를 실행한다.
  5) Service들은 Seed API를 통해 전달 받은 Document id를 이용하여 Document정보를 가져온다. 
  6) Document에는 Image관련 정보가 포함되어 있는데, Image가 50개 이하일 경우 nextUrl정보를 이용하여 다음 Document의 정보를 가져온다.
  7) Document에 포함되어 있는 Image는 Feature Extraction API를 통해 feature를 태깅하게 된다.
  8) feature는 id를 키값으로 갖는 HashMap으로 구성되어, 각 id에 feature가 존재할 경우에만 태깅이 된다.  
  9) 8번과정까지 모두 마치게 되면, 데이터들을 용도에 맞게 생성된(ADD, DELETE) JSONArray에 저장되게 된다.
  10) JSONArray에 저장된 JSONObject와 상승하는 Image데이터는 Document에서 삭제된다.
  11) 각각의 JSONArray는 데이터가 50개를 채울 경우 ADD하거나 DELETE하게 된다.
  12) 1~10번 과정은 토큰이 유효하지 않을때까지 수행하게 된다.
  13) 프로그램이 구동하면서 Document API, Feature Extraction API의 응답이 503일 경우, 0.1초 대기하였다가 다시 요청하게 된다.
  
  
   