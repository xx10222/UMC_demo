package com.example.demo.src.post.model;

import lombok.*;
import org.graalvm.compiler.serviceprovider.ServiceProvider;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPostReq {
    private int userIdx;
    private String textcontent;
    private String postImgUrl;
}
