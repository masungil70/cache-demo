package kr.or.kosa.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.kosa.dao.CodeDAO;
import kr.or.kosa.model.CodeVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class CodeService {

	private final CodeDAO codeDAO;

	@Transactional(readOnly = true)
	@Cacheable(value = "codeListCache", key = "#code.grp_cd")
	public List<CodeVO> codeList(CodeVO code) {
    	System.out.println("CodeService.codeList->");
		return codeDAO.codeList(code);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "codeCacheInfo", key = "#code.fullKey")
	public CodeVO findByCd(CodeVO code) {
		CodeVO findCode = codeDAO.findByCd(code);
		System.out.println("code = " + code);
		System.out.println("findCode = " + findCode);
		return findCode;
	}

	@Transactional
	public int insertCode(CodeVO code) {
		return codeDAO.insertCode(code);
	}

	@Transactional
	@CacheEvict(value = "codeCacheInfo", key = "#code.fullKey", beforeInvocation = false)
	public int updateCode(CodeVO code) {
		return codeDAO.updateCode(code);
	}

	@Transactional
	@CacheEvict(value = { "codeListCache", "codeCacheInfo" }, allEntries = true)
	public int deleteCode(CodeVO code) {
		return codeDAO.deleteCode(code);
	}
}