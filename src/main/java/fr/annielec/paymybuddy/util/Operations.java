package fr.annielec.paymybuddy.util;

import org.springframework.stereotype.Component;

@Component
public class Operations {
	 public  static  final  double feesRate = 0.05;

	public Operations() {
		// TODO Auto-generated constructor stub
	}
	
	public double credit(double balance, double amount) {
		return balance + amount;
	}
	
	public double debit(double balance, double amount) {
		return balance - amount;
	}
	
	public double calculFees(double amount) {
		return Math.round(amount * feesRate*100.0)/100.0;
	}


}
