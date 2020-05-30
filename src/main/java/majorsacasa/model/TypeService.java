package majorsacasa.model;

public class TypeService {
    Integer idType;
    String serviceType;

    public TypeService() {
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String toString() {
        return "TypeService{" +
                "idType=" + idType +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}
