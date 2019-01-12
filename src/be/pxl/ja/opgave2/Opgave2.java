package be.pxl.ja.opgave2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Opgave2 {
	public static void main(String[] args) {
		List<List<String>> passPhrases = new ArrayList<>();

		try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/opgave2/passphrases.txt"))) {
			String line = null;

			while ((line = reader.readLine()) != null) {
				String[] passPhraseArray = line.split(" ");
				List<String> passPhrase = Arrays.asList(passPhraseArray);
				passPhrases.add(passPhrase);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		int numberOfValidPassPhrases = 0;

		for (List<String> passPhrase:passPhrases) {
			PassPhraseValidator<String> passPhraseValidator = new PassPhraseValidator<>(passPhrase);
			passPhraseValidator.start();

			if(passPhraseValidator.isValid()) {
				numberOfValidPassPhrases++;
			}
		}

		System.out.println("Aantal geldige wachtwoordzinnen: " + numberOfValidPassPhrases);
	}
}
