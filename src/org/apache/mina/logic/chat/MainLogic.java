package org.apache.mina.logic;

import java.util.ArrayList;

import org.apache.mina.tcp.base.LoginBaseRequest;

public class MainLogic implements Runnable
{
	public ArrayList<LoginBaseRequest> onActionList = new ArrayList<LoginBaseRequest>();
    
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		for(int i = 0; i < onActionList.size(); i++)
		{
			LoginBaseRequest action = onActionList.get(i);
			if(action != null)
			{
				action.OnRequest();
			}
		}

	}

}
