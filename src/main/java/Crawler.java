
import java.io.*;
import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	
	//private Set<String> urlsVisited = new HashSet<String>();
	//private List<String> urlsToVisit = new ArrayList<String>();
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private Set<String> links = new HashSet<String>(); // Just a list of URLs
	
	
	public boolean crawlWebPages(String url) //given url
	{
		this.links.add(url);//adding the first url
		try
		{
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			//System.out.println("Received web page at " + url);
			
			if(connection.response().statusCode() == 200)//if response was good
			{
				System.out.println("Response was good at: " + url);
			}
			
			if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
			
			Elements linksOnPage = htmlDocument.getElementById("mw-content-text").select("a[href]");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
			
			this.saveContentIntoFile();
			return true;
		}
		catch (IOException ioe)
		{
			System.out.println("Exception");
			return false;
		}
	}
	
	/* 
	 * 1 - This method iterates through each crawled URL
	 * 2 - For each URL page it saves contents into a text file
	 * */
	public void saveContentIntoFile()
	{
		//int counter = 1;
		
		for(String eachLink : links)
		{
			String fileName = "";
			String titleText = "";
			FileWriter fw = null;
			BufferedWriter bw = null;
			
			try
			{
				Connection connection = Jsoup.connect(eachLink).userAgent(USER_AGENT);
				Document htmlDocument = connection.get();
				if(connection.response().statusCode() == 200)//if response was good
				{
					//System.out.println("Response was good at: " + eachLink);
					titleText = htmlDocument.title();
					Element allContentTextDiv = htmlDocument.getElementById("mw-content-text");
					Elements allContentTextElements = null;
					
					if(allContentTextDiv != null && allContentTextDiv.childNodeSize()!=0)
					{
						allContentTextElements	= allContentTextDiv.select("*");
					}
					else
					{
						break;
					}
					
					Elements allP = allContentTextElements.select("p");
					Elements allTables = allContentTextElements.select("table");
					Elements allULs = allContentTextElements.select("ul");
					
					StringBuilder textForFile = null;
					
					String[] tempArray = titleText.split("-");
					if(tempArray.length >1)
					{
						fileName = tempArray[0];
					}
					else
					{
						fileName = titleText;
					}
					//remove \/:*?"<>|
					fileName = fileName.replace("\\", " ").replace(":", " ").replace("*", " ").replace("?", " ").replace("\"", " ");
					fileName = fileName.replace("<", " ").replace(">", " ").replace("|", " ").replace("&", " ");
					fileName = fileName.replace("/"," ").trim();
					
					
					//code to add all p elements
					textForFile = new StringBuilder();
					textForFile.append(titleText);
					int pCounter = 0;
					int pFileCounter = 1;
					if(allP.size() > 1)
					{
						for(Element eachElement : allP)
						{
							if(pCounter < 4)
							{
								pCounter++;
								textForFile.append(eachElement.text() + "\n");
							}
							
							if( pCounter>=4 || pCounter == allP.size()-1 || (pCounter+pFileCounter) == (allP.size()-1))
							{
								File file = new File("CrawledFiles\\"+fileName+"_"+pFileCounter+ ".txt");
								file.getParentFile().mkdir();
								file.createNewFile();
								fw = new FileWriter(file);
								bw = new BufferedWriter(fw);
								bw.write(textForFile.toString());
								bw.close();
								fw.close();
								pFileCounter++;
								pCounter = 0;
								textForFile = new StringBuilder();
							}
						}
						//end allP.
					}
					
					//code to write all table elements 
					textForFile = new StringBuilder();
					textForFile.append(titleText);
					int tableCounter = 0;
					int tableFileCounter = pFileCounter;
					if(allTables.size() >0)
					{
						for(Element eachElement : allTables)
						{
							if(tableCounter<3)
							{
								tableCounter++;
								textForFile.append(eachElement.text());
							}
							
							if(tableCounter >=3 || tableCounter == (allTables.size()-1) || (tableCounter + tableFileCounter) == (allTables.size()-1))
							{
								File file2 = new File("CrawledFiles\\"+fileName+"_"+tableFileCounter+ ".txt");
								file2.getParentFile().mkdir();
								file2.createNewFile();
								fw = new FileWriter(file2);
								bw = new BufferedWriter(fw);
								bw.write(textForFile.toString());
								bw.close();
								fw.close();
								tableCounter = 0;
								tableFileCounter++;
								textForFile = new StringBuilder();
							}
						}
						
						
						//end allTables
					}
					
					//code to write all ULs
					textForFile = new StringBuilder();
					textForFile.append(titleText);
					int ulCounter = 0;
					int ulFileCouter = tableFileCounter;
					if(allULs.size() > 2)
					{
						for(Element eachElement : allULs)
						{
							if(ulCounter<4)
							{
								ulCounter++;
								textForFile.append(eachElement.text() + "\n");
							}
							
							if(ulCounter>=4 || ulCounter == allULs.size()-1 || (ulCounter + ulFileCouter) == (allULs.size()-1))
							{
								File file3 = new File("CrawledFiles\\"+fileName+"_"+ulFileCouter+ ".txt");
								file3.getParentFile().mkdir();
								file3.createNewFile();
								fw = new FileWriter(file3);
								bw = new BufferedWriter(fw);
								bw.write(textForFile.toString());
								bw.close();
								fw.close();
								ulCounter=0;
								ulFileCouter++;
								textForFile = new StringBuilder();
							}
						}
						
					}
				}	

			}
			catch(IOException ioe)
			{
				System.out.println("Exception in Writing: " + fileName);
			}
		}
	}
	

	public static void main(String[] args)
	{
		Crawler c = new Crawler();
		c.crawlWebPages("https://en.wikibooks.org/wiki/Java_Programming");
		
		
	}
	
	
	
	
}
