package com.gerenciamentoInventario.gerenciamentoInventario.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper objectMapper = configurarObjectMapper();


    private static ObjectMapper configurarObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Formatação com indentação para melhor legibilidade
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Suporte para LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    public static void salvarJson(Object objeto, String arquivo) throws IOException {
        File file = new File(arquivo);

        // Cria diretório se não existir
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        objectMapper.writeValue(file, objeto);
    }


    public static <T> T carregarJson(String arquivo, Class<T> classe) throws IOException {
        File file = new File(arquivo);

        if (!file.exists()) {
            return null;
        }

        return objectMapper.readValue(file, classe);
    }


    public static JsonNode carregarComoJsonNode(String arquivo) throws IOException {
        File file = new File(arquivo);

        if (!file.exists()) {
            return null;
        }

        return objectMapper.readTree(file);
    }


    public static <T> T converterJsonNode(JsonNode node, Class<T> classe) {
        return objectMapper.convertValue(node, classe);
    }

    public static boolean arquivoExiste(String arquivo) {
        return new File(arquivo).exists();
    }

    public static void criarArquivoVazio(String arquivo) throws IOException {
        salvarJson(new Object[0], arquivo);
    }
}
