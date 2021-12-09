package egov.bo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class KakaoBO {
	
	private String client_id;
	private String callbackURL;
	private String apiURL;
	
	public KakaoBO() throws UnsupportedEncodingException {
		this.client_id = "Your Clinet iD"; //JavaScript Key ( App key )
		this.callbackURL = URLEncoder.encode("http://localhost:8090/kakaoCallback.do","UTF-8");
		this.apiURL = "https://kauth.kakao.com/oauth/authorize?"+
						"client_id=" + client_id +
						"&redirect_uri=" +callbackURL +
						"&response_type=code";
				
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

/*	https://kauth.kakao.com/oauth/authorize
	    ?client_id=730975601d99f3b911f8fb8fff4edafa
	    &redirect_uri=http://localhost:8080/login
	    &response_type=code""; */
}
