package kr.or.kosa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.model.CodeVO;
import kr.or.kosa.service.CodeService;
import lombok.RequiredArgsConstructor;

/* 테스트 하는 방법
 * 코드목록얻기  
 * curl http://localhost:8090/codeList?grp_cd=lang
 * 코드 상세보기 
 * curl http://localhost:8090/findByCd?grp_cd=lang&cd=java
 * 코드 삭제보기 
 * curl http://localhost:8090/deleteCode?grp_cd=lang&cd=java
 * 코드목록얻기  
 * curl http://localhost:8090/findByCd?grp_cd=lang
 * 코드 상세보기 
 * curl http://localhost:8090/deleteCode?grp_cd=lang&cd=java
 * 
 * 
 */
@RestController
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;
    
    @GetMapping("/codeList")
    public ResponseEntity<List<CodeVO>> codeList(CodeVO code){
    	System.out.println("CodeController.codeList->");
        return ResponseEntity.status(HttpStatus.OK).body(
        		codeService.codeList(code));
    }
    
    @GetMapping("/findByCd")
    public ResponseEntity<CodeVO> findByCd(CodeVO code){
    	System.out.println("CodeController.findByCd->");
        return ResponseEntity.status(HttpStatus.OK).body(
        		codeService.findByCd(code));
    }
    
    @GetMapping("/deleteCode")
    public ResponseEntity<Integer> deleteCode(@RequestParam(name="grp_cd") String grp_cd, @RequestParam(name="cd") String cd){
    	System.out.println("CodeController.deleteCode->");
        return ResponseEntity.status(HttpStatus.OK).body(
        		codeService.deleteCode(CodeVO.builder().grp_cd(grp_cd).cd(cd).build()));
    }
}