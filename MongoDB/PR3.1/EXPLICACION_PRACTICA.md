# Explicación de la Práctica 3.1: MongoDB + XML Stack Exchange

Esta documentación está pensada para que entiendas **qué** hemos hecho en esta práctica, **cómo** lo hemos solucionado y **por qué** hemos tomado cada decisión técnica. Si eres estudiante, esta guía te servirá para comprender el código a fondo y mejorar tus habilidades con Node.js y MongoDB.

---

## 🚀 1. Preparación del Entorno

Antes de escribir el código, necesitábamos preparar algunas herramientas básicas:

1. **Docker y MongoDB**: En lugar de instalar MongoDB directamente en tu ordenador (lo cual a veces da problemas de versiones o rutas), usamos **Docker** con un archivo `docker-compose.yml`. Al ejecutar `docker-compose up -d`, se levanta una base de datos limpia de forma aislada e incluye una interfaz web (Mongo Express) en el puerto `8081` para que puedas ver gráficamente tus datos.
2. **`package.json`**: Este archivo define nuestro proyecto Node.js y las librerías externas que vamos a usar:
   - `mongodb`: El driver oficial para conectarse y lanzar consultas a la base de datos.
   - `xml2js`: Transforma el texto de los archivos `.xml` gigantes en objetos JavaScript (JSON) que son muy fáciles de manipular.
   - `he`: Lo usamos para decodificar las entidades HTML (convierte `&#x3C;p&#x3E;` en `<p>`).
   - `winston`: Un creador de "Logs" profesionales. Te permite ver mensajes en pantalla y guardarlos al mismo tiempo en un archivo (`.log`).
   - `pdfkit`: Una herramienta genial y sencilla para dibujar y escribir documentos PDF desde código.
   - `dotenv`: Sirve para leer el archivo `.env`.
3. **`.env`**: Es una buena práctica en la industria separar la configuración (contraseñas, puertos, rutas de archivos) del código fuente. Si mañana cambias de servidor, solo editas el `.env` y no tocas el código.

---

## 🛠️ 2. Exercici 1: Lectura, Procesamiento e Inserción

El objetivo del archivo `exercici1.js` es coger los datos en crudo del XML (`Posts.xml`), limpiarlos, ordenarlos y guardarlos en MongoDB.

**Pasos de la solución:**
1. **Logs con Winston**: Configuramos Winston para que todo lo que pase (ej. `logger.info(...)`) salga por consola y también se guarde en `./data/logs/exercici1.log`. Así, si algo falla a medianoche, tienes un registro de qué ocurrió.
2. **Parsear el XML**: Usamos `xml2js`. Almacena todo el contenido del XML en memoria como un objeto gigante de JavaScript. Le decimos `{ explicitArray: false, mergeAttrs: true }` para que los atributos XML (como `Id="1"`) se conviertan en propiedades directas del objeto en vez de arrays confusos.
3. **Filtrar Documentos**: El archivo XML contiene varios tipos de posts (preguntas, respuestas, etc.). El enunciado pide solo **preguntas**, con lo cual nos quedamos únicamente con los elementos cuyo `PostTypeId` sea igual a `"1"`.
4. **Ordenar por `ViewCount`**: Usamos la función nativa `.sort()` en el array. Sin embargo, hay un detalle importante: **en el XML todo es texto**. Para ordenar de mayor a menor (`ViewCount` numérico), usamos `parseInt()` para convertir temporalmente ese texto a número en el proceso de ordenación.
5. **Decodificar HTML y adaptar formato**: El enunciado nos daba un formato exacto que debíamos cumplir: un objeto con la propiedad `question` y varios campos dentro. Iteramos con `.map()` las top 10.000 preguntas. Para el campo `Body`, aplicamos la librería `he` (`he.decode()`), que transforma todo el galimatías de caracteres (como `&#x3C;`) en código HTML legible (como `<`).
6. **Inserción masiva en MongoDB**: Nos conectamos a la BD con `MongoClient`. Para evitar insertar datos duplicados si ejecutas el script 10 veces, primero hacemos `await collection.deleteMany({})` (vaciamos la tabla) y luego usamos `await collection.insertMany(documents)` para meter los hasta 10.000 documentos de golpe. Es mucho más eficiente que insertarlos uno por uno.

