package com.dh.catalog.model;

import com.datastax.oss.driver.shaded.codehaus.jackson.annotate.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Catalog")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Serie {

    @Id
    private String id;
    private String name;
    private String genre;
    private List<Season> seasons = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Season {

        private Integer seasonNumber;
        private List<Serie.Chapter> chapters = new ArrayList<>();

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Chapter {

        private String name;
        private Integer number;
        private String urlStream;


    }
}