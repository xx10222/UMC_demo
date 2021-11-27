package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PatchUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired //readme 참고
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 게시물 추가
    public int createPost(PostPostReq postPostReq) {
        String createPostQuery = "insert into Post (userIdx, textcontent, postImgUrl) VALUES (?,?,?)";
        Object[] createPostParams = new Object[]{postPostReq.getUserIdx(), postPostReq.getTextcontent(), postPostReq.getPostImgUrl()};
        this.jdbcTemplate.update(createPostQuery, createPostParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // Post 테이블에 존재하는 전체 게시물들의 정보 조회
    public List<GetPostRes> getPosts() {
        String getPostsQuery = "select * from Post"; //User 테이블에 존재하는 모든 회원들의 정보를 조회하는 쿼리
        return this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("userIdx"),
                        rs.getInt("postIdx"),
                        rs.getString("textcontent"),
                        rs.getString("postImgUrl"))
        );
    }

    // Post 테이블에 존재하는 전체 게시물들의 정보 조회 - 페이지
    public List<GetPostRes> getPostsPage(int last_data_id, int size) {
        String getPostsPageQuery = "select * from Post limit ? offset ?";
        Object[] getPostsPageParams = new Object[]{size, last_data_id};

        return this.jdbcTemplate.query(getPostsPageQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("textcontent"),
                        rs.getString("postImgUrl")),
                getPostsPageParams);
    }

    // pageinfo 조회하기
    public PageInfo getPageInfo(int last_date_id) {
        int hasNext = 0; //다음 페이지 유무
        int currentPage, totalPage;
        int size = 5;
        String getLastDataQuery = "SELECT postIdx FROM Post ORDER BY postIdx DESC LIMIT 1;";
        int totalData = this.jdbcTemplate.queryForObject(getLastDataQuery, int.class);

        if(totalData>last_date_id+size) //페이지 유무 확인
        {
            hasNext = 1;
        }
        System.out.println("dao : "+hasNext);
        currentPage = last_date_id/size+1;
        totalPage = totalData/size+1;

        PageInfo pageInfo = new PageInfo(hasNext,size,currentPage,totalPage);
        /*pageInfo.setDataPerPage(size);
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setTotalPage(totalPage);
        pageInfo.setDataPerPage(size);
        pageInfo.setHasNext(hasNext);*/

        return pageInfo;
    }

    // 해당 postIdx를 갖는 게시물 조회
    public GetPostRes getPost(int postIdx) {
        String getPostQuery = "select * from Post where postIdx = ?"; // 해당 userIdx를 만족하는 유저를 조회하는 쿼리문
        int getPostParams = postIdx;
        return this.jdbcTemplate.queryForObject(getPostQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("userIdx"),
                        rs.getInt("postIdx"),
                        rs.getString("textcontent"),
                        rs.getString("postImgUrl")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getPostParams); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    // 게시물의 전체 댓글 조회
    public List<GetCommentRes> getComments(int postIdx) {
        String getCommentsQuery = "select * from Comment where postIdx = ?";
        int getCommentParams = postIdx;
        return this.jdbcTemplate.query(getCommentsQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("content")),
                getCommentParams
        );
    }

    // 해당 commentIdx을 갖는 댓글의 정보 조회
    public GetCommentRes getCommentByCommentIdx(int commentIdx) {
        String getCommentByCommentIdxQuery = "select * from Comment where commentIdx =?"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        int getCommentByCommentParams = commentIdx;
        return this.jdbcTemplate.queryForObject(getCommentByCommentIdxQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("content")),
                getCommentByCommentParams);
    }

    // 게시물 수정
    public int modifyPostTextcontent(PatchPostReq patchPostReq) {
        String modifyPostTextcontentQuery = "update Post set textcontent = ? where postIdx = ? ";
        Object[] modifyPostTextcontentParams = new Object[]{patchPostReq.getTextcontent(), patchPostReq.getPostIdx()}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyPostTextcontentQuery, modifyPostTextcontentParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

    // 댓글 추가
    public int createComment(PostCommentReq postCommentReq, int postIdx) {
        String createCommentQuery = "insert into Comment(postIdx, userIdx, content) values (?,?,?);";
        Object[] createCommentParams = new Object[]{postIdx, postCommentReq.getUserIdx(), postCommentReq.getContent()};
        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    // 게시물 수정
    public int modifyComment(PatchCommentReq patchCommentReq) {
        String modifyCommentQuery = "update Comment set content = ? where commentIdx = ? ";
        Object[] modifyCommentParams = new Object[]{patchCommentReq.getContent(), patchCommentReq.getCommentIdx()}; // 주입될 값들(nickname, userIdx) 순

        return this.jdbcTemplate.update(modifyCommentQuery, modifyCommentParams); // 대응시켜 매핑시켜 쿼리 요청(생성했으면 1, 실패했으면 0)
    }

}
