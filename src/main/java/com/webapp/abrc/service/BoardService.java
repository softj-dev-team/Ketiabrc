package com.webapp.abrc.service;

//import com.webapp.abrc.domain.FileVO;
import com.webapp.abrc.domain.BoardReplyVO;
import com.webapp.abrc.domain.BoardVO;
import com.webapp.abrc.domain.FileVO;
import com.webapp.abrc.domain.SearchVO;
import com.webapp.abrc.mapper.BoardMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    private SqlSessionTemplate sqlSession;
    @Autowired
    private DataSourceTransactionManager txManager;
    @Autowired
    private BoardMapper boardMapper;
        
    public int selectBoardCount(SearchVO params) {
        return boardMapper.selectBoardCount(params);
    }
    
    public List<?> selectBoardList(SearchVO params) {
        return boardMapper.selectBoardList(params);
    }

    public BoardVO selectBoardOne(String params) {
        return boardMapper.selectBoardOne(params);
    }

    public List<?> selectMgBoardList(SearchVO param) {
        return sqlSession.selectList("selectMgBoard8List", param);
    }
    public List<?> selectMgBoardListToast(SearchVO param) {
        return sqlSession.selectList("selectMgBoardListToast", param);
    }
    
    /**
     * 글 저장.
     */
    public void insertBoard(BoardVO params, List<FileVO> filelist, String[] fileno) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        
        try {
            if (params.getBrdno() == null || "".equals(params.getBrdno())) {
                 boardMapper.insertBoard(params);
            } else {
                boardMapper.updateBoard(params);
            }
            
            if (fileno != null) {
                HashMap<String, String[]> fparam = new HashMap<String, String[]>();
                fparam.put("fileno", fileno);
                boardMapper.deleteBoard8File(fparam);
            }
            
            if (filelist != null) {
	            for (FileVO f : filelist) {
	                f.setParentPK(params.getBrdno());
	                boardMapper.insertBoard8File(f);
	            }
            }
            txManager.commit(status);
        } catch (TransactionException ex) {
            txManager.rollback(status);
            System.out.println("데이터 저장 오류: " + ex.toString());
        }

    }

    public void updateBoard8Read(String params) {
        boardMapper.updateBoard8Read(params);
    }
    
    public void deleteBoardOne(String param) {
        sqlSession.delete("deleteBoard8One", param);
    }
    
    public List<?> selectBoard8FileList(String params) {
        return boardMapper.selectBoard8FileList(params);
    }
    
    /* =============================================================== */
    public List<?> selectBoard8ReplyList(String param) {
        return sqlSession.selectList("selectBoard8ReplyList", param);
    }
    
    /**
     * 댓글 저장.
     */
    public void insertBoardReply(BoardReplyVO param) {
        if (param.getReno() == null || "".equals(param.getReno())) {
            if (param.getReparent() != null) {
                BoardReplyVO replyInfo = sqlSession.selectOne("selectBoard8ReplyParent", param.getReparent());
                param.setRedepth(replyInfo.getRedepth());
                param.setReorder(replyInfo.getReorder() + 1);
                sqlSession.selectOne("updateBoard8ReplyOrder", replyInfo);
            } else {
                Integer reorder = sqlSession.selectOne("selectBoard8ReplyMaxOrder", param.getBrdno());
                param.setReorder(reorder);
            }
            
            sqlSession.insert("insertBoard8Reply", param);
        } else {
            sqlSession.insert("updateBoard8Reply", param);
        }
    }   

    /**
     * 댓글 삭제.
     * 자식 댓글이 있으면 삭제 안됨.
     */
    public boolean deleteBoard8Reply(String param) {
        Integer cnt = sqlSession.selectOne("selectBoard8ReplyChild", param);
        
        if ( cnt > 0) {
            return false;
        }
        
        sqlSession.update("updateBoard8ReplyOrder4Delete", param);
        
        sqlSession.delete("deleteBoard8Reply", param);
        
        return true;
    }
    public BoardVO selectBoardMaxOne(String param) {
        return sqlSession.selectOne("selectBoardMaxOne", param);
    }
}
