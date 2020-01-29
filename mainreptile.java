import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class mainreptile  {
	    //电影url
		private static final String MOVIE = "(https://movie.douban.com/subject.*)(\"\\s)";
		private static final String NAME ="(<span class=\"title\">)([^&].*)(</span>)";
	    static  String URL="https://movie.douban.com/top250";
	    //图片 url地址
	    static final String IMGURL_REG = "<img width.*src=\"(.*)\" class=\"\">";
	    //短评url
	    public static  List<String> listmovies=new ArrayList<String>(); //电影url
	    public static  List<String> listname=new ArrayList<String>();  //电影名字
	    public static List<String> image=new ArrayList<String>();      //电影图片url
	    public static  String buff_movie[]=new String[250];
	    public static  String buff_comments[]=new String[250];   
		public static void main(String[] args) throws Exception {
			 starUrl();  //打开电影url来得到url列表
			 note_name();//把电影名字输出到一个文件中,便于管理   
		     getmovies(); //获取每个电影所需要的html并解析
		     download_picture(image,listname); //下载图片
		}
		static void starUrl()throws Exception
		{
			for(int i=0;i<10;i++)
			{
				String s=URL+"?start="+i*25+"&amp;filter=";
				String html=getHTML(s); 	
			    getmovieURL(html);
			    getImage(html); 
			}
		}
		//记录电影名字
		static void note_name() throws Exception
		{    
			 PrintWriter fo = new PrintWriter(new File("d:/movie/"+"name.txt"));
			 for(String jString:listname)
	       {		    	   
	    	    fo.println(jString);
		   }
	       fo.close();   
		}
		// 获取html内
		public static String getHTML(String Url) throws Exception {
			URL url = new URL(Url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			br.close();
			is.close();	
			return buffer.toString();
		}
		//电影的url和名字
		public static void getmovieURL(String html) {
			Matcher matcher = Pattern.compile(MOVIE).matcher(html);   //匹配电影url
			while (matcher.find())
			{
				listmovies.add(matcher.group(1));
			}
		   Matcher matcher2=Pattern.compile(NAME).matcher(html);  //匹配电影名字
		   while (matcher2.find()) {
				listname.add(matcher2.group(2));
			}
		}
		//下载每个电影html信息
		public static void getmovies() throws Exception {
			    int i=0;
		     for(String Url:listmovies)
		     {
		     Thread t = new MyThread(Url,i,listname,image);
		     t.start(); // 启动新线程
		    System.out.println(listname.get(i)+"解析并下载成功");
		    i++;
			}
		}
		//匹配电影图片的url
	   public static void getImage(String html) {
			Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
			while (matcher.find()) {
				image.add(matcher.group(1));
			}
		}
		//下载图片
	   public static void download_picture(List<String> list,List<String> listname)
			{   try {
				    int i=0;
				 for (String url:list)
				 {   
					 URL urlx=new URL(url);
					 String imageName =listname.get(i);
					 InputStream in = urlx.openStream();
		             FileOutputStream fo = new FileOutputStream(new File("d:/pictures/"+imageName+".jpg"));//文件输出流
		             byte[] buf = new byte[1024];
		             int length = 0;
		             while ((length = in.read(buf, 0, buf.length)) != -1) {
		                 fo.write(buf, 0, length);
		             }
		             //关闭流
		             in.close();
		             fo.close();
		             System.out.println(imageName + ":图片下载完成");	             
		             i++;
				 }
			     } 
			     catch (Exception e) {
		         System.out.println("下载失败");
		     }
			}
}

class MyThread extends Thread {
	private String  Url;
	private int i;
	public   List<String> listname;  //电影名字
	public   List<String> image;      //电影图片url
    static final String IMGURL_REG = "<img width.*src=\"(.*)\" class=\"\">"; //图片 url正则表达式
    static  String NAME1 ="我要写短评</span>\n([\\s\\S]*?)</span>";  //短评url正则表达式
    static  String NAME2 ="<a href=\"(.*)\">";
    static  String commentString="<a href=\"(.*)\" data-page=\"\" class=\"next\">后页";
	public MyThread(String url,int j, List<String> listname,List<String> image) {
		Url=url;  
		i=j;
		this.listname=listname;
		this.image=image;		
	}
    public void run()
    {
    	try {
        String mString=getmessage(Url, i);
        mainreptile.buff_movie[i]=mString;
    	try {
            Thread.sleep(2000);
        }
		catch (InterruptedException e)
    	{}                               //每个电影用暂停降低被封风险
    	
		String S=getComments(mString);
		
		try {
            Thread.sleep(2000);
        }
		catch (InterruptedException e) {}
		String cString=download_comments(S,listname.get(i));//解析并下载短评
        analysis_movies cAnalysis_movies=new analysis_movies(mString,cString,listname.get(i));
        }
		catch (Exception e) {
			System.out.println("爬取失败");	
		}    
    }
    String getmessage(String Url,int i) throws Exception
	{
		    URL url = new URL(Url);
			URLConnection conn=url.openConnection();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String line = null;
	        StringBuffer aBuffer=new StringBuffer();
	        while ((line = br.readLine()) != null) {			
				aBuffer.append(line);
				aBuffer.append("\n");
			}	        
			br.close();
			is.close();		
			return aBuffer.toString();
	}
    String getComments (String aBuffer) throws Exception
	 {
		String uString="";
		 Matcher matcher1 = Pattern.compile(NAME1).matcher(aBuffer.toString()); //匹配短评的url
		   while (matcher1.find()) 
		   {
			    Matcher matcher2 = Pattern.compile(NAME2).matcher(matcher1.group(1)); 
			    while(matcher2.find())
			    {
			     uString=matcher2.group(1);    //匹配短评
				}
		   }
		   return uString;	
	 }
	//下载短评
     String download_comments(String uString,String name)throws Exception
	   	{	            	                              
	   		            String ur=uString.substring(0,uString.length()-9);	   		     	   		     
	   		            URL url = new URL(uString);	  
	   		            StringBuffer cBuffer=new StringBuffer(); 		             	       
	   		            for(int i=0;i<3;i++)
	   		            {
	   					URLConnection conn=url.openConnection();
	   					InputStream is = conn.getInputStream();
	   					BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
	   					String line = null;	   				    
	   			        while ((line = br.readLine()) != null) {
	   			         Matcher matcher = Pattern.compile(commentString).matcher(line); 
	   			             while(matcher.find())
	   			             {
	   			            	String s=matcher.group(1);	
	   			            	s=ur+s;
	   			            	url=new URL(s);	 	   			           	
	   			             }	   			     	   					
	   						cBuffer.append(line);
	   						cBuffer.append("\n");
	   					}
	   			        br.close();
	   					is.close();
	   		            }	   		        		   				
	   				    return cBuffer.toString();
	   					 						
	      }	 
	
    
}

	
