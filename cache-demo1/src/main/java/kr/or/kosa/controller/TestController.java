package kr.or.kosa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.model.TestVo;
import kr.or.kosa.service.TestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

	@Autowired
	CacheManager cacheManager;
	

    private final TestService testService;

    /* 케시 어노테이션에 대한 설명 
     value        = "TestVo" : 케시 내부에서 key에 대한 객체를 저장할 때 사용되는 객체 이름 
	 key          = "#id" : 이 API에서 id에 따라 응답값이 달라지므로 저장될 Key로 id 파라미터 값을 선언 
	 cacheManager = "cacheManager" : 위의 config에서 작성한 cacheManager 사용
	 unless       = "#id == ''" : id가 "" 일때 캐시를 저장하지 않음
	 condition    = "#id.length > 2" : id의 length가 3 이상일 때만 캐시 저장
			 
	 아래 코드 실행 확인 
	 curl http://localhost:8090/getTest?id=masungil2
	 curl http://localhost:8090/getTest?id=masungil3
	 curl http://localhost:8090/getTest?id=masungil4
	 curl http://localhost:8090/getTest?id=masungil5
	 curl http://localhost:8090/getTest?id=masungil2
	 curl http://localhost:8090/getTest?id=masungil5
	 curl http://localhost:8090/getTest?id=ma -> 케싱 안됨
	 
	 로그 확인시 서비스 영역 실행 되는 것을 확인 한다
     */
    @Cacheable(value = "TestVo", key = "#id", cacheManager = "cacheManager", unless = "#id == ''", condition = "#id.length > 2")
    @GetMapping("/getTest")
    public TestVo getData(@RequestParam String id ){

    	if (cacheManager instanceof ConcurrentMapCacheManager concurrentMapCacheManager) {
        	System.out.println("getCacheNames()->" + concurrentMapCacheManager.getCacheNames().toString());
        	System.out.println("getCache()-> " + concurrentMapCacheManager.getCache("TestVo").toString());
        	System.out.println("getName()-> " + concurrentMapCacheManager.getCache("TestVo").getName());
        	System.out.println("getNativeCache()-> " + concurrentMapCacheManager.getCache("TestVo").getNativeCache());
    	}
    	
        return testService.getTest(id);
    }
}