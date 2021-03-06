package com.digicap.dcblock.admin.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.digicap.dcblock.admin.dao.AdminDAO;
import com.digicap.dcblock.admin.model.AdminDetail;
import com.digicap.dcblock.admin.model.ResponseMessage;
import com.digicap.dcblock.admin.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource("classpath:/config/common.properties")
@RestController
public class AdminController {

	@Autowired
	private AdminDAO adminDao;
	
	@Deprecated
	@Value("${SHA_256_KEY}")
	private String shaKey;
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/api/admin/page/{paging}")
	public ResponseEntity<String> getAllAdminInfo(@PathVariable("paging") int paging, HttpServletRequest req){
		//Admin admin = new Admin();
		
		List<AdminDetail> adminList = adminDao.selectAdminAll();
		
		//if(adminList != null) {
		//	admin.setAdminDetail(adminList);
		//}
		int totalPages = adminService.getAdminTotalPages();
		
		if(adminList != null) {
			MultiValueMap<String, String> headers = new HttpHeaders();
			headers.put("Content-Type", Arrays.asList("application/json"));
			
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("total_pages", totalPages);
			map.put("lists", adminList);
			
			String response = "";
			try {
				//response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(admin);
				response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);

				return new ResponseEntity<String>(response, headers, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/api/admin/{id}")
	public ResponseEntity<String> getAdminByIndex(@PathVariable("id") String id, HttpServletRequest req){
		AdminDetail adminDetail = adminDao.selectAdminById(id);
		
		if(adminDetail != null) {
			MultiValueMap<String, String> headers = new HttpHeaders();
			headers.put("Content-Type", Arrays.asList("application/json"));
			
			String response = "";
			try {
				response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(adminDetail);

				return new ResponseEntity<String>(response, headers, HttpStatus.OK);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/api/admin")
	public ResponseEntity<String> postAdmin(@RequestBody AdminDetail adminDetail, HttpServletRequest req){
		int retVal = 0;
	
		if(adminDetail != null) {
			
			//TODO: ~~Password SHA256 적용~~ Front단에서 처리하기로 함
			//String sha256 = DigestUtils.sha256Hex(adminDetail.getPassword() + shaKey);			
			//adminDetail.setPassword(sha256);
			
			//TODO: 중복된 id 값이 요청올 경우 exception 처리 혹은 ID 중복체크 URL 만들 것
			retVal = adminDao.insertAdmin(adminDetail);
			
			if(retVal != 0) {
				MultiValueMap<String, String> headers = new HttpHeaders();
				//headers.put("Content-Type", Arrays.asList("application/json"));
				headers.put("Content-Type", Arrays.asList("application/json; charset=utf-8"));

				String response = "";
				try {
					ResponseMessage resMessage = new ResponseMessage();
					resMessage.setCode(HttpStatus.OK.value());
					resMessage.setReason("success");
					
					response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(resMessage);

					return new ResponseEntity<String>(response, headers, HttpStatus.OK);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	@PatchMapping("/api/admin/{id}")
	public ResponseEntity<String> patchAdmin(@PathVariable("id") String id, 
			@RequestBody AdminDetail adminDetail, HttpServletRequest req){
		
		int retVal = 0;
		
		//TODO: id Check Service 추가 할 것
		
		if(adminDetail != null) {
			retVal = adminDao.updateAdmin(adminDetail);
			
			if(retVal != 0) {
				MultiValueMap<String, String> headers = new HttpHeaders();
				//headers.put("Content-Type", Arrays.asList("application/json"));
				headers.put("Content-Type", Arrays.asList("application/json; charset=utf-8"));

				String response = "";
				try {
					ResponseMessage resMessage = new ResponseMessage();
					resMessage.setCode(HttpStatus.OK.value());
					resMessage.setReason("success");
					
					response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(resMessage);

					return new ResponseEntity<String>(response, headers, HttpStatus.OK);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/api/admin/{id}")
	public ResponseEntity<String> deleteAdmin(@PathVariable("id") String id, HttpServletRequest req){
		int retVal = 0;
		if(id != null) {
			retVal = adminDao.deleteAdmin(id);
			
			if(retVal != 0) {
				MultiValueMap<String, String> headers = new HttpHeaders();
				//headers.put("Content-Type", Arrays.asList("application/json"));
				headers.put("Content-Type", Arrays.asList("application/json; charset=utf-8"));

				String response = "";
				try {
					ResponseMessage resMessage = new ResponseMessage();
					resMessage.setCode(HttpStatus.OK.value());
					resMessage.setReason("success");
					
					response = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(resMessage);

					return new ResponseEntity<String>(response, headers, HttpStatus.OK);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}
	
}
