# 스프링배치 완벽 가이드 2/e

[![](http://image.kyobobook.co.kr/images/book/large/168/l9791161755168.jpg)](https://www.kyobobook.co.kr/product/detailViewKor.laf?barcode=9791161755168)

MariaDB 계정 생성

```sql
create database spring_batch;
create user 'spring_batch'@'%' identified by 'p@ssw0rd';
grant all privileges on spring_batch.* to 'spring_batch'@'%';
flush privileges;
```
