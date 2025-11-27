package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Movimiento {
    private int id_movimiento;
    private int id_cliente;
    private String tipo_movimiento; 
    private BigDecimal monto;
    private String descripcion;
    private Timestamp fecha_movimiento;
    
    private String nombreCliente;
    private String apellidoCliente;

    public Movimiento() {
    }

    public Movimiento(int id_movimiento, int id_cliente, String tipo_movimiento, BigDecimal monto, String descripcion, Timestamp fecha_movimiento) {
        this.id_movimiento = id_movimiento;
        this.id_cliente = id_cliente;
        this.tipo_movimiento = tipo_movimiento;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha_movimiento = fecha_movimiento;
    }

    public int getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha_movimiento() {
        return fecha_movimiento;
    }

    public void setFecha_movimiento(Timestamp fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    @Override
    public String toString() {
        return "Movimiento{" + "id_movimiento=" + id_movimiento + ", id_cliente=" + id_cliente + ", tipo_movimiento=" + tipo_movimiento + ", monto=" + monto + ", descripcion=" + descripcion + ", fecha_movimiento=" + fecha_movimiento + ", nombreCliente=" + nombreCliente + ", apellidoCliente=" + apellidoCliente + '}';
    }

}