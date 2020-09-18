package com.webapp.abrc.service;

import com.webapp.abrc.domain.BoardGroupVO;
import com.webapp.abrc.mapper.BoardGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardGroupService {
    @Autowired
    private BoardGroupMapper boardGroupMapper;

    public BoardGroupVO selectBoardGroupOne4Used(String param) {
        return boardGroupMapper.selectBoardGroupOne4Used(param);
    }

    /*public List<?> selectBoardGroupList() {
        return sqlSession.selectList("selectBoardGroupList");
    }
 
    public BoardGroupVO selectBoardGroupOne(String param) {
        return sqlSession.selectOne("selectBoardGroupOne", param);
    }


    
    public void deleteBoardGroup(String param) {
        sqlSession.delete("deleteBoardGroup", param);
    }
    */
}
