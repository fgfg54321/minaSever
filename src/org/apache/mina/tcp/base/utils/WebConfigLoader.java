package org.apache.mina.tcp.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class WebConfigLoader
{
	public static void main(String[] args) throws IOException 
    {
		//LoadFormRemote(String url,Action<Response> onCompleted)
		
		String result = sendGet("http://redlist2.warfarestrike.com/info/HttpInfo","logic=HttpAnnouncement&appId=1001&channelId=8888888&serviceId=12292&uid=2999612&language=ara");
		System.out.println(result);
    }
	

	public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	
	/*
	public void LoadFromLocal(String file)
	{
		
	}
	
	public void LoadFormRemote(String url,Action<Response> onCompleted)
	{
		AsyncHttpClient asyncHttpClient    = new DefaultAsyncHttpClient();
		BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
		requestBuilder.execute(new AsyncCompletionHandler<Response>(){
		    
		    @Override
		    public Response onCompleted(Response response) throws Exception
		    {
		    	String text = response.getResponseBody(Charset.forName("UTF-8"));
		        return response;
		    }
		    
		    @Override
		    public void onThrowable(Throwable t)
		    {
		    	MLog.Error("LoadFormRemote Error %s", t.getMessage());;
		        t.printStackTrace();
		    }
		});
	}
	*/
}
