package com.training.services;

import java.sql.*;

import com.training.model.Invoice;

public class CustomerService {

	
	private Connection connection;

	public CustomerService(Connection connection) {
		super();
		this.connection = connection;
	}
	
	
	public boolean add(Invoice inv) {
		
		boolean result = false;
		PreparedStatement invStmt;
		PreparedStatement custStmt;
		Savepoint point1=null;
		Savepoint point2=null;
		try  {
		
			
			String addInvSQL= "insert into invoice values(?,?,?)";
			String addCustSQL= "insert into txcustomer values(?,?)";
			
			
			connection.setAutoCommit(false);
			
			invStmt = connection.prepareStatement(addInvSQL);
			
			   invStmt.setInt(1, inv.getInvoiceNumber());
			   invStmt.setString(2, inv.getCustomerName());
			   invStmt.setDouble(3, inv.getAmount());
			
			   
			custStmt = connection.prepareStatement(addCustSQL);
			
			    custStmt.setString(1, inv.getCustomerName());
			    custStmt.setDouble(2, inv.getAmount()*.90);
			   
					    
					   int count2 = custStmt.executeUpdate();

					      point1 = connection.setSavepoint("point1");

					    int count1 = invStmt.executeUpdate();
						
					      point2 = connection.setSavepoint("point2");

			    connection.commit();
			    if(count1==1 && count2 ==1) {
			         result = true;
			    }
		} catch (SQLException e) {
			
			try {
				connection.rollback(point1);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
		
		return result;
		
	}
}
