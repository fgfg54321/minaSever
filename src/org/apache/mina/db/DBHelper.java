package org.apache.mina.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.mina.utils.ToolUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;

import com.mysql.jdbc.DatabaseMetaData;

public class DBHelper 
{
	public static final String configPath = "resource/config.properties";
	
	public String DB_CONFIG;
	
	public SessionFactory factory;
	
	public String      errorMsg = "";
	
	protected boolean cacheEnable = false;
	
	protected String dbName;
	
	protected List<String> allTableNames;

	public DBHelper()
	{
		Initialize();
	}
	
	public void Initialize()
	{
		loadConfig();
		
		try 
		{
			File dbfile           = new File(DB_CONFIG);
			Configuration configuration = new Configuration().configure(dbfile);
			
			factory               = configuration.buildSessionFactory();
		
			
		} 
		catch (Throwable ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void loadConfig()
	{
		    	
		try 
		{
			Properties properites = new Properties();
			properites.load(new FileInputStream(configPath));
			
			DB_CONFIG          = properites.getProperty("db_config");
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		    	
		 
	}
	
	
	public void SetCacheEnable(boolean cache)
	{
		cacheEnable = true;
	}
	
	public boolean GetCacheEnable()
	{
		return cacheEnable;
	}
	
	public Integer Insert(Object entity) 
	{
		ClearErrorMsg();
		
		Session session    = factory.openSession();
		Transaction tx     = null;
		Integer id         = null;
		try 
		{
			tx             = session.beginTransaction();
			id             = (Integer) session.save(entity);
			tx.commit();
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		return id;
	}
	
	public  List Query(Class cls,String where) 
	{
		ClearErrorMsg();
		
		List datas       = null;
		String tablename = cls.getSimpleName();
		String querysql  = String.format("from %s",tablename);
		if(where != "")
		{
			querysql     = String.format("from %s %s",tablename,where);
		}
		Session session  = factory.openSession();
		Transaction tx   = null;
		try 
		{
			tx           = session.beginTransaction();
			datas        = session.createQuery(querysql).setCacheable(cacheEnable).list();
			tx.commit();
		}
		catch (Exception e)
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		}
		finally 
		{
			session.close();
		}
		
		return datas;
	}
	
	public Object Query(Class cls,Integer id)
	{
		ClearErrorMsg();
		
		Object obj      = null;
		Session session = factory.openSession();
		Transaction tx  = null;
		try
		{
			tx  = session.beginTransaction();
			obj = session.get(cls, id);
			tx.commit();
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return obj;
	}
	
	public boolean UpdateObject(Object obj) 
	{
		ClearErrorMsg();
		
		Session session = factory.openSession();
		Transaction tx  = null;
		try 
		{
			tx = session.beginTransaction();
			session.update(obj);
			tx.commit();
			return true;
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return false;
	}
	
	
	public boolean DeleteObject(Object obj) 
	{
		ClearErrorMsg();
		
		Session session = factory.openSession();
		Transaction tx  = null;
		try 
		{
			tx = session.beginTransaction();
			session.delete(obj);
			tx.commit();
			return true;
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return false;
	}
	
	
	/*
	 * columns count = 1 return List<string>
	 * columns count >1  return List<Object[]>
	 */
	public List SqlQuery(String sql,String... columns)
	{
		ClearErrorMsg();
		
		List objList = null;
		Session session = factory.openSession();
		Transaction tx  = null;
		try
		{
			tx                = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setCacheable(cacheEnable);
			for(int i = 0; i < columns.length; i++)
			{
				sqlQuery.addScalar(columns[i]);
			}
			objList           = sqlQuery.list();
			tx.commit();
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return objList;
	}
	
	
	public List SqlQuery(String sql,Class cls)
	{
		ClearErrorMsg();
		
		List objList = null;
		Session session = factory.openSession();
		Transaction tx  = null;
		try
		{
			tx                = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setCacheable(cacheEnable);
			sqlQuery.addEntity(cls);
			objList          = sqlQuery.list();
			tx.commit();
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return objList;
	}
	
	public List SqlQuery(String sql,Map<String,Class> tmap)
	{
		
		ClearErrorMsg();
		
		List objList = null;
		Session session = factory.openSession();
		Transaction tx  = null;
		try
		{
			tx                = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setCacheable(cacheEnable);
			
			Iterator<Map.Entry<String,Class>> it=tmap.entrySet().iterator();  
	        while(it.hasNext())
	        {  
	            Map.Entry<String,Class> entry = it.next();  
	            sqlQuery.addEntity(entry.getKey(),entry.getValue());
	        }
			
			objList          = sqlQuery.list();
			tx.commit();
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return objList;
	}
	
	
	public int SqlExecute(String sql)
	{
		
		ClearErrorMsg();
		
		Session session = factory.openSession();
		Transaction tx  = null;
		try
		{
			tx                = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setCacheable(cacheEnable);
			int effect        = sqlQuery.executeUpdate();
			session.flush();
			tx.commit();
			
			return effect;
		} 
		catch (Exception e) 
		{
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
		return 0;
	}
	
	public String GetDBName()
	{
		UpdateDBName();
		return dbName;
	}
	
	protected void UpdateDBName()
	{
		ClearErrorMsg();
		
		Session session = factory.openSession();
		
		try
		{
			session.doWork
			(
			new Work() 
			{
	
				@Override
				public void execute(Connection connection) throws SQLException 
				{
					// TODO Auto-generated method stub
					dbName =  connection.getCatalog();
				}
			});
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
			SetErrorMsg(e);
		} 
		finally 
		{
			session.close();
		}
		
	}
	
	public List<String> GetAllTableNames()
	{
		UpdateAllTableNames();
		return allTableNames;
	}
	
	protected void UpdateAllTableNames()
	{
		
		Session session = factory.openSession();
		
		try
		{
			session.doWork
			(
			new Work() 
			{
	
				@Override
				public void execute(Connection connection) throws SQLException 
				{
					
					// TODO Auto-generated method stub
					DatabaseMetaData metaData = (DatabaseMetaData) connection.getMetaData();
					 
			        try 
			        {  
			        	ResultSet resultSet = metaData.getTables(dbName, null, null, null);  
			            allTableNames =  new ArrayList<String>(); 
				        
			            while(resultSet.next())
			            {  
			                allTableNames.add(resultSet.getString("TABLE_NAME"));  
			            }  
			            
			        } 
			        catch (Exception e) 
			        {  
			        	e.printStackTrace();
			        }   
				}
			});
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			session.close();
		}
		
	}
	
	
	public void  ClearErrorMsg()
	{
		errorMsg = "";	
	}
	
	public void SetErrorMsg(Exception e)
	{
		errorMsg = ToolUtils.GetErrorInfoFromException(e);
	}
	
	public String GetErrorMsg()
	{
		return errorMsg;
	}
	
	
}
