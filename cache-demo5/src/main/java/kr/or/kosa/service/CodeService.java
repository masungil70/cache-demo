package kr.or.kosa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.kosa.dao.CodeDAO;
import kr.or.kosa.dto.PubSubMessage;
import kr.or.kosa.model.CodeVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeService {

	@Autowired
	CacheManager cacheManager;
	
	private final RedisPublisher redisPublisher;
	
	private final CodeDAO codeDAO;

	ObjectMapper objectMapper = new ObjectMapper();
	
	@Transactional(readOnly = true)
	@Cacheable(value = "codeListCache", key = "#code.grp_cd")
	public List<CodeVO> codeList(CodeVO code) {
    	System.out.println("CodeService.codeList->");
    	List<CodeVO> codeList = codeDAO.codeList(code);
    	return codeList;
		
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
    	redisPublisher.publish(ChannelTopic.of("code-invalidation"), code.getGrp_cd());
    	
		return codeDAO.updateCode(code);
	}

	//cache에 관련 삭제 어너테이션을 제거 하고 redis의 pub/sub관련의 구조를 이용하여 sub:onMessage()부분에서 
	//cache 관련 작업을 직접 코드로 구현한다        
	@Transactional
	public int deleteCode(CodeVO code) throws Exception {
		//디비에서 코드를 삭제한다
		int result = codeDAO.deleteCode(code); 
		System.out.println("deleteCode : cacheManager = " + cacheManager);

		//cache에서 그룹 코드와 코드로 저장된 객체를 삭제한다 (1건) 
		//cache에서 그룹 코드로 저장된 객체를 삭제한다 (N건)
		//cache 관려 작업을 하기위해 객체를 생성한다
		redisPublisher.publish(ChannelTopic.of("code-invalidation")
				, objectMapper.writeValueAsString(PubSubMessage.of("codeCacheInfo", code.getGrp_cd(), code.getCd())));
		
		return result;
	}
}