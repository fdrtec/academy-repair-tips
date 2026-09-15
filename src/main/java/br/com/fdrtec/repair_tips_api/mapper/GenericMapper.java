package br.com.fdrtec.repair_tips_api.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Arrays;

public interface GenericMapper {

    ObjectMapper objectMapper();

    default <S, T> T map(S source, Class<T> targetType, String... ignoredProperties) {
        ObjectNode sourceNode = objectMapper().valueToTree(source);
        sourceNode.remove(Arrays.asList(ignoredProperties));
        try {
            return objectMapper().readerFor(targetType)
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(sourceNode);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not map source object", exception);
        }
    }

    default <S, T> void update(S source, T target, String... ignoredProperties) {
        ObjectNode sourceNode = objectMapper().valueToTree(source);
        sourceNode.remove(Arrays.asList(ignoredProperties));
        try {
            objectMapper().readerForUpdating(target).readValue(sourceNode);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not update target object", exception);
        }
    }
}