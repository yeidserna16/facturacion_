package co.com.castor.facturacion.infraestructura.persistencia;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.com.castor.facturacion.aplicacion.dto.FacturaCalculoDTO;
import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;
import co.com.castor.facturacion.aplicacion.dto.FacturaDTO.DetallesFacDTO;
import co.com.castor.facturacion.aplicacion.dto.ItemFacturaDTO;

@Repository
public class FacturaRepositorioOracleImpl {

    private final JdbcTemplate oracleJdbc;

    public FacturaRepositorioOracleImpl(JdbcTemplate oracleJdbc) {
        this.oracleJdbc = oracleJdbc;
    }
    
    public int insertarFactura(FacturaCalculoDTO factura) {

        String sql = """
            INSERT INTO FACTURAS (ID_CLIENTE, MONEDA, IMPUESTO, TOTAL, ESTADO_FACTURA, FECHA_CREACION)
            VALUES (?, ?, ?, ?, ?, SYSTIMESTAMP)
            RETURNING ID INTO ?
              """;

        return oracleJdbc.execute((org.springframework.jdbc.core.CallableStatementCreator) connection -> {
            java.sql.CallableStatement cs = connection.prepareCall(sql);
            cs.setInt(1, factura.getIdCliente());
            cs.setString(2, "COP");
            cs.setBigDecimal(3, factura.getImpuesto());
            cs.setBigDecimal(4, factura.getTotal());
            cs.setString(5, "GENERADA");

            cs.registerOutParameter(6, java.sql.Types.INTEGER);
            return cs;

        }, (org.springframework.jdbc.core.CallableStatementCallback<Integer>) cs -> {
            cs.executeUpdate();
            return cs.getInt(6);
        });
    }




    public void insertarDetalleFactura(int idFactura, ItemFacturaDTO item) throws Exception {
    	try {
        String sql = "INSERT INTO DETALLE_FACTURA (ID_FACTURA, DESCRIPCION, CANTIDAD, PRECIO, SUBTOTAL) "
                + "VALUES (?, ?, ?, ?, ?)";

        oracleJdbc.update(sql, idFactura,
                item.getDescripcion(),
                item.getCantidad(),
                item.getPrecio(),
                item.getPrecio().multiply(new java.math.BigDecimal(item.getCantidad())));
    	}
        catch(Exception ex) {
        	 throw new Exception("No se pudo insertar en detalle factura. " + ex.getMessage());
        }
    }
    
        
    public int insertarFacturaSP(FacturaCalculoDTO factura) { 

        return oracleJdbc.execute(
            (org.springframework.jdbc.core.CallableStatementCreator) con -> {
                var cs = con.prepareCall("{ CALL SP_INSERTAR_FACTURA(?, ?, ?, ?, ?, ?, ?) }");
                
                cs.setInt(1, factura.getIdCliente());
                cs.setString(2, factura.getCodigoFactura());         
                cs.setBigDecimal(3, factura.getDescuento());        
                cs.setBigDecimal(4, factura.getImpuesto());
                cs.setBigDecimal(5, factura.getTotal());
                cs.setString(6, "GENERADA");
                cs.registerOutParameter(7, java.sql.Types.INTEGER);  // se entrega el idFactura
                
                return cs;
            },
            (org.springframework.jdbc.core.CallableStatementCallback<Integer>) cs -> {
                cs.execute();
                return cs.getInt(7);  
            }
        );
    }
    
    public List<FacturaDTO> listarFacturas() {
        String sql = "SELECT ID, ID_CLIENTE, CODIGO_FACTURA, ESTADO_FACTURA, TOTAL, IMPUESTO, DESCUENTO, MONEDA, FECHA_CREACION FROM FACTURAS";
        List<FacturaDTO> facturasDTO = oracleJdbc.query(sql, new FacturaDTORowMapper());

        //Cargar el detalle de la factura
        facturasDTO.forEach(f -> f.setDetalleFactura(listarDetalleFactura(f.getId())));

        return facturasDTO;
    }


    public FacturaDTO buscarPorId(Integer id) {

        String sql = "SELECT ID, ID_CLIENTE, CODIGO_FACTURA, ESTADO_FACTURA, TOTAL, IMPUESTO, DESCUENTO, MONEDA, FECHA_CREACION FROM FACTURAS WHERE ID=?";        
        FacturaDTO facturasDTO = oracleJdbc.queryForObject(sql, new FacturaDTORowMapper(), id);
       // facturasDTO.setDetalles(listarDetalleFactura(id));

        return facturasDTO;
    }


    public List<FacturaDTO> listarPorCliente(Integer idCliente) {
        String sql = "SELECT ID, ID_CLIENTE, CODIGO_FACTURA, ESTADO_FACTURA, TOTAL, IMPUESTO, DESCUENTO, MONEDA, FECHA_CREACION FROM FACTURAS WHERE ID_CLIENTE=?";
        List<FacturaDTO> facturasDTO = oracleJdbc.query(sql, new FacturaDTORowMapper(), idCliente);
        facturasDTO.forEach(f -> f.setDetalleFactura(listarDetalleFactura(f.getId())));

        return facturasDTO;
    }


    public List<DetallesFacDTO> listarDetalleFactura(int idFactura) {
        String sql = "SELECT DESCRIPCION, CANTIDAD, PRECIO, SUBTOTAL FROM DETALLE_FACTURA WHERE id_factura = ?";
       
        return oracleJdbc.query(sql, (rs, rowNum) -> DetallesFacDTO.builder()
                .descripcion(rs.getString("DESCRIPCION"))
                .cantidad(rs.getInt("CANTIDAD"))
                .precio(rs.getBigDecimal("PRECIO"))
                .subtotal(rs.getBigDecimal("SUBTOTAL"))
                .build(), idFactura);
    }


}
