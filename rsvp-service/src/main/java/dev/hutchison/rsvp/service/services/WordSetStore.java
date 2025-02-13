package dev.hutchison.rsvp.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/***
 * Class to lazy load the word list in a singleton style.
 */
@NoArgsConstructor
class WordSetStore {
    private static final Logger logger = LoggerFactory.getLogger(WordSetStore.class);
    private HashMap<String, NameSeries> wordMap = null;

    Optional<NameSeries> tryGetFrom(String wordSet) {
        if (null == wordMap) {
            fillMap();
        }

        if (!wordMap.containsKey(wordSet)) {
            return Optional.empty();
        }

        return Optional.of(wordMap.get(wordSet));
    }

    private void fillMap() {
        logger.info("Initializing word set mapping...");

        try {
            File file = ResourceUtils.getFile("classpath:guest-list.json");

            ObjectMapper objectMapper = new ObjectMapper();
            MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, NameSeries.class);

            wordMap = objectMapper.readValue(file, mapType);
            logger.info("Word set mapping created");
        } catch (IOException e) {
            logger.error("There was an error creating word set mapping");
        }
    }
}
