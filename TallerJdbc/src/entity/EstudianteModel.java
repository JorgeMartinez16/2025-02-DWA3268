

package entity;

public class EstudianteModel {
    private long id;
    private String nombre;
    private String apellido;
    private String correo;
    private int edad;
    private EstadoCivil estadoCivil;

    // Getters y setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(EstadoCivil estadoCivil) { this.estadoCivil = estadoCivil; }

    @Override
    public String toString() {
        return "ID=" + id + " | " + nombre + " " + apellido +
                " | " + correo + " | edad=" + edad +
                " | " + estadoCivil;
    }
}
