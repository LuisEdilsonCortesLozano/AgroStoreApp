package LuisCortes.AgroStore.Model;

public class Comment {
    private String uid;
    private String product;
    private String Descripcion;
    private String NombreUser;

    public Comment() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getNombreUser() {
        return NombreUser;
    }

    public void setNombreUser(String nombreUser) {
        NombreUser = nombreUser;
    }

    @Override
    public String toString() {
        return "Usuario: " + NombreUser + '\n' +
                "Comentario: " + Descripcion + '\n';
    }
}
