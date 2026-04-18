package soda_roja.backend.service;

import org.springframework.stereotype.Service;
import soda_roja.backend.dtoResponse.*;
import soda_roja.backend.model.*;
import java.util.List;

@Service
public class MapToDTO {

	public ZonaDTOResponse mapToDTO(Zona zona, String[] populate) {
		ZonaDTOResponse.ZonaDTOResponseBuilder builder = ZonaDTOResponse.builder().id(zona.getId()).dia(zona.getDia())
				.nombre(zona.getNombre()).detalle(zona.getDetalle());

		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZonas(
					zona.getProductosZona() != null
							? zona.getProductosZona().stream()
									.map(productoZona -> this.mapToDTO(productoZona, populate)).toList()
							: List.of());
		} else {
			builder.productoZonaIds(zona.getProductosZona() != null
					? zona.getProductosZona().stream().map(productoZona -> productoZona.getId()).toList()
					: List.of());
		}

		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilios(zona.getDomicilio() != null
					? zona.getDomicilio().stream().map(domicilio -> this.mapToDTO(domicilio, populate)).toList()
					: List.of());
		} else {
			builder.domicilioIds(zona.getDomicilio() != null
					? zona.getDomicilio().stream().map(domicilio -> domicilio.getId()).toList()
					: List.of());
		}
		if (populate != null && List.of(populate).contains("camion")) {
			builder.camion(zona.getCamion() != null ? this.mapToDTO(zona.getCamion(), populate) : null);
		} else {
			builder.camionId(zona.getCamion() != null ? zona.getCamion().getId() : null);
 		}
		return builder.build();
	}

	public VentaDTOResponse mapToDTO(Venta venta, String[] populate) {
		VentaDTOResponse.VentaDTOResponseBuilder builder = VentaDTOResponse.builder().id(venta.getId())
				.fecha(venta.getFecha()).total(venta.getTotal()).estado(venta.getEstado()).pagado(venta.isPagado())
				.lineasPedido(
						venta.getLineasPedido().stream().map(lineaPedido -> this.mapToDTO(lineaPedido, null)).toList());
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(venta.getDomicilio() != null ? this.mapToDTO(venta.getDomicilio(), populate) : null);
		} else {
			builder.idDomicilio(venta.getDomicilio().getId());
		}

		return builder.build();
	}

	public UsuarioDTOResponse mapToDTO(Usuario usuario, String[] populate) {
		UsuarioDTOResponse.UsuarioDTOResponseBuilder builder = UsuarioDTOResponse.builder().id(usuario.getId())
				.nombreUsuario(usuario.getNombreUsuario()).email(usuario.getEmail())
				.nivelAcceso(usuario.getNivelAcceso());
		if (populate != null && List.of(populate).contains("persona")) {
			builder.persona(this.mapToDTO(usuario.getPersona(), populate));
		} else {
			builder.personaId(usuario.getPersona() != null ? usuario.getPersona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("carga")) {
			builder.cargas(usuario.getCargas() != null
					? usuario.getCargas().stream().map(carga -> this.mapToDTO(carga, populate)).toList()
					: null);
		} else {
			builder.cargaIds(
					usuario.getCargas() != null ? usuario.getCargas().stream().map(carga -> carga.getId()).toList()
							: null);
		}
		return builder.build();
	}

	public ProductoZonaDTOResponse mapToDTO(ProductoZona productoZona, String[] populate) {
		ProductoZonaDTOResponse.ProductoZonaDTOResponseBuilder builder = ProductoZonaDTOResponse.builder()
				.id(productoZona.getId());
		if (populate != null && List.of(populate).contains("producto")) {
			builder.producto(
					productoZona.getProducto() != null ? this.mapToDTO(productoZona.getProducto(), populate) : null);
		} else {
			builder.productoId(productoZona.getProducto() != null ? productoZona.getProducto().getId() : null)
					.productoId(productoZona.getProducto() != null ? productoZona.getProducto().getId() : null);
		}
		if (populate != null && List.of(populate).contains("zona")) {
			builder.zona(productoZona.getZona() != null ? this.mapToDTO(productoZona.getZona(), populate) : null);
		} else {
			builder.zonaId(productoZona.getZona() != null ? productoZona.getZona().getId() : null)
					.zonaId(productoZona.getZona() != null ? productoZona.getZona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("lineaPedido")) {
			builder.lineaPedidos(productoZona.getLineaPedidos() != null ? productoZona.getLineaPedidos().stream()
					.map(lineaPedido -> this.mapToDTO(lineaPedido, populate)).toList() : null);
		} else {
			builder.lineaPedidosIds(productoZona.getLineaPedidos() != null
					? productoZona.getLineaPedidos().stream().map(lineaPedido -> lineaPedido.getId()).toList()
					: null);
		}
		if (populate != null && List.of(populate).contains("pedidoSemanal")) {
			builder.pedidoSemanal(productoZona.getPedidoSemanales() != null
					? productoZona.getPedidoSemanales().stream().map(p -> this.mapToDTO(p, populate)).toList()
					: null);
		} else {
			builder.pedidoSemanalIds(productoZona.getPedidoSemanales() != null
					? productoZona.getPedidoSemanales().stream().map(p -> p.getId()).toList()
					: null);
		}
		return builder.build();
	}

	public ProductoDTOResponse mapToDTO(Producto producto, String[] populate) {
		ProductoDTOResponse.ProductoDTOResponseBuilder builder = ProductoDTOResponse.builder().id(producto.getId())
				.nombre(producto.getNombre()).detalle(producto.getDetalle()).precio(producto.getPrecio())
				.stock(producto.getStock()).imagenUrl(producto.getImagenUrl()).activo(producto.isActivo());
		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZonas(
					producto.getProductosZona() != null
							? producto.getProductosZona().stream()
									.map(productoZona -> this.mapToDTO(productoZona, populate)).toList()
							: List.of());
		} else {
			builder.productoZonaIds(producto.getProductosZona() != null
					? producto.getProductosZona().stream().map(productoZona -> productoZona.getId()).toList()
					: List.of());
		}
		if (populate != null && List.of(populate).contains("productoDomicilio")) {
			builder.productosDomicilio(
					producto.getProductosDomicilio() != null
							? producto.getProductosDomicilio().stream()
									.map(productoDomicilio -> this.mapToDTO(productoDomicilio, populate)).toList()
							: List.of());
		}
		if (populate != null && List.of(populate).contains("cargaProducto")) {
			builder.cargasProducto(
					producto.getCargasProducto() != null
							? producto.getCargasProducto().stream()
									.map(cargaProducto -> this.mapToDTO(cargaProducto, populate)).toList()
							: List.of());
		} else {
			builder.productoDomicilioIds(
					producto.getProductosDomicilio() != null
							? producto.getProductosDomicilio().stream()
									.map(productoDomicilio -> productoDomicilio.getId()).toList()
							: List.of());
		}

//                .cargaProductos(producto.getCargaProductos() != null)
//                        ? producto.getCargaProductos().stream()
//                          .map(cargaProducto -> this.mapToDTO(cargaProducto))
//                          .toList()
//                        : List.of())
//
		return builder.build();
	}

	public ProductoDomicilioDTOResponse mapToDTO(ProductoDomicilio productoDomicilio, String[] populate) {
		ProductoDomicilioDTOResponse.ProductoDomicilioDTOResponseBuilder builder = ProductoDomicilioDTOResponse
				.builder().id(productoDomicilio.getId()).cantVaciosActuales(productoDomicilio.getCantVaciosActuales());
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(
					productoDomicilio.getDomicilio() != null ? this.mapToDTO(productoDomicilio.getDomicilio(), populate)
							: null);
		} else {
			builder.domicilioId(
					productoDomicilio.getDomicilio() != null ? productoDomicilio.getDomicilio().getId() : null);
		}
		if (populate != null && List.of(populate).contains("producto")) {
			builder.producto(
					productoDomicilio.getProducto() != null ? this.mapToDTO(productoDomicilio.getProducto(), populate)
							: null);
		} else {
			builder.productoId(productoDomicilio.getProducto() != null ? productoDomicilio.getProducto().getId() : null)
					.nombreProducto(
							productoDomicilio.getProducto() != null ? productoDomicilio.getProducto().getNombre()
									: null);
		}

		return builder.build();
	}

	public PersonaDTOResponse mapToDTO(Persona persona, String[] populate) {
		PersonaDTOResponse.PersonaDTOResponseBuilder builder = PersonaDTOResponse.builder().id(persona.getId())
				.tipoDoc(persona.getTipoDoc()).nroDocumento(persona.getNroDocumento()).nombre(persona.getNombre())
				.apellido(persona.getApellido()).email(persona.getEmail()).telefono(persona.getTelefono())
				.saldo(persona.getSaldo());
		if (populate != null && List.of(populate).contains("usuario")) {
			builder.usuario(persona.getUsuario() != null ? this.mapToDTO(persona.getUsuario(), populate) : null);
		} else {
			builder.usuarioId(persona.getUsuario() != null ? persona.getUsuario().getId() : null);
		}
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilios(persona.getDomicilios() != null
					? persona.getDomicilios().stream().map(domicilio -> this.mapToDTO(domicilio, populate)).toList()
					: List.of());
		} else {
			builder.domicilioIds(persona.getDomicilios() != null
					? persona.getDomicilios().stream().map(domicilio -> domicilio.getId()).toList()
					: List.of());
		}
		if (populate != null && List.of(populate).contains("pago")) {
			builder.pagos(persona.getPagos() != null
					? persona.getPagos().stream().map(pago -> this.mapToDTO(pago, populate)).toList()
					: List.of());
		} else {
			builder.pagosIds(persona.getPagos() != null ? persona.getPagos().stream().map(pago -> pago.getId()).toList()
					: List.of());
		}
		return builder.build();
	}

	public PedidoSemanalDTOResponse mapToDTO(PedidoSemanal pedidoSemanal, String[] populate) {
		PedidoSemanalDTOResponse.PedidoSemanalDTOResponseBuilder builder = PedidoSemanalDTOResponse.builder()
				.id(pedidoSemanal.getId()).cantidad(pedidoSemanal.getCantidad());
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(
					pedidoSemanal.getDomicilio() != null ? this.mapToDTO(pedidoSemanal.getDomicilio(), populate)
							: null);
		} else {
			builder.domicilioId(pedidoSemanal.getDomicilio() != null ? pedidoSemanal.getDomicilio().getId() : null);
		}
		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZona(
					pedidoSemanal.getProductoZona() != null ? this.mapToDTO(pedidoSemanal.getProductoZona(), populate)
							: null);
		} else {
			builder.productoZonaId(
					pedidoSemanal.getProductoZona() != null ? pedidoSemanal.getProductoZona().getId() : null);
		}

		return builder.build();
	}

	public PagoDTOResponse mapToDTO(Pago pago, String[] populate) {
		PagoDTOResponse.PagoDTOResponseBuilder builder = PagoDTOResponse.builder().id(pago.getId())
				.monto(pago.getMonto()).metodoPago(pago.getMetodoPago()).fecha(pago.getFecha());
		if (populate != null && List.of(populate).contains("persona")) {
			builder.persona(pago.getPersona() != null ? this.mapToDTO(pago.getPersona(), populate) : null);
		} else {
			builder.personaId(pago.getPersona() != null ? pago.getPersona().getId() : null);
		}

		return builder.build();
	}

	public LineaPedidoDTOResponse mapToDTO(LineaPedido lineaPedido, String[] populate) {
		LineaPedidoDTOResponse.LineaPedidoDTOResponseBuilder builder = LineaPedidoDTOResponse.builder()
				.id(lineaPedido.getId()).cantidad(lineaPedido.getCantidad()).subtotal(lineaPedido.getSubtotal());
		if (populate != null && List.of(populate).contains("venta")) {
			builder.venta(lineaPedido.getVenta() != null ? this.mapToDTO(lineaPedido.getVenta(), populate) : null);
		} else {
			builder.ventaId(lineaPedido.getVenta() != null ? lineaPedido.getVenta().getId() : null);
		}
		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZona(
					lineaPedido.getProductoZona() != null ? this.mapToDTO(lineaPedido.getProductoZona(), populate)
							: null);
		} else {
			builder.productoZonaId(
					lineaPedido.getProductoZona() != null ? lineaPedido.getProductoZona().getId() : null);
		}
		return builder.build();
	}

	public GastoDTOResponse mapToDTO(Gasto gasto, String[] populate) {
		GastoDTOResponse.GastoDTOResponseBuilder builder = GastoDTOResponse.builder().id(gasto.getId())
				.detalle(gasto.getDetalle()).monto(gasto.getMonto()).fecha(gasto.getFecha());
		if (populate != null && List.of(populate).contains("camion")) {
			builder.camion(gasto.getCamion() != null ? this.mapToDTO(gasto.getCamion(), populate) : null);
		} else {
			builder.camionId(gasto.getCamion() != null ? gasto.getCamion().getId() : null);

		}
		return builder.build();
	}

	public DomicilioDTOResponse mapToDTO(Domicilio domicilio, String[] populate) {
		DomicilioDTOResponse.DomicilioDTOResponseBuilder builder = DomicilioDTOResponse.builder().id(domicilio.getId())
				.calle(domicilio.getCalle()).numero(domicilio.getNumero()).casa(domicilio.getCasa())
				.dia(domicilio.getDia()).activo(domicilio.getActivo());
		if (populate != null && List.of(populate).contains("venta")) {
			builder.ventas(domicilio.getVentas() != null
					? domicilio.getVentas().stream().map(venta -> this.mapToDTO(venta, populate)).toList()
					: List.of());
		} else {
			builder.ventaIds(
					domicilio.getVentas() != null ? domicilio.getVentas().stream().map(venta -> venta.getId()).toList()
							: List.of());
		}
		if (populate != null && List.of(populate).contains("zona")) {
			builder.zona(domicilio.getZona() != null ? this.mapToDTO(domicilio.getZona(), populate) : null);
		} else {
			builder.zonaId(domicilio.getZona() != null ? domicilio.getZona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("pedidoSemanal")) {
			builder.pedidosSemanales(domicilio.getPedidosSemanal() != null ? domicilio.getPedidosSemanal().stream()
					.map(pedidoSemanal -> this.mapToDTO(pedidoSemanal, populate)).toList() : List.of());
		} else {
			builder.pedidoSemanalIds(domicilio.getPedidosSemanal() != null
					? domicilio.getPedidosSemanal().stream().map(pedidoSemanal -> pedidoSemanal.getId()).toList()
					: List.of());
		}
		if (populate != null && List.of(populate).contains("productosDomicilio")) {
			builder.productosDomicilio(
					domicilio.getProductoDomicilio() != null
							? domicilio.getProductoDomicilio().stream()
									.map(productoDomicilio -> this.mapToDTO(productoDomicilio, populate)).toList()
							: List.of());
		} else {
			builder.productoDomicilioIds(domicilio.getProductoDomicilio() != null ? domicilio.getProductoDomicilio()
					.stream().map(productoDomicilio -> productoDomicilio.getId()).toList() : List.of());
		}
		if (populate != null && List.of(populate).contains("persona")) {
			builder.persona(domicilio.getPersona() != null ? this.mapToDTO(domicilio.getPersona(), populate) : null);
		} else {
			builder.personaId(domicilio.getPersona() != null ? domicilio.getPersona().getId() : null);
		}
		return builder.build();
	}

	public CargaDTOResponse mapToDTO(Carga carga, String[] populate) {
		CargaDTOResponse.CargaDTOResponseBuilder builder = CargaDTOResponse.builder().id(carga.getId())
				.tipo(carga.getTipo()).fechaHora(carga.getFechaHora());
		if (populate != null && List.of(populate).contains("usuario")) {
			builder.usuario(carga.getUsuario() != null ? this.mapToDTO(carga.getUsuario(), populate) : null);
		} else {

			builder.usuarioId(carga.getUsuario() != null ? carga.getUsuario().getId() : null);
		}
		if (populate != null && List.of(populate).contains("camion")) {
			builder.camion(carga.getCamion() != null ? this.mapToDTO(carga.getCamion(), populate) : null);
		}
		if (populate != null && List.of(populate).contains("cargasProducto")) {
			builder.cargasProducto(
					carga.getCargasProducto() != null
							? carga.getCargasProducto().stream()
									.map(cargaProducto -> this.mapToDTO(cargaProducto, populate)).toList()
							: null);
		} else {
			builder.camionId(carga.getCamion() != null ? carga.getCamion().getId() : null);
		}
		return builder.build();

	}

	public CargaProductoDTOResponse mapToDTO(CargaProducto cargaProducto, String[] populate) {
		CargaProductoDTOResponse.CargaProductoDTOResponseBuilder builder = CargaProductoDTOResponse.builder()
				.id(cargaProducto.getId()).cantLleno(cargaProducto.getCantLleno())
				.cantVacio(cargaProducto.getCantVacio());
		if (populate != null && List.of(populate).contains("carga")) {
			builder.carga(cargaProducto.getCarga() != null ? mapToDTO(cargaProducto.getCarga(), populate) : null);
		} else {
			builder.cargaId(cargaProducto.getCarga().getId());

		}
		if (populate != null && List.of(populate).contains("producto")) {
			builder.producto(
					cargaProducto.getProducto() != null ? this.mapToDTO(cargaProducto.getProducto(), populate) : null);
		} else {
			builder.productoId(cargaProducto.getProducto().getId());
		}
		return builder.build();
	}

	public CamionDTOResponse mapToDTO(Camion camion, String[] populate) {
		CamionDTOResponse.CamionDTOResponseBuilder builder = CamionDTOResponse.builder().id(camion.getId())
				.patente(camion.getPatente()).modelo(camion.getModelo()).marca(camion.getMarca())
				.kilometraje(camion.getKilometraje()).estado(camion.getEstado());
		if (populate != null && List.of(populate).contains("gasto")) {
			builder.gastos(camion.getGastos() != null
					? camion.getGastos().stream().map(gasto -> this.mapToDTO(gasto, populate)).toList()
					: null);
		} else {
			builder.gastoIds(
					camion.getGastos() != null ? camion.getGastos().stream().map(gasto -> gasto.getId()).toList()
							: null);
		}
		if (populate != null && List.of(populate).contains("carga")) {
			builder.cargas(camion.getCargas() != null
					? camion.getCargas().stream().map(carga -> this.mapToDTO(carga, populate)).toList()
					: null);
		} else {
			builder.cargasIds(
					camion.getCargas() != null ? camion.getCargas().stream().map(carga -> carga.getId()).toList()
							: null);
		}
		if (populate != null && List.of(populate).contains("zonas")) {
			builder.zonas(camion.getZonas() != null
					? camion.getZonas().stream().map(zona -> this.mapToDTO(zona, populate)).toList()
					: null);
		} else {
			builder.zonaIds(
					camion.getZonas() != null ? camion.getZonas().stream().map(zona -> zona.getId()).toList()
							: null);
		}
		return builder.build();
	}

	public OrdenZonaDTOResponse mapToDTO(OrdenZona ordenZona, String[] populate) {
		OrdenZonaDTOResponse.OrdenZonaDTOResponseBuilder builder = OrdenZonaDTOResponse.builder().id(ordenZona.getId())
				.dia(ordenZona.getDia()).orden(ordenZona.getOrden());
		if (populate != null && List.of(populate).contains("zona")) {
			builder.zona(ordenZona.getZona() != null ? this.mapToDTO(ordenZona.getZona(), populate) : null);
		} else {
			builder.zonaId(ordenZona.getZona() != null ? ordenZona.getZona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(
					ordenZona.getDomicilio() != null ? this.mapToDTO(ordenZona.getDomicilio(), populate) : null);
		} else {
			builder.domicilioId(ordenZona.getDomicilio() != null ? ordenZona.getDomicilio().getId() : null);
		}
		
		return builder.build();
	}
}
