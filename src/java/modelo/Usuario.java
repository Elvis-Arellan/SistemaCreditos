package modelo;

public class Usuario {
    private int id;
    private String username;
    private String clave;
    private String nombre_completo;
    private String rol;

    public Usuario() {
    }

    public Usuario(int id, String username, String clave, String nombre_completo, String rol) {
        this.id = id;
        this.username = username;
        this.clave = clave;
        this.nombre_completo = nombre_completo;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", username=" + username + ", clave=" + clave + ", nombre_completo=" + nombre_completo + ", rol=" + rol + '}';
    }

   
    
    
}
