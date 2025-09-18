

package entity;

public enum EstadoCivil {
    SOLTERO, CASADO, VIUDO, UNION_LIBRE, DIVORCIADO;

    public static EstadoCivil fromMenu(int opt) {
        return switch (opt) {
            case 1 -> SOLTERO;
            case 2 -> CASADO;
            case 3 -> VIUDO;
            case 4 -> UNION_LIBRE;
            case 5 -> DIVORCIADO;
            default -> throw new IllegalArgumentException("Opción inválida");
        };
    }
}
