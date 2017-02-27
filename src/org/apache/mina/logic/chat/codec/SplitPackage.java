package org.apache.mina.logic.codec;

public class SplitPackage
{
    public int    index;
    public byte[] datas;

    public void Dispose()
    {
        datas = null;
    }
}
