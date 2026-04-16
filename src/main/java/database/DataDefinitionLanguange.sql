    --  membuat database pos bengkel
    CREATE DATABASE pos_bengkel;
    -- memakai ddatabase pos_bengkel
    USE pos_bengkel;

    -- Membuat tabel user
    CREATE TABLE tb_user (
        id_user INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) UNIQUE,
        user_password VARCHAR(255),
        nama_lengkap VARCHAR (100)
    );


            -- Membuat tabel Jasa
    CREATE TABLE tb_jasa(
    id_jasa INT AUTO_INCREMENT PRIMARY KEY,
    nama_jasa VARCHAR (100) NOT NULL ,
    tarif_jasa  INT NOT NULL
    );

    -- Membuat tabel Sparepart
    CREATE TABLE tb_sparepart (
        id_sparepart INT AUTO_INCREMENT PRIMARY KEY,
        nama_sparepart VARCHAR(100) NOT NULL,
        harga_beli INT NOT NULL,
        harga_jual INT NOT NULL,
        stok INT NOT NULL DEFAULT 0
    );

    -- -- Membuat tabel tipe motor tabel tipe motor sementara dihilangkan mungkin ada di next update
    -- CREATE TABLE tb_tipe_motor (
    --     id_tipe_motor INT AUTO_INCREMENT PRIMARY KEY,
    --     merek VARCHAR(50) NOT NULL,
    --     nama_model VARCHAR(100) NOT NULL
    -- );


    -- Membuat tabel Transaksimerek
    CREATE TABLE tb_transaksi (
        id_transaksi      INT PRIMARY KEY AUTO_INCREMENT,
        id_user           INT,
        nama_pelanggan    VARCHAR(100),
        waktu_transaksi   DATETIME,       
        total_bayar       INT,
        status_pembayaran VARCHAR(50),
        nomor_kendaraan   VARCHAR(20),

        FOREIGN KEY (id_user) REFERENCES tb_user(id_user)

    );
    -- SEMENTARA TABEL MOTOR TIDAK ADA
    -- -- Membuat tabel motor 
    -- CREATE TABLE tb_motor (
    --     id_motor INT AUTO_INCREMENT PRIMARY KEY,
    --     nomor_plat VARCHAR(20),
    --     merek VARCHAR(50) NOT NULL,
    --     tipe VARCHAR(100) NOT NULL,
    --     nama_pelanggan VARCHAR(100)
    -- );


    -- Membuat tabel Restock
    CREATE TABLE tb_restock (
        id_restock     INT PRIMARY KEY AUTO_INCREMENT,
        id_user        INT,
        nama_supplier  VARCHAR(100),
        waktu_restock  DATETIME,
        total_biaya   INT,

        FOREIGN KEY (id_user) REFERENCES tb_user(id_user)
    );


    -- Membuat tabel detail transaksi data
    CREATE TABLE tb_detail_transaksi_jasa (
        id_detail_transaksi_jasa INT AUTO_INCREMENT PRIMARY KEY,
        id_transaksi INT NOT NULL,
        id_jasa INT NOT NULL,
        nama_mekanik VARCHAR(100) NOT NULL,
        tarif_jasa INT NOT NULL,
        sub_total INT NOT NULL,

        FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi(id_transaksi),
        FOREIGN KEY (id_jasa) REFERENCES tb_jasa(id_jasa)
    );


    -- membuat tabel detail transaksi sparepart
    CREATE TABLE detail_transaksi_sparepart (
        id_detail_sp  INT PRIMARY KEY AUTO_INCREMENT,
        id_transaksi  INT,
        id_sparepart  INT,
        jumlah        INT,
        harga_jual    INT,
        subtotal      INT,

        FOREIGN KEY (id_transaksi) REFERENCES tb_transaksi(id_transaksi),
        FOREIGN KEY (id_sparepart) REFERENCES tb_sparepart(id_sparepart)
    
    );

    -- Membuat tabel detail restock 
    CREATE TABLE tb_detail_restock (
        id_detail_restock INT AUTO_INCREMENT PRIMARY KEY,
        id_restock INT NOT NULL,
        id_sparepart INT NOT NULL,
        jumlah_restock INT NOT NULL,
        subtotal_restock INT NOT NULL,
        FOREIGN KEY (id_restock) REFERENCES tb_restock(id_restock),
        FOREIGN KEY (id_sparepart) REFERENCES tb_sparepart(id_sparepart)
    );


    SHOW TABLES;
