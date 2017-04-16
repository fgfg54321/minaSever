package org.apache.mina.tcp.base.utils;

import java.nio.charset.Charset;

import org.apache.mina.log.MLog;
import org.apache.mina.utils.Action;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

public class WebConfigLoader
{

	public void LoadFromLocal(String file)
	{
		
	}
	
	public void LoadFormRemote(String url,Action<Response> onCompleted)
	{
		
		AsyncHttpClient asyncHttpClient    = new DefaultAsyncHttpClient();
		BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url);
		requestBuilder.addFormParam(name, value);
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
}
