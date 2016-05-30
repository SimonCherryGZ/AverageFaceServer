package com.simoncherry.averageface;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class DirectoryServlet
 */
public class DirectoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DirectoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String req = new String(request.getParameter("request").getBytes(
				"iso-8859-1"), "UTF-8");
		String dat = new String(request.getParameter("data").getBytes(
				"iso-8859-1"), "UTF-8");
		String type = new String(request.getParameter("type").getBytes(
				"iso-8859-1"), "UTF-8");
		System.out.println("req= " + req);
		System.out.println("dat= " + dat);
		System.out.println("type= " + type);
		PrintWriter out = response.getWriter();
		System.out.println("======start======");
		
		if(req.equals("getdir")){
		
			//File dir = new File("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/faceset/");
			File dir = new File("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/" + type + "/");
			String dirList[];
			dirList = dir.list();
			DirectoryBean[] dirBeanList = new DirectoryBean[dirList.length];
			
			for(int i=0; i<dirList.length; i++){
				String subDirName = dirList[i];
				//String subDirPath = "D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/faceset/" + dirList[i];
				String subDirPath = "D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/" + type + "/" + dirList[i];
				File subDir = new File(subDirPath);
				Long subDirDate = subDir.lastModified();
				String subDirTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(subDirDate));
				int subDirFileNum = subDir.list().length;
				
				dirBeanList[i] = new DirectoryBean();
				dirBeanList[i].setImgID(0);
				dirBeanList[i].setFileName(subDirName);
				dirBeanList[i].setFileDate(subDirDate);
				dirBeanList[i].setFileCount(subDirFileNum);
			}
			Gson gson = new Gson();
			String strGson = gson.toJson(dirBeanList);
			System.out.println(strGson);
			out.write(strGson);
			
			System.out.println("======end======");
			out.flush();
			out.close();
			
		}else if(req.equals("imglist")){
			
			//String temp = "D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/faceset/" + dat;
			String temp = "D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/" + type + "/" + dat;
			File dir = new File(temp);
			String pathList[];
			pathList = dir.list();
			ImagePathBean[] pathBeanList = new ImagePathBean[pathList.length];
			
			for(int i=0; i<pathList.length; i++){
				String imgPath = pathList[i];
				pathBeanList[i] = new ImagePathBean();
				pathBeanList[i].setImgPath(imgPath);
			}
			
			Gson gson = new Gson();
			String strGson = gson.toJson(pathBeanList);
			System.out.println(strGson);
			out.write(strGson);
			
			System.out.println("======end======");
			out.flush();
			out.close();
			
		}else if(req.equals("newdir")){
			
			//File dir = new File("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/faceset/" + dat);
			File dir = new File("D:/Simon/Works/Web/webapps/webapps/AverageFaceServer/" + type + "/" + dat);
			if(!dir.exists()){
				Boolean isOK = dir.mkdir();
				if(isOK){
					System.out.println("======create success======");
					out.write("success");
				}else{
					System.out.println("======create failed======");
					out.write("failed");
				}
			}else{
				System.out.println("======already exist======");
				out.write("exist");
			}
			
			out.flush();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
