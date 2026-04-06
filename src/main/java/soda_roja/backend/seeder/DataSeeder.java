package soda_roja.backend.seeder;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;


@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ZonaRepository zonaRepository;
    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private UsuarioRepository  usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoZonaRepository productoZonaRepository;
    @Autowired
    private PersonaDomicilioRepository personaDomicilioRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private GastoRepository gastoRepository;
    @Autowired
    private CargaProductoRepository cargaProductoRepository;
    @Autowired
    private CargaRepository cargaRepository;
    @Autowired
    private PagoRepository pagoRepository;


    private final Faker faker = new Faker(new Locale("es")); // datos en español

    @Override
    public void run(String... args) throws Exception {
        if (productoRepository.count() > 0) {
            return; // Si ya hay datos, no hacer nada
        }
        List<Producto> productos = new ArrayList<>();
        Producto producto1 = new Producto();
        producto1.setNombre("Soda");
        producto1.setDetalle("Agua carbonatada con sabor a limón");
        producto1.setPrecio(1500);
        producto1.setStock(100);
        productos.add(producto1);
        Producto producto2 = new Producto();
        producto2.setNombre("Agua Mineral");
        producto2.setDetalle("Agua con gas sin sabor");
        producto2.setPrecio(1200);
        producto2.setStock(100);
        productos.add(producto2);
        Producto producto3 = new Producto();
        producto3.setNombre("Cajon de soda");
        producto3.setDetalle("Caja con 6 botellas de soda");
        producto3.setPrecio(8000);
        producto3.setStock(100);
        productos.add(producto3);
        Producto producto4 = new Producto();
        producto4.setNombre("Cajon de agua mineral");
        producto4.setDetalle("Caja con 6 botellas de agua mineral");
        producto4.setPrecio(6000);
        producto4.setStock(100);
        productos.add(producto4);
            productoRepository.saveAll(productos);


        List<Zona> zonas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Zona zona = new Zona();
            zona.setNombre(faker.address().cityName());
            zonas.add(zona);

        }
        zonaRepository.saveAll(zonas);
        List<Domicilio> domicilios = new ArrayList<>();
        for (int i = 0; i < 100; i++) {

            Domicilio domicilio = new Domicilio();
            domicilio.setCalle(faker.address().streetName());
            domicilio.setNumero(faker.address().buildingNumber());
            domicilio.setCasa(faker.number().numberBetween(1, 100) + "A");
            domicilio.setZona(zonas.get(faker.number().numberBetween(0, zonas.size())));
            domicilios.add(domicilio);
        }
        domicilioRepository.saveAll(domicilios);
        List<Persona> personas = new ArrayList<>();
        for (int i = 0; i < 100; i++)  {
            Persona persona = new Persona();
            persona.setNombre(faker.name().firstName());
            persona.setApellido(faker.name().lastName());
            persona.setTipoDoc(faker.options().option("DNI", "Pasaporte", "Cédula"));
            persona.setNroDocumento(Integer.toString(faker.number().numberBetween(10000000, 99999999)));
            persona.setTelefono(faker.phoneNumber().cellPhone());
            persona.setEmail(faker.internet().emailAddress());
            persona.setDeuda(faker.number().numberBetween(0, 100000));
            personas.add(persona);
        }
        personaRepository.saveAll(personas);

        List<Usuario> usuarios = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(faker.name().username() + i);
            usuario.setContrasena(faker.internet().password(8, 16));
            usuario.setNivelAcceso("Usuario");
            usuario.setPersona(personas.get(i));
            usuarios.add(usuario);


        }
        for (int i = 60; i < 80; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(faker.name().username());
            usuario.setContrasena(faker.internet().password(8, 16));
            usuario.setNivelAcceso("Empleado");
            usuario.setPersona(personas.get(i));
            usuarios.add(usuario);
        }
        usuarioRepository.saveAll(usuarios);

        List<ProductoZona> productoZonas = new ArrayList<>();
        zonas.forEach( zona -> {
            productos.forEach( producto -> {
                ProductoZona productoZona = new ProductoZona();
                productoZona.setProducto(producto);
                productoZona.setZona(zona);
                productoZonas.add(productoZona);
            });
        });
        productoZonaRepository.saveAll(productoZonas);


        List<PersonaDomicilio> personaDomicilios = new ArrayList<>();

        personas.forEach(persona -> {
            PersonaDomicilio personaDomicilio = new PersonaDomicilio();
            personaDomicilio.setPersona(persona);
            personaDomicilio.setDomicilio(domicilios.get(faker.number().numberBetween(0, domicilios.size())));
            personaDomicilios.add(personaDomicilio);
        });
        personaDomicilioRepository.saveAll(personaDomicilios);


        List<LineaPedido> lineasPedido = new ArrayList<>();

        for (int i = 0; i < 150; i++) {
            LineaPedido lineaPedido = new LineaPedido();
            lineaPedido.setCantidad(faker.number().numberBetween(1, 10));
            lineaPedido.setSubtotal(faker.number().numberBetween(100, 1000));
            lineaPedido.setProductoZona(productoZonas.get(faker.number().numberBetween(0, productoZonas.size())));
            lineasPedido.add(lineaPedido);
        }

        List<Venta> ventas = new ArrayList<>();
        for (int i = 0; i < 150; i+=5){
                Venta venta = new Venta();
                venta.setTotal(faker.number().numberBetween(500, 5000));
                venta.setPagado(faker.bool().bool());
                venta.setFecha(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
                venta.setPersonaDomicilio(personaDomicilios.get(faker.number().numberBetween(0, personaDomicilios.size())));
                List<LineaPedido> lineaPedidoIds = new ArrayList<>();
                for (int k = 0; k < 5; k++) {
                    lineaPedidoIds.add(lineasPedido.get(i+k));
            }
                venta.setLineasPedido(lineaPedidoIds);
                ventas.add(venta);
        };

        ventaRepository.saveAll(ventas);

        List<Camion> camiones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Camion camion = new Camion();
            camion.setPatente(faker.bothify("??###??"));
            camion.setModelo(faker.name().name());
            camion.setMarca(faker.name().name());
            camion.setKilometraje(faker.number().numberBetween(0, 200000));
            List<PersonaDomicilio> personaDomicilioCamion =new ArrayList<>();
            for (int j=0;j<6;j++){
                personaDomicilioCamion.add(personaDomicilios.get(i+j));
            }
            camiones.add(camion);
        }
        camionRepository.saveAll(camiones);

        List<Gasto> gastos = new ArrayList<>();
        camiones.forEach(camion -> {
            Gasto gasto = new Gasto();
            gasto.setMonto(faker.number().numberBetween(1000, 10000));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = sdf.format(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
            gasto.setFecha(fecha);
            gasto.setDetalle(faker.lorem().sentence());
            gasto.setCamion(camion);
            gastos.add(gasto);
        });
        gastoRepository.saveAll(gastos);

        List<Pago> pagos = new ArrayList<>();

        for(int i =0 ; i<60;i++) {
            for (int j = 0; j< faker.number().numberBetween(0, 5); j++) {
                Pago pago = new Pago();

                pago.setMonto(faker.number().numberBetween(100, 1000));
                pago.setFecha(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
                pago.setMetodoPago(faker.options().option("Efectivo", "Tarjeta de crédito", "Transferencia bancaria"));
                pago.setPersona(personas.get(i));
                pagos.add(pago);
            }
        }
        pagoRepository.saveAll(pagos);

        List<Carga> cargas = new ArrayList<>();
        for(int x = 0; x<camiones.size();x++){
            Carga carga = new Carga();
            for ( int j =0 ;j<15;j++){
                carga.setCamion(camiones.get(x));
                carga.setTipo(faker.options().option( "Descarga", "Carga"));
                carga.setUsuario(usuarios.get(60+x));
                carga.setFechaHora(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
            cargas.add(carga);
            }
        }
        cargaRepository.saveAll(cargas);

        List<CargaProducto> cargaProductos = new ArrayList<>();
        cargas.forEach(carga -> {
            CargaProducto cargaProducto = new CargaProducto();
            cargaProducto.setCarga(carga);
            cargaProducto.setProducto(productos.get(faker.number().numberBetween(0, productos.size())));
            cargaProducto.setCantVacio(faker.number().numberBetween(1, 20));
            cargaProducto.setCantLleno(faker.number().numberBetween(1, 20));
            cargaProductos.add(cargaProducto);
        });
        cargaProductoRepository.saveAll(cargaProductos);


    }
}