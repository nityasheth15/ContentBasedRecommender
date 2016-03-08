import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	public static void indexFile(IndexWriter writer, File f) throws IOException {
		System.out.println("Indexing " + f.getName());
		Document doc = new Document();
		doc.add(new TextField("filename", f.getName(), TextField.Store.YES));
		
		//open each file to index the content
		try{
				FileInputStream is = new FileInputStream(f);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		        StringBuffer stringBuffer = new StringBuffer();
		        String line = null;
		        while((line = reader.readLine())!=null){
		          stringBuffer.append(line).append("\n");
		        }
		        reader.close();
				doc.add(new TextField("contents", stringBuffer.toString(), TextField.Store.YES));

		}catch (Exception e) {
            
			System.out.println("something wrong with indexing content of the files");
        }    
		writer.addDocument(doc);
	}	
	
	
	public static void main(String args[]) throws IOException
	{
		try{
		File[] listOfFiles = new File("CrawledFiles/").listFiles();
		
		for(File eachFile : listOfFiles)
		{
			if (!eachFile.exists() && !eachFile.isDirectory()) {
				throw new IOException(
						eachFile + " does not exist or is not a directory");
			}
			Directory indexDir = FSDirectory.open(Paths.get("IndexedFile")); //new RAMDirectory();
			
			// Specify the analyzer for tokenizing text.
			StandardAnalyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(indexDir, config);
			
			indexFile(writer, eachFile);
			writer.close();
		}
		}
		catch(Exception e)
		{
			System.out.println("Please re-run the program");
		}
	}
}
