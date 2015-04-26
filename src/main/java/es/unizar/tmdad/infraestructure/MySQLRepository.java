package es.unizar.tmdad.infraestructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.unizar.tmdad.domain.chart.Chart;
import es.unizar.tmdad.domain.chart.ChartData;
import es.unizar.tmdad.domain.chart.ChartRepository;

public class MySQLRepository implements ChartRepository{

	private final String POLITICAL_PARTIES_TABLE = "political_parties";
	private final String SOCIAL_NETWORK_INFO_TABLE = "social_network_info";

	@Override
	public ChartData getFollowers(String politicalParty) {
		Connection connection = null;
		ChartData chartData = new ChartData();
		try{
			connection = MySQLConnection.getConnection();
			String queryString = "SELECT followers,date FROM "+POLITICAL_PARTIES_TABLE
					+" JOIN "+SOCIAL_NETWORK_INFO_TABLE
					+" ON "+POLITICAL_PARTIES_TABLE+".id = "+SOCIAL_NETWORK_INFO_TABLE+".id"
					+ " WHERE name = ?";

			PreparedStatement preparedStatement = 
					connection.prepareStatement(queryString);

			preparedStatement.setString(1, politicalParty);
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()){
				chartData.addData(result.getDate("date").toString(), result.getInt("followers"));
			}

		} 
		catch (SQLException e) {
			e.printStackTrace(System.err);
		} 
		finally{
			try {
				if(connection != null){
					connection.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}  
		return chartData;
	}
	
	@Override
	public ChartData getLikes(String politicalParty) {
		Connection connection = null;
		ChartData chartData = new ChartData();
		try{
			connection = MySQLConnection.getConnection();
			String queryString = "SELECT likes,date FROM "+POLITICAL_PARTIES_TABLE
					+" JOIN "+SOCIAL_NETWORK_INFO_TABLE
					+" ON "+POLITICAL_PARTIES_TABLE+".id = "+SOCIAL_NETWORK_INFO_TABLE+".id"
					+ " WHERE name = ?";

			PreparedStatement preparedStatement = 
					connection.prepareStatement(queryString);

			preparedStatement.setString(1, politicalParty);
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()){
				chartData.addData(result.getDate("date").toString(), result.getInt("likes"));
			}

		} 
		catch (SQLException e) {
			e.printStackTrace(System.err);
		} 
		finally{
			try {
				if(connection != null){
					connection.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}  
		return chartData;
	}
	
	@Override
	public Chart getEvolution(){
		Connection connection = null;
		Chart evolution = new Chart();
		try{
			connection = MySQLConnection.getConnection();
			String queryString = "SELECT name FROM "+POLITICAL_PARTIES_TABLE;

			PreparedStatement preparedStatement = 
					connection.prepareStatement(queryString);

			ResultSet result = preparedStatement.executeQuery();
			while(result.next()){
				evolution.addChatData(getEvolution(result.getString("name")));
			}
		}
		catch (SQLException e) {
			e.printStackTrace(System.err);
		} 
		finally{
			try {
				if(connection != null){
					connection.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}  
		return evolution;
	}

	@Override
	public ChartData getEvolution (String politicalParty) {
		Connection connection = null;
		ChartData chartData = new ChartData();
		int oldAmount;
		try{
			connection = MySQLConnection.getConnection();
			String queryString = "SELECT likes + followers AS evolution, date "
					+ "FROM "+POLITICAL_PARTIES_TABLE
					+" JOIN "+SOCIAL_NETWORK_INFO_TABLE
					+" ON "+POLITICAL_PARTIES_TABLE+".id = "+SOCIAL_NETWORK_INFO_TABLE+".id"
					+ " WHERE name = ?";

			PreparedStatement preparedStatement = 
					connection.prepareStatement(queryString);

			preparedStatement.setString(1, politicalParty);
			ResultSet result = preparedStatement.executeQuery();

			if(result.next()){
				oldAmount = result.getInt("evolution");
				while(result.next()){					
					chartData.addData(result.getDate("date").toString(), 100 * ((float) result.getInt("evolution")/oldAmount -1));
				}
			}

		} 
		catch (SQLException e) {
			e.printStackTrace(System.err);
		} 
		finally{
			try {
				if(connection != null){
					connection.close();
				}
			} 
			catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}  
		return chartData;
	}
}
