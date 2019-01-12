package be.pxl.ja.opgave1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityProcessor {

	private CustomerRepository customerRepository;
	
	public ActivityProcessor(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	public List<Activity> processActivities(Path activityFile, Path errorFile) {
		if (activityFile.toString().contains("strava") || activityFile.toString().contains("endomondo")) {
			try (BufferedReader activityReader = Files.newBufferedReader(activityFile); BufferedWriter errorWriter = Files.newBufferedWriter(errorFile)){
				List<Activity> activities = new ArrayList<>();
				String line;
				while ((line = activityReader.readLine()) != null) {
					String[] splitLine = line.split(";");
					Activity activity = new Activity();
					String customerNumber = null;
					Customer customer = null;

					if (activityFile.toString().contains("strava")) {
						customerNumber = splitLine[0].split(" ")[splitLine[0].split(" ").length - 1];
						activity.setCustomerNumber(customerNumber);
						activity.setActivityDate(LocalDate.parse(splitLine[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
						activity.setTracker(ActivityTracker.STRAVA);
					} else if (activityFile.toString().contains("endomondo")) {
						activity.setActivityDate(LocalDate.parse(splitLine[0], DateTimeFormatter.ofPattern("yyyyMMdd")));
						customerNumber = splitLine[1];
						activity.setCustomerNumber(customerNumber);
						activity.setTracker(ActivityTracker.ENDOMODO);
					}

					if (customerRepository.getByCustomerNumber(customerNumber) != null) {
						customer = customerRepository.getByCustomerNumber(customerNumber);
					} else {
						errorWriter.write(LocalDateTime.now() + " - " + activityFile.getFileName().toString() + " - " + "UNKNOWN CUSTOMER: \n" + line);
					}

					int points = 0;

					if (splitLine[2].equalsIgnoreCase("swimming")) {
						activity.setDistance((Double.parseDouble(splitLine[3]) / 1000));
						activity.setActivityType(ActivityType.SWIMMING);
						points = (int) Math.floor(ActivityType.SWIMMING.getPointsPerKm() * activity.getDistance());
					} else if (splitLine[2].equalsIgnoreCase("running")) {
						activity.setDistance(Double.parseDouble(splitLine[3]));
						activity.setActivityType(ActivityType.RUNNING);
						points = (int) Math.floor(ActivityType.RUNNING.getPointsPerKm() * activity.getDistance());
					} else if (splitLine[2].equalsIgnoreCase("riding")) {
						activity.setDistance(Double.parseDouble(splitLine[3]));
						activity.setActivityType(ActivityType.RIDING);
						points = (int) Math.floor(ActivityType.RIDING.getPointsPerKm() * activity.getDistance());
					}

					customer.addPoints(points);

					activities.add(activity);
				}
				return activities;
			} catch (Exception ex) {
				try {
					BufferedWriter errorWriter = Files.newBufferedWriter(errorFile);
					errorWriter.write(LocalDateTime.now() + " - " + activityFile.getFileName().toString() + " - " + ex.getMessage());
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		} else {
			try (BufferedWriter errorWriter = Files.newBufferedWriter(errorFile)){
				errorWriter.write(LocalDateTime.now() + " - " + activityFile.getFileName().toString() + " - " + "INVALID FILENAME");
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}

		return null;
	}
}
