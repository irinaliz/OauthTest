package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestUtil {

	/* Securety Random Generator 
	 * 보안문제를 위한 state 생성기
	 */
	public static String getRandomState() {
		return new BigInteger(130, new SecureRandom()).toString();
	}
	
	/* Token Parser -> Get access_token */
	public static Map<String,String> parseToken(String json) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = mapper.readValue(json, new TypeReference<HashMap<String,String>>() {});
		return map;
	}
	
	/*response Parser -> Get User Profile (네이버용)*/
	public static Map<String,String> parseProfile(String json) throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> map = new HashMap<>();
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		/* 정상응답시 response에 데이터들이 모여있으므로 response 부분만 떼와서 for문 */
		for(Map.Entry<String, JsonElement> responseMap : jsonObject.get("response").getAsJsonObject().entrySet()) {
			System.out.println(responseMap.getKey() +" : " + responseMap.getValue());
			map.put(responseMap.getKey(), responseMap.getValue().getAsString());
		}

		return map;
	}
	
	
	/*맨 아래 2개메소드는 get()메소드에서 쓰이며 Json형태의 문자열을 리턴시켜줌. */
    public static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

}
