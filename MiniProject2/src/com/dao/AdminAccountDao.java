package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.adminend.AdminAccount;
import com.adminend.Transactions;

public interface AdminAccountDao {
	
		//public void insertAdminAccount(AdminAccount admin);
	
	
		public AdminAccount selectAdminAccountByUN(String password) throws SQLException;
		public void ApproveUserAccount (String username);
		public List<Transactions> getAllTransactions();



}
