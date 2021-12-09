package egov.bo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class KakaoBO {
	
	private String client_id;
	private String client_pw;
	private String callbackURL;
	private String apiURL;
	
	public KakaoBO() throws UnsupportedEncodingException {
		this.client_id = "8fe6cfe27f2054eace2b183b22379311"; //JavaScript Key ( App key )
		this.client_pw = "5SV_f1VAKg";
		this.callbackURL = URLEncoder.encode("http://localhost:8090/kakaoCallback.do","UTF-8");
		this.apiURL = "https://kauth.kakao.com/oauth/authorize?"+
						"client_id=" + client_id +
						"&redirect_uri=" +callbackURL +
						"&response_type=code";
				//+"&state=";
	}
	
	public String getClient_id() {
		
		return client_id;
	}
	public String getCallbackURL() {
		return callbackURL;
	}
	public String getApiURL() {
		return apiURL;
	}
	public String getClient_pw() {
		return client_pw;
	}

/*	https://kauth.kakao.com/oauth/authorize
	    ?client_id=730975601d99f3b911f8fb8fff4edafa
	    &redirect_uri=http://localhost:8080/login
	    &response_type=code""; */
}