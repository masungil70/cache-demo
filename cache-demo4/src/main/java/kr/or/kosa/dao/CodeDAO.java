package kr.or.kosa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.or.kosa.model.CodeVO;

@Mapper
@Repository("codeDAO")
public interface CodeDAO {

	List<CodeVO> codeList(CodeVO code);
	CodeVO findByCd(CodeVO code);
	int insertCode(CodeVO code);
	int updateCode(CodeVO code);
	int deleteCode(CodeVO code);
	
}

