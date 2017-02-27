package org.apache.mina.client.codec;

import org.apache.mina.stream.ProtocolStreamWriter;
import org.apache.mina.utils.Action;
import org.apache.mina.utils.IDGensUtils;

public  class ClientBaseResponse
{
    public ProtocolStreamWriter writer = new ProtocolStreamWriter();

    public Action<ClientBaseResponse>  onSendEvent;

    public IDGensUtils idgen = new IDGensUtils(2);
    
    public long GetUniqueId()
    {
        long   unqiue   = idgen.NextId();
        return unqiue;
    }

    public int GetSrcServerId()
    {
        return Integer.MIN_VALUE;
    }

    public int GetMessageId()
    {
        return Integer.MAX_VALUE;
    }

    public int GetTag()
    {
        return Integer.MIN_VALUE;
    }
    public void BaseSerialize()
    {
        writer.WriteInt32(GetTag());
    }

    public void Serialize()
    {

    }

    public void OnSent()
    {
        if(onSendEvent != null)
        {
            onSendEvent.Invoke(this);
        }
    }

}
