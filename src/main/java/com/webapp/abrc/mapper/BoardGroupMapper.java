package com.webapp.abrc.mapper;

import com.webapp.abrc.domain.BoardGroupVO;
import com.webapp.abrc.domain.BoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface BoardGroupMapper {
    BoardGroupVO selectBoardGroupOne4Used(String param);
}
