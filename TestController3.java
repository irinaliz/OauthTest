package egov.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egov.bo.KakaoBO;
import utils.TestUtil;

@Controller
public class TestController3 {
	@RequestMapping("kakaologin.do")
	public String kakaoLogin(KakaoBO bo) {

		String redirectURL = bo.getApiURL();
		System.out.println(redirectURL);
		return "redirect:"+redirectURL;
		
	}
	
	@RequestMapping("kakaoCallback.do")
	public String kakao(String code, KakaoBO bo,HttpServletRequest request) {
		System.out.println("인가 코드 : "+code);

		String apiURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code";
		apiURL += "&client_id=" + bo.getClient_id();
		apiURL += "&redirect_uri=" + bo.getCallbackURL();
		apiURL += "&code=" + code;
		String access_token ="";
		String refresh_token ="";
		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);

			int responseCode = con.getResponseCode();
			System.out.println(responseCode == 200 ? "연결 성공!" : "연결 실패");
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
				System.out.println("사용자 토큰 : "+access_token);
				System.out.println("사용자 리프레쉬 토큰" +refresh_token);
				 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		String locationMove ="";
		
		if(access_token == null || access_token.equals("null")) {
			locationMove = "redirect:/";
		} else {
			request.setAttribute("access_token", access_token);
			request.setAttribute("refresh_token", refresh_token);

			locationMove = "forward:/kakaoGetProfile.do";
		}
		
		return locationMove;
	}
	
	@RequestMapping("kakaoGetProfile.do")
	public String kakaoGetProfile(HttpServletRequest request) {

		String access_token = (String) request.getAttribute("access_token");
		String refresh_token = (String) request.getAttribute("refresh_token");
		String header = "Bearer " + access_token;
		String apiURL = "https://kapi.kakao.com/v2/user/me";
		
		Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = TestUtil.get(apiURL,requestHeaders);
        System.out.println("유저 정보 : "+responseBody);
        
		return "redirect:/";
	}
	
}
