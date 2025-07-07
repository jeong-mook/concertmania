-- 개발, 테스트 환경 스키마 생성
CREATE DATABASE  IF NOT EXISTS concertmania_dev;
CREATE DATABASE  IF NOT EXISTS concertmania_test;

-- 개발, 테스트 환경 계정 생성
CREATE USER IF NOT EXISTS 'concertuser_dev'@'%' IDENTIFIED BY 'miRmaw-jisjem-5quhwi';
CREATE USER IF NOT EXISTS 'concertuser_test'@'%' IDENTIFIED BY 'zufnY0-mestuc-vudvap';
GRANT ALL ON concertmania_dev.* TO 'concertuser_dev'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON concertmania_test.* TO 'concertuser_test'@'%';
FLUSH PRIVILEGES;