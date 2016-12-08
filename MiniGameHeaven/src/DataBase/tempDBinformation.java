package DataBase;

public class tempDBinformation {
	private String winner;
	private String startDate,endDate;

	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;

	}
	
	public void setWinner(String winner) {
		this.winner = winner;
	}
	
	
	public String getWinner() {
		return winner;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}
}
