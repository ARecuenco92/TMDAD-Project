package es.unizar.tmdad.infraestructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.unizar.tmdad.application.ChartRepository;
import es.unizar.tmdad.domain.Amount;

public class MySQLRepository implements ChartRepository{

	private final String POLITICAL_PARTIES_TABLE = "political_parties";
	private final String SOCIAL_NETWORK_INFO_TABLE = "social_network_info";

	@Override
	public List<Amount> getFollowers(String politicalParty) {
		Connection connection = null;
		List<Amount> charts = new ArrayList<Amount>();
		Amount amount;
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
				amount = new Amount();
				amount.setAmount(result.getInt("followers"));
				amount.setDate(result.getDate("date"));			
				charts.add(amount);
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
		return charts;
	}
	
	@Override
	public List<Amount> getLikes(String politicalParty) {
		Connection connection = null;
		List<Amount> charts = new ArrayList<Amount>();
		Amount amount;
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
				amount = new Amount();
				amount.setAmount(result.getInt("followers"));
				amount.setDate(result.getDate("date"));			
				charts.add(amount);
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
		return charts;
	}

	@Override
	public List<Amount> getEvolution (String politicalParty) {
		Connection connection = null;
		List<Amount> charts = new ArrayList<Amount>();
		Amount amount; int oldAmount;
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
					amount = new Amount();
					amount.setAmount(100 * ((float) result.getInt("evolution")/oldAmount -1));
					amount.setDate(result.getDate("date"));			
					charts.add(amount);
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
		return charts;
	}
}
