package com.example.demo.src.post.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchCommentReq {
    private int commentIdx;
    private String content;
}
