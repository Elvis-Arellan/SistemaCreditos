package servicios.impl;

import modelo.Movimiento;
import modelo.Cliente;
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.BeforeClass;

public class MovimientoServiciosImplTest {
    
    private MovimientoServiciosImpl movServicio;
    private ClienteServiciosImpl cliServicio;
    
    private static final int ID_CLIENTE_PRUEBA = 8;
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  PRUEBAS DE MOVIMIENTOS - INICIO      ║");
        System.out.println("║  Cliente: Paolo Guerrero (ID=8)       ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    @Before
    public void setUp() {
        movServicio = new MovimientoServiciosImpl();
        cliServicio = new ClienteServiciosImpl();
        System.out.println("\n========================================");
    }
    

    @Test
    public void testRegistrarFiadoActualizaSaldo() {
        System.out.println("=== TEST 5: Registrar FIADO ===");
        
        Cliente cliente = cliServicio.obtenerClientePorId(ID_CLIENTE_PRUEBA);
        
        assertNotNull("ERROR: No existe cliente con ID " + ID_CLIENTE_PRUEBA, cliente);
        
        BigDecimal saldoInicial = cliente.getSaldo();
        System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Saldo inicial: S/. " + saldoInicial);
        
        Movimiento mov = new Movimiento();
        mov.setId_cliente(ID_CLIENTE_PRUEBA);
        mov.setTipo_movimiento("FIADO");
        mov.setMonto(new BigDecimal("100.00"));
        mov.setDescripcion("TEST JUnit - FIADO de prueba");
        
        boolean registrado = movServicio.registrarMovimiento(mov);
        
        assertTrue("ERROR: El movimiento FIADO no se registró", registrado);
        
        Cliente clienteNuevo = cliServicio.obtenerClientePorId(ID_CLIENTE_PRUEBA);
        BigDecimal saldoFinal = clienteNuevo.getSaldo();
        System.out.println("Saldo final: S/. " + saldoFinal);
        
        BigDecimal saldoEsperado = saldoInicial.add(new BigDecimal("100.00"));
        
        assertEquals("ERROR: Saldo esperado S/. " + saldoEsperado + " pero fue S/. " + saldoFinal, 
                     0, saldoEsperado.compareTo(saldoFinal));
        
        System.out.println("✅ APROBADO - Saldo aumentó correctamente de S/. " + saldoInicial + " a S/. " + saldoFinal);
    }
    

    @Test
    public void testRegistrarPagoReduceSaldo() {
        System.out.println("=== TEST 6: Registrar PAGO ===");
        
        Cliente cliente = cliServicio.obtenerClientePorId(ID_CLIENTE_PRUEBA);
        
        assertNotNull("ERROR: No existe cliente con ID " + ID_CLIENTE_PRUEBA, cliente);
        
        BigDecimal saldoInicial = cliente.getSaldo();
        System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Saldo inicial: S/. " + saldoInicial);
        
        Movimiento mov = new Movimiento();
        mov.setId_cliente(ID_CLIENTE_PRUEBA);
        mov.setTipo_movimiento("PAGO");
        mov.setMonto(new BigDecimal("50.00"));
        mov.setDescripcion("TEST JUnit - PAGO de prueba");
        
        boolean registrado = movServicio.registrarMovimiento(mov);
        
        assertTrue("ERROR: El movimiento PAGO no se registró", registrado);
        
        Cliente clienteNuevo = cliServicio.obtenerClientePorId(ID_CLIENTE_PRUEBA);
        BigDecimal saldoFinal = clienteNuevo.getSaldo();
        System.out.println("Saldo final: S/. " + saldoFinal);
        
        BigDecimal saldoEsperado = saldoInicial.subtract(new BigDecimal("50.00"));
        
        assertEquals("ERROR: Saldo esperado S/. " + saldoEsperado + " pero fue S/. " + saldoFinal,
                     0, saldoEsperado.compareTo(saldoFinal));
        
        System.out.println("✅ APROBADO - Saldo redujo correctamente de S/. " + saldoInicial + " a S/. " + saldoFinal);
    }
}