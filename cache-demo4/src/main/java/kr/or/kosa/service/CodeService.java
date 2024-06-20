package kr.or.kosa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.kosa.dao.CodeDAO;
import kr.or.kosa.model.CodeVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeService {

	@Autowired
	CacheManager cacheManager;
	
	private final CodeDAO codeDAO;

	@Transactional(readOnly = true)
	@Cacheable(value = "codeListCache", key = "#code.grp_cd")
	public List<CodeVO> codeList(CodeVO code) {
    	System.out.println("CodeService.codeList->");
		return codeDAO.codeList(code);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "codeCacheInfo", key = "#code.grp_cd + #code.cd")
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

	//codeList에 있는 자료를 변경되지 않는 문제 있음 
	//해결법 : 아래 deleteCode에서 하는 방법과 같이 처리해야 함 
	@Transactional
	@CacheEvict(value = "codeCacheInfo", key = "#code.grp_cd + #code.cd", beforeInvocation = false)
	public int updateCode(CodeVO code) {
		return codeDAO.updateCode(code);
	}

	@Transactional
	@CacheEvict(value = "codeCacheInfo", key = "#code.grp_cd + #code.cd", beforeInvocation = false)
//	@CacheEvict(value = { "codeListCache", "codeCacheInfo" }, allEntries = true)
//위와 같이 하면 cache에 저장되는 갑의 이름이 "codeListCache", "codeCacheInfo"인 모든 자료가 제거 된다
//cache되기는 하지만 약간 성능의 저하가 생길 수 있다 
//이를 해결하기 위해 어노테이션으로 삭제되는 항목만 cache에서 제거 하고
//전체 그룹에 대한 것은 코드로 삭제를 하는 것이 좋다 
	public int deleteCode(CodeVO code) {

		cacheManager.getCache("codeListCache").evict(code.getGrp_cd());
		
		return codeDAO.deleteCode(code);
	}
}