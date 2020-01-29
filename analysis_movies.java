import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.BufferedWriter;
public class analysis_movies{
	 //HTML
    private String html;
    private String hString;
	//��Ӱ��Ϣ����
    private String MOVIE_NAME="<span property=\"v:itemreviewed\">(.*)(</span>)";
    private String MOVIE_DIRECTOR="director[\\s\\S]*?author";
    private String MOVIE_AUTHOR="author[\\s\\S]*?actor";
    private String MOVIE_ACTOR="actor[\\s\\S]*?datePublished";
    private String MOVIE_TYPE="v:genre\">(.*?)</span>";
    private String MOVIE_COUTRY="��Ƭ����/����:</span>(.*)<br/>";
    private String MOVIE_LANGUAGE="����:</span> (.*)<br/>";
    private String MOVIE_DATE="\"datePublished\": \"(.*)\"";
    private String MOVIE_TIME="\"v:runtime\".*>(.*?)</span>";
    private String MOVIE_NANE2="����:</span> (.*)<br/>";
    private String MOVIE_SCORE="\"ratingValue\": \"(.*)\"";
    private String MOVIE_COUNT="\"ratingCount\": \"(.*)\"";
    private String MOVIE_DESCRIBE="(<span property=\"v:summary\" class=\"\">|<span class=\"all hidden\">)\n([\\s\\S]*?)</span>";
    private String NAME=" \"name\": \"(.*?)\\s";
     //��������
 	 private static String comments="<div class=\"avatar\">\n([\\s\\S]*?)</p>";
 	 private static String name2="<a title=\"(.*?)\" href";
 	 private static String like="title=\"(.*?)\"></span>";
 	 private static String time="<span class=\"comment-time \" title=\"(.*?)\">";
 	 private static String see="<span>(.*?)</span>";
 	 private static String comment="<span class=\"short\">([\\s\\S]*?)</span>";
    public analysis_movies(String html,String hString,String name) throws Exception {
    	  this.html=html;
    	  this.hString=hString;
    	FileOutputStream fw = new FileOutputStream(new File("d:/movie2/"+name+".txt"));
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fw,"UTF-8")); 
    	match_movie(bw);
    	match_comments(bw);
		   bw.close();		  
    }    
     void match_movie(BufferedWriter bw) throws Exception 
     {
    	 bw.write("��Ӱ����:  ");
	     match1(MOVIE_NAME,bw);
	     bw.write("����:  ");
	     match1(MOVIE_NANE2,bw);
	     bw.write("����:  ");
	     match2(MOVIE_DIRECTOR,bw);
	     bw.write("���:  ");
	     match2(MOVIE_AUTHOR, bw);
	     bw.write("����:  ");
	     match2(MOVIE_ACTOR, bw);
	     bw.write("����:  ");
	     match1(MOVIE_TYPE, bw);
	     bw.write("��Ƭ����/����:  ");
	     match1(MOVIE_COUTRY,bw);
	     bw.write("����:  ");
	     match1(MOVIE_LANGUAGE,bw);
	     bw.write("��ӳ����:  ");
	     match1(MOVIE_DATE,bw);
	     bw.write("Ƭ��:  ");
	     match1(MOVIE_TIME, bw);
	     bw.write("����:  ");
	     match1(MOVIE_SCORE,bw);
	     bw.write("��������:  ");
	     match1(MOVIE_COUNT, bw);
	     bw.write("������: \n");
	     match_describe(MOVIE_DESCRIBE,bw);
     }
     void match_comments(BufferedWriter bw) throws Exception
     {
    	 bw.write("\n���Ŷ���: \n");
	     Matcher matcher = Pattern.compile(comments).matcher(hString); 
         while(matcher.find())
         {           	   	
      	   String mString=matcher.group(1);
      	   bw.write("�û���");
      	   match_comments(name2, bw,mString);
      	   match_comments(see, bw,mString);
      	   match_comments(like, bw,mString);
      	   match_comments(time, bw,mString);
      	   bw.write("\n ");
      	   match_comments(comment,bw,mString);         	    
      	   bw.write("\n==========================================\n");       	
         }
     }
     void match1(String m,BufferedWriter bw) throws Exception
       {  
     	   
     	  Matcher matcher = Pattern.compile(m).matcher(html);
 		   while (matcher.find())
 		    {         
 					  bw.write(matcher.group(1));	
 					  bw.write("  ");
 			} 
 		    bw.write("\r\n"); 	
       }
     
       void match2(String m,BufferedWriter bw) throws Exception
      {  
    	   
    	  Matcher matcher = Pattern.compile(m).matcher(html);
		   while (matcher.find())
		    {   
			    Matcher matcher2= Pattern.compile(NAME).matcher(matcher.group());			     
				  while(matcher2.find())
				  {
					  bw.write(matcher2.group(1));	
					  bw.write(" / ");
				  }
			} 
		   bw.write("\r\n"); 		
      } 
       void match_describe(String n,BufferedWriter bw) throws Exception
       {  
     	  Matcher matcher = Pattern.compile(n).matcher(html);
 		   while (matcher.find())
 		    { 
 			  String s=matcher.group(2);
     	      s=s.replaceAll("<br />","xxx");
     	      s=s.replaceAll("\\s+","");
     	      s=s.replaceAll("xxx","\n");
 			  bw.write(s); 
 			} 
 		   	 bw.write("\r\n");		
       }
       void match_comments(String m,BufferedWriter bw,String html) throws Exception
       {    	   
     	  Matcher matcher = Pattern.compile(m).matcher(html);
  		   while (matcher.find())
  		    {   
  					  bw.write(matcher.group(1));	
  					  bw.write("  ");
  			} 	
       }
       
}
      
	
	
	