package org.apache.mina.gate.codec;

import org.apache.mina.stream.ProtocolStreamReader;
import org.apache.mina.utils.Action;

public class GateBaseRequest
{

    public Action<GateBaseRequest> OnReceiveComplete;

    public int tag = 0;

    public int GetSrcServerId()
    {
        return Integer.MAX_VALUE;
    }

    public  int GetMessageId()
    {
        return Integer.MIN_VALUE;
    }

    public  int GetTag()
    {
        return tag;
    }

    public  void BaseSerialize(ProtocolStreamReader reader)
    {
        tag = reader.ReadInt32();
    }
    public  void DeSerialize(ProtocolStreamReader reader)
    {

    }
    public void OnReceived()
    {
        if (OnReceiveComplete != null)
        {
            OnReceiveComplete.Invoke(this);
        }
    }

}
