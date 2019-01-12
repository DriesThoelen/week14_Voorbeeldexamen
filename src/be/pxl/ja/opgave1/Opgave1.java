package be.pxl.ja.opgave1;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Opgave1 {
	public static void main(String[] args) {
		CustomerRepository customerRepository = new CustomerRepository();
		System.out.println("*** Klanten uit Louisville:");
		// TODO: 1
		customerRepository.findAll().stream().filter(c -> c.getCity().equalsIgnoreCase("Louisville")).forEach(c -> System.out.println(c));
		
		System.out.println("*** Jarige klanten: ");
		// TODO: 2
		customerRepository.findAll().stream().filter(c -> (c.getDateOfBirth().getDayOfMonth() == LocalDate.now().getDayOfMonth()) && (c.getDateOfBirth().getMonthValue() == LocalDate.now().getMonthValue())).forEach(c -> System.out.println(c));

		System.out.println("*** 10 jongste klanten:");
		// TODO: 3
		customerRepository.findAll().stream().sorted(Comparator.comparing(Customer::getDateOfBirth).reversed()).limit(10).forEach(c -> System.out.println(c));

		// TODO: 4

		ActivityProcessor activityFileProcessor = new ActivityProcessor(customerRepository);
		List<Activity> allActivities = new ArrayList<>();
		Path directory = Paths.get("resources/opgave1/");
		File directoryFile = directory.toFile();
		File[] activityFiles = directoryFile.listFiles();
		List<Path> activityPaths = Arrays.stream(activityFiles).map(f -> f.toPath()).collect(Collectors.toList());
		Path errorFile = Paths.get("resources/opgave1/log/errors.log");
		for (Path activityPath: activityPaths) {
			List<Activity> tempActivities = activityFileProcessor.processActivities(activityPath, errorFile);

			if (tempActivities != null) {
				for (Activity activity:tempActivities) {
					allActivities.add(activity);
				}
			}
		}


		System.out.println("*** Top 10 klanten");
		// TODO: 5		
		customerRepository.findAll().stream().sorted(Comparator.comparing(Customer::getPoints).reversed()).limit(10).forEach(c -> System.out.println(c));

		System.out.println("** Alle activiteiten meest actieve klant (gesorteerd op datum):");
		// TODO: 6
		Customer customerWithMostPoints = customerRepository.findAll().stream().max(Comparator.comparing(Customer::getPoints)).get();
		allActivities.stream().filter(a -> a.getCustomerNumber().equalsIgnoreCase(customerWithMostPoints.getCustomerNumber())).sorted(Comparator.comparing(Activity::getActivityDate).reversed()).forEach(a -> System.out.println(a));
	}
}
