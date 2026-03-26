# POS-BENGKEL
Semua ini digunakan untuk memenuhi tugas UTS PBO


# Arsitektur

Proyek ini menggunakan arsitektur MVC yang sudah dimodifikasi dan disesuaikan dengan kebutuhan serta bahasa pemrograman yang digunakan. 

# HOW TO ??:
1. Melakukan git pull
2. Membuat database secara lokal dengan menjalankan file src\database\DataDefinitionLanguange.sql 
3. Menjalankan projek java dengan menggunakan syntax dibawah ini:

```bash
javac -d bin -sourcepath src -cp "lib\*" src\model\*.java src\database\*.java src\dao\*.java src\view\*.java src\controller\*.java src\*.java

java -cp "bin;lib\*" Main
```
