package amst4.t12020.espol.clasificadordepesos.ui.home;

public class objeto {
    String id;
    String Categoria;
    String fechaRegistro;
    Float peso;
    String aciertos;

    public objeto() {
    }

    public objeto(String id, String categoria, String fechaRegistro, Float peso, String aciertos) {
        this.id = id;
        Categoria = categoria;
        this.fechaRegistro = fechaRegistro;
        this.peso = peso;
        this.aciertos = aciertos;
    }

    public String getId() {
        return id;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public Float getPeso() {
        return peso;
    }

    public String getAciertos() {
        return aciertos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public void setAciertos(String aciertos) {
        this.aciertos = aciertos;
    }
}
