package servicios.impl;

import modelo.Cliente;
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Before;

public class ClienteServiciosImplTest {
    
    private ClienteServiciosImpl servicio;
    
    @Before
    public void setUp() {
        servicio = new ClienteServiciosImpl();
        System.out.println("\n========================================");
    }

    @Test
    public void testRegistrarClienteValido() {
        System.out.println("=== TEST 3: Registrar Cliente Válido ===");
        
 
        Cliente cliente = new Cliente();
        cliente.setId_usuario(2); 
        cliente.setNombre("JUNIT");
        cliente.setApellido("TEST");
        cliente.setEmail("junit" + System.currentTimeMillis() + "@test.com"); 
        cliente.setTelefono("987654321");
        cliente.setSaldo(BigDecimal.ZERO);
        cliente.setEstado("ACTIVO");
        
        System.out.println("Registrando cliente con email: " + cliente.getEmail());
        

        boolean resultado = servicio.registrarCliente(cliente);
        

        assertTrue("ERROR: El cliente debería registrarse correctamente", resultado);
        
        System.out.println("✅ APROBADO - Cliente registrado: " + cliente.getNombre() + " " + cliente.getApellido());
        

        if (resultado) {
            Cliente clienteInsertado = servicio.obtenerClientesPorUsuario(2)
                .stream()
                .filter(c -> c.getEmail().startsWith("junit"))
                .findFirst()
                .orElse(null);
            
            if (clienteInsertado != null) {
                servicio.eliminarCliente(clienteInsertado.getId_cliente());
                System.out.println("🗑️ Cliente de prueba eliminado (ID: " + clienteInsertado.getId_cliente() + ")");
            }
        }
    }
    

    @Test
    public void testValidacionClienteSinNombre() {
        System.out.println("=== TEST 4: Validación Cliente Sin Nombre ===");
        
        Cliente cliente = new Cliente();
        cliente.setId_usuario(2); 
        cliente.setNombre(""); 
        cliente.setApellido("TEST");
        cliente.setTelefono("987654321");
        

        boolean resultado = servicio.registrarCliente(cliente);
        

        assertFalse("ERROR: No debería permitir registrar cliente sin nombre", resultado);
        
        System.out.println("✅ APROBADO - Validación correcta: Cliente sin nombre fue rechazado");
    }
}