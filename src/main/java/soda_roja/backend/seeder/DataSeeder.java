package soda_roja.backend.seeder;

import net.datafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

	@Autowired
	private ZonaRepository zonaRepository;
	@Autowired
	private DomicilioRepository domicilioRepository;
	@Autowired
	private PersonaRepository personaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private ProductoRepository productoRepository;
	@Autowired
	private ProductoZonaRepository productoZonaRepository;
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
	@Autowired
	private PedidoSemanalRepository pedidoSemanalRepository;
	@Autowired
	private DiaRepository diaRepository;
	@Autowired
	private DiaDomicilioRepository diaDomicilioRepository;
	@Autowired
	private DiaZonaRepository diaZonaRepository;
	@Autowired
	private DiaZonaOrdenRepository diaZonaOrdenRepository;

	private List<Persona> personas = new ArrayList<>();
	private List<Usuario> usuarios = new ArrayList<>();

	private final Faker faker = new Faker(new Locale("es"));
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void run(String... args) throws Exception {
	    if (productoRepository.count() > 0) {
	        return;
	    }
//	    List<Producto> productos = seedProductos();
//	    List<Camion> camiones = seedCamiones();
//	    List<Zona> zonas = seedZonas(camiones);
	    seedPersonaYUsuario();
	    List<Dia> dias = seedDias();
//	    List<Domicilio> domicilios = seedDomicilios(zonas, personas);
//	    List<DiaZona> diasZona = seedDiasZona(zonas, dias);
//	    List<DiaDomicilio> diasDomicilio = seedDiasDomicilio(domicilios, dias,diasZona);
//	    List<ProductoZona> productoZonas = seedProductoZonas(zonas, productos);
//	    List<ProductoDomicilio> productosDomicilio = seedProductosDomicilio(domicilios, productos);
//	    List<PedidoSemanal> pedidosSemanales = seedPedidosSemanales(domicilios, productoZonas);
//	    List<DiaZonaOrden> diaZonaOrdenes = seedDiaZonaOrdenes(diasZona, diasDomicilio, domicilios);
//	    List<Venta> ventas = seedVentas(domicilios, productoZonas);
//	    List<Gasto> gastos = seedGastos(camiones);
//	    List<Pago> pagos = seedPagos();
//	    List<Carga> cargas = seedCargas(camiones, productos);
	}

	private List<Producto> seedProductos() {
		List<Producto> productos = new ArrayList<>();
		Producto producto1 = new Producto();
		producto1.setNombre("Soda");
		producto1.setDetalle("Agua carbonatada con sabor a limón");
		producto1.setPrecio(1000.0);
		producto1.setStock(100);
		producto1.setImagenUrl("./../../assets/producto.jpeg");
		producto1.setActivo(true);
		productos.add(producto1);
		Producto producto2 = new Producto();
		producto2.setNombre("Agua Mineral");
		producto2.setDetalle("Agua con gas sin sabor");
		producto2.setPrecio(1200.0);
		producto2.setStock(100);
		producto2.setImagenUrl("./../../assets/producto.jpeg");
		producto2.setActivo(true);
		productos.add(producto2);
		Producto producto3 = new Producto();
		producto3.setNombre("Cajon de soda");
		producto3.setDetalle("Caja con 6 botellas de soda");
		producto3.setPrecio(8000.0);
		producto3.setStock(100);
		producto3.setImagenUrl("./../../assets/producto.jpeg");
		producto3.setActivo(true);
		productos.add(producto3);
		Producto producto4 = new Producto();
		producto4.setNombre("Cajon de agua mineral");
		producto4.setDetalle("Caja con 6 botellas de agua mineral");
		producto4.setPrecio(6000.0);
		producto4.setStock(100);
		producto4.setImagenUrl("./../../assets/producto.jpeg");
		producto4.setActivo(true);
		productos.add(producto4);
		for (int i = 0; i < 100; i++) {
			Producto producto = new Producto();
			producto.setNombre(faker.commerce().productName() + i);
			producto.setDetalle(faker.lorem().sentence());
			producto.setPrecio(Double.valueOf(faker.number().numberBetween(500, 5000)));
			producto.setStock(faker.number().numberBetween(50, 200));
			producto.setImagenUrl("./../../assets/producto.jpeg");
			producto.setActivo(faker.bool().bool());
			productos.add(producto);
		}
		productoRepository.saveAll(productos);
		return productos;
	}

	private List<Camion> seedCamiones() {
		List<Camion> camiones = new ArrayList<>();
		for (int i = 0; i < 250; i += 25) {
			Camion camion = new Camion();
			camion.setPatente(faker.bothify("??###??").toUpperCase());
			camion.setModelo(faker.name().name());
			camion.setMarca(faker.name().name());
			camion.setEstado(faker.bool().bool());
			camion.setKilometraje(faker.number().numberBetween(0, 200000));
			camiones.add(camion);
		}
		camionRepository.saveAll(camiones);
		return camiones;
	}

	private List<Zona> seedZonas(List<Camion> camiones) {
	    List<Zona> zonas = new ArrayList<>();
	    int zonasPorCamion = 2; // Cada camión tendrá 2 zonas
	    
	    for (int i = 0; i < 6; i++) {
	        Zona zona = new Zona();
	        zona.setNombre(faker.address().cityName() + i);
	        zona.setDetalle(faker.address().streetAddress());
	        // Asigna el camión de forma cíclica para distribuir zonas entre camiones
	        zona.setCamion(camiones.get((i / zonasPorCamion) % camiones.size()));
	        zona.setDiasZona(new ArrayList<>());
	        zona.setDomicilios(new ArrayList<>());
	        zonas.add(zona);
	    }
	    zonaRepository.saveAll(zonas);
	    return zonas;
	}

	private void seedPersonaYUsuario() {
		List<Persona> personasList = new ArrayList<>();
		List<Usuario> usuariosList = new ArrayList<>();

//		for (int i = 0; i < 60; i++) {
//			Persona persona = new Persona();
//			persona.setNombre(faker.name().firstName());
//			persona.setApellido(faker.name().lastName());
//			persona.setTipoDoc(faker.options().option("DNI", "Pasaporte", "Cédula"));
//			persona.setNroDocumento(Integer.toString(faker.number().numberBetween(10000000, 99999999) + i));
//			persona.setTelefono(faker.phoneNumber().cellPhone());
//			String email = faker.internet().emailAddress() + i;
//			persona.setEmail(email);
//			persona.setSaldo(faker.number().numberBetween(-2000, 100000));
//			persona.setEstado(faker.options().option("Habilitado", "Deshabilitado"));
//			persona.setDomicilios(new ArrayList<>());
//			personasList.add(persona);
//
//			Usuario usuario = new Usuario();
//			usuario.setNombreUsuario(faker.name().username().replace(".", "_") + i);
//			usuario.setContrasena(passwordEncoder.encode("123456"));
//			usuario.setNivelAcceso("Usuario");
//			usuario.setEmail(email);
//			usuario.setPersona(persona);
//			usuariosList.add(usuario);
//		}
//
//		for (int i = 0; i < 20; i++) {
//			Persona persona = new Persona();
//			persona.setNombre(faker.name().firstName());
//			persona.setApellido(faker.name().lastName());
//			persona.setTipoDoc(faker.options().option("DNI", "Pasaporte", "Cédula"));
//			persona.setNroDocumento(Integer.toString(faker.number().numberBetween(10000000, 99999999) + 100 + i));
//			persona.setTelefono(faker.phoneNumber().cellPhone());
//			String email = faker.internet().emailAddress() + "emp" + i;
//			persona.setEmail(email);
//			persona.setSaldo(faker.number().numberBetween(-2000, 100000));
//			persona.setDomicilios(new ArrayList<>());
//			personasList.add(persona);
//			Usuario usuario = new Usuario();
//			usuario.setNombreUsuario(faker.name().username().replace(".", "_") + "emp" + i);
//			usuario.setContrasena(passwordEncoder.encode("123456"));
//			usuario.setNivelAcceso("Empleado");
//			usuario.setEmail(email);
//			usuario.setPersona(persona);
//			usuariosList.add(usuario);
//		}

		Persona adminPersona = new Persona();
		adminPersona.setNombre("Admin");
		adminPersona.setApellido("Principal");
		adminPersona.setTipoDoc("DNI");
		adminPersona.setNroDocumento("99999999");
		adminPersona.setTelefono("123456789");
		String adminEmail = "admin@sodaroja.com";
		adminPersona.setEmail(adminEmail);
		adminPersona.setSaldo(0);
		adminPersona.setEstado("Habilitado");
		adminPersona.setDomicilios(new ArrayList<>());
		personasList.add(adminPersona);

		Usuario adminUsuario = new Usuario();
		adminUsuario.setNombreUsuario("admin");
		adminUsuario.setContrasena(passwordEncoder.encode("123456"));
		adminUsuario.setNivelAcceso("Administrador");
		adminUsuario.setEmail(adminEmail);
		adminUsuario.setPersona(adminPersona);
		usuariosList.add(adminUsuario);

		personaRepository.saveAll(personasList);
		usuarioRepository.saveAll(usuariosList);

		this.personas = personasList;
		this.usuarios = usuariosList;
	}

	private List<Dia> seedDias() {
		List<Dia> dias = new ArrayList<>();
		String[] nombresDias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
		for (int i = 0; i < 7; i++) {
			Dia dia = new Dia();
			dia.setNombre(nombresDias[i]);
			dias.add(dia);
		}
		diaRepository.saveAll(dias);
		return dias;
	}

	private List<Domicilio> seedDomicilios(List<Zona> zonas, List<Persona> personas) {
	    List<Domicilio> domicilios = new ArrayList<>();
	    personas.forEach(persona -> {
	        if (persona.getId() < 60) {
	            for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
	                Domicilio domicilio = new Domicilio();
	                Zona zona = zonas.get(faker.number().numberBetween(0, zonas.size()));
	                domicilio.setCalle(faker.address().streetName());
	                domicilio.setNumero(String.valueOf(faker.number().numberBetween(1, 1000)));
	                domicilio.setCasa(faker.number().numberBetween(1, 100) + "A");
	                domicilio.setZona(zona);
	                domicilio.setProductoDomicilio(new ArrayList<>());
	                domicilio.setDiasDomicilio(new ArrayList<>());
	                domicilio.setPersona(persona);
					domicilio.setHabilitado(faker.options().option("Habilitado", "Pendiente","Deshabilitado"));
	                domicilio.setActivo(faker.options().option("Inactiva", "Activa"));
	                domicilios.add(domicilio);
	            }
	        } else {
	            Domicilio domicilio = new Domicilio();
	            Zona zona = zonas.get(faker.number().numberBetween(0, zonas.size()));
	            domicilio.setCalle(faker.address().streetName());
	            domicilio.setNumero(String.valueOf(faker.number().numberBetween(1, 1000)));
				domicilio.setHabilitado("Habilitado");

				domicilio.setCasa(faker.number().numberBetween(1, 100) + "A");
	            domicilio.setZona(zona);
	            domicilio.setProductoDomicilio(new ArrayList<>());
	            domicilio.setDiasDomicilio(new ArrayList<>());
	            domicilio.setPersona(persona);
	            domicilio.setActivo("Activa");
	            domicilios.add(domicilio);
	        }
	    });
	    domicilioRepository.saveAll(domicilios);
	    return domicilios;
	}


	private List<ProductoZona> seedProductoZonas(List<Zona> zonas, List<Producto> productos) {
		List<ProductoZona> productoZonas = new ArrayList<>();
		zonas.forEach(zona -> {
			productos.forEach(producto -> {
				ProductoZona productoZona = new ProductoZona();
				productoZona.setProducto(producto);
				productoZona.setZona(zona);
				productoZonas.add(productoZona);
			});
		});
		productoZonaRepository.saveAll(productoZonas);
		return productoZonas;
	}

	private List<ProductoDomicilio> seedProductosDomicilio(List<Domicilio> domicilios, List<Producto> productos) {
		List<ProductoDomicilio> productosDomicilio = new ArrayList<>();
		domicilios.forEach(domicilio -> {
			if (domicilio.getProductoDomicilio() != null) {
				for (int i = 0; i < faker.number().numberBetween(1, 5); i++) {
					ProductoDomicilio productoDomicilio = new ProductoDomicilio();
					productoDomicilio.setDomicilio(domicilio);
					productoDomicilio.setProducto(productos.get(faker.number().numberBetween(0, productos.size())));
					productoDomicilio.setCantVaciosActuales(faker.number().numberBetween(1, 10));
					productosDomicilio.add(productoDomicilio);
					domicilio.getProductoDomicilio().add(productoDomicilio);
				}
				domicilioRepository.save(domicilio);
			}
		});
		return productosDomicilio;
	}

	private List<PedidoSemanal> seedPedidosSemanales(List<Domicilio> domicilios, List<ProductoZona> productoZonas) {
		List<PedidoSemanal> pedidosSemanales = new ArrayList<>();
		for (int i = 0; i < domicilios.size(); i++) {
			for (int j = 0; j < faker.number().numberBetween(1, 5); j++) {
				PedidoSemanal pedidoSemanal = new PedidoSemanal();
				pedidoSemanal.setDomicilio(domicilios.get(i));
				pedidoSemanal.setProductoZona(productoZonas.get(faker.number().numberBetween(0, productoZonas.size())));
				pedidoSemanal.setCantidad(faker.number().numberBetween(1, 10));
				pedidosSemanales.add(pedidoSemanal);
			}
		}
		pedidoSemanalRepository.saveAll(pedidosSemanales);
		return pedidosSemanales;
	}


	private List<Venta> seedVentas(List<Domicilio> domicilios, List<ProductoZona> productoZonas) {
		List<Venta> ventas = new ArrayList<>();
		for (int i = 0; i < 1000; i += 5) {
			Venta venta = new Venta();
			venta.setTotal((double) faker.number().numberBetween(500, 5000));
			venta.setFecha(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
			venta.setDomicilio(domicilios.get(faker.number().numberBetween(0, domicilios.size())));
			venta.setEstado(faker.options().option("Pendiente", "En proceso", "Completada", "Cancelada"));
			List<LineaPedido> ventaLineas = new ArrayList<>();
			for (int j = 0; j < 5; j++) {
				LineaPedido lp = new LineaPedido();
				lp.setCantidad(faker.number().numberBetween(1, 10));
				lp.setSubtotal(faker.number().numberBetween(100, 1000));
				lp.setProductoZona(productoZonas.get(faker.number().numberBetween(0, productoZonas.size())));
				lp.setVenta(venta);
				ventaLineas.add(lp);
			}

			venta.setLineasPedido(ventaLineas);
			ventaRepository.save(venta);
			ventas.add(venta);
		}
		return ventas;
	}

	private List<Gasto> seedGastos(List<Camion> camiones) {
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
		return gastos;
	}

	private List<Pago> seedPagos() {
		List<Pago> pagos = new ArrayList<>();

		for (int i = 0; i < 60; i++) {
			for (int j = 0; j < faker.number().numberBetween(0, 5); j++) {
				Pago pago = new Pago();

				pago.setMonto(faker.number().numberBetween(100, 1000));
				pago.setEstado(faker.options().option("Pendiente", "Aprobado", "Rechazado"));
				pago.setFecha(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS));
				pago.setMetodoPago(faker.options().option("Efectivo", "Tarjeta de crédito", "Transferencia bancaria"));
				pago.setPersona(personas.get(i));
				pagos.add(pago);
			}
		}
		pagoRepository.saveAll(pagos);
		return pagos;
	}

	private List<Carga> seedCargas(List<Camion> camiones, List<Producto> productos) {
		List<Carga> cargas = new ArrayList<>();
		List<CargaProducto> cargaProductos = new ArrayList<>();
		List<Usuario> usuariosEmpleados = usuarios.subList(60, 80);
		for (int x = 0; x < camiones.size(); x++) {
			Usuario usuarioAsignado = usuariosEmpleados.get(x % usuariosEmpleados.size());
			for (int j = 0; j < 15; j++) {
				Carga cargaCarga = new Carga();
				cargaCarga.setCamion(camiones.get(x));
				cargaCarga.setTipo("Carga");
				cargaCarga.setUsuario(usuarioAsignado);
				java.util.Date fechaCarga = faker.date().past(30, java.util.concurrent.TimeUnit.DAYS);
				cargaCarga.setFechaHora(fechaCarga);
				cargas.add(cargaCarga);
				CargaProducto cargaProducto = new CargaProducto();
				cargaProducto.setCarga(cargaCarga);
				cargaProducto.setProducto(productos.get(faker.number().numberBetween(0, productos.size())));
				cargaProducto.setCantVacio(0);
				cargaProducto.setCantLleno(faker.number().numberBetween(1, 20));
				cargaProductos.add(cargaProducto);

				Carga descargaCarga = new Carga();
				descargaCarga.setCamion(camiones.get(x));
				descargaCarga.setTipo("Descarga");
				descargaCarga.setUsuario(usuarios.get(60 + x));
				java.util.Calendar calendar = java.util.Calendar.getInstance();
				calendar.setTime(fechaCarga);
				calendar.add(java.util.Calendar.HOUR_OF_DAY, faker.number().numberBetween(2, 8));
				descargaCarga.setFechaHora(calendar.getTime());

				CargaProducto cargaProducto2 = new CargaProducto();
				cargaProducto2.setCarga(descargaCarga);
				cargaProducto2.setProducto(productos.get(faker.number().numberBetween(0, productos.size())));
				cargaProducto2.setCantVacio(faker.number().numberBetween(1, 20));
				cargaProducto2.setCantLleno(faker.number().numberBetween(1, 20));
				cargaProductos.add(cargaProducto2);

				cargas.add(descargaCarga);
		}
	}
	cargaRepository.saveAll(cargas);
	cargaProductoRepository.saveAll(cargaProductos);
	return cargas;
	}

	private List<DiaZona> seedDiasZona(List<Zona> zonas, List<Dia> dias) {
	    List<DiaZona> diasZona = new ArrayList<>();
	    
	    // Agrupar zonas por camión
	    Map<Camion, List<Zona>> zonasPorCamion = zonas.stream()
	            .collect(Collectors.groupingBy(Zona::getCamion));
	    
	    // Patrones de días para cada camión
	    boolean[][] patronesDias = {
	        {false, true, false, true, false, true, false},   // Martes y Jueves
	        {true, false, true, false, true, true, false},    // Lunes, Miércoles y Viernes
	        {false, false, false, false, false, true, true},  // Domingo
	        {false, true, false, true, false, true, false},   // Martes y Jueves
	        {true, false, true, false, true, true, false},    // Lunes, Miércoles y Viernes
	        {false, false, false, false, false, true, true}   // Domingo
	    };
	    
	    int camionIndex = 0;
	    for (Camion camion : zonasPorCamion.keySet()) {
	        List<Zona> zonasDelCamion = zonasPorCamion.get(camion);
	        boolean[] patron = patronesDias[camionIndex % patronesDias.length];
	        
	        // Todos las zonas del mismo camión se asignan a los mismos días
	        for (int i = 0; i < 7; i++) {
	            if (patron[i]) {
	                for (Zona zona : zonasDelCamion) {
	                    DiaZona diaZona = DiaZona.builder()
	                            .dia(dias.get(i))
	                            .zona(zona)
	                            .build();
	                    diasZona.add(diaZona);
	                }
	            }
	        }
	        camionIndex++;
	    }
	    
	    diaZonaRepository.saveAll(diasZona);
	    return diasZona;
	}

	private List<DiaDomicilio> seedDiasDomicilio(List<Domicilio> domicilios, List<Dia> dias, List<DiaZona> diasZona) {
	    List<DiaDomicilio> diasDomicilio = new ArrayList<>();

	    domicilios.forEach(domicilio -> {
	        Zona zona = domicilio.getZona();

	        List<DiaZona> diasZonaPorZona = diasZona.stream()
	            .filter(dz -> dz.getZona().getId().equals(zona.getId()))
	            .toList();

	        List<Long> diasActivosIds = diasZonaPorZona.stream()
	            .map(dz -> dz.getDia().getId())
	            .toList();

	        dias.forEach(dia -> {
	            String estado = diasActivosIds.contains(dia.getId()) ? "ACTIVO" : "NODISPONIBLE";
	            DiaDomicilio diaDomicilio = DiaDomicilio.builder()
	                .dia(dia)
	                .domicilio(domicilio)
	                .estado(estado)
	                .build();
	            diasDomicilio.add(diaDomicilio);
	        });
	    });

	    diaDomicilioRepository.saveAll(diasDomicilio);
	    return diasDomicilio;
	}
	private List<DiaZonaOrden> seedDiaZonaOrdenes(List<DiaZona> diasZona, List<DiaDomicilio> diasDomicilio, List<Domicilio> domicilios) {
	    List<DiaZonaOrden> diaZonaOrdenes = new ArrayList<>();
	    
	    diasZona.forEach(diaZona -> {
	        Zona zona = diaZona.getZona();
	        Dia dia = diaZona.getDia();
	        int orden = 1;
	        
	        // Obtener domicilios de esta zona que están activos ese día
	        List<Domicilio> domiciliosPorZona = domicilios.stream()
	                .filter(d -> d.getZona().getId().equals(zona.getId()))
	                .filter(d -> "Habilitado".equals(d.getHabilitado()))
	                .filter(d -> "Activa".equals(d.getActivo()))
	                .filter(d -> diasDomicilio.stream()
	                        .anyMatch(dd -> dd.getDomicilio().getId().equals(d.getId())
	                                && dd.getDia().getId().equals(dia.getId())
	                                && "ACTIVO".equals(dd.getEstado()))
					)
	                .toList();
	        
	        for (Domicilio domicilio : domiciliosPorZona) {
	            DiaZonaOrden diaZonaOrden = DiaZonaOrden.builder()
	                    .diaZona(diaZona)
	                    .domicilio(domicilio)
	                    .orden(orden++)
	                    .build();
	            diaZonaOrdenes.add(diaZonaOrden);
	        }
	    });
	    
	    diaZonaOrdenRepository.saveAll(diaZonaOrdenes);
	    return diaZonaOrdenes;
	}

}
