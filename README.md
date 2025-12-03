# Sistema de Créditos - JSP/Servlets + MySQL

## 🚀 Despliegue en AWS EC2

### Requisitos
- Java 11+
- Apache Tomcat 9
- MySQL 8.0+
- AWS Account

### Despliegue Rápido
```bash
# 1. Clonar repositorio
git clone https://github.com/Elvis-Arellan/SistemaCreditos.git

# 2. Generar WAR
cd sistema-creditos
ant clean build

# 3. Desplegar en AWS (script)
./deploy/deploy-aws.sh
