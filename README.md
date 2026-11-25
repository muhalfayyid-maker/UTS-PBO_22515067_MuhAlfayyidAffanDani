# UTS Object Oriented Programming — Manajemen Restoran Mini

Project take-home individu untuk mata kuliah Object Oriented Programming.
Aplikasi console berbasis Java untuk mengelola menu, pelanggan, dan pesanan
sebuah restoran kecil. Data disimpan di memori selama program berjalan.

| | |
|---|---|
| Nama | Muh. Alfayyid Affan Dani |
| NIM | 22515067 |
| Program Studi | Sistem dan Teknologi Informasi |
| Topik | Restoran (digit terakhir NIM = 7) |
| ADT | Stack (digit kedua dari belakang 6, 6 mod 3 = 0) |
| Dosen | Muhammad Avied Bachmid, S.Kom., M.Kom. |

## Fitur

1. Kelola data menu (tambah dan tampilkan)
2. Kelola data pelanggan (tambah dan tampilkan)
3. Buat pesanan yang menghubungkan pelanggan dengan beberapa menu
4. Cari menu, pelanggan (kode atau nama), dan pesanan (kode)
5. Riwayat pesanan dengan ADT Stack: tampilkan, peek, pop (batalkan pesanan terakhir), cek isEmpty/isFull/size, kosongkan
6. Ringkasan dan laporan sederhana (jumlah data, pajak, total pendapatan)
7. Keluar

## Aturan bisnis

- Pesanan minimal memuat 1 menu.
- Pajak 10 persen dihitung otomatis dari subtotal.
- Stok menu tidak boleh negatif. Stok berkurang saat pesanan disimpan dan kembali saat pesanan dibatalkan.
- Harga menu harus lebih dari 0.
- Riwayat pesanan berkapasitas 5. Kalau penuh, pesanan baru ditolak sampai ada yang dibatalkan atau riwayat dikosongkan.
- Setiap pelanggaran aturan menampilkan pesan error, program tidak berhenti.

## Struktur project

```
.
├── src/
│   └── restoran/
│       ├── Main.java                 # menu console
│       ├── Restoran.java             # pengelola data dan logika utama
│       ├── Menu.java
│       ├── Pelanggan.java
│       ├── ItemPesanan.java
│       ├── Pesanan.java
│       ├── RiwayatPesananStack.java  # ADT Stack
│       └── FormatUtil.java           # format rupiah
├── docs/
│   └── class-diagram.mmd             # class diagram (Mermaid)
└── .gitignore
```

## Relasi antarclass

- `Restoran` o-- `Menu` dan `Pelanggan` (agregasi)
- `Restoran` *-- `Pesanan` dan `RiwayatPesananStack` (komposisi)
- `Pesanan` --> `Pelanggan` (asosiasi)
- `Pesanan` *-- `ItemPesanan` (komposisi)
- `ItemPesanan` --> `Menu` (asosiasi)
- `RiwayatPesananStack` o-- `Pesanan` (agregasi)

## Cara menjalankan

Dibutuhkan JDK 11 atau lebih baru.

```bash
# dari folder utama project
mkdir out
javac -d out src/restoran/*.java
java -cp out restoran.Main
```

Folder `out` sudah masuk `.gitignore`, jadi tidak ikut ter-commit.

Secara bawaan program memuat beberapa data contoh (4 menu dan 2 pelanggan)
supaya mudah dicoba. Untuk memulai dari data kosong, ubah konstanta
`MUAT_DATA_CONTOH` di `Main.java` menjadi `false`.

## Catatan

- Operasi `push` pada stack berjalan otomatis setiap pesanan berhasil disimpan.
- Class diagram bisa dibuka lewat draw.io (Arrange → Insert → Advanced → Mermaid)
  atau tool lain yang mendukung Mermaid.

> Repository ini dikelola dengan riwayat commit bertahap untuk dokumentasi UTS PBO.
