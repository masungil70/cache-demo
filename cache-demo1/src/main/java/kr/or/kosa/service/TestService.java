package kr.or.kosa.service;

import org.springframework.stereotype.Service;

import kr.or.kosa.model.TestVo;

@Service
public class TestService {


    public TestVo getTest(String id){
        TestVo testVO = TestVo.builder()
        		.id(id)
        		.text( id + "님, 안녕하세요~!")
        		.build();

        System.out.println("[id:" + id + "] Service 에서 연산을 수행합니다");

        return testVO;
    }
}