package openSWproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class searcher {
	public String path;
	public String query;
	// 생성자
	public searcher(String path, String query) {
		this.path = path;
		this.query = query;
	}
	
	public double[] InnerProduct() throws Exception {
		// 쿼리 String 에 있는 키워드와 빈도수 저장 <keyword, TF>
		HashMap<String, Integer> queryMap = new HashMap<>();
		
		// 1) 쿼리 입력값의 형태소 분석(꼬꼬마 분석기)
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		for(int i = 0; i < kl.size(); i++) {
			Keyword kwrd = kl.get(i);
			queryMap.put(kwrd.getString(), kwrd.getCnt());
		}
		
		// 2) index.post 불러와서 문서별 가중치 가져오기
		// (확인용) 파일에서 객체 불러와 읽어오기
		FileInputStream fileStream_input = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream_input);
		// object 읽어오기
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		// 우선 읽어온 object 가 무슨 타입인지 확인하고
		// System.out.println("읽어온 객체의 type → " + object.getClass());
		// 해당 타입에 맞게 형변환 하여 object 담기
		HashMap hashMap = (HashMap) object;
		// 쿼리와 각 문서의 유사도 값 담는 변수, 초기화
		double[] q_id = new double[5];
		for(int i = 0; i < 5; i++) {
			q_id[i] = 0.0;
		}
		
		
		// 3) 쿼리 키워드들의 각 문서와의 유사도 계산
		// 문서 개수 만큼 반복
		for(int i = 0; i < 5; i++) {
			Iterator<String> it1 = queryMap.keySet().iterator();
			while(it1.hasNext()) {
				String key1 = it1.next();
				int value1 = queryMap.get(key1);
				if(hashMap.containsKey(key1)) {
					String[] post = ((String)hashMap.get(key1)).split(" ");
					String temp = "";
					for(int j = 0; j < post.length; j++) {
						if(post[j].isEmpty()) {
							continue;
						} else {
							temp += (post[j] + " ");
						}
					}
					String[] post2 = temp.split(" ");
//					for(int j = 0; j < post2.length; j++) {
//						System.out.print(post2[j] + " ");
//					}
//					System.out.println();
//					System.out.println(post2[i + 1]);
					
					q_id[i] += (value1 * Double.parseDouble(post2[i + 1]));
				}
			}
		}
		return q_id;

	}
	
	// document 의 id 를 입력 받아 document 의 제목을 리턴하는 메소드
	public String returnDocName(String docID) throws Exception {
		Document document = getXML(new File("./collection.xml"));
		NodeList nList = document.getElementsByTagName("title");
		Node nItem = nList.item(Integer.parseInt(docID));
		String title = nItem.getTextContent();
		return title;
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
