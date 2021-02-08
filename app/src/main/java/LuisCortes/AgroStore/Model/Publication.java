package LuisCortes.AgroStore.Model;

public class Publication {
    private String uid;
    private String Descripcion;
    private String Unidad;
    private String Precio;
    private String User;

    public Publication() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String unidad) {
        Unidad = unidad;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    @Override
    public String toString() {
        return "Producto: " + Descripcion + '\n' +
                "Presentación: " + Unidad + '\n' +
                "Precio: " + Precio + '\n';
    }
}
