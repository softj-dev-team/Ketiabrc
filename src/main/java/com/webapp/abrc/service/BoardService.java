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
    public List<?> selectBoard8ReplyList(String params) {
        return boardMapper.selectBoard8ReplyList(params);
    }
    
    /**
     * 댓글 저장.
     */
    public void insertBoardReply(BoardReplyVO params) {
        if (params.getReno() == null || "".equals(params.getReno())) {
            if (params.getReparent() != null) {
                BoardReplyVO replyInfo = boardMapper.selectBoard8ReplyParent(params.getReparent());
                params.setRedepth(replyInfo.getRedepth());
                params.setReorder(replyInfo.getReorder() + 1);
                boardMapper.updateBoard8ReplyOrder(replyInfo);
            } else {
                Integer reorder = boardMapper.selectBoard8ReplyMaxOrder(params.getBrdno());
                params.setReorder(reorder);
            }
            
            boardMapper.insertBoard8Reply(params);
        } else {
            boardMapper.updateBoard8Reply(params);
        }
    }   

    /**
     * 댓글 삭제.
     * 자식 댓글이 있으면 삭제 안됨.
     */
    public boolean deleteBoard8Reply(String params) {
        Integer cnt = boardMapper.selectBoard8ReplyChild(params);
        
        if ( cnt > 0) {
            return false;
        }
        
        boardMapper.updateBoard8ReplyOrder4Delete(params);
        
        boardMapper.deleteBoard8Reply(params);
        
        return true;
    }

    public BoardVO selectBoardMaxOne(String param) {
        return sqlSession.selectOne("selectBoardMaxOne", param);
    }
}
