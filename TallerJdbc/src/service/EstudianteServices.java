package service;

import entity.EstadoCivil;
import entity.EstudianteModel;

import java.sql.*;
import java.util.Scanner;

public class EstudianteServices {

    public void crearTablaSiNoExiste(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS estudiantes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                edad INTEGER NOT NULL CHECK (edad BETWEEN 0 AND 120),
                estado_civil TEXT NOT NULL
            )
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    public void insertarEstudianteConValores(Connection conn, Scanner in) throws SQLException {
        System.out.print("Nombre: ");
        String nombre = in.nextLine().trim();
        System.out.print("Apellido: ");
        String apellido = in.nextLine().trim();
        System.out.print("Correo: ");
        String correo = in.nextLine().trim();
        int edad = leerEdad(in);
        EstadoCivil ec = leerEstadoCivil(in);

        String sql = "INSERT INTO estudiantes (nombre, apellido, correo, edad, estado_civil) VALUES (?,?,?,?,?)";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, nombre);
            stm.setString(2, apellido);
            stm.setString(3, correo);
            stm.setInt(4, edad);
            stm.setString(5, ec.name());
            int rs = stm.executeUpdate();
            System.out.println(rs > 0 ? "✅ Insertado" : "❌ Fallo en la inserción");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("⚠️ Correo duplicado.");
            } else {
                throw e;
            }
        }
    }

    public void obtenerEstudiantes(Connection conn) throws SQLException {
        String sql = "SELECT id,nombre,apellido,correo,edad,estado_civil FROM estudiantes ORDER BY id";
        try (PreparedStatement stm = conn.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            boolean any = false;
            while (rs.next()) {
                any = true;
                long id = rs.getLong("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                int edad = rs.getInt("edad");
                String estadoCivil = rs.getString("estado_civil");
                System.out.printf("ID=%d | %s %s | %s | edad=%d | %s%n",
                        id, nombre, apellido, correo, edad, estadoCivil);
            }
            if (!any) {
                System.out.println("(sin registros)");
            }
        }
    }

    // 🔹 NUEVO: Actualizar un campo específico
    public void actualizarCampoEstudiante(Connection conn, Scanner in) throws SQLException {
        System.out.print("ID del estudiante a actualizar: ");
        long id = Long.parseLong(in.nextLine().trim());

        // Mostrar estudiante actual
        EstudianteModel actual = buscarPorId(conn, id);
        if (actual == null) {
            System.out.println("⚠️ No existe estudiante con ID=" + id);
            return;
        }
        System.out.println("Actual: " + actual);

        System.out.println("¿Qué campo deseas actualizar?");
        System.out.println("1. Nombre");
        System.out.println("2. Apellido");
        System.out.println("3. Correo");
        System.out.println("4. Edad");
        System.out.println("5. Estado Civil");
        System.out.print("Opción: ");
        int opcion = Integer.parseInt(in.nextLine().trim());

        String campo = null;
        Object nuevoValor = null;

        switch (opcion) {
            case 1 -> {
                System.out.print("Nuevo nombre: ");
                campo = "nombre";
                nuevoValor = in.nextLine().trim();
            }
            case 2 -> {
                System.out.print("Nuevo apellido: ");
                campo = "apellido";
                nuevoValor = in.nextLine().trim();
            }
            case 3 -> {
                System.out.print("Nuevo correo: ");
                campo = "correo";
                nuevoValor = in.nextLine().trim();
            }
            case 4 -> {
                int edad = leerEdad(in);
                campo = "edad";
                nuevoValor = edad;
            }
            case 5 -> {
                EstadoCivil ec = leerEstadoCivil(in);
                campo = "estado_civil";
                nuevoValor = ec.name();
            }
            default -> {
                System.out.println("❌ Opción no válida");
                return;
            }
        }

        String sql = "UPDATE estudiantes SET " + campo + "=? WHERE id=?";
        try (PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setObject(1, nuevoValor);
            stm.setLong(2, id);
            int rs = stm.executeUpdate();
            System.out.println(rs > 0 ? "✅ Actualizado" : "❌ No se pudo actualizar");
        }
    }

    // 🔹 NUEVO: Buscar por correo
    public void consultarPorEmail(Connection conn, Scanner in) throws SQLException {
        System.out.print("Correo a buscar: ");
        String correo = in.nextLine().trim();
        EstudianteModel e = buscarPorEmail(conn, correo);
        if (e == null) {
            System.out.println("⚠️ No existe estudiante con ese correo.");
        } else {
            System.out.println("✅ Encontrado: " + e);
        }
    }

    // Helpers privados
    private EstudianteModel buscarPorId(Connection conn, long id) throws SQLException {
        String sql = "SELECT id,nombre,apellido,correo,edad,estado_civil FROM estudiantes WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapResultSet(rs);
            }
        }
    }

    private EstudianteModel buscarPorEmail(Connection conn, String correo) throws SQLException {
        String sql = "SELECT id,nombre,apellido,correo,edad,estado_civil FROM estudiantes WHERE correo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapResultSet(rs);
            }
        }
    }

    private EstudianteModel mapResultSet(ResultSet rs) throws SQLException {
        EstudianteModel e = new EstudianteModel();
        e.setId(rs.getLong("id"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setCorreo(rs.getString("correo"));
        e.setEdad(rs.getInt("edad"));
        e.setEstadoCivil(EstadoCivil.valueOf(rs.getString("estado_civil")));
        return e;
    }

    private int leerEdad(Scanner in) {
        while (true) {
            try {
                System.out.print("Edad (0-120): ");
                int edad = Integer.parseInt(in.nextLine().trim());
                if (edad < 0 || edad > 120) throw new IllegalArgumentException();
                return edad;
            } catch (Exception ex) {
                System.out.println("Edad inválida. Intenta de nuevo.");
            }
        }
    }

    private EstadoCivil leerEstadoCivil(Scanner in) {
        System.out.println("Estado civil: 1) SOLTERO  2) CASADO  3) VIUDO  4) UNION_LIBRE  5) DIVORCIADO");
        while (true) {
            try {
                System.out.print("Elige (1-5): ");
                int opt = Integer.parseInt(in.nextLine().trim());
                return EstadoCivil.fromMenu(opt);
            } catch (Exception ex) {
                System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
    }
}
