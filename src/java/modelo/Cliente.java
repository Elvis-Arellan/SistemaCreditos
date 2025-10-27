package modelo;

import java.math.BigDecimal;
import java.sql.Date;

public class Cliente {
    private int id_cliente;
    private int id_usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private BigDecimal saldo; 
    private Date fecha_registro;
    private String estado;

    public Cliente() {
    }

    public Cliente(int id_cliente, int id_usuario, String nombre, String apellido, String email, String telefono, BigDecimal saldo, Date fecha_registro, String estado) {
        this.id_cliente = id_cliente;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.saldo = saldo;
        this.fecha_registro = fecha_registro;
        this.estado = estado;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id_cliente=" + id_cliente + ", id_usuario=" + id_usuario + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", telefono=" + telefono + ", saldo=" + saldo + ", fecha_registro=" + fecha_registro + ", estado=" + estado + '}';
    }


    
}
