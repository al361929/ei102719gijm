package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Invoice implements Comparable<Invoice> {

    Integer invoiceNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateInvoice;
    Integer totalPrice;
    String dniElderly;
    Boolean invoicePDF;

    public Invoice() {
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(LocalDate dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDniElderly() {
        return dniElderly;
    }

    public void setDniElderly(String dniElderly) {
        this.dniElderly = dniElderly;
    }

    public Boolean getInvoicePDF() {
        return invoicePDF;
    }

    public void setInvoicePDF(Boolean invoicePDF) {
        this.invoicePDF = invoicePDF;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber=" + invoiceNumber +
                ", dateInvoice=" + dateInvoice +
                ", totalPrice=" + totalPrice +
                ", dniElderly='" + dniElderly + '\'' +
                ", invoicePDF=" + invoicePDF +
                '}';
    }

    @Override
    public int compareTo(Invoice otro) {
        Boolean after = this.getDateInvoice().isBefore(otro.getDateInvoice());
        if (after) {
            return 1;
        }
        return -1;
    }
}