create database tryit default character set utf8mb4 default collate utf8mb4_general_ci;
//general VS. unicode
// general : 간단하고, 빠른 비교를 제공하지만, 유니코드 표준에 따른 정밀한 비교가 아니다
// unicode : 유니코드 표준에 따라, 정밀한 비교를 제공하며, UTF-8, UTF-16, UTF-32 encoding 에서도 사용 가능하나 성능이 약간 떨어짐.
// -> 현재 나는 다국어 지원이 필요한 것도 아니고, 쇼핑몰 기능들을 구현하는 토이 프로젝트이기 때문에 제너럴을 택했다.

use tryit;