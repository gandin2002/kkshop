package com.bohong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.bohoon.framework.util.DateUtil;

public class Test {
//	public static void main(String[] args) throws IOException {
//		File files = new File("D:"+ File.separator +"upload"+ File.separator +"b2b"+ File.separator +"image"+ File.separator +"upload"+ File.separator +"contract"); //要打包的文件
//		File zipfile = new File("D:"+File.separator+"image.zip"); //要输出的文件地址
//		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipfile)); //zip 输出流
//		zos.setComment("test"); //注释
//		if(files.isDirectory()){
//			File[] filea = files.listFiles();
//			for (File file2 : filea) {
//				InputStream is = new FileInputStream(file2);
//				zos.putNextEntry(new ZipEntry(files.getName() + File.separator + file2.getName())); //每个实例
//				int itme = 0;
//				while ((itme = is.read()) != -1) {
//					 zos.write(itme);
//				}
//				is.close();
//			}
//		}
//		zos.close();
//	}
//	public static void oneFile() throws Exception{
//		 File file = new File("D:"+File.separator+"test.txt");
//		 File zipfile = new File("D:"+File.separator+"test.zip");
//		 InputStream input = new FileInputStream(file);
//		 ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(zipfile));
//		 zipout.putNextEntry(new ZipEntry(file.getName()));
//		 zipout.setComment("aa");
//		 int temp = 0;
//		 while((temp = input.read()) != -1){
//			 zipout.write(temp);  
//		 }
//		 input.close();
//		 zipout.close();
//	}
}
