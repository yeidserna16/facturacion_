# 🧾 Sistema de Facturación – (Spring Boot + Python + PostgreSQL + Oracle + Kubernetes + SonarQube + JaCoCo)

Proyecto de **Facturación para Castor**, compuesto por:

|------------|------------|
| Backend | Spring Boot (Java 17) |
| Cálculo de Facturas (impuestos y descuestos) | Microservicio Python |
| Base de Datos 1 | PostgreSQL 16 |
| Base de Datos 2 | Oracle XE |
| Testing | JUnit + Mockito |
| Cliente de pruebas | Node.js |
| Despliegue | Kubernetes + Docker |

---

### 🚀 1) Instrucciones de Despliegue

## 🔧 Requisitos
- Java 17+
- Maven 3+
- Python 3.11+
- Docker + Docker Compose
- Node.js (opcional para script de pruebas)
- SonarQube (opcional)


---

### 🐳 Levantar Base de Datos con Docker

Ejecutar en la carpeta donde está `docker-compose.yml`:

docker compose up -d

### Servicio	Puerto
## PostgreSQL	5432
## Oracle XE	1521

### 🐳 Levantar microservicio Python
pip install -r requirements.txt
python app.py
sube http://localhost:5051


###Levantar Backend Spring Boot
mvn spring-boot:run
sube http://localhost:8085

###Ejecución de Pruebas + Reportes SonarQube
mvn test

###Ejecución análisis con SonarQube
mvn clean verify sonar:sonar "-Dsonar.login=sq_MITOKEN_857686c9e50fa304a" "-Dsonar.host.url=http://localhost:9000"
en http://localhost:9000
  
###Base URL del backend
http://localhost:8085

#Crear Cliente
curl -X POST http://localhost:8085/clientes/ \
-H "Content-Type: application/json" \
-d '{"nombres":"Carlos","tipoDoc":"CC","identificacion":"123456789","email":"carlos@gmail.com","activo":true}'


#Buscar Cliente por identificacion
curl http://localhost:8085/clientes/1

#Actualizar Cliente 
curl -X PUT http://localhost:8085/clientes/10 
-H "Content-Type: application/json" \
-d '{"nombres":"Carlos Actualizado","tipoDoc":"CC","identificacion":"12345678","email":"actualizado@gmail.com","activo":true}'


#eliminar Cliente 
curl -X DELETE http://localhost:8085/cliente/1


####Controlador de Facturas
##Calcular factura con Python y Oracle
curl -X POST http://localhost:8085/facturas/calcular-factura 
-H "Content-Type: application/json" \
-d '{"idCliente":1,"items":[{"descripcion":"Mouse","precio":25000,"cantidad":2}]}'

###Listar factura en Oracle
curl http://localhost:8085/facturas/


###Listar factura por id en Oracle
curl http://localhost:8085/facturas/10


###Ejecutar prueba con node.js
##Ir a al ruta donde estan los js ejm: D:\Pruebas\node_facturacion_js
node cliente.js

