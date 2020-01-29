import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
public class search extends Application {
	 private String nameString="";
	 private String typeString="全部类型";
	 private String countryString="全部国家";
	 private String tString="类型:(.*)";
	 private String cString="制片国家/地区:(.*)";
	 private flow_movie fMovie=new flow_movie();
	 private VBox vBox=new VBox();
	 private ScrollPane scrollPane=new ScrollPane(fMovie);
	 private BorderPane pane = new BorderPane();
	 private flow fl=new flow();
	public void start(Stage primaryStage)
	{
	    // Create a pane and set its properties 	  
	    pane.setLeft(new ScrollPane(new vb()));  //榜单面板
	    pane.setTop(new hb());    //标题面板
	    VBox vBox=new VBox();     
	    vBox.getChildren().addAll(fl,scrollPane);  //
	    pane.setCenter(vBox);  //电影标签面板
	    Scene scene = new Scene(pane,1000,900);
	    primaryStage.setTitle("Mr XIA's Search"); // Set the stage title
	    primaryStage.setScene(scene);  // Place the scene in the stage
	    primaryStage.show();
	} 
	 class hb extends HBox
	 {
		  hb() {	
			   setSpacing(15); 
			   TextField text1=new TextField(); 
			   text1.setOnAction(e->{
			   text1.setText(text1.getText());			   
			    });
			    Button button=new Button("搜索");
			    Text t1=new Text("豆瓣Top250电影");
			    t1.setFont(new Font(30)); 
			    button.setOnAction(e->show(text1.getText()));	  	   			   
			    getChildren().addAll(t1,text1,button);
			    setAlignment(Pos.TOP_CENTER);
		}
	 }
     class vb extends VBox
     {
     	  vb() {
     		 Text t2=new Text("榜单");
     		 t2.setFont(new Font(20));    		
     		 setSpacing(10);    
             setAlignment(Pos.TOP_CENTER);
             ArrayList<String> arrayList=new ArrayList<String>();
             try {
            	int i=1;
 				InputStream na = new FileInputStream("d:/movie/name.txt");
 				BufferedReader re = new BufferedReader(new InputStreamReader(na));
 				String line = null;
 				while ((line = re.readLine()) != null)
 				{    
 					 line="Top"+i+" "+line;
 				     arrayList.add(line);
 				     i++;
 				}
 				na.close();
 				re.close();	
 			} 
 			  catch (Exception e) {
 					e.printStackTrace();
 				  }
            ListView<String> lv=new ListView<>(FXCollections.observableArrayList(arrayList));
            lv.setPrefHeight(820);
            lv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            getChildren().addAll(t2,lv);
     	}
     }
     class flow_movie extends GridPane
     {   
    	private Label[] labels=new Label[250];
     	public flow_movie() { 
     	        setPadding(new Insets(12));
     	        setHgap(10);    	        
   				ArrayList<String> arrayList=new ArrayList<String>();
   				arrayList=Limit(arrayList);
                int i=0;	   
   				for(String a: arrayList)
   				{   
   					ImageView imageView=new ImageView(a+".jpg");
   					imageView.setFitHeight(185);
   				    imageView.setFitWidth(150);   				 
   					labels[i]=new Label(a,imageView);
   					labels[i].setContentDisplay(ContentDisplay.TOP);
   					final int j=i;
   				    labels[i].setOnMouseClicked(e->{                    
                         show(labels[j].getText());
   				    });   					
   					add(labels[i],i%4,i/4);
   					i++;
   				}
   			 
 		}    	
     	ArrayList<String> Limit(ArrayList<String> aList)
     	{        
     		ArrayList<String> aList2=new ArrayList<String>();      
     		 try {   			
    				InputStream na = new FileInputStream("d:/movie/name.txt");
    				BufferedReader re = new BufferedReader(new InputStreamReader(na));
    				String line = null;
    				while ((line = re.readLine()) != null)    					
    				{    
    					int i=0;
    					if(typeString.equals("全部类型")==true)
    						i++;
    					if(countryString.equals("全部国家")==true)
							  i++;						 
    					try {  					
    					    FileInputStream is = new FileInputStream("d:/movie2/"+line+".txt");	
    						BufferedReader read = new BufferedReader(new InputStreamReader(is,"utf-8"));  				
    						String line2 = null;    						
    						while ((line2 = read.readLine()) != null)
    						{  
    							if(i==2)
    							{
    							  aList2.add(line);
    							  break;
    							}	   					
    							if(mat(tString,line2,typeString))
    							  i++;
    							if(mat(cString,line2,countryString))
    							  i++;   						
    						}			
    						read.close();	
    						is.close();   					   
    					} 
    					  catch (Exception e) {
    						e.printStackTrace();  						
    					}  
    				}
    				na.close();
    				re.close();	
    			} 
    			  catch (Exception e) {
    					e.printStackTrace();
    				  }
     		 return  aList2;
     	}
        boolean mat(String a,String b,String c)
        {   
     	   Matcher matcher = Pattern.compile(a).matcher(b);
  		   while (matcher.find())
  		    {         
  			    if(matcher.group(1).contains(c))
  			    {
  			    	return true;
  			    }	
  			}
  		   return false;
        }
     }    
    class  flow extends FlowPane
    {    
    	 flow(){
        setHgap(20);
        setVgap(20);
        setPadding(new Insets(10));
        HBox hBox=new HBox();
        hBox.setSpacing(10);
	    Button button1=new Button("全部类型");
	    button1.setOnAction(e->{
	       typeString="全部类型";   
	       Repaint();
	    });
	    Button button11=new Button("喜剧");
	    button11.setOnAction(e->{
	       typeString="喜剧"; 
	       
	       Repaint();
	    });
	    Button button12=new Button("爱情");
	    button12.setOnAction(e->{
		       typeString="爱情";
		       button12.setStyle("-fx-border-color: yellow");
		       Repaint();
		    });
	    Button button13=new Button("动作");
	    button13.setOnAction(e->{
		       typeString="动作";   
		       Repaint();
		    });
	    Button button14=new Button("悬疑");
	    button14.setOnAction(e->{
		       typeString="悬疑";   
		       Repaint();
		    });
	    Button button15=new Button("科幻");
	    button15.setOnAction(e->{
		       typeString="科幻";   
		       Repaint();
		    });
	    Button button16=new Button("动画");
	    button16.setOnAction(e->{
		       typeString="动画";   
		       Repaint();
		    });
	    Button button17=new Button("犯罪");
	    button17.setOnAction(e->{
		       typeString="犯罪";   
		       Repaint();
		    });
	    Button button18=new Button("恐怖");
	    button18.setOnAction(e->{
		       typeString="恐怖";   
		       Repaint();
		    });
	    Button button19=new Button("战争");
	    button19.setOnAction(e->{
		       typeString="战争";   
		       Repaint();
		    });
	    hBox.getChildren().addAll(button1,button11,button12,button13,button14,button15,button16,button17,button18,button19);
	    HBox hBox2=new HBox();
	    hBox2.setSpacing(10);
	    Button button2=new Button("全部国家");
	    button2.setOnAction(e->{
	    	countryString="全部国家";
	    	Repaint();
    	 });
	    Button button21=new Button("中国");
	    button21.setOnAction(e->{
	    	countryString="中国";
	    	Repaint();
    	 });
	    Button button22=new Button("美国");
	    button22.setOnAction(e->{
	    	countryString="美国";
	    	Repaint();
    	 });
	    Button button23=new Button("法国");
	    button23.setOnAction(e->{
	    	countryString="法国";
	    	Repaint();
    	 });
	    Button button24=new Button("日本");
	    button24.setOnAction(e->{
	    	countryString="日本";
	    	Repaint();
    	 });
	    Button button25=new Button("印度");
	    button25.setOnAction(e->{
	    	countryString="印度";
	    	Repaint();
    	 });
	    Button button26=new Button("德国");
	    button26.setOnAction(e->{
	    	countryString="德国";
	    	Repaint();
    	 });
	    Button button27=new Button("泰国");
	    button27.setOnAction(e->{
	    	countryString="泰国";
	    	Repaint();
    	 });
	    Button button28=new Button("加拿大");
	    button28.setOnAction(e->{
	    	countryString="加拿大";
	    	Repaint();
    	 });
	    hBox2.getChildren().addAll(button2,button21,button22,button23,button24,button25,button26,button27,button28);
	    getChildren().addAll(hBox,hBox2);
		} 
    	 void Repaint()
    	  {
    		vBox.getChildren().clear();   
 		    vBox.getChildren().addAll(new flow(),new ScrollPane(new flow_movie()));
 		    pane.setCenter(vBox);
    	  }
    }
   
