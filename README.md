# 🚀 Gatling Performance Testing Framework

Framework profesional de pruebas de performance con Gatling, diseñado para múltiples aplicaciones y ambientes.

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.java.com/)
[![Scala](https://img.shields.io/badge/Scala-2.13-red.svg)](https://www.scala-lang.org/)
[![Gatling](https://img.shields.io/badge/Gatling-3.10.5-yellow.svg)](https://gatling.io/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Ejecución](#-ejecución)
- [Tipos de Pruebas](#-tipos-de-pruebas)
- [Configuración](#️-configuración)
- [Reportes](#-reportes)
- [Troubleshooting](#-troubleshooting)

---

## ✨ Características

- ✅ **Multi-aplicación**: Soporta Walmart APIs y APIs Demo
- ✅ **Multi-ambiente**: Dev, QA, Staging, Production
- ✅ **Tipos de pruebas**: Sanity, Baseline, Load, Stress
- ✅ **Debug mode**: Logs detallados activables
- ✅ **Modular**: Escenarios reutilizables
- ✅ **Think times**: Aleatorios y realistas
- ✅ **Assertions automáticas**: P95, P99, Success Rate
- ✅ **Reportes HTML**: Gráficas y estadísticas detalladas

---

## 📁 Estructura del Proyecto

```
gatling-framework/
├── .gitignore
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── application.conf          # Configuración por ambientes
│   └── test/
│       └── scala/
│           ├── config/
│           │   └── BaseConfig.scala      # Configuración centralizada
│           ├── scenarios/
│           │   ├── walmart/              # Escenarios Walmart
│           │   │   ├── WalmartAuthScenario.scala
│           │   │   ├── WalmartProductsScenario.scala
│           │   │   └── WalmartSanityScenario.scala
│           │   └── demo/                 # Escenarios Demo/Practice
│           │       ├── PostsScenario.scala
│           │       ├── UsersScenario.scala
│           │       └── DemoSanityScenario.scala
│           ├── simulations/
│           │   ├── WalmartSimulation.scala
│           │   ├── WalmartSanitySimulation.scala
│           │   ├── DemoSimulation.scala
│           │   ├── DemoSanitySimulation.scala
│           │   └── ReqResSimulation.scala
│           └── utils/
│               ├── DebugUtils.scala      # Debug y logging
│               └── ThinkTimeUtils.scala  # Think times aleatorios
└── target/                               # Generado (no en Git)
    └── gatling/                          # Reportes HTML
```

---

## 🚀 Requisitos

- **Java JDK**: 11 o superior
- **Maven**: 3.6 o superior
- **Scala**: 2.13 (descargado automáticamente por Maven)
- **Git**: Para versionamiento (opcional)

### Verificar instalación:

```bash
java -version    # Debe mostrar 11+
mvn -version     # Debe mostrar 3.6+
```

---

## 📦 Instalación

### 1. Clonar el repositorio:

```bash
git clone https://github.com/TU_USUARIO/gatling-framework.git
cd gatling-framework
```

### 2. Compilar el proyecto:

```bash
mvn clean compile
```

---

## 🏃 Ejecución

### Ejecución básica (ambiente por defecto):

```bash
mvn gatling:test
```

### Walmart - Prueba completa:

```bash
# Windows PowerShell
$env:ENV="walmart"; $env:DEBUG="true"; mvn gatling:test "-Dgatling.simulationClass=simulations.WalmartSimulation"

# Mac/Linux
ENV=walmart DEBUG=true mvn gatling:test -Dgatling.simulationClass=simulations.WalmartSimulation
```

### Walmart - Sanity Check (rápido):

```bash
# Windows
$env:ENV="walmart-sanity"; mvn gatling:test "-Dgatling.simulationClass=simulations.WalmartSanitySimulation"

# Mac/Linux
ENV=walmart-sanity mvn gatling:test -Dgatling.simulationClass=simulations.WalmartSanitySimulation
```

### Demo API - Prueba completa:

```bash
# Windows
$env:ENV="demo"; mvn gatling:test "-Dgatling.simulationClass=simulations.DemoSimulation"

# Mac/Linux
ENV=demo mvn gatling:test -Dgatling.simulationClass=simulations.DemoSimulation
```

---

## 🧪 Tipos de Pruebas

### 1️⃣ Sanity Check
**Propósito**: Verificar que el sistema está UP  
**Duración**: 10-30 segundos  
**Usuarios**: 1

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.WalmartSanitySimulation
```

### 2️⃣ Load Test
**Propósito**: Validar comportamiento bajo carga normal  
**Duración**: 30-60 minutos  
**Usuarios**: 10-100

```bash
mvn gatling:test -Dgatling.simulationClass=simulations.WalmartSimulation
```

### 3️⃣ Stress Test
**Propósito**: Encontrar el límite del sistema  
**Duración**: 60+ minutos  
**Usuarios**: Incremental hasta falla

---

## ⚙️ Configuración

### Variables de Ambiente

| Variable | Descripción | Valores | Default |
|----------|-------------|---------|---------|
| `ENV` | Ambiente a usar | walmart, walmart-sanity, walmart-load, demo, demo-sanity, reqres, reqres-sanity | walmart |
| `DEBUG` | Activar logs detallados | true, false | true |
| `USERS` | Usuarios concurrentes | 1-1000+ | (del config) |
| `DURATION` | Duración en segundos | 10-3600+ | (del config) |
| `RAMP_UP` | Tiempo de ramp-up | 1-600+ | (del config) |

### Ejemplo con variables personalizadas:

```bash
# Windows
$env:ENV="walmart"; $env:DEBUG="false"; $env:USERS="50"; $env:DURATION="300"; mvn gatling:test

# Mac/Linux
ENV=walmart DEBUG=false USERS=50 DURATION=300 mvn gatling:test
```

### Editar configuración:

Edita `src/main/resources/application.conf` para cambiar:
- URLs base por ambiente
- Credenciales (NO subir a Git)
- Thresholds de performance
- Configuración de timeouts

---

## 📊 Reportes

### Ubicación:

```
target/gatling/[simulacion]-[timestamp]/index.html
```

### Abrir reporte:

```bash
# Windows
start target/gatling/walmartsimulation-20251112150000/index.html

# Mac
open target/gatling/walmartsimulation-20251112150000/index.html

# Linux
xdg-open target/gatling/walmartsimulation-20251112150000/index.html
```

### Métricas incluidas:

- ✅ Request count (OK/KO)
- ✅ Response times (min, max, mean, percentiles)
- ✅ Requests per second
- ✅ Gráficas de distribución
- ✅ Detalles de errores

---

## 🔧 Troubleshooting

### Error: "JAVA_HOME not defined"

```bash
# Windows
$env:JAVA_HOME = "C:\Program Files\Java\jdk-16.0.2"

# Mac/Linux
export JAVA_HOME=/path/to/jdk
```

### Error: "Cannot resolve symbol"

```bash
mvn clean install -U
# En IntelliJ: File → Invalidate Caches → Restart
```

### Error: "Simulation class not found"

```bash
mvn clean compile
mvn gatling:test "-Dgatling.simulationClass=simulations.TU_SIMULACION"
```

### Cambios en application.conf no se reflejan:

```bash
mvn clean compile  # Siempre usar clean después de cambiar configs
```

---

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama: `git checkout -b feature/nueva-funcionalidad`
3. Commit cambios: `git commit -m 'Agregar nueva funcionalidad'`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Abre un Pull Request

---

## 📚 Referencias

- [Gatling Documentation](https://gatling.io/docs/current/)
- [Scala Documentation](https://docs.scala-lang.org/)
- [Maven Gatling Plugin](https://gatling.io/docs/gatling/reference/current/extensions/maven_plugin/)

---

## 👤 Autor

**Tu Nombre**  
Performance Engineer  
[GitHub](https://github.com/TU_USUARIO) | [LinkedIn](https://linkedin.com/in/TU_PERFIL)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## 📁 Estructura del Proyecto

```
gatling-framework/
├── pom.xml
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── application.conf
│   └── test/
│       └── scala/
│           ├── config/
│           │   └── BaseConfig.scala
│           ├── scenarios/
│           │   ├── WalmartAuthScenario.scala
│           │   └── WalmartProductsScenario.scala
│           ├── simulations/
│           │   └── WalmartSimulation.scala
│           └── utils/
│               ├── DebugUtils.scala
│               └── ThinkTimeUtils.scala
└── README.md
```

## 🚀 Requisitos Previos

- Java JDK 11 o superior
- Maven 3.6+
- Scala 2.13 (se descarga automáticamente con Maven)

## ⚙️ Configuración

### Variables de Ambiente

```bash
DEBUG=true              # Activar logs detallados
ENV=walmart            # walmart o walmart-load
USERS=10               # Número de usuarios concurrentes
DURATION=60            # Duración en segundos
RAMP_UP=30             # Tiempo de ramp-up en segundos
```

### Ambientes Disponibles

- **walmart**: 10 usuarios, 60 segundos (pruebas normales)
- **walmart-load**: 50 usuarios, 300 segundos (prueba de carga)

## 🏃 Ejecución

### Ejecución básica (usa walmart por defecto)

```bash
mvn clean compile
mvn gatling:test
```

### Con debug activado

```bash
# Windows PowerShell
$env:DEBUG="true"; mvn gatling:test

# Mac/Linux
DEBUG=true mvn gatling:test
```

### Prueba de carga

```bash
# Windows PowerShell
$env:ENV="walmart-load"; $env:DEBUG="false"; mvn gatling:test

# Mac/Linux
ENV=walmart-load DEBUG=false mvn gatling:test
```

### Ver resultados

Los reportes HTML se generan en:
```
target/gatling/walmartsimulation-{timestamp}/index.html
```

## 📊 Escenarios Incluidos

### 1. Walmart Auth Scenario
- POST /api/auth - Autenticación con usuario/password
- Extrae y guarda el token JWT
- Muestra response completo en logs

### 2. Walmart Products Scenario
- Ejecuta autenticación primero
- GET /api/products - Obtiene productos con Bearer token
- Valida estructura de respuesta
- Think time aleatorio entre requests

## 🎯 Assertions y Thresholds

- **Response Time Max**: < 5000ms
- **Success Rate**: > 95%
- **P95**: < 2000ms
- **P99**: < 3000ms

## 🔧 Troubleshooting

### Error: Cannot resolve symbol username/password
```bash
mvn clean install -U
```

### Tests muy lentos
Desactiva debug: `DEBUG=false mvn gatling:test`

### Error de compilación
```bash
mvn clean compile
# Si persiste:
File → Invalidate Caches → Invalidate and Restart (en IntelliJ)
```

## 📚 Referencias

- [Gatling Documentation](https://gatling.io/docs/current/)
- [Scala Documentation](https://docs.scala-lang.org/)
- [Maven Gatling Plugin](https://gatling.io/docs/gatling/reference/current/extensions/maven_plugin/)