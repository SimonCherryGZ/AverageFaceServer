
AverageFaceServer
===================================  
对应客户端[AverageFaceClient](https://github.com/SimonCherryGZ/AverageFaceClient)

此服务器实现如下功能：
  1. 向Android客户端发送人脸目录目录信息。
  2. 接收客户端上传的人脸图片。
  3. 调用Matlab编译的jar包实现平均脸合成功能(该jar包使用face++技术)。
  4. 通过集成极光推送(JPush)向客户端推送合成进度。


Matlab编译的jar包 
-----------------------------------  
averageface.jar由该M文件编译：

	function [ path ] = getAverageFace( indir, outdir, imgname )
		im = AverageFace(indir);
		file = strcat(imgname, '.png');
		temp = strcat('\', file);
		path = strcat(outdir, temp);
		imwrite(im, path, 'png');

JAVA调用averageface.jar：

	Object[] getpath;
	...
	averageFaceUtil util = new averageFaceUtil();
	getpath = util.getAverageFace(1, indir, outdir, filename);
	System.out.println(getpath[0].toString()); // 获取合成的平均脸的路径


第三方技术支持
-----------------------------------  
  1. [JPush极光推送](https://www.jpush.cn/)
  2. [Face++](http://www.faceplusplus.com.cn/)

![image](http://www.faceplusplus.com.cn/static/resources/facepp_inside.png)


> Written with [StackEdit](https://stackedit.io/).
