package egov.bo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NaverBO {
	private String client_id;
	private String client_pw;
	private String callbackURL;
	private String apiURL;
	
	public NaverBO() throws UnsupportedEncodingException {
		this.client_id = "bzztPwkcwAXHAsOSKtqi";
		this.client_pw = "5SV_f1VAKg";
		this.callbackURL = URLEncoder.encode("http://localhost:8090/naverCallback.do","UTF-8");
		this.apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code" +
				"&client_id=" + client_id +
				"&redirect_uri=" +callbackURL +
				"&state=";
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

}
