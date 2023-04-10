package com.dh.catalog.client;

import com.dh.catalog.model.Movie;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="api-movie",url = "http://localhost:8085/api-movie")
public interface MovieServiceClient {

	@GetMapping("/api/v1/movies/{genre}")
	List<MovieDto> getMovieByGenre(@PathVariable (value = "genre") String genre);

	@GetMapping("/movies/{genre}")
	List<Movie> getMoviesByCatalog(@PathVariable String genre);

	@PostMapping("/movies/save")
	Movie saveMovieByCatalog(@RequestBody Movie movie);


	@Getter
	@Setter
	class MovieDto{
		private Long id;

		private String name;

		private String genre;

		private String urlStream;
	}

}
