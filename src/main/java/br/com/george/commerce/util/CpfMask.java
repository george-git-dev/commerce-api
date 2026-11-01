package br.com.george.commerce.util;

/**
 * CPF mascarado para mostrar na tela: {@code 52998224725 → ***.982.247-**}. O CPF completo só sai
 * da API onde for indispensável (nota fiscal, pagamento) — nunca para exibir.
 */
public final class CpfMask {

    private CpfMask() {
    }

    /** {@code null} continua {@code null} (conta sem CPF ainda); valor fora do formato vira só asteriscos. */
    public static String mask(String cpf) {
        if (cpf == null) {
            return null;
        }
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) {
            return "***.***.***-**";
        }
        return "***." + digits.substring(3, 6) + "." + digits.substring(6, 9) + "-**";
    }
}
