package com.simoncherry.averageface;

public class DirectoryBean{
	int imgID;
	String fileName;
	int fileCount;
	Long fileDate;
	
	public void setImgID(int imgID){
        this.imgID = imgID;
    }

    public int getImgID(){
        return this.imgID;
    }
	
	public void setFileName(String dirName){
		this.fileName = dirName;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public void setFileCount(int fileNum){
		this.fileCount = fileNum;
	}
	
	public int getFileCount(){
		return this.fileCount;
	}
	
	public void setFileDate(Long date){
		this.fileDate = date;
	}
	
	public Long getFileDate(){
		return this.fileDate;
	}
}