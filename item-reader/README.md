# ItemReader

### ItemReader 인터페이스

[Appendix A: List of ItemReaders and ItemWriters](https://docs.spring.io/spring-batch/docs/current/reference/html/appendix.html#itemReadersAppendix)

### 파일 입력

* FlatFileItemReader
* StaxEventItemReader

### JSON

* JsonItemReader

### 데이터베이스 입력

* JdbcCursorItemReader
* JdbcPagingItemReader
* HibernateCursorItemReader
* HibernatePagingItemReader
* MongoItemReader
* RepositoryItemReader

### 기존 서비스

* ItemReaderAdapter

### 커스텀 입력

* ItemReader 직접 구현
* ItemStream `open`, `update`, `close`

### 에러 처리

* 스탭 빌드 시에 저장한 예외와 스킵 타운트 설정
* ItemReadListener, `@OnReadError`
* StepListener, `@AfterStep`
