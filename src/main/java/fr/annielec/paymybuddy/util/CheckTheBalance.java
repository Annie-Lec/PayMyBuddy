package fr.annielec.paymybuddy.util;

import org.springframework.stereotype.Component;

@Component
public class CheckTheBalance {

	public CheckTheBalance() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Before a transfer, we check if the amount of the balance is enough to pay 
	 * the buddy transfer or bank transfer WITH the fees added !!
	 * @param balance
	 * @param amountToTransfer
	 * @param fees
	 * @return
	 */
	public boolean ItsGoodEnough(double balance, double amountToTransfer, double fees) {
		double totalAmount = amountToTransfer + fees;
		double rest = balance - totalAmount;
		if(rest < 0) {
			return false;
		}else {
			return true;
		}
	}

}
