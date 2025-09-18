import service.EstudianteServices;
import java.sql.Connection;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        EstudianteServices service = new EstudianteServices();
        Scanner sc = new Scanner(System.in);

        try (Connection cx = Connexion.getConnection()) {
            if (cx == null) return;

            service.crearTablaSiNoExiste(cx);

            while (true) {
                System.out.println("\n--- MENÚ ESTUDIANTES ---");
                System.out.println("1. Insertar estudiante");
                System.out.println("2. Listar estudiantes");
                System.out.println("3. Actualizar estudiante (un campo)");
                System.out.println("4. Buscar estudiante por correo");
                System.out.println("5. Eliminar estudiante por ID");
                System.out.println("6. Salir");
                System.out.print("Opción: ");
                String input = sc.nextLine().trim();

                int op;
                try {
                    op = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Debes ingresar un número válido.");
                    continue;
                }

                if (op == 1) {
                    service.insertarEstudianteConValores(cx, sc);
                } else if (op == 2) {
                    service.obtenerEstudiantes(cx);
                } else if (op == 3) {
                    service.actualizarCampoEstudiante(cx, sc);
                } else if (op == 4) {
                    service.consultarPorEmail(cx, sc);
                } else if (op == 6) {
                    System.out.println("👋 Adiós!");
                    break;
                } else {
                    System.out.println("❌ Opción no válida");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