	void show(String a)
	{      
		   
		   boolean name=false;		 
		   try {
				InputStream na = new FileInputStream("d:/movie/name.txt");
				BufferedReader re = new BufferedReader(new InputStreamReader(na));
				String line = null;
				nameString=a;
				while ((line = re.readLine()) != null)
				{    
				  if(line.contains(nameString))
					{
					 name=true;
					 nameString=line;
					 break;
					}				 
				}
				na.close();
				re.close();	
			} 
			  catch (Exception e) {
					e.printStackTrace();
				  }		
		  if(name==false)
		  {	 
			 Scene scene = new Scene(new Button("抱歉\n查无此电影"),200,150);	
			 Stage stage=new Stage();
		     stage.setTitle("show");
		     stage.setScene(scene);
		     stage.show();
		  }
		  else 
		  {
		  FlowPane pane1 = new FlowPane();
		  pane1.setHgap(5);
		  pane1.setVgap(10);
		  ImageView imageView = new ImageView(nameString+".jpg"); 
		  imageView.setFitHeight(500);
		  imageView.setFitWidth(350);
		  imageView.setX(0);
		  imageView.setY(0);
		  StringBuffer buffer1=new StringBuffer();
		  StringBuffer buffer2=new StringBuffer();
		  boolean remark=false;
		   try {
				InputStream is = new FileInputStream("d:/movie2/"+nameString+".txt");
				BufferedReader read = new BufferedReader(new InputStreamReader(is,"utf-8"));
				String line = null;
				while ((line = read.readLine()) != null)
				{   
					if(line.equals("热门短评: ")==true)
					 remark=true;
					if(remark==false)
					{
					buffer1.append(line);
					buffer1.append("\n");
					}
					else {
						buffer2.append(line);
						buffer2.append("\n");
					}
				}
				is.close();
				read.close();	
			} 
			  catch (Exception e) {
				e.printStackTrace();
			  }
	     TextArea textArea=new TextArea(buffer1.toString());
	     TextArea textArea2=new TextArea(buffer2.toString());
	     textArea.setWrapText(true);
	     textArea.setEditable(false);
	     textArea.setPrefColumnCount(38);
	     textArea.setPrefRowCount(25);
	     textArea2.setWrapText(true);
	     textArea2.setEditable(false);
	     textArea2.setPrefColumnCount(60);
	     textArea2.setPrefRowCount(20);
	     ScrollPane scrollPane=new ScrollPane(textArea);
	     ScrollPane scrollPane2=new ScrollPane(textArea2);
	     pane1.getChildren().addAll(imageView,scrollPane,scrollPane2);
	     Scene scene1 = new Scene(pane1, 1000, 950);	
	     Stage stage=new Stage();
	     stage.setTitle("show");
	     stage.setScene(scene1);
	     stage.show();
	     nameString=null;
		}
}
}