---

## 🔍 3. Exercici 2: Consultas Complejas e Informes en PDF

El archivo `exercici2.js` ataca la base de datos que acabamos de llenar en el paso anterior y exporta los resultados.

### Consulta 1: ViewCount > media de la colección
**El problema:** Necesitas primero saber la media aritmética y luego buscar los que superen esa media. Además, el `ViewCount` se guardó como texto (String) porque así venía en el XML y el JSON del enunciado.
**La solución:** En lugar de traernos los 1.500 registros a Node.js y sumarlos con un bucle `for`, delegamos el esfuerzo a MongoDB mediante **agregaciones** (`$aggregate`):
1. Calculamos la media: Primero usamos la etapa `$project` con el operador `$toInt` para decirle a Mongo que considere el campo `question.ViewCount` como un número. Luego aplicamos `$group` y con `$avg` sacamos la media exacta.
2. Luego de tener el número en una variable JavaScript (`avgViewCount`), hacemos una segunda consulta con `$match` donde pedimos encontrar los documentos cuyo `viewCountInt` sea mayor (`$gt` -> *greater than*) a nuestra media. ¡Elegante y muy eficiente!

### Consulta 2: Palabras clave en el título
**El problema:** Buscar si un título contiene "pug", O "wig", O "yak"...
**La solución:** Creamos un array vacío y, con un `.map()`, construimos expresiones regulares (`$regex`) para cada palabra.
- Al usar `$regex` con la opción `$options: 'i'`, le decimos a MongoDB que haga la búsqueda de manera *Case Insensitive* (le da igual "PUG", "Pug" o "pug").
- Todo esto se lo pasamos al operador lógico `$or`. Es decir: *Búscame los elementos donde el título cumpla la RegExp A, o la RegExp B, o la RegExp C...*

### Creación de los PDFs (`pdfkit`)
Para no duplicar código, creé una función auxiliar `generatePDF(ruta, titulo, subtitulo, arrayDeResultados)`.
Lo que hace es:
- Abrir un lienzo u hoja en blanco (`PDFDocument`).
- Especificar fuentes (`Helvetica-Bold`), tamaños y colores y añadir los textos principales.
- Recorrer con un `.forEach()` el listado de los títulos que obtuvimos de la BBDD e imprimirlos línea a línea en el PDF. Si llega al final de la página (la posición 'y' del documento baja del margen inferior), auto-lanza `doc.addPage()` para saltar de hoja.
- Finalmente "cierra" el stream, que se guarda automáticamente en la ruta especificada de `./data/out/`.

---

## 🏁 Conclusión y Aprendizajes Clave

1. Has aprendido a procesar archivos que no son código mediante Node.js (`xml2js`), preparándolos y "limpiándolos" (`he.decode`).
2. Has aprendido a escribir **logs de forma industrial** con la herramienta *Winston*, preparándote para un entorno de producción real.
3. Has entendido que MongoDB es extremadamente veloz para **ingestar datos de golpe** (`insertMany()`).
4. Te has iniciado en el concepto avanzado de **Aggregation Framework** de MongoDB (pipelines `$project`, `$group`, `$sort`, `$match`), evitando colapsar la memoria de Node.js al hacer el trabajo duro (medias, cálculos) directamente dentro del motor de base de datos.
5. Has integrado un pequeño motor de renderizado de UI para generar reportes en PDF totalmente formateados desde cero.

¡Usa estos conceptos en tu repositorio personal de GitHub para demostrar tus habilidades backend!
