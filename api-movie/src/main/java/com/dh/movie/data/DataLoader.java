package com.dh.movie.data;

import com.dh.movie.model.Movie;
import com.dh.movie.repository.MovieRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final MovieRepository repository;

    public DataLoader(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repository.save(new Movie(1L, "Terminator", "accion", "terminator.com"));
        repository.save(new Movie(2L, "Kung Fu Panda", "animación", "KungFuPanda.com"));
        repository.save(new Movie(3L, "La viuda negra", "acción", "avengers.com"));
        repository.save(new Movie(4L, "El Aro", "terror", "ElAro.com"));
        repository.save(new Movie(5L, "Detras del ultimo no hay nadie", "accion", "nolose.com"));
        repository.save(new Movie(6L, "Tres dias debajo de un palo de aguacates", "suspenso", "aguacates.com"));
        repository.save(new Movie(7L, "boné", "romance", "romances.com"));
        repository.save(new Movie(8L, "xícara", "romance", "noselaurl.com"));
        repository.save(new Movie(9L, "Ant-Man y la Avispa: Quantumania", "accion", "noselaurl.com"));
        repository.save(new Movie(10L, "Creed 3", "accion", "noselaurl.com"));
        repository.save(new Movie(11L, "World of Warcraft", "accion", "solowow.com"));
    }
}
