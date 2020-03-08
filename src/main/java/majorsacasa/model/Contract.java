package majorsacasa.model;

import java.sql.Date;

public class Contract {
    Integer ncontract;
    String firma;
    String datos;
    Double precio;
    Date releaseDate;
    Date dateDown;
    Integer cantidad;
    String descripcion;
    String nif;
    String dnielderly;

    public Contract() {
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getDateDown() {
        return dateDown;
    }

    public void setDateDown(Date dateDown) {
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
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
