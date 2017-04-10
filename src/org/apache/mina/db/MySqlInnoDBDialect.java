package org.apache.mina.db;

import org.hibernate.dialect.MySQL5Dialect;

public class MySqlInnoDBDialect extends MySQL5Dialect 
{
	public boolean supportsCascadeDelete()
	{
	        return true;
	}
	
	public String getTableTypeString() 
	{
		return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
	
	public boolean hasSelfReferentialForeignKeyBug() 
	{
	    return true;
	}
}
