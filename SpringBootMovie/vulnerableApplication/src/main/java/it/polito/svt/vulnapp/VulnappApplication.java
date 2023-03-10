package it.polito.svt.vulnapp;

import it.polito.svt.vulnapp.model.Movie;
import it.polito.svt.vulnapp.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Date;

@SpringBootApplication
public class VulnappApplication implements CommandLineRunner {

	private final MovieRepository movieRepository;

	public VulnappApplication(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(VulnappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		/* adding movies to the H2 database */
		this.movieRepository.save(new Movie("Kill Bill: Vol. 1", "Quentin Tarantino", 7, new Date(), "After awakening from a four-year coma, a former assassin wreaks vengeance on the team of assassins who betrayed her.", "movieImages/KillBill1.png"));
		this.movieRepository.save(new Movie("Kill Bill: Vol. 2", "Quentin Tarantino", 7, new Date(), "The Bride continues her quest of vengeance against her former boss and lover Bill, the reclusive bouncer Budd, and the treacherous, one-eyed Elle.", "movieImages/KillBill2.jpg"));
		this.movieRepository.save(new Movie("Reservoir Dogs", "Quentin Tarantino", 8, new Date(), "When a simple jewelry heist goes horribly wrong, the surviving criminals begin to suspect that one of them is a police informant.", "movieImages/ReservoirDogs.png"));
		this.movieRepository.save(new Movie("Inglourious Basterds", "Quentin Tarantino", 8, new Date(), "In Nazi-occupied France during World War II, a plan to assassinate Nazi leaders by a group of Jewish U.S. soldiers coincides with a theatre owner's vengeful plans for the same.", "movieImages/InglouriousBasterds.png"));
		this.movieRepository.save(new Movie("Django Unchained", "Quentin Tarantino", 8, new Date(), "With the help of a German bounty hunter, a freed slave sets out to rescue his wife from a brutal Mississippi plantation owner.", "movieImages/DjangoUnchained.png"));
		this.movieRepository.save(new Movie("The Hateful Eight", "Quentin Tarantino", 7, new Date(), "In the dead of a Wyoming winter, a bounty hunter and his prisoner find shelter in a cabin currently inhabited by a collection of nefarious characters.", "movieImages/TheHatefulEight.png"));
		this.movieRepository.save(new Movie("Once Upon a Time in Hollywood", "Quentin Tarantino", 8, new Date(), "A faded television actor and his stunt double strive to achieve fame and success in the final years of Hollywood's Golden Age in 1969 Los Angeles.", "movieImages/OnceUponATimeInHollywood.png"));
		this.movieRepository.save(new Movie("Pulp Fiction", "Quentin Tarantino", 8, new Date(), "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", "movieImages/PulpFiction.png"));
	}
}