package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageInfo {
    private int hasNext; //다음 페이지 유무
    private int dataPerPage; //페이지 별 데이터 수
    //private int startPage;
    //private int endPage;
    private int currentPage;
    private int totalPage;
}
