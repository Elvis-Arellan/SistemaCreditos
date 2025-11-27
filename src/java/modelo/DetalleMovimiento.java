package modelo;

import java.math.BigDecimal;

public class DetalleMovimiento {
    private int id_detalle;
    private int id_movimiento;
    private String descripcion_producto;
    private BigDecimal cantidad;

    public DetalleMovimiento() {
    }

    public DetalleMovimiento(int id_detalle, int id_movimiento, String descripcion_producto, BigDecimal cantidad) {
        this.id_detalle = id_detalle;
        this.id_movimiento = id_movimiento;
        this.descripcion_producto = descripcion_producto;
        this.cantidad = cantidad;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public String getDescripcion_producto() {
        return descripcion_producto;
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetalleMovimiento{" + "id_detalle=" + id_detalle + ", id_movimiento=" + id_movimiento + ", descripcion_producto=" + descripcion_producto + ", cantidad=" + cantidad + '}';
    }


}