package com.dh.catalog.controller;

import com.dh.catalog.client.MovieServiceClient;

import com.dh.catalog.client.SerieServiceClient;
import com.dh.catalog.model.CatalogResponse;
import com.dh.catalog.model.Movie;
import com.dh.catalog.model.Serie;
import com.dh.catalog.queue.MovieListener;
import com.dh.catalog.repository.MovieRepository;
import com.dh.catalog.repository.SerieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {

	@Autowired
	private MovieServiceClient movieServiceClient;

	@Autowired
	private SerieServiceClient serieServiceClient;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private SerieRepository serieRepository;

	private final Logger logger = LoggerFactory.getLogger(MovieListener.class);


	@GetMapping("/all/{genre}")
	public CatalogResponse getCatalogByGenreOnline(@PathVariable String genre) {
		List<Movie> movies = movieServiceClient.getMoviesByCatalog(genre);
		List<Serie> series = serieServiceClient.getSerieByGenre(genre);
		return new CatalogResponse(genre, movies, series);
	}

	@GetMapping("/local/all")
	public CatalogResponse getCatalogOffline() {
		List<Movie> movies = movieRepository.findAll();
		List<Serie> series = serieRepository.findAll();
		return new CatalogResponse("local", movies, series);
	}

	@GetMapping("/local/all/{genre}")
	public CatalogResponse getCatalogByGenreOffline(@PathVariable String genre) {
		List<Movie> movies = movieRepository.findAllByGenre(genre);
		List<Serie> series = serieRepository.findAllByGenre(genre);
		return new CatalogResponse(genre, movies, series);
	}

	@GetMapping("/local/all-movies")
	ResponseEntity<List<Movie>> getAllMovies() {
		return ResponseEntity.ok().body(movieRepository.findAll());
	}

	@GetMapping("/local/all-movies/{genre}")
	public List<Movie> getMovieByGenreOff(@PathVariable String genre) {
		return movieRepository.findAllByGenre(genre);
	}

	@GetMapping("/local/all-series")
	ResponseEntity<List<Serie>> getAllSeries() {
		return ResponseEntity.ok().body(serieRepository.findAll());
	}

	@GetMapping("/local/all-series/{genre}")
	public List<Serie> getSerieByGenreOff(@PathVariable String genre) {
		return serieRepository.findAllByGenre(genre);
	}

	/* Este metodo se usa para obtener una lista de peliculas por género utilizando el
	   servicio de Movies o la base de datos local como fallback en caso de error.
	   @param genre, es el género de la película
	   @return muestra la lista de películas que corresponden al género especificado
	   @throws RuntimeException en caso de que no se pueda obtener la lista de películas por ningún medio
	 */
	@CircuitBreaker(name = "movies", fallbackMethod = "fallbackMovies")
	@Retry(name = "movies")
	@GetMapping("/fall-movies/{genre}")
	public List<Movie> getMovieByGenre(@PathVariable String genre) {
		logger.info("Buscando peliculas por genero :" + genre + ". En la base de datos online");
		List<Movie> movieList = movieServiceClient.getMoviesByCatalog(genre);
		if (movieList.isEmpty()) {
			movieList = movieRepository.findAllByGenre(genre);
		}
		return movieList;
	}

	/* Este método es el fallback que se ejecuta si no se puede obtener la lista de películas
	   desde el servicio externo o desde la base de datos local.
	   @param genre el género de la película
	   @param e     la excepción que causó el fallback
	   @return la lista de películas que se obtiene desde la base de datos local
	   @throws RuntimeException si no se puede obtener ninguna película desde la base de datos local
	 */
	private List<Movie> fallbackMovies(String genre, RuntimeException e) {
		logger.info("Usando fallback de peliculas para buscar el genero: " + genre + ". En la base de datos local");
		List<Movie> movieList = movieRepository.findAllByGenre(genre);
		if (movieList.isEmpty()) {
			throw new RuntimeException("No se pudo encontrar ninguna película en la base de datos local");
		}
		return movieList;
	}


	/* Este metodo se usa para obtener una lista de series por género utilizando el
	  servicio de Series o la base de datos local como fallback en caso de error.

	  @param genre el género de la serie a buscar.
	  @return la lista de series encontradas por género.
	 */
	@CircuitBreaker(name = "series", fallbackMethod = "fallbackSeries")
	@Retry(name = "series")
	@GetMapping("/fall-series/{genre}")
	public List<Serie> getSerieByGenre(@PathVariable String genre) {
		logger.info("Buscando series por genero :" + genre + ". En la base de datos online");
		List<Serie> serieList = serieServiceClient.getSerieByGenre(genre);
		if (serieList.isEmpty()) {
			serieList = serieRepository.findAllByGenre(genre);
		}
		return serieList;
	}

	/* Método fallback para buscar series por género en la base de datos local.
	   @param genre el género de la serie a buscar.
	   @param e     la excepción lanzada por el servicio de Series.
	   @return la lista de series encontradas por género en la base de datos local.
	   @throws RuntimeException si no se pudo encontrar ninguna serie en la base de datos local.
	 */
	private List<Serie> fallbackSeries(String genre, RuntimeException e) {
		logger.info("Usando fallback de series para buscar el género: " + genre + ". En la base de datos local");
		List<Serie> serieList = serieRepository.findAllByGenre(genre);
		if (serieList.isEmpty()) {
			throw new RuntimeException("No se pudo encontrar ninguna serie en la base de datos local");
		}
		return serieList;
	}

}
