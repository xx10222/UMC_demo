package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Pageable;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class GetPostsPageRes {
    private List<GetPostRes> getPostsRes;
    //private Pageable pageable;
    private PageInfo pageInfo;
}
