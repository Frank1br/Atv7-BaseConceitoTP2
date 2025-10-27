package com.gerenciamentoInventario.gerenciamentoInventario.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email.trim());
        return matcher.matches();
    }

    public static boolean validarId(String id) {
        return id != null && !id.trim().isEmpty();
    }

    public static boolean validarNome(String nome) {
        return nome != null && !nome.trim().isEmpty() && nome.trim().length() >= 3;
    }

    public static boolean validarPreco(double preco) {
        return preco > 0;
    }

    public static boolean validarQuantidade(int quantidade) {
        return quantidade >= 0;
    }

    public static boolean validarQuantidadePedido(int quantidade) {
        return quantidade > 0;
    }
}
