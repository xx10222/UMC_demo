package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Post {
    private int postIdx;
    private int userIdx;
    private String textcontent;
    private String postImgUrl;
}
