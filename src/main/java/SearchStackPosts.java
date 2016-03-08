import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tartarus.snowball.ext.PorterStemmer;

public class SearchStackPosts {

	public static List<String> posts = new ArrayList<String>();
	private static Set<String> stopWords = new HashSet<String>();
	
	public void extractData()
	{
		FileInputStream fis = null;
		
		try {
				ClassLoader classLoader = getClass().getClassLoader();
				System.out.println(classLoader.getResource("/data.xlsx").toString());
				fis = new FileInputStream(new File(classLoader.getResource("/data.xlsx").getFile()));
	            //fis = new FileInputStream("data.xlsx");
	            Workbook workbook = new XSSFWorkbook(fis);
	            Sheet sheet = workbook.getSheetAt(0);
        		Iterator<Row> rowIterator = sheet.iterator();
        		//iterating over each row
                while (rowIterator.hasNext()) 
                {
                	Row row = rowIterator.next();
                	Iterator<Cell> cellIterator = row.cellIterator();
                	while (cellIterator.hasNext()) 
                	{
                		Cell cell = cellIterator.next();
            			if (cell.getColumnIndex() == 1) 
        				{
            				String processString = cell.getStringCellValue();
            				processString = processString.replaceAll("[^\\p{L}\\p{Nd}]+", " ");
            				posts.add(processString);
        				}
                	}
                }
			}
			catch(FileNotFoundException e)
			{
				System.out.println("File not found");
			}
			catch(IOException io)
			{
				System.out.println("IO Exception");
			}
	}
	
	
	public static String stemQuery(String query)
	{
		String[] allWords = query.split(" ");
		Set<String> temp = new HashSet<String>();
		PorterStemmer stem = new PorterStemmer();
		StringBuilder result = new StringBuilder();
		for(String eachWord : allWords)
		{
			stem.setCurrent(eachWord);
			stem.stem();
			String current = stem.getCurrent();
			if(!temp.contains(current))
			{
				temp.add(current);
				result.append(stem.getCurrent() + " ");
			}
		}
		return result.toString();
	}
	
	
	private void getStopWords() throws IOException
	{
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		String currentLine = "";
		FileReader fr = new FileReader(classLoader.getResource("/stopwordslist.txt").getFile());
        BufferedReader br= new BufferedReader(fr);
        while ((currentLine = br.readLine()) != null){
        	stopWords.add(currentLine);
        }
        br.close();
        fr.close();
	}
	
	private String removeStopWords(String post)
	{
		if(stopWords.size() == 0)
		{
			try {
				this.getStopWords();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for(String stopWord : stopWords){
		    post = post.replaceAll(" "+ stopWord + " ", " ");
		}
		return post;
	}
	
	public ArrayList<String> querySearch(int postIndex)
	{
		//initial processing
		try {
			getStopWords();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		this.extractData();
		//initial processing over
		
		
		String contents = posts.get(postIndex);
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		//KeywordAnalyzer analyzer = new KeywordAnalyzer();
		//SimpleAnalyzer analyzer = new SimpleAnalyzer();
		
		//String querystr = "contents:"+stemQuery(removeStopWords(contents));
		String querystr = "contents:"+removeStopWords(contents);
		Directory indexDir = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			try {
				indexDir = FSDirectory.open(Paths.get(classLoader.getResource("/IndexedFile").toURI()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("SearchStackPost: " + Thread.currentThread().getStackTrace()[1].getLineNumber()) ;
		} 

		Query q = null;
		QueryParser qp = null;
		try {
			qp = new QueryParser( "contents", analyzer);
			//qp.setDefaultOperator(QueryParser.Operator.AND);
			q = qp.parse(querystr);
		} catch (ParseException e) {
			System.out.println("SearchStackPost: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		
		int hitsPerPage = 10;
		IndexReader reader = null;
		 
		 TopScoreDocCollector collector = null;
		 IndexSearcher searcher = null;
		 try {
			reader = DirectoryReader.open(indexDir);
		} catch (IOException e) {
			System.out.println("SearchStackPost: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		 searcher = new IndexSearcher(reader);
		 collector = TopScoreDocCollector.create(hitsPerPage);
		 try {
			searcher.search(q, collector);
		} catch (IOException e) {
			System.out.println("SearchStackPost: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		 
		 
		 
		 ScoreDoc[] hits = collector.topDocs().scoreDocs;
		 System.out.println("Found " + hits.length + " hits.");
		 System.out.println();
		 
		 System.out.println(contents);
		 ArrayList<String> recommendedPosts = new ArrayList<String>();
		 recommendedPosts.add(contents);
		 for (int i = 0; i < hits.length; ++i) {
			 int docId = hits[i].doc;
			 Document d = null;
			 try {
				d = searcher.doc(docId);
			} catch (IOException e) {
				System.out.println("SearchStackPost: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
			}
			 recommendedPosts.add((i + 1) + ". " + d.get("filename") + "\n" + d.get("contents"));
			 //System.out.println((i + 1) + ". " + d.get("filename") + "\n" +d.get("contents"));
		 }
		 try {
			reader.close();
		} catch (IOException e) {
			System.out.println("SearchStackPost: " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
		 return recommendedPosts;
	}
	
	
	public static void main(String[] args)  {
	}

}
