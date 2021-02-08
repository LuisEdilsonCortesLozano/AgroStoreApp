package LuisCortes.AgroStore.Model;

public class BuyProduct {
    private String CompraId;
    private String CompraCantidad;
    private String CompraObservacion;
    private String Compradoruid;
    private String CompradorNombre;
    private String CompradorNumero;
    private String CompradorCiudad;
    private String CompradorDireccion;
    private String Publicadoruid;
    private String PublicadorNombre;
    private String PublicadorNumero;
    private String PublicadorCiudad;
    private String PublicadorDireccion;
    private String Productouid;
    private String ProductoDescripcion;
    private String ProductoUnidad;
    private String ProductoPrecio;

    public BuyProduct() {
    }

    public String getCompraId() {
        return CompraId;
    }

    public void setCompraId(String compraId) {
        CompraId = compraId;
    }

    public String getCompraCantidad() {
        return CompraCantidad;
    }

    public void setCompraCantidad(String compraCantidad) {
        CompraCantidad = compraCantidad;
    }

    public String getCompraObservacion() {
        return CompraObservacion;
    }

    public void setCompraObservacion(String compraObservacion) {
        CompraObservacion = compraObservacion;
    }

    public String getCompradoruid() {
        return Compradoruid;
    }

    public void setCompradoruid(String compradoruid) {
        Compradoruid = compradoruid;
    }

    public String getCompradorNombre() {
        return CompradorNombre;
    }

    public void setCompradorNombre(String compradorNombre) {
        CompradorNombre = compradorNombre;
    }

    public String getCompradorNumero() {
        return CompradorNumero;
    }

    public void setCompradorNumero(String compradorNumero) {
        CompradorNumero = compradorNumero;
    }

    public String getCompradorCiudad() {
        return CompradorCiudad;
    }

    public void setCompradorCiudad(String compradorCiudad) {
        CompradorCiudad = compradorCiudad;
    }

    public String getCompradorDireccion() {
        return CompradorDireccion;
    }

    public void setCompradorDireccion(String compradorDireccion) {
        CompradorDireccion = compradorDireccion;
    }

    public String getPublicadoruid() {
        return Publicadoruid;
    }

    public void setPublicadoruid(String publicadoruid) {
        Publicadoruid = publicadoruid;
    }

    public String getPublicadorNombre() {
        return PublicadorNombre;
    }

    public void setPublicadorNombre(String publicadorNombre) {
        PublicadorNombre = publicadorNombre;
    }

    public String getPublicadorNumero() {
        return PublicadorNumero;
    }

    public void setPublicadorNumero(String publicadorNumero) {
        PublicadorNumero = publicadorNumero;
    }

    public String getPublicadorCiudad() {
        return PublicadorCiudad;
    }

    public void setPublicadorCiudad(String publicadorCiudad) {
        PublicadorCiudad = publicadorCiudad;
    }

    public String getPublicadorDireccion() {
        return PublicadorDireccion;
    }

    public void setPublicadorDireccion(String publicadorDireccion) {
        PublicadorDireccion = publicadorDireccion;
    }

    public String getProductouid() {
        return Productouid;
    }

    public void setProductouid(String productouid) {
        Productouid = productouid;
    }

    public String getProductoDescripcion() {
        return ProductoDescripcion;
    }

    public void setProductoDescripcion(String productoDescripcion) {
        ProductoDescripcion = productoDescripcion;
    }

    public String getProductoUnidad() {
        return ProductoUnidad;
    }

    public void setProductoUnidad(String productoUnidad) {
        ProductoUnidad = productoUnidad;
    }

    public String getProductoPrecio() {
        return ProductoPrecio;
    }

    public void setProductoPrecio(String productoPrecio) {
        ProductoPrecio = productoPrecio;
    }

    public String toString() {
        return "                                   Detalle: " + '\n' +
                "Producto: " + ProductoDescripcion + '\n' +
                "Presentación: " + ProductoUnidad + '\n' +
                "Precio: " + ProductoPrecio + '\n' +
                "Cantidad Comprada: " + CompraCantidad + '\n' +
                "Observación: " + CompraObservacion + '\n' +
                "                               Vendedor: " + '\n' +
                "Nombre: " + PublicadorNombre + '\n' +
                "Número: " + PublicadorNumero + '\n' +
                "Ciudad: " + PublicadorCiudad + '\n' +
                "Dirección: " + PublicadorDireccion + '\n' +
                "                             Comprador: " + '\n' +
                "Nombre: " + CompradorNombre + '\n' +
                "Número: " + CompradorNumero + '\n' +
                "Ciudad: " + CompradorCiudad + '\n' +
                "Dirección: " + CompradorDireccion + '\n' +
                "" + '\n' ;
    }
}


