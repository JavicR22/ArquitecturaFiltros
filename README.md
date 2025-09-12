# ArquitecturaFiltros

Este proyecto implementa una arquitectura monolítica basada en el patrón de Filtros y Tuberías, utilizando Java puro. El objetivo es ofrecer componentes reutilizables que permitan manipular y transformar archivos mediante diferentes filtros, como cifrado, conversión de formatos, búsqueda de palabras, entre otros.

## Tabla de Contenidos

- [Estructura del repositorio](#estructura-del-repositorio)
- [Componentes principales](#componentes-principales)
- [Ejecutables y uso](#ejecutables-y-uso)
- [Ejemplo de uso](#ejemplo-de-uso)
- [Autores](#autores)

---

## Estructura del repositorio

El repositorio contiene diversas carpetas, cada una relacionada con un filtro o funcionalidad específica:

- **Base64ABinario/**
- **BuscarPalabraEnArchivoTexto/**
- **CargarArchivo/**
- **ConvertirArchivoBinarioABase64/**
- **ConvertirTextoABinario/**
- **Diagramas/**
- **EncriptarArchivoTextoSHA256/**
- **FiltroImagenes/**
- **IFiltro/**
- **ListarPalabrasFrecuenciasDeOcurrencias/**
- **Principal/**
- **pruebas/** (directorio con ejecutables `.jar` y archivos de prueba)

También encontrarás un archivo de ayuda con comandos de uso:  
[Comandos de uso.txt](https://github.com/JavicR22/ArquitecturaFiltros/blob/main/Comandos%20de%20uso.txt)

---

## Componentes principales

Cada carpeta corresponde a un filtro o módulo independiente, permitiendo aplicar operaciones como:

- Conversión entre Base64 y binario
- Búsqueda de palabras en archivos de texto
- Carga y manipulación de archivos
- Conversión de texto a binario y viceversa
- Encriptado SHA256 de archivos de texto
- Filtros de imágenes
- Listado de palabras y frecuencias en textos

---

## Ejecutables y uso

Los componentes ejecutables se encuentran en la carpeta [`pruebas`](https://github.com/JavicR22/ArquitecturaFiltros/tree/main/pruebas):

- Base64ABinario.jar
- BuscarPalabraEnArchivoTexto.jar
- CargarArchivo.jar
- ConvertirArchivoBinarioABase64.jar
- ConvertirTextoABinario.jar
- EncriptarArchivoTexto.jar
- FiltroImagenes.jar
- ListarPalabrasFrecuenciasDeOcurrencias.jar
- Principal.jar

### Cómo ejecutar

Desde la consola, navega al directorio `pruebas` y ejecuta el componente deseado con:

```sh
java -jar <Componente.jar> <ruta de directorio o archivo>
```

Por ejemplo, para aplicar el filtro de imágenes:

```sh
java -jar FiltroImagenes.jar <RutaDelDirectorio>
```

Puedes encontrar imágenes de ejemplo en:
- [`pruebas/img`](https://github.com/JavicR22/ArquitecturaFiltros/tree/main/pruebas/img)
- [`pruebas/prueba.png`](https://github.com/JavicR22/ArquitecturaFiltros/blob/main/pruebas/prueba.png)

---

## Ejemplo de uso

```sh
# Ejecutar el filtro de imágenes sobre un directorio con imágenes de prueba:
java -jar FiltroImagenes.jar pruebas/img
```
Consulta el archivo [Comandos de uso.txt](https://github.com/JavicR22/ArquitecturaFiltros/blob/main/Comandos%20de%20uso.txt) para ejemplos detallados de cada filtro.

---

## Autores

- **Javic Rojas**
- **Camilo Herrera**

---

Proyecto académico para aprendizaje y experimentación con arquitecturas monolíticas y filtros en Java.
