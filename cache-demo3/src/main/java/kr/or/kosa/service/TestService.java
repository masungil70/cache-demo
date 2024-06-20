package kr.or.kosa.service;

import org.springframework.stereotype.Service;

import kr.or.kosa.model.TestVo;

@Service
public class TestService {


    public TestVo getTestSvc(String id){
        TestVo tvo = new TestVo();
        tvo.setId(id);
        tvo.setText( id + "님, 안녕하세요~!");

        System.out.println("[id:" + id + "] Service 에서 연산을 수행합니다");

        return tvo;
    }
}