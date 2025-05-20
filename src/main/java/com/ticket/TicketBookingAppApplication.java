package com.ticket;

import com.ticket.entities.Train;
import com.ticket.entities.Users;
import com.ticket.services.UserBookingService;
import com.ticket.util.UserServiceUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class TicketBookingAppApplication {

	public static void main(String[] args) {
		System.out.println("Running Train Booking App");
		Scanner scanner = new Scanner(System.in);
		int option = 0;
		UserBookingService userBookingService;
		try {
			userBookingService = new UserBookingService();
		} catch (IOException e) {
			System.out.println("There is somethings wrong");
			return;
		}
		while (option != 7){
			System.out.println("Choose option");
			System.out.println("1. Sign up");
			System.out.println("2. Login");
			System.out.println("3. Fetch Booking");
			System.out.println("4. Search Train");
			System.out.println("5. Book a Ticket");
			System.out.println("6. Cancel my Booking");
			System.out.println("Exit from app");
			option = scanner.nextInt();
			Train trainSelectedForBooking = new Train();

			switch (option){
				case 1:
					System.out.println("Enter the username to signup");
					String nameToSignUp = scanner.next();
					System.out.println("Enter the password to signup");
					String passwordToSignUp = scanner.next();
					Users userToSignup = new Users(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
					userBookingService.signUp(userToSignup);
					break;
				case 2:
					System.out.println("Enter the username to Login");
					String nameToLogin = scanner.next();
					System.out.println("Enter the password to signup");
					String passwordToLogin = scanner.next();
					Users userToLogin = new Users(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());
					try{
						userBookingService = new UserBookingService(userToLogin);
					}catch (IOException ex){
						return;
					}
					break;
				case 3:
					System.out.println("Fetching your bookings");
					userBookingService.featchBookings();
					break;
				case 4:
					System.out.println("Type your source station");
					String source = scanner.next();
					System.out.println("Type your destination station");
					String dest = scanner.next();
					List<Train> trains = userBookingService.getTrains(source, dest);
					int index = 1;
					for (Train t: trains){
						System.out.println(index+" Train id : "+t.getTrainId());
						for (Map.Entry<String, String> entry: t.getStationTime().entrySet()){
							System.out.println("station "+entry.getKey()+" time: "+entry.getValue());
						}
					}
					System.out.println("Select a train by typing 1,2,3...");
					trainSelectedForBooking = trains.get(scanner.nextInt());
					break;
				case 5:
					System.out.println("Select a seat out of these seats");
					List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
					for (List<Integer> row: seats){
						for (Integer val: row){
							System.out.print(val+" ");
						}
						System.out.println();
					}
					System.out.println("Select the seat by typing the row and column");
					System.out.println("Enter the row");
					int row = scanner.nextInt();
					System.out.println("Enter the column");
					int col = scanner.nextInt();
					System.out.println("Booking your seat....");
					Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
					if(booked.equals(Boolean.TRUE)){
						System.out.println("Booked! Enjoy your journey");
					}else{
						System.out.println("Can't book this seat");
					}
					break;
				default:
					break;
			}
		}
	}
}
