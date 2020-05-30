package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class Elderly implements Comparable<Elderly> {

    String nombre;
    String apellidos;
    String direccion;
    String dni;
    String alergias;
    String nuevasAlergias;
    String telefono;
    String usuario;
    String contraseña;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dataDown;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthday;
    String cuentaBancaria;
    String email;
    String socialWorker;

    public String getNuevasAlergias() {
        return nuevasAlergias;
    }

    public void setNuevasAlergias(String nuevasAlergias) {
        this.nuevasAlergias = nuevasAlergias;
    }

    public Elderly() {
    }

    public String getDatos() {
        return nombre + " " + apellidos + " " + dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getDateDown() {
        return dataDown;
    }

    public void setDateDown(LocalDate dataDown) {
        this.dataDown = dataDown;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public String getSocialWorker() {
        return socialWorker;
    }

    public void setSocialWorker(String socialWorker) {
        this.socialWorker = socialWorker;
    }

    public boolean Alergias() {
        return (alergias.length() > 1);
    }

    public void actualizarAlergias() {
        Collection<String> col = new HashSet<>();
        String todas = "";
        if (getNuevasAlergias() != null) {
            todas = getAlergias() + "," + getNuevasAlergias();
        } else {
            todas = getAlergias();
        }
        String[] alergias = todas.split(",");
        for (String a : alergias) {
            col.add(a);
        }
        todas = "";
        Object[] ab = col.toArray();
        todas = (String) ab[0];
        for (int i = 1; i < ab.length; i++) {
            todas = todas + "," + ab[i];

        }
        setAlergias(todas);
        setNuevasAlergias("");

    }

    @Override
    public int compareTo(Elderly otro) {
        return this.getNombre().compareTo(otro.getNombre());
    }
}
