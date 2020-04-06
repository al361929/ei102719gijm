package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Contract {
    Integer ncontract;
    String firma;
    Double precio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate releaseDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateDown;
    Integer cantidad;
    String descripcion;
    String nifcompany;
    String dnielderly;

    public Contract() {
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getDateDown() {
        return dateDown;
    }

    public void setDateDown(LocalDate dateDown) {
        this.dateDown = dateDown;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNifcompany() {
        return nifcompany;
    }

    public void setNifcompany(String nif) {
        this.nifcompany = nif;
    }

    public Integer getNcontract() {
        return ncontract;
    }

    public void setNcontract(Integer ncontract) {
        this.ncontract = ncontract;
    }

    public String getDnielderly() {
        return dnielderly;
    }

    public void setDnielderly(String dnielderly) {
        this.dnielderly = dnielderly;
    }
}
