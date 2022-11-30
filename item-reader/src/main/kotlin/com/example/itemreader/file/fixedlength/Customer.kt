package com.example.itemreader.file.fixedlength

data class Customer(
    /**
     * 이름
     */
    val firstName: String,
    /**
     * 가운데 이름의 첫 글자
     */
    val middleInitial: String,
    /**
     * 성
     */
    val lastName: String,
    /**
     * 주소에서 건물 번호 부분
     */
    val addressNumber: String,
    /**
     * 거주하는 거리 이름
     */
    val street: String,
    /**
     * 거주 도시
     */
    val city: String,
    /**
     * CA(캘리포티나), TX(텍사스) 등 주의 두 자리 약자
     */
    val state: String,
    /**
     * 우편번호
     */
    val zipCode: String
)
