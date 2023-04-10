package com.dh.catalog.client;

import com.dh.catalog.model.Serie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "serie-service-parcial")
public interface SerieServiceClient {
    @GetMapping
    List<Serie> getAll();

    @GetMapping("/series/{genre}")
    List<Serie> getSerieByGenre(@PathVariable String genre);

    @PostMapping("/series/save")
    @ResponseStatus(HttpStatus.CREATED)
    String create(@RequestBody Serie serie);

}

