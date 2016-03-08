

import java.io.IOException;
import java.util.ArrayList;

/*import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
*/
/**
 * Servlet implementation class ControlData
 */
@WebServlet("ControlData.do")

public class ControlData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlData() {
        super();
        //System.out.println("Here");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Here");
		
		String searchButton = request.getParameter("search");
		ArrayList<String> recommendedItems = null;
		if(searchButton != null &&  searchButton.equals("Search"))
		{
			//search for the user entered query!
			SearchStackPosts ssp = new SearchStackPosts();
			String userEnteredQuery = request.getParameter("text1");
			SearchStackPosts.posts.add(userEnteredQuery);
			recommendedItems = ssp.querySearch(0);
			request.setAttribute("recommendedPost2_0", "User Query:" + recommendedItems.get(0));
			for(int i=1;i<recommendedItems.size();i++)
			{
				request.setAttribute("recommendedPost2_"+i, recommendedItems.get(i));
			}
			
			if(recommendedItems.size() < 11)
			{
				for(int i=recommendedItems.size();i<11;i++)
				{
					request.setAttribute("recommendedPost2_"+i, "-");
				}
			}
			
		}
		
		
		String selectedPost = request.getParameter("post");
		if(selectedPost != null)
		{
			SearchStackPosts ssp = new SearchStackPosts();
			recommendedItems = ssp.querySearch(Integer.parseInt(selectedPost.split(" ")[1]));
			request.setAttribute("recommendedPost0", "Selected Post: " + recommendedItems.get(0));
			for(int i=1;i<recommendedItems.size();i++)
			{
				request.setAttribute("recommendedPost"+i, recommendedItems.get(i));
			}
		}	
		SearchStackPosts.posts = new ArrayList<String>();
		getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
