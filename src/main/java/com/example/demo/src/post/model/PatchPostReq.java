package com.example.demo.src.post.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchPostReq {
    private int postIdx;
    private String textcontent;
}
