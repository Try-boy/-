import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class  read_movies{
     static analysis_movies movie[]=new analysis_movies[250];
	public static void main(String[] args) throws Exception {
		int i=0;
		try {
			InputStream is = new FileInputStream("d:/movie/name.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String str = null;
			while (true) {
				str = reader.readLine();
				if(str!=null)
				{
				String mString=getmessage(str,1);
				String hString=getmessage(str,2);
			    movie[i]=new analysis_movies(mString,hString,str); //正则解析电影信息
			    System.out.println(str+":分析写入成功");	
			    i++;
				}
				else
	        	break;
			}
			is.close();
			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	} 
	static String getmessage(String name,int k)
	{   
		 
		try {
			InputStream is=new FileInputStream("d:/movie/"+name+".txt");
			if(k==1);
			if(k==2)
		    is = new FileInputStream("d:/comments/"+name+".txt");	
			BufferedReader read = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String line = null;
			StringBuffer buffer=new StringBuffer();
			while ((line = read.readLine()) != null)
			{
				buffer.append(line);
				buffer.append("\n");
			}			
			read.close();	
			is.close();
		    return buffer.toString();
		} 
		  catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}  
	}
	
}
