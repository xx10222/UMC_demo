package com.example.demo.src.post.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommentReq {
    private int userIdx;
    private String content;
}
