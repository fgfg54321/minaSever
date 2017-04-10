package org.apache.mina.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.log.MLog;
import org.jboss.logging.Logger;


public class ToolUtils
{
	public static String Join(String splitter,List<String> strs) 
	{
		return StringUtils.join(strs, splitter);
    }
	
	public static <T> List<T> Find(List<T> list,Func2<Object,Boolean> func)
	{
		List  findList = new ArrayList();
		Iterator itr   = list.iterator();
		while(itr.hasNext())
		{
			Object obj    =  itr.next();
			boolean match =  func.invoke(obj);
			if(match)
			{
				findList.add(obj);
			}
		}
		
		return findList;
	}
	
	public static void DeleteFile(Object obj)
	{
		
		File file = null;  
        if (obj instanceof File)
        {  
        	file = (File) obj;  
        } 
        else 
        {  
        	file = new File(obj.toString());  
        }  
		
        if(file.isFile())
        {
            file.delete();
            return;
        }
        
        if(file.isDirectory())
        {
            File[] childFile = file.listFiles();
            
            if(childFile == null || childFile.length == 0)
            {
                file.delete();
                return;
            }
            
            for(int i = 0; i < childFile.length; i++)
            {
            	File childf  = childFile[i];
            	DeleteFile(childf);
            }
          
            file.delete();
        }
    }
	
	
	 public static List<File> GetFiles(Object obj) 
	 {  
        File directory = null;  
        if (obj instanceof File)
        {  
            directory = (File) obj;  
        } 
        else 
        {  
            directory = new File(obj.toString());  
        }  
        
        ArrayList<File> files = new ArrayList<File>();  
        if (directory.isFile()) 
        {  
            files.add(directory);  
            return files;  
        } 
        else if (directory.isDirectory()) 
        {  
            File[] fileArr = directory.listFiles();  
            for (int i = 0; i < fileArr.length; i++) 
            {  
                File fileOne = fileArr[i];  
                files.addAll(GetFiles(fileOne));  
            }  
        }  
        
        return files;  
	 }
	 
	public static boolean StringIsEmpty(String str)
	{
		if(str.equals("")|| str == null)
		{
			return true;
		}
		
		return false;
	}
	
	
	public static List<String> ReadAllLines(String filepath)
	{
			
		List<String> lines = new ArrayList<String>(); 

		try
		{
			FileInputStream fi      = new FileInputStream(filepath);
	        InputStreamReader isr   = new InputStreamReader(fi, "UTF-8");
	        BufferedReader br       = new BufferedReader(isr);
	        String str              = null;
	        while((str = br.readLine()) != null)
	        {
	        	lines.add(str);
	        }
	        
	        br.close();
	        isr.close();
	        fi.close();
		}
		catch(Exception e)
		{
			MLog.Error(e.getMessage());
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static String readLine()
	{
		Scanner s = new Scanner(System.in);
		return s.nextLine();			
	}
	
	public static String GetErrorInfoFromException(Exception e) 
	{
		try 
		{

			StringWriter sw     = new StringWriter();
			Throwable  throwable = e.getCause();
			if(throwable != null)
			{
				sw.write(throwable.toString() + "\r\n");
			}
			PrintWriter pw       = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			return "\r\n" + sw.toString() +  "\r\n";
		} 
		catch (Exception e2)
		{
			return "bad getErrorInfoFromException";
		}
	}
	
}
