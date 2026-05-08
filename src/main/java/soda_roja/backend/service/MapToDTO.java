package soda_roja.backend.service;

import org.springframework.stereotype.Service;

import soda_roja.backend.dtoResponse.*;
import soda_roja.backend.model.*;
import java.util.List;

@Service
public class MapToDTO {

	public ZonaDTOResponse mapToDTO(Zona zona, String[] populate) {
		ZonaDTOResponse.ZonaDTOResponseBuilder builder = ZonaDTOResponse.builder().id(zona.getId())
				.nombre(zona.getNombre()).detalle(zona.getDetalle());

		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZonas(zona.getProductosZona() != null ? zona.getProductosZona().stream()
					.map(productoZona -> this.mapToDTO(productoZona, removeFromPopulate(populate, "productoZona")))
					.toList() : List.of());
		}
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilios(
					zona.getDomicilios() != null
							? zona.getDomicilios().stream()
									.map(domicilio -> this.mapToDTO(domicilio,
											removeFromPopulate(populate, "domicilio")))
									.toList()
							: List.of());
		} 	
		if(populate != null && List.of(populate).contains("diasZona")) {
			builder.diasZona(
					zona.getDiasZona() != null
							? zona.getDiasZona().stream()
									.map(diaZona -> this.mapToDTO(diaZona, removeFromPopulate(populate, "diasZona")))
									.toList()
							: List.of());
		}
		if (populate != null && List.of(populate).contains("camion")) {
			builder.camion(
					zona.getCamion() != null ? this.mapToDTO(zona.getCamion(), removeFromPopulate(populate, "camion"))
							: null);
		} else {
			builder.camionId(zona.getCamion() != null ? zona.getCamion().getId() : null);


		}
		
		return builder.build();
	}

	public VentaDTOResponse mapToDTO(Venta venta, String[] populate) {
		VentaDTOResponse.VentaDTOResponseBuilder builder = VentaDTOResponse.builder();

		if (venta.getId() != null) {
			builder.id(venta.getId());
		}

		builder.fecha(venta.getFecha()).total(venta.getTotal()).estado(venta.getEstado())
				.lineasPedido(
						venta.getLineasPedido() != null ? venta.getLineasPedido().stream().map(lineaPedido -> this.mapToDTO(lineaPedido, null)).toList() : List.of());

		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(venta.getDomicilio() != null
					? this.mapToDTO(venta.getDomicilio(), removeFromPopulate(populate, "domicilio"))
					: null);
		} else {
			if (venta.getDomicilio() != null) {
				builder.idDomicilio(venta.getDomicilio().getId());
			}
		}
		if (populate != null && List.of(populate).contains("lineaPedido")) {
			builder.lineasPedido(venta.getLineasPedido() != null ? venta.getLineasPedido().stream()
					.map(lineaPedido -> this.mapToDTO(lineaPedido, removeFromPopulate(populate, "lineaPedido")))
					.toList() : List.of());
		} 
		return builder.build();
	}

	public UsuarioDTOResponse mapToDTO(Usuario usuario, String[] populate) {
		UsuarioDTOResponse.UsuarioDTOResponseBuilder builder = UsuarioDTOResponse.builder().id(usuario.getId())
				.nombreUsuario(usuario.getNombreUsuario()).email(usuario.getEmail())
				.nivelAcceso(usuario.getNivelAcceso())
				.imagenUrl(usuario.getImagenUrl());
		if (populate != null && List.of(populate).contains("persona")) {
			builder.persona(this.mapToDTO(usuario.getPersona(), removeFromPopulate(populate, "persona")));
		} else {
			builder.personaId(usuario.getPersona() != null ? usuario.getPersona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("carga")) {
			builder.cargas(
					usuario.getCargas() != null
							? usuario.getCargas().stream()
									.map(carga -> this.mapToDTO(carga, removeFromPopulate(populate, "carga"))).toList()
							: null);
		} 	
		return builder.build();
	}

	public ProductoZonaDTOResponse mapToDTO(ProductoZona productoZona, String[] populate) {
		ProductoZonaDTOResponse.ProductoZonaDTOResponseBuilder builder = ProductoZonaDTOResponse.builder()
				.id(productoZona.getId());
		if (populate != null && List.of(populate).contains("producto")) {
			builder.producto(productoZona.getProducto() != null
					? this.mapToDTO(productoZona.getProducto(), removeFromPopulate(populate, "producto"))
					: null);
		} else {
			builder.productoId(productoZona.getProducto() != null ? productoZona.getProducto().getId() : null)
					.productoId(productoZona.getProducto() != null ? productoZona.getProducto().getId() : null);
		}
		if (populate != null && List.of(populate).contains("zona")) {
			builder.zona(productoZona.getZona() != null
					? this.mapToDTO(productoZona.getZona(), removeFromPopulate(populate, "zona"))
					: null);
		} else {
			builder.zonaId(productoZona.getZona() != null ? productoZona.getZona().getId() : null)
					.zonaId(productoZona.getZona() != null ? productoZona.getZona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("lineaPedido")) {
			builder.lineaPedidos(productoZona.getLineaPedidos() != null ? productoZona.getLineaPedidos().stream()
					.map(lineaPedido -> this.mapToDTO(lineaPedido, removeFromPopulate(populate, "lineaPedido")))
					.toList() : null);
		}
		if (populate != null && List.of(populate).contains("pedidoSemanal")) {
			builder.pedidoSemanal(
					productoZona.getPedidoSemanales() != null
							? productoZona.getPedidoSemanales().stream()
									.map(p -> this.mapToDTO(p, removeFromPopulate(populate, "pedidoSemanal"))).toList()
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
									.map(productoZona -> this.mapToDTO(productoZona,
											removeFromPopulate(populate, "productoZona")))
									.toList()
							: List.of());
		} 	
		if (populate != null && List.of(populate).contains("productoDomicilio")) {
			builder.productosDomicilio(
					producto.getProductosDomicilio() != null
							? producto.getProductosDomicilio().stream()
									.map(productoDomicilio -> this.mapToDTO(productoDomicilio,
											removeFromPopulate(populate, "productoDomicilio")))
									.toList()
							: List.of());
		}
		if (populate != null && List.of(populate).contains("cargaProducto")) {
			builder.cargasProducto(
					producto.getCargasProducto() != null
							? producto.getCargasProducto().stream()
									.map(cargaProducto -> this.mapToDTO(cargaProducto,
											removeFromPopulate(populate, "cargaProducto")))
									.toList()
							: List.of());
		}
		return builder.build();
	}

	public ProductoDomicilioDTOResponse mapToDTO(ProductoDomicilio productoDomicilio, String[] populate) {
		ProductoDomicilioDTOResponse.ProductoDomicilioDTOResponseBuilder builder = ProductoDomicilioDTOResponse
				.builder().id(productoDomicilio.getId()).cantVaciosActuales(productoDomicilio.getCantVaciosActuales());
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(productoDomicilio.getDomicilio() != null
					? this.mapToDTO(productoDomicilio.getDomicilio(), removeFromPopulate(populate, "domicilio"))
					: null);
		} else {
			builder.domicilioId(
					productoDomicilio.getDomicilio() != null ? productoDomicilio.getDomicilio().getId() : null);
		}
		if (populate != null && List.of(populate).contains("producto")) {
			builder.producto(productoDomicilio.getProducto() != null
					? this.mapToDTO(productoDomicilio.getProducto(), removeFromPopulate(populate, "producto"))
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
				.saldo(persona.getSaldo()).estado(persona.getEstado());
		if (populate != null && List.of(populate).contains("usuario")) {
			builder.usuario(persona.getUsuario() != null
					? this.mapToDTO(persona.getUsuario(), removeFromPopulate(populate, "usuario"))
					: null);
		} else {
			builder.usuarioId(persona.getUsuario() != null ? persona.getUsuario().getId() : null);
		}
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilios(
					persona.getDomicilios() != null
							? persona.getDomicilios().stream()
									.map(domicilio -> this.mapToDTO(domicilio,
											removeFromPopulate(populate, "domicilio")))
									.toList()
							: List.of());
		}
		if (populate != null && List.of(populate).contains("domicilioCompleto")) {
		    builder.domicilios(
		            persona.getDomicilios() != null
		                    ? persona.getDomicilios().stream()
		                            .map(domicilio -> this.mapToDTO(domicilio,
		                                    new String[]{"zona", "diaDomicilio","zonaCamion"}))
		                            .toList()
		                    : List.of());
		}
		if (populate != null && List.of(populate).contains("pago")) {
			builder.pagos(
					persona.getPagos() != null
							? persona.getPagos().stream()
									.map(pago -> this.mapToDTO(pago, removeFromPopulate(populate, "pago"))).toList()
							: List.of());
		} 	
		return builder.build();
	}

	public PedidoSemanalDTOResponse mapToDTO(PedidoSemanal pedidoSemanal, String[] populate) {
		PedidoSemanalDTOResponse.PedidoSemanalDTOResponseBuilder builder = PedidoSemanalDTOResponse.builder()
				.id(pedidoSemanal.getId()).cantidad(pedidoSemanal.getCantidad());
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(pedidoSemanal.getDomicilio() != null
					? this.mapToDTO(pedidoSemanal.getDomicilio(), removeFromPopulate(populate, "domicilio"))
					: null);
		} else {
			builder.domicilioId(pedidoSemanal.getDomicilio() != null ? pedidoSemanal.getDomicilio().getId() : null);
		}
		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZona(pedidoSemanal.getProductoZona() != null
					? this.mapToDTO(pedidoSemanal.getProductoZona(), removeFromPopulate(populate, "productoZona"))
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
			builder.persona(pago.getPersona() != null
					? this.mapToDTO(pago.getPersona(), removeFromPopulate(populate, "persona"))
					: null);
		} else {
			builder.personaId(pago.getPersona() != null ? pago.getPersona().getId() : null);
		}

		return builder.build();
	}

	public LineaPedidoDTOResponse mapToDTO(LineaPedido lineaPedido, String[] populate) {
		LineaPedidoDTOResponse.LineaPedidoDTOResponseBuilder builder = LineaPedidoDTOResponse.builder()
				.id(lineaPedido.getId()).cantidad(lineaPedido.getCantidad()).subtotal(lineaPedido.getSubtotal());
		if (populate != null && List.of(populate).contains("venta")) {
			builder.venta(lineaPedido.getVenta() != null
					? this.mapToDTO(lineaPedido.getVenta(), removeFromPopulate(populate, "venta"))
					: null);
		} else {
			builder.ventaId(lineaPedido.getVenta() != null ? lineaPedido.getVenta().getId() : null);
		}
		if (populate != null && List.of(populate).contains("productoZona")) {
			builder.productoZona(lineaPedido.getProductoZona() != null
					? this.mapToDTO(lineaPedido.getProductoZona(), removeFromPopulate(populate, "productoZona"))
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
			builder.camion(
					gasto.getCamion() != null ? this.mapToDTO(gasto.getCamion(), removeFromPopulate(populate, "camion"))
							: null);
		} else {
			builder.camionId(gasto.getCamion() != null ? gasto.getCamion().getId() : null);

		}
		return builder.build();
	}

	public DomicilioDTOResponse mapToDTO(Domicilio domicilio, String[] populate) {
		DomicilioDTOResponse.DomicilioDTOResponseBuilder builder = DomicilioDTOResponse.builder().id(domicilio.getId())
				.calle(domicilio.getCalle()).numero(domicilio.getNumero()).casa(domicilio.getCasa());
		builder.activo(domicilio.getActivo())
				.habilitado(domicilio.getHabilitado());	
		if (populate != null && List.of(populate).contains("venta")) {
			builder.ventas(domicilio.getVentas() != null
					? domicilio.getVentas().stream()
							.map(venta -> this.mapToDTO(venta, removeFromPopulate(populate, "venta"))).toList()
					: List.of());
		} 	
		if (populate != null && List.of(populate).contains("zona")) {
			builder.zona(domicilio.getZona() != null
					? this.mapToDTO(domicilio.getZona(), removeFromPopulate(populate, "zona"))
					: null);
		} else {
			builder.zonaId(domicilio.getZona() != null ? domicilio.getZona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("pedidoSemanal")) {
			builder.pedidosSemanales(domicilio.getPedidosSemanal() != null ? domicilio.getPedidosSemanal().stream()
					.map(pedidoSemanal -> this.mapToDTO(pedidoSemanal, removeFromPopulate(populate, "pedidoSemanal")))
					.toList() : List.of());
		} 	
		if (populate != null && List.of(populate).contains("productosDomicilio")) {
			builder.productosDomicilio(domicilio.getProductoDomicilio() != null
					? domicilio.getProductoDomicilio().stream().map(productoDomicilio -> this
							.mapToDTO(productoDomicilio, removeFromPopulate(populate, "productosDomicilio"))).toList()
					: List.of());
		} 	
		if (populate != null && List.of(populate).contains("persona")) {
			builder.persona(domicilio.getPersona() != null
					? this.mapToDTO(domicilio.getPersona(), removeFromPopulate(populate, "persona"))
					: null);
		} else {
			builder.personaId(domicilio.getPersona() != null ? domicilio.getPersona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("diaDomicilio")) {
			builder.diasDomicilio(
					domicilio.getDiasDomicilio() != null ? domicilio.getDiasDomicilio().stream()
							.map(diaDomicilio -> this.mapToDTO(diaDomicilio, removeFromPopulate(populate, "diaDomicilio")))
							.toList()
							: List.of());
		} else {
			builder.diasDomicilioIds(domicilio.getDiasDomicilio() != null ? domicilio.getDiasDomicilio().stream().map(diaDomicilio -> diaDomicilio.getId()).toList() : List.of());
		}
		if (populate != null && List.of(populate).contains("zonaCamion")) {
		    builder.zona(domicilio.getZona() != null
		            ? this.mapToDTO(domicilio.getZona(), new String[]{"camion"})
		            : null);
		}



		return builder.build();
	}

	public CargaDTOResponse mapToDTO(Carga carga, String[] populate) {
		CargaDTOResponse.CargaDTOResponseBuilder builder = CargaDTOResponse.builder().id(carga.getId())
				.tipo(carga.getTipo()).fechaHora(carga.getFechaHora());
		if (populate != null && List.of(populate).contains("usuario")) {
			builder.usuario(carga.getUsuario() != null
					? this.mapToDTO(carga.getUsuario(), removeFromPopulate(populate, "usuario"))
					: null);
		} else {

			builder.usuarioId(carga.getUsuario() != null ? carga.getUsuario().getId() : null);
		}
		if (populate != null && List.of(populate).contains("camion")) {
			builder.camion(
					carga.getCamion() != null ? this.mapToDTO(carga.getCamion(), removeFromPopulate(populate, "camion"))
							: null);
		}
		if (populate != null && List.of(populate).contains("cargasProducto")) {
			builder.cargasProducto(carga.getCargasProducto() != null ? carga.getCargasProducto().stream()
					.map(cargaProducto -> this.mapToDTO(cargaProducto, removeFromPopulate(populate, "cargaProducto")))
					.toList() : null);
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
			builder.carga(cargaProducto.getCarga() != null
					? mapToDTO(cargaProducto.getCarga(), removeFromPopulate(populate, "carga"))
					: null);
		} else {
			builder.cargaId(cargaProducto.getCarga().getId());

		}
		if (populate != null && List.of(populate).contains("producto")) {
			builder.producto(cargaProducto.getProducto() != null
					? this.mapToDTO(cargaProducto.getProducto(), removeFromPopulate(populate, "producto"))
					: null);
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
			builder.gastos(
					camion.getGastos() != null
							? camion.getGastos().stream()
									.map(gasto -> this.mapToDTO(gasto, removeFromPopulate(populate, "gasto"))).toList()
							: null);
		} 	
		if (populate != null && List.of(populate).contains("carga")) {
			builder.cargas(
					camion.getCargas() != null
							? camion.getCargas().stream()
									.map(carga -> this.mapToDTO(carga, removeFromPopulate(populate, "carga"))).toList()
							: null);
		} 	
		if (populate != null && List.of(populate).contains("zona")) {
			builder.zonas(
					camion.getZonas() != null
							? camion.getZonas().stream()
									.map(zona -> this.mapToDTO(zona, removeFromPopulate(populate, "zona"))).toList()
							: null);
		} 	
		return builder.build();
	}
	public DiaZonaDTOResponse mapToDTO(DiaZona diaZona, String[] populate) {
	    DiaZonaDTOResponse.DiaZonaDTOResponseBuilder builder = DiaZonaDTOResponse.builder()
	            .id(diaZona.getId())
	            .diaId(diaZona.getDia() != null ? diaZona.getDia().getId() : null)
	            .zonaId(diaZona.getZona() != null ? diaZona.getZona().getId() : null);

	    if (populate != null && List.of(populate).contains("zona")) {
	        builder.zona(diaZona.getZona() != null
	                ? this.mapToDTO(diaZona.getZona(), removeFromPopulate(populate, "zona"))
	                : null);
	    }
	    
	    if (populate != null && List.of(populate).contains("dia")) {
	        builder.dia(diaZona.getDia() != null
	                ? this.mapToDTO(diaZona.getDia(), removeFromPopulate(populate, "dia"))
	                : null);
	    }
	    
	    if (populate != null && List.of(populate).contains("diaZonaOrden")) {
	        // Crear populate solo para domicilio, sin incluir zona aquí
	        builder.diaZonaOrdenes(diaZona.getDiaZonaOrdenes() != null
	                ? diaZona.getDiaZonaOrdenes().stream()
	                        .map(diaZonaOrden -> this.mapToDTO(diaZonaOrden, removeFromPopulate(populate, "diaZonaOrden")))
	                        .toList()
	                : List.of());


		}
	    
	    return builder.build();
	}

	public DiaZonaOrdenDTOResponse mapToDTO(DiaZonaOrden diaZonaOrden, String[] populate) {
		DiaZonaOrdenDTOResponse.DiaZonaOrdenDTOResponseBuilder builder = DiaZonaOrdenDTOResponse.builder()
				.id(diaZonaOrden.getId()).orden(diaZonaOrden.getOrden());
		if (populate != null && List.of(populate).contains("diaZona")) {
			builder.diaZona(diaZonaOrden.getDiaZona() != null
					? this.mapToDTO(diaZonaOrden.getDiaZona(), removeFromPopulate(populate, "diaZona"))
					: null);
		} else {
			builder.diaZonaId(diaZonaOrden.getDiaZona() != null ? diaZonaOrden.getDiaZona().getId() : null);
		}
		if (populate != null && List.of(populate).contains("domicilio")) {
			builder.domicilio(diaZonaOrden.getDomicilio() != null
					? this.mapToDTO(diaZonaOrden.getDomicilio(), removeFromPopulate(populate, "domicilio"))
					: null);
		} else {
			builder.domicilioId(diaZonaOrden.getDomicilio() != null ? diaZonaOrden.getDomicilio().getId() : null);
		}
		
		return builder.build();
	}
	
	public DiaDomicilioDTOResponse mapToDTO(DiaDomicilio diaDomicilio, String[] populate) {
	    DiaDomicilioDTOResponse.DiaDomicilioDTOResponseBuilder builder = DiaDomicilioDTOResponse.builder()
	            .id(diaDomicilio.getId()).estado(diaDomicilio.getEstado());
	    if (populate != null && List.of(populate).contains("dia")) {
	        builder.dia(diaDomicilio.getDia() != null
	                ? this.mapToDTO(diaDomicilio.getDia(), removeFromPopulate(populate, "dia"))
	                : null);
	    } else {
	        builder.diaId(diaDomicilio.getDia() != null ? diaDomicilio.getDia().getId() : null);
	    }
	    if (populate != null && List.of(populate).contains("domicilio")) {
	        builder.domicilio(diaDomicilio.getDomicilio() != null
	                ? this.mapToDTO(diaDomicilio.getDomicilio(),
	                        removeFromPopulate(populate, "domicilio"))
	                : null);
	    } else {
	        builder.domicilioId(diaDomicilio.getDomicilio() != null ? diaDomicilio.getDomicilio().getId() : null);
	    }
	    
	    return builder.build();
	}
	public DiaDTOResponse mapToDTO(Dia dia, String[] populate) {
	    DiaDTOResponse.DiaDTOResponseBuilder builder = DiaDTOResponse.builder()
	            .id(dia.getId()).nombre(dia.getNombre());
	    return builder.build();
	}
	private String[] addToPopulate(String[] populate, String key) {
	    if (populate == null) {
	        return new String[]{key};
	    }
	    String[] newPopulate = new String[populate.length + 1];
	    System.arraycopy(populate, 0, newPopulate, 0, populate.length);
	    newPopulate[populate.length] = key;
	    return newPopulate;
	}

	private String[] removeFromPopulate(String[] populate, String key) {
		if (populate == null)
			return null;
		return java.util.Arrays.stream(populate).filter(s -> !s.equals(key)).toArray(String[]::new);
	}
	
}
