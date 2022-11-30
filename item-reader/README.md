# ItemReader

### org.springframework.batch.item.file.FlatFileItemReader

고정너비, 구분자로 나워진 파일을 읽어들인다.

* org.springframework.batch.item.file.transform.LineTokenizer : 라인 파싱 → FieldSet 커스터마이징
* org.springframework.batch.item.file.mapping.FieldSetMapper : FieldSet → <T> 커스터마이징
* org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper : 여러 종류의 포멧을 처리할 때 사용
* 제어 중지 로직<sup>`control break logic`</sup> 사용하기
* org.springframework.batch.item.file.MultiResourceItemReader : 여러 파일을 읽어서 처리
