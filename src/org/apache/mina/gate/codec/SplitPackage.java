package org.apache.mina.gate.codec;

public class SplitPackage
{
    public int    index;
    public byte[] datas;

    public void Dispose()
    {
        datas = null;
    }
}
