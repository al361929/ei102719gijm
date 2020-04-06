package majorsacasa.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Invoice {

    Integer invoiceNumber;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate dateInvoice;
    Integer totalPrice;
    String dniElderly;

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

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber=" + invoiceNumber +
                ", dateInvoice=" + dateInvoice +
                ", totalPrice=" + totalPrice +
                ", dniElderly='" + dniElderly + '\'' +
                '}';
    }
}
