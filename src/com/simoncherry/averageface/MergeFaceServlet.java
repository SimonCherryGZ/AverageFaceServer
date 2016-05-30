package com.simoncherry.averageface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mathworks.toolbox.javabuilder.MWException;
import averageface.averageFaceUtil;

/**
 * Servlet implementation class MergeFaceServlet
 */
public class MergeFaceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String appKey ="6d2bbce30520014b443e5f74";  
    private static final String masterSecret = "1a0d41542b569500a1845efd";
    PrintStream out = System.out; //保存原输出流
    static int subDirFileNum = 0;
	int logIndex = 0;
	boolean isProcessing = false;
	String resultFilePath = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MergeFaceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
		// Put your code here
	}
	
	public void destroy() {
		super.destroy();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("---------merge start-------------");
		String path = new String(request.getParameter("path").getBytes(
				"iso-8859-1"), "UTF-8");
		System.out.println("---------" + path +"-------------");
		
		if(!isProcessing){
			isProcessing = true;
		
			//String indir = "D:/Simon/Works/Web/Project/AverageFaceServer/WebContent/faceset/" + path;
			//String outdir = "D:/Simon/Works/Web/Project/AverageFaceServer/WebContent/output";
			String indir = "D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/faceset/" + path;
			String outdir = "D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/output/" + path;
			
			File subDir = new File(indir);
			subDirFileNum = subDir.list().length;
			System.out.printf("文件数量： %d\n", subDirFileNum);
			
			File dir = new File(outdir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			//String filename = "output";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmssSSS");
			String filename = sdf.format(new Date());
			Object[] getpath;
			
			System.out.println("---------" + indir +"-------------");
			JPushUtil.SendJPushMsg(appKey, masterSecret, "开始合成");
			
			try {
				PrintStream ps=new PrintStream("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/log.txt"); //创建文件输出流
				System.setOut(ps); //设置使用新的输出流
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			MyThread thread = new MyThread();
			thread.start();
			
			try {
				averageFaceUtil util = new averageFaceUtil();
				getpath = util.getAverageFace(1, indir, outdir, filename);
				System.out.println(getpath[0].toString());
				resultFilePath = getpath[0].toString().substring(getpath[0].toString().indexOf("output/"));
				
				System.setOut(out); //恢复原有输出流
				System.out.println("程序运行完毕");
				
			} catch (MWException e) {
				e.printStackTrace();
			}
			
			//JPushUtil.testSendPush(appKey, masterSecret);
			//JPushUtil.SendJPushMsg(appKey, masterSecret, "合成已完成");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
	
	
	
	private static int getTextLine(String path){
		int lines = 0;
		
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			while ((br.readLine()) != null) {
				lines++;
			 }
			
			isr.close();
			fis.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return lines;
	}
	
	public static List<String> getFileContent(String path) {
		List<String> strList = new ArrayList<String>();
        try {
	        File file = new File(path);
	        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
	        BufferedReader reader = new BufferedReader(read);
	        String line;
	        while((line = reader.readLine()) != null) {
	        	strList.add(line);
	        }
	        reader.close();
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        return strList;
 }
	
	public static String listFileByRow(String path, Integer row) {
		List<String> strList = getFileContent(path);
		int size = strList.size();
		if(size >= (row + 1)) 
			return strList.get(row);
		else
			return "";
	}
	
	class MyThread extends Thread{
		
		public MyThread(){
			logIndex = 0;
	    }

		@Override
	    public void run() {

			while(logIndex < subDirFileNum){
				if(getTextLine("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/log.txt") > logIndex){
					String temp = listFileByRow("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/log.txt", logIndex);
					//JPushUtil.SendJPushMsg(appKey, masterSecret, temp);
					JPushUtil.SendJPushMsg(appKey, masterSecret, "已扫描第 " + String.valueOf(logIndex+1) + " 张人脸");
					logIndex++;
				}
			}
			JPushUtil.SendJPushMsg(appKey, masterSecret, "合成已完成");
			JPushUtil.SendJPushMsg(appKey, masterSecret, resultFilePath);
			isProcessing = false;
		}
	}

}
