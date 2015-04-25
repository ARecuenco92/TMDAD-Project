package es.unizar.tmdad.domain;
import java.sql.Date;

public class Amount {

	private int amount;
	private Date date;
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
}
