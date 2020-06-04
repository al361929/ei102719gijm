package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Company implements Comparable<Company> {

    String nombre;
    String nombreResponsable;
    String direccion;
    String nif;
    String numeroTelf;
    String nombreUsuario;
    String password;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaAlta;
    String cuentaBancaria;
    String email;

    public Company() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNumeroTelf() {
        return numeroTelf;
    }

    public void setNumeroTelf(String numeroTelf) {
        this.numeroTelf = numeroTelf;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Company{" +
                "nombre='" + nombre + '\'' +
                ", nombreResponsable='" + nombreResponsable + '\'' +
                ", direccion='" + direccion + '\'' +
                ", nif='" + nif + '\'' +
                ", numeroTelf='" + numeroTelf + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", password='" + password + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", cuentaBancaria='" + cuentaBancaria + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int compareTo(Company otro) {
        return this.getNombre().compareTo(otro.getNombre());
    }
}

