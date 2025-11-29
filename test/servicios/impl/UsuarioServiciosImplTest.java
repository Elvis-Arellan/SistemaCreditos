package servicios.impl;

import modelo.Usuario;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class UsuarioServiciosImplTest {
    
    private UsuarioServiciosImpl servicio;
    
    @Before
    public void setUp() {
        servicio = new UsuarioServiciosImpl();
        System.out.println("\n========================================");
    }
    

    @Test
    public void testLoginExitoso() {
        System.out.println("=== TEST 1: Login Exitoso ===");
        
        Usuario resultado = servicio.login("hakimi", "1234");
        

        assertNotNull("ERROR: El usuario no debería ser nulo. Verifica que 'hakimi' existe en BD.", resultado);
        assertEquals("ERROR: Username esperado 'hakimi'", "hakimi", resultado.getUsername());
        assertEquals("ERROR: Rol esperado 'USUARIO'", "USUARIO", resultado.getRol());
        assertEquals("ERROR: Nombre esperado 'Hakimi Psg'", "Hakimi Psg", resultado.getNombre_completo());
        
        System.out.println("✅ APROBADO - Usuario: " + resultado.getUsername() + " | Rol: " + resultado.getRol());
    }
    

    @Test
    public void testLoginFallido() {
        System.out.println("=== TEST 2: Login Fallido ===");
        
        Usuario resultado = servicio.login("hakimi", "incorrecta999");
        

        assertNull("ERROR: El usuario debería ser nulo con contraseña incorrecta", resultado);
        
        System.out.println("✅ APROBADO - Login rechazado correctamente");
    }
}