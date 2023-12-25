package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "root@123";
	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection);
			
			while(true) {
				System.out.println("Welcome to Hospital Management System");
				System.out.println("1. Add Patient");
				System.out.println("2. View Patient");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter Your Choice");
				int choice = scanner.nextInt();
				
				switch(choice) {
				case 1:
					//Add Patient
					patient.addPatient();
					System.out.println();
					break;
				case 2:
					//View Patient
					patient.viewPatients();
					System.out.println();
					break;
				case 3:
					//View Doctors
					doctor.viewDoctors();
					System.out.println();
					break;
				case 4:
					//Book Appointment
					bookAppointment(patient, doctor, connection, scanner);
					System.out.println();
					break;
					
				case 5:
					return;
				default :
					System.out.println("Enter Valid Choice !!!!!!");
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static void bookAppointment(Patient patient, Doctor doctor,Connection connection, Scanner scanner){
		System.out.print("Enter Patient Id : ");
		int patientId = scanner.nextInt();
		System.out.print("Enter Doctor Id : ");
		int doctorId = scanner.nextInt();
		System.out.print("Enter Appointment Date (YYYY/MM/DD)");
		String appointmentDate = scanner.next();
		
		if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
			if(checkDoctorAvailablity(doctorId, appointmentDate, connection)) {
				String appointmentQuery = "insert into appointments(patient_id, doctor_id, apppointment_date) values (?,?,?)";
				
				try {
					PreparedStatement preparedstatement =connection.prepareStatement(appointmentQuery);
					preparedstatement.setInt(1, patientId);
					preparedstatement.setInt(2, doctorId);
					preparedstatement.setString(3, appointmentDate);
					int rowsAffected = preparedstatement.executeUpdate();
					if(rowsAffected>0) {
						System.out.println("Appointment Booked!!!");
					}else {
						System.out.println("Failed to Book Appointment!!!!");
					}
					
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Doctor not available in this Date !!!");
			}
			
		}else {
			System.out.println("Either Doctor or Patient Doesn't Exist !!!!!");
		}
		
	}
	
	public static boolean checkDoctorAvailablity(int doctorId, String appointmentDate, Connection connection) {
		String query = "select count (*) from appointments where doctor_id = ? AND appointment_date = ?";
		try {
			PreparedStatement preparedstatement =connection.prepareStatement(query);
			preparedstatement.setInt(1, doctorId);
			preparedstatement.setString(2, appointmentDate);
			ResultSet resultSet = preparedstatement.executeQuery();
			if(resultSet.next()) {
				int count = resultSet.getInt(1);
				if(count==0) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
