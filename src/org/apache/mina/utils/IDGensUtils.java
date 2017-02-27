package org.apache.mina.utils;

public class IDGensUtils
{
	 private final long        serverId;
	 private final static long twepoch            = 1361753741828L;
	 private final static long serverIdBits       = 4L;
	 public  final static long maxServerId        = -1L ^ -1L << serverIdBits;
	 private final static long sequenceBits       = 10L;
	 private final static long serverIdShift      = sequenceBits;
	 private final static long timestampLeftShift = sequenceBits + serverIdBits;
	 public  final static long sequenceMask       = -1L ^ -1L << sequenceBits;
	 private long  sequence      = 0L;
	 private long  lastTimestamp = -1L;
	 
	 public IDGensUtils(final long serverId)
	 {
		  if (serverId > IDGensUtils.maxServerId || serverId < 0)
		  {
		    throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",IDGensUtils.maxServerId));
		  }
		  this.serverId = serverId;
	 }
	 
	 public synchronized long NextId() 
	 {
	   long timestamp = timeGen();
	   if (lastTimestamp == timestamp) 
	   {
	     sequence = ( sequence + 1) & IDGensUtils.sequenceMask;
	     if (this.sequence == 0) 
	     {
	       timestamp = this.tilNextMillis(lastTimestamp);
	     }
	   }
	   else 
	   {
		  this.sequence = 0;
	   }
	  if (timestamp < lastTimestamp) 
	  {
	   try
	   {
	     throw new Exception(String.format( "Clock moved backwards.  Refusing to generate id for %d milliseconds",this.lastTimestamp - timestamp));
	   }
	   catch (Exception e)
	   {
	     e.printStackTrace();
	   }
	  }
	  lastTimestamp = timestamp;
	  long nextId = ((timestamp - twepoch) << timestampLeftShift)
			        | (this.serverId << IDGensUtils.serverIdShift) 
			        | (this.sequence);
	  return nextId;
	 }
	 
	 private long tilNextMillis(final long lastTimestamp)
	 {
	   long timestamp = this.timeGen();
	   while (timestamp <= lastTimestamp) 
	   {
	     timestamp = this.timeGen();
	   }
	   return timestamp;
	 }
	 
	 private long timeGen()
	 {
	   return System.currentTimeMillis();
	 }
		 
}
