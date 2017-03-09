package org.apache.mina.tcp.base.stream;

public class SplitPackage
{
    public int    index;
    public long   srcServerId;
    public long   dstServerId;
    public int    messageId;
    public byte[] datas;

    public void Dispose()
    {
        datas = null;
    }
}
