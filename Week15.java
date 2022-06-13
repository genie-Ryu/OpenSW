package week15_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class Week15 {

	public static void main(String[] args) throws IOException, ParseException {
		// 1. 쿼리 입력 받기
		Scanner scan = new Scanner(System.in);
		String query = null;
		System.out.print("검색어를 입력하세요 : ");
		query = scan.next();
		scan.close();
		
		// 2. API 호출에 필요한 정보 입력
		String clientId = "EbPHB0zxqi9E9dpBsj3E";
		String clientSeret = "0kNs_SyQaa";
		
		String text = URLEncoder.encode(query, "UTF-8");
		String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + text;
		URL url = new URL(apiURL);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("X-Naver-Client-Id", clientId);
		con.setRequestProperty("X-Naver-Client-Secret", clientSeret);
		
		// 3. API 응답 수신
		int responseCode = con.getResponseCode();
		BufferedReader br;
		if(responseCode == 200) {
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else {
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		//System.out.println(response);
		br.close();
		
		// 4. JSON 파싱
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(response.toString());
		JSONArray infoArray = (JSONArray) jsonObject.get("items");
		
		for (int i = 0; i < infoArray.size(); i++) {
			System.out.println("=item_" + i + "===========================================");
			JSONObject itemObject = (JSONObject) infoArray.get(i);
			System.out.println("title :\t" + itemObject.get("title"));
			System.out.println("link :\t" + itemObject.get("link"));
			System.out.println("image :\t" + itemObject.get("image"));
			System.out.println("subtitle :\t" + itemObject.get("subtitle"));
			System.out.println("pubDate :\t" + itemObject.get("pubDate"));
			System.out.println("director :\t" + itemObject.get("director"));
			System.out.println("actor :\t" + itemObject.get("actor"));
			System.out.println("userRating :\t" + itemObject.get("userRating") + "\n");
		}
	}

}
