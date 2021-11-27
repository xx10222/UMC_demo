package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.GetCommentRes;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.PageInfo;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class PostProvider {
    private final PostDao postDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired //readme 참고
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService; // JWT부분은 7주차에 다루므로 모르셔도 됩니다!
    }

    // Post들의 정보를 조회
    public List<GetPostRes> getPosts() throws BaseException {
        try {
            List<GetPostRes> getPostRes = postDao.getPosts();
            return getPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Post들의 정보를 조회 - 페이지
    public List<GetPostRes> getPostsPage(int last_data_id, int size) throws BaseException {
        try {
            List<GetPostRes> getPostsRes = postDao.getPostsPage(last_data_id, size);
            return getPostsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 userIdx를 갖는 User의 정보 조회
    public GetPostRes getPost(int postIdx) throws BaseException {
        try {
            GetPostRes getPostRes = postDao.getPost(postIdx);
            return getPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 pageInfo
    public PageInfo getPageInfo(int last_data_id) throws BaseException {
        //try {
            PageInfo pageInfo = postDao.getPageInfo(last_data_id);
            System.out.println("provider : "+pageInfo.getDataPerPage());
            return pageInfo;
        //} catch (Exception exception) {
        //    throw new BaseException(DATABASE_ERROR);
        //}
    }

    // Comment들의 정보를 조회
    public List<GetCommentRes> getComments(int postIdx) throws BaseException {
        try {
            List<GetCommentRes> getCommentRes = postDao.getComments(postIdx);
            return getCommentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 해당 commentIdx을 갖는 Comment들의 정보 조회
    public GetCommentRes getCommentByCommentIdx(int commentIdx) throws BaseException {
        try {
            GetCommentRes getCommentRes = postDao.getCommentByCommentIdx(commentIdx);
            return getCommentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
