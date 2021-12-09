package egov.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egov.bo.NaverBO;
import utils.TestUtil;

@Controller
public class TestController2 {
	
	@RequestMapping("naverLogin1.do")
	public String naverLogin(NaverBO bo , HttpSession session) {
		String state = TestUtil.getRandomState(); //토큰 생성기
		String redirectURL = bo.getApiURL() + state; //기존 ApiURL에다가 생성된 토큰을 추가
		session.setAttribute("state", state); //토큰을 서버에도 등록함
	//	System.out.println(redirectURL);
		return "redirect:"+redirectURL;
	}
	
	/**
	 * @param code   	★//인가코드
	 * @param state  	★//보안 state CSRF공격 차단을 위해 존재
	 * @param bo	 	//비즈니스 오브젝트 (메소드 내에서 선언해도 문제 없으나 한 곳에 두고 공유하는 것을 권장)
	 * @param request	//Http 응답
	 * @return
	 */
	@RequestMapping("naverCallback.do")
	public String naverLoginCallback (String code, String state, NaverBO bo , HttpServletRequest request) {
		System.out.println("code : " + code);
		String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
		apiURL += "&client_id=" + bo.getClient_id();
		apiURL += "&client_secret=" + bo.getClient_pw();
		apiURL += "&redirect_uri=" + bo.getCallbackURL(); //없어도 상관없는?
		apiURL += "&code=" + code;
		apiURL += "&state=" + state;
		
		String access_token = "";
		String refresh_token ="";
		try {
			URL url = new URL (apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			int responseCode = con.getResponseCode();
			
			BufferedReader br;
			
			if(responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			else 					br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			
			String inputLine;
			StringBuffer res = new StringBuffer();
			while((inputLine = br.readLine()) != null ) {
				res.append(inputLine);
			}
			br.close();
			if(responseCode == 200) {
				System.out.println("get token json : "+res.toString());
				Map <String, String> map = TestUtil.parseToken(res.toString());
				access_token = map.get("access_token");
				refresh_token = map.get("refresh_token");
				
				 
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		String locationMove ="";
		
		if(access_token == null || access_token.equals("null")) {
			/* 토큰 정보가 없으면 정보가 올바르지 않으므로 메인화면으로*/
			locationMove = "redirect:/";
		} else {
			request.setAttribute("access_token", access_token);
			request.setAttribute("refresh_token", refresh_token);
			locationMove = "forward:/naverGetProfile.do";
			/* 
			 * request 헤더에 access_token , refresh_token 등록
			 * forward를 할 시 현재 위치한 request 데이터 (+access_token 외 1)를 갖고
			 * 해당 RequestMapping으로 이동
			 */ 
		}
		
		return locationMove;
	}
	
	/**
	 * @param request		//위에서 그대로 갖고온 request 토큰을 갖고, 회원 조회
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("naverGetProfile.do")
	public String naverGetProfile(HttpServletRequest request) throws IOException {
		
		String access_token = (String) request.getAttribute("access_token");
		String refresh_token = (String) request.getAttribute("refresh_token");
		String header = "Bearer " + access_token;
					/* "Bearer " 공백 1칸 필수 */
		
		String apiURL = "https://openapi.naver.com/v1/nid/me";
					/*토큰을 requestHeader에 등록하고, 해당 주소로 이동 */
		Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        					
        String responseBody = TestUtil.get(apiURL,requestHeaders);
        						/*(메소드 처리) 해당 주소 내에 데이터를 Json형태의 문자열로 저장함 */
		System.out.println("get Response : "+responseBody);
		
		Map <String,String> map = TestUtil.parseProfile(responseBody.toString());
								/* 문자열Json을 Map으로 변환하여 필요한 데이터를 쉽게 뽑을 수 있도록 가공 */
		
		return "redirect:/";
	}
	/*
	@RequestMapping("testPage.do")
	public String testPage() {
		System.out.println("테스트 페이지");
		return "admin/testPage";
	}
	@RequestMapping("index.do")
	public String indexPage(Model model) {
		System.out.println("인덱스 페이지");
		return "index";
	} */
}
