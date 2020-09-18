package com.webapp.abrc.mapper;

import com.webapp.abrc.domain.BoardVO;
import com.webapp.abrc.domain.FileVO;
import com.webapp.abrc.domain.SearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BoardMapper {
    int selectBoardCount(SearchVO params);
	List<Map<String,Object>> selectBoardList(SearchVO params);
	BoardVO selectBoardOne(String parmas);
	int insertBoard(BoardVO params);
	int updateBoard(BoardVO params);
	int deleteBoard(Map<String,Object> params);
	int deleteBoard8File(HashMap<String, String[]> hashMap);
	int insertBoard8File(FileVO params);
	int updateBoard8Read(String params);
	List<Map<String,Object>> selectBoard8FileList(String params);
}
