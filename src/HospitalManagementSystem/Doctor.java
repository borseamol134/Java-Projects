package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Doctor {
	private Connection connection;
	
	
	public Doctor(Connection connection) {
		this.connection = connection;
		
	}
	
	
	public void viewDoctors() {
		String query = "select * from doctors";
		try {
			PreparedStatement preparedstatement =connection.prepareStatement(query);
			ResultSet resultset = preparedstatement.executeQuery();
			System.out.println("DOCTORS :");
			System.out.println("+------------+---------------------------+--------------------------+");
			System.out.println("| Doctor Id  | Name                      | Specialization           |");
			System.out.println("+------------+---------------------------+--------------------------+");
			while(resultset.next()) {
				int id = resultset.getInt("id");
				String name =resultset.getString("name");
				String specialization =resultset.getString("specialization");
				System.out.printf("| %-11s| %-25s | %-15s |\n", id, name, specialization);
				System.out.println("+------------+---------------------------+--------------------------+");
				
			}
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}

}
	public boolean getDoctorById(int id) {
		String query = "select * from doctors where id =?";
		try {
			PreparedStatement preparedstatement =connection.prepareStatement(query);
			preparedstatement.setInt(1, id);
			ResultSet resultSet = preparedstatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}


