package kr.or.kosa.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
Spring Boot Cache 사용목적

- API에서 Database로부터 데이터를 조회하는 경우 
 '동일한 데이터를 반복하여 조회' 해옴으로써 '불필요한 일을 반복하는 문제'가 발생함
 
- 이에 따라서 API 캐시를 통해서 DB로부터 반복적으로 데이터를 조회해 
  오는 일에 대해 최초 데이터를 조회해 온 뒤 이후는 캐시에서 데이터를 
  조회해 오는 처리를 수행함으로써 API의 성능을 올리며 응답시간을 
  단축하는 효율성을 가져오다 
  
Spring Boot Cache 사용 대상

- '자주 조회되는 데이터'에 대해서 캐시에 저장하여 성능향상을 기대할 수 있음. 
  이를 통해 외부 저장소에 대해서 부하 및 데이터베이스 등의 부하를 줄여서 서버의 성능을 개선할 수 있음.
- Spring Boot Cache의 사용처는 사용자의 로그인 정보(인증정보), 게시글 목록 등에 사용된다.

Spring Boot Cache 어노테이션 설명
Annotation	설명
@EnableCaching	Spring Boot Cache를 사용하기 위해 '캐시 활성화'를 위한 어노테이션을 의미함

@CacheConfig	캐시정보를 '클래스 단위'로 사용하고 관리하기 위한 어노테이션을 의미함.

@Cacheable	    캐시정보를 메모리 상에 '저장'하거나 '조회' 해오는 기능을 수행하는 어노테이션임.
                - 캐시 존재 시 '메서드 호출 전 실행'
				- 캐시 미 존재 시 '메서드 호출 후 실행'
				
@CachePut	    캐시 정보를 메모리상에 '저장'하며 존재 시 갱신하는 기능을 수행하는 어노테이션임.
				- 캐시 존재 시 '메서드 호출 후 실행'
				- 캐시 미 존재 시 '메서드 호출 후 실행'
				
@CacheEvict     캐시 정보를 메모리상에 '삭제'하는 기능을 수행하는 어노테이션임.
				- beforeInvocation 속성값이 true일때 '메서드 호출 전 실행'
				- beforeInvocation 속성값이 false일때 '메서드 호출 후 실행'

@Caching        여러 개의 '캐시 어노테이션'을 '함께 사용'할 때 사용하는 어노테이션임.
  
참조 : https://adjh54.tistory.com/165#2)%20API%20%EC%BA%90%EC%8B%B1%2C%20Spring%20Boot%20Cache%20%EC%9D%B4%ED%95%B4-1
  
*/

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
    	return new ConcurrentMapCacheManager();
    }
}