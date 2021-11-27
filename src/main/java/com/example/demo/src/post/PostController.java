package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;


@RestController
@RequestMapping("/app/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;

    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService) {
        this.postProvider=postProvider;
        this.postService=postService;
        this.jwtService=jwtService;
    }

    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/posts
    public BaseResponse<List<GetPostRes>> getPosts() throws BaseException {
        List<GetPostRes> getPostsRes = postProvider.getPosts();
        return new BaseResponse<>(getPostsRes);
    }

    @ResponseBody
    @GetMapping("/page") // (GET) 127.0.0.1:9000/app/posts/page?last_data_id=?
    public BaseResponse<GetPostsPageRes> getPostsPage(@RequestParam(required = false) int last_data_id) throws BaseException {
        PageInfo pageInfo = postProvider.getPageInfo(last_data_id);
        int size = pageInfo.getDataPerPage();
        List<GetPostRes> getPostsRes = postProvider.getPostsPage(last_data_id, size);
        GetPostsPageRes getPostsPageRes = new GetPostsPageRes(getPostsRes,pageInfo);
        //getPostsPageRes.setGetPostsRes(getPostsRes);
        //getPostsPageRes.setPageInfo(pageInfo);

        return new BaseResponse<>(getPostsPageRes);
    }

    //[POST] 게시물 추가
    @ResponseBody
    @PostMapping("/write")    // (GET) 127.0.0.1:9000/app/posts/write
    public BaseResponse<PostPostRes> createPost(@RequestBody PostPostReq postPostReq) throws BaseException {
        PostPostRes postPostRes = postService.createPost(postPostReq);
        return new BaseResponse<>(postPostRes);
    }

     /**
     * 게시물 1개 조회 API
     * [GET] /posts/:postIdx
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{postIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetPostRes> getPost(@PathVariable("postIdx") int postIdx) {
        try {
            GetPostRes getPostRes = postProvider.getPost(postIdx);
            return new BaseResponse<>(getPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물정보변경 API
     * [PATCH] /posts/:postIdx/update
     */
    @ResponseBody
    @PatchMapping("/{postIdx}/update") // (GET) 127.0.0.1:9000/app/posts/:postIdx/update
    public BaseResponse<String> modifyPostContent(@PathVariable("postIdx") int postIdx, @RequestBody Post post) {
        try {
            PatchPostReq patchPostReq = new PatchPostReq(postIdx, post.getTextcontent());
            postService.modifyPostTextcontent(patchPostReq);
            String result = "게시물이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물의 댓글 조회 API
     * [GET] /posts/:postIdx/comments
     */
    @ResponseBody
    @GetMapping("/{postIdx}/comments") // (GET) 127.0.0.1:9000/app/posts/:postIdx/comments
    public BaseResponse<List<GetCommentRes>> getComments(@PathVariable("postIdx") int postIdx) {
        try {
            List<GetCommentRes> getCommentRes = postProvider.getComments(postIdx);
            return new BaseResponse<>(getCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시물의 댓글 추가 API
     * [POST] /posts/:postIdx/comments/write
     */
    @ResponseBody
    @PostMapping("/{postIdx}/comments/write")    // (GET) 127.0.0.1:9000/app/posts/write
    public BaseResponse<PostCommentRes> createComment(@RequestBody PostCommentReq postCommentReq, @PathVariable("postIdx") int postIdx) throws BaseException {
        PostCommentRes postCommentRes = postService.createComment(postCommentReq, postIdx);
        return new BaseResponse<>(postCommentRes);
    }



    /**
     * 특정 댓글 조회 API
     * [GET] /app/posts/comments/{commentIdx}
     */
    @ResponseBody
    @GetMapping("/comments/{commentIdx}")
    public BaseResponse<GetCommentRes> getCommentByCommentIdx(@PathVariable("commentIdx") int commentIdx) {
        try {
            GetCommentRes getCommentRes = postProvider.getCommentByCommentIdx(commentIdx);
            return new BaseResponse<>(getCommentRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 수정 API !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * [PATCH] /app/posts/comments/{commentIdx}/update
     */
    @ResponseBody
    @PatchMapping("/comments/{commentIdx}/update") // (GET) 127.0.0.1:9000/app/posts/:postIdx/update
    public BaseResponse<String> modifyComment(@PathVariable("commentIdx") int commentIdx, @RequestBody Comment comment) {
        try {
            PatchCommentReq patchCommentReq = new PatchCommentReq(commentIdx, comment.getContent());
            postService.modifyComment(patchCommentReq);
            String result = "댓글이 수정되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
