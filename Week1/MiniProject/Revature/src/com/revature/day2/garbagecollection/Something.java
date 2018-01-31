package com.revature.day2.garbagecollection;

public class Something {

	int id;
	
	public Something(int i){
		id = i;
		System.out.println(id + ": Created!");
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("\t\t" + id + ": Collected");
	}
}
