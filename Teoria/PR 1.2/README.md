
# MP0486-RA1 - Exemple de guardat de dades a arxius #

[![Java CI with Maven](https://github.com/jpala4-ieti/DAM-M0486-RA1-Repositori-Referencia-25-26/actions/workflows/maven.yml/badge.svg)](https://github.com/jpala4-ieti/DAM-M0486-RA1-Repositori-Referencia-25-26/actions/workflows/maven.yml)

En aquest projecte hi ha diversos exemples de com guardar dades en Java, cap a arxius de text, binaris, XML, CSV i objectes serialitzats

### Instruccions ###

Primer posar en funcionament el servidor

Després executar el client i comprovar com els càlculs obtenen resultat des del servidor

### Compilació i funcionament ###

Cal el 'Maven' per compilar el projecte
```bash
mvn clean
mvn compile
mvn clean compile test package
```

Per executar el projecte a Windows cal
```bash
.\run.ps1 com.project.Main
```

Per executar el projecte a Linux/macOS cal
```bash
./run.sh com.project.Main
```

Per fer anar classes específiques amb main:
```bash
.\run.ps1 com.project.EscripturaDadesPrimitives
./run.sh com.project.EscripturaDadesPrimitives
```

Per executar sense usar script propi, directament amb maven:
```bash
mvn exec:java "-Dexec.mainClass=com.project.ExempleNIO"
```

Per executar, un cop generat l'artefacte .jar
```bash
java -cp ./target/ams2-m0486-pr11-repo-ref-1.0.1.jar com.project.ExempleNIO
```



### Ordre recomanat d'estudi:

```
    GestioArxius.java

    ExempleNIO.java
    
    EscripturaArxiuWriter.java
    LecturaArxiuScanner.java

    EscripturaArxiuList.java
    LecturaArxiuList.java

    EscripturaDadesPrimitives.java
    LecturaDadesPrimitives.java

    EscripturaObjectes.java
    LecturaObjectes.java

    EscripturaLlistes.java
    LecturaLlistes.java

    GestioCSV.java
    GestioXML.java
```