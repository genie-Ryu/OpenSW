package openSWproject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MidTerm {
	
	public String path;
	public String query;
	
	public MidTerm(String path, String query) {
		this.path = path;
		this.query = query;
	}

	public void showSnippet() throws Exception {
		// 수정할 collection.xml 파일 불러와서 document 객체 생성
		Document document = getXML(new File(path));
		// getBody() 메소드에서 값을 수정하고자 하는 <Body> 태그의 값을 String[] 형태로 받아옴
		String[] bodytextArr = getBody(document);
		// 쿼리 String 에 있는 키워드와 빈도수 저장 <i, keyword>
		HashMap<Integer, String> queryMap = new HashMap<>();
		
		// 1) 쿼리 입력값의 형태소 분석(꼬꼬마 분석기)
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for(int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			queryMap.put(i, kwrd.getString());
		}
		//
		HashMap<Integer, String> bodyMap = new HashMap<>();
		Iterator<Integer> it1 = queryMap.keySet().iterator();
		Iterator<Integer> it2 = bodyMap.keySet().iterator();
		
		// 문서별 반복
		for(int i = 0; i < 5; i++) {
			int f_index = 0;
			while(f_index == bodytextArr.length - 1) {
				for(int j = 0; j < f_index + 30; j++) {
					// 30개 글자씩 떼어옴
					String bodytext30 = null;
					bodytext30 = bodytext30 + bodytextArr[i].split("");
					KeywordExtractor ke2 = new KeywordExtractor();
					KeywordList kl2 = ke.extractKeyword(bodytext30, true);
					for(int k = 0; k < kl.size(); k++) {
						Keyword kwrd = kl.get(k);
						bodyMap.put(k, kwrd.getString());
					}
					
					
					while(it1.hasNext()) {
						int key1 = it1.next();
						String value1 = queryMap.get(key1);
						while(it2.hasNext()) {
							
							
						}
						
					}
					
				}
				
				f_index += 30;
			}
		}
		
	}
	
	
	// document 의 id 를 입력 받아 document 의 제목을 리턴하는 메소드
	public String returnDocName(String docID) throws Exception {
		Document document = getXML(new File("./collection.xml"));
		NodeList nList = document.getElementsByTagName("title");
		Node nItem = nList.item(Integer.parseInt(docID));
		String title = nItem.getTextContent();
		return title;
	}
	
	// xml 문서의 수정할 body 노드 내용 불러오기 메소드
	public static String[] getBody(Document document) throws Exception {
		// body 이름으로 생성한 태그를 list 로 가져오기
		NodeList nList = document.getElementsByTagName("body");
		// 아래 반복문에서 받아올 bodytext 변수 내용을 담는 String[] 변수
		String[] bodytextArr = new String[nList.getLength()];
		
		for(int i = 0; i < nList.getLength(); i++) {
			// 반복문을 통해 <body> 태그에 해당하는 값들을 차례로 가져와 bodytext 변수에 담음
			Node nItem = nList.item(i);
			String bodytext = nItem.getTextContent();
			// System.out.println(bodytext);
			bodytextArr[i] = bodytext;
		}
		return bodytextArr;
	}
	
	// 파일 생성 메소드
	public static Document getXML(File file) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(file);
		// 문서 구조 안정화
		document.getDocumentElement().normalize();
		return document;
	}
}
