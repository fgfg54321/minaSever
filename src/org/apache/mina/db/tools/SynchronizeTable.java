package org.apache.mina.db.tools;

import org.apache.mina.db.DBHelper;

public class SynchronizeTable 
{
	private static DBHelper dataBaseManager;
	
	public static void main(String[] args)
	{
		//RecordManager.DB_CONFIG = "resource/hibernate.cfg-tools.xml";
		//dataBaseManager         = new DBHelper();
		//dataBaseManager.Initialize();
		
		//StatisticTask task = new StatisticTask("dddd","2017:7:18 22:09:12","2017:7:19 22:09:12","condition","1","2017:7:19 7:09:12",-1,0,true);
		//dataBaseManager.Insert(task);
		
		/*
		dataBaseManager = new DBHelper();
		dataBaseManager.Initialize();
		

		Employee employee0  = new Employee("Zssara", "Assli", 10060);
		Employee employee1 = new Employee("Dassisy", "Dsas", 50080);
		Employee employee2  = new Employee("Johssn", "Passul", 100500);
		dataBaseManager.Insert(employee0);
		dataBaseManager.Insert(employee1);
		dataBaseManager.Insert(employee2);
		
		employee1.setLastName("lei");
		
		dataBaseManager.UpdateObject(employee1);
		
		dataBaseManager.DeleteObject(employee2);
		
		
		List<Employee> queryobj2 = dataBaseManager.Query(Employee.class, "");
		Iterator<Employee> itr   = queryobj2.iterator();
		while(itr.hasNext())
		{
			Employee e = itr.next();
		}
		List<Employee> queryobj3 = dataBaseManager.Query(Employee.class, "");
		List<Employee> queryobj4 = dataBaseManager.Query(Employee.class, "");
		
	
		
		List a   =  dataBaseManager.SqlQuery("select * from Employee","id","first_name");
		Object[] b = (Object[])a.get(0);
		
		List<String> string = dataBaseManager.GetAllTableNames();
		
		dataBaseManager.SqlQuery(Employee.class, "select * from Employee");
		
		dataBaseManager.SqlExecute("update Employee set salary=21345");
		*/
	}
}
