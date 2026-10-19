/**
 * Capa de aplicación del módulo.
 *
 * Aquí se orquestan los casos de uso del sistema, coordinando el dominio con la infraestructura.
 *  Esta capa no contiene lógica de negocio, sino que define: 
 * - Servicios de aplicación (coordinadores de casos de uso)
 * - DTOs de entrada y salida
 * - Componentes de seguridad necesarios para ejecutar los casos de uso
 * La capa de aplicación depende del dominio y expone operaciones listas para ser consumidas por adaptadores 
 * externos como controladores REST.
 */
package co.com.castor.facturacion.aplicacion;