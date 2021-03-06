package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public class VolunteerTime implements Comparable<VolunteerTime> {

    Integer idVolunteerTime;
    String dniVolunteer;
    String dniElderly;
    String mes;
    int dia;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime endTime;
    String availability;

    public VolunteerTime() {

    }

    public Integer getIdVolunteerTime() {
        return idVolunteerTime;
    }

    public String getId() {
        return idVolunteerTime + "";
    }

    public void setIdVolunteerTime(Integer idVolunteerTime) {
        this.idVolunteerTime = idVolunteerTime;
    }

    public String getDniVolunteer() {
        return dniVolunteer;
    }

    public void setDniVolunteer(String dniVolunteer) {
        this.dniVolunteer = dniVolunteer;
    }

    public String getDniElderly() {
        return dniElderly;
    }

    public void setDniElderly(String dniElderly) {
        this.dniElderly = dniElderly;
    }

    public String getMes() {
        return mes;
    }

    public int getMesInt() {
        switch (mes) {
            case "Enero":
                return 1;
            case "Febrero":
                return 2;
            case "Marzo":
                return 3;
            case "Abril":
                return 4;
            case "Mayo":
                return 5;
            case "Junio":
                return 6;
            case "Julio":
                return 7;
            case "Agosto":
                return 8;
            case "Septiembre":
                return 9;
            case "Octubre":
                return 10;
            case "Noviembre":
                return 11;
            default:
                return 12;
        }
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "VolunteerTime{" +
                "idVolunteerTime='" + idVolunteerTime + '\'' +
                ", dniVolunteer='" + dniVolunteer + '\'' +
                ", dniElderly='" + dniElderly + '\'' +
                ", mes='" + mes + '\'' +
                ", dia=" + dia +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", availability='" + availability + '\'' +
                '}';
    }

    @Override
    public int compareTo(VolunteerTime otro) {
        return this.getDniElderly().compareTo(otro.getDniElderly());
    }
}
