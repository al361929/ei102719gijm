package majorsacasa.model;

public class Service implements Comparable<Service> {

    Integer idService;
    String serviceType;
    Integer price;
    String description;

    public Service() {

    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public String getInfo() {
        return description + " Precio: " + price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", serviceType='" + serviceType + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Service otro) {
        return this.getDescription().compareTo(otro.getDescription());
    }
}
