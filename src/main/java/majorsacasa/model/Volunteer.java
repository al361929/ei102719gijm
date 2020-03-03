package majorsacasa.model;

import java.time.LocalDate;

public class Volunteer {
    String nombre;
    String apellidos;
    String direccion;
    String dni;
    String telefono;
    String usuario;
    String contraseña;
    LocalDate realaseDate;
    LocalDate dataDown;
    LocalDate birthday;

    public Volunteer() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nom) {
        this.nombre = nom;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellido) {
        this.apellidos = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public LocalDate getRealaseDate() {
        return realaseDate;
    }

    public void setRealaseDate(LocalDate realaseDate) {
        this.realaseDate = realaseDate;
    }

    public LocalDate getDataDown() {
        return dataDown;
    }

    public void setDataDown(LocalDate dataDown) {
        this.dataDown = dataDown;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
