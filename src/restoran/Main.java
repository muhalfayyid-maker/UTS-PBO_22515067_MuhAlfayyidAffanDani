package restoran;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class Main: menu console aplikasi manajemen restoran mini.
 * Semua input dibaca lewat method bantu supaya salah ketik tidak membuat program crash.
 */
public class Main {

    private static final Scanner input = new Scanner(System.in);
    private static final Restoran restoran = new Restoran("Warung Makan Sedap", "Jl. Contoh No. 1");

    // ubah ke false kalau ingin mulai dengan data kosong
    private static final boolean MUAT_DATA_CONTOH = true;

    public static void main(String[] args) {
        if (MUAT_DATA_CONTOH) {
            isiDataContoh();
        }

        int pilihan = 0;
        try {
            do {
                tampilkanMenuUtama();
                pilihan = bacaInt("Pilih menu");
                try {
                    switch (pilihan) {
                        case 1:
                            menuDataMenu();
                            break;
                        case 2:
                            menuDataPelanggan();
                            break;
                        case 3:
                            buatPesanan();
                            break;
                        case 4:
                            menuCari();
                            break;
                        case 5:
                            menuAdt();
                            break;
                        case 6:
                            restoran.tampilkanRingkasan();
                            break;
                        case 7:
                            System.out.println("Terima kasih, program selesai.");
                            break;
                        default:
                            System.out.println("Error: pilihan harus antara 1 sampai 7.");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // pelanggaran aturan bisnis / validasi: tampilkan pesan, program lanjut
                    System.out.println("Error: " + e.getMessage());
                }
            } while (pilihan != 7);
        } catch (NoSuchElementException e) {
            System.out.println("\nInput berakhir, program ditutup.");
        }
    }

    // ================= tampilan menu =================

    private static void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("===== " + restoran.getNama().toUpperCase() + " =====");
        System.out.println("1. Kelola data menu");
        System.out.println("2. Kelola data pelanggan");
        System.out.println("3. Buat pesanan");
        System.out.println("4. Cari data");
        System.out.println("5. Riwayat pesanan (ADT Stack)");
        System.out.println("6. Ringkasan / laporan");
        System.out.println("7. Keluar");
    }

    // ================= menu 1: data menu =================

    private static void menuDataMenu() {
        System.out.println("\n-- Data Menu --");
        System.out.println("1. Tambah menu");
        System.out.println("2. Tampilkan semua menu");
        int pilih = bacaInt("Pilih");
        if (pilih == 1) {
            String kode = bacaTeks("Kode menu");
            String nama = bacaTeks("Nama menu");
            String kategori = bacaTeks("Kategori (Makanan/Minuman/dll)");
            double harga = bacaDouble("Harga");
            int stok = bacaInt("Stok awal");
            restoran.tambahMenu(new Menu(kode, nama, kategori, harga, stok));
            System.out.println("Menu berhasil ditambahkan.");
        } else if (pilih == 2) {
            tampilkanSemuaMenu();
        } else {
            System.out.println("Error: pilihan tidak valid.");
        }
    }

    private static void tampilkanSemuaMenu() {
        ArrayList<Menu> daftar = restoran.getDaftarMenu();
        if (daftar.isEmpty()) {
            System.out.println("(belum ada menu)");
            return;
        }
        for (Menu m : daftar) {
            m.tampilkanInfo();
        }
    }

    // ================= menu 2: data pelanggan =================

    private static void menuDataPelanggan() {
        System.out.println("\n-- Data Pelanggan --");
        System.out.println("1. Tambah pelanggan");
        System.out.println("2. Tampilkan semua pelanggan");
        int pilih = bacaInt("Pilih");
        if (pilih == 1) {
            String kode = bacaTeks("Kode pelanggan");
            String nama = bacaTeks("Nama pelanggan");
            String telp = bacaTeks("No. telepon");
            restoran.tambahPelanggan(new Pelanggan(kode, nama, telp));
            System.out.println("Pelanggan berhasil ditambahkan.");
        } else if (pilih == 2) {
            ArrayList<Pelanggan> daftar = restoran.getDaftarPelanggan();
            if (daftar.isEmpty()) {
                System.out.println("(belum ada pelanggan)");
            }
            for (Pelanggan p : daftar) {
                p.tampilkanInfo();
            }
        } else {
            System.out.println("Error: pilihan tidak valid.");
        }
    }

    // ================= menu 3: buat pesanan =================

    private static void buatPesanan() {
        System.out.println("\n-- Buat Pesanan --");
        if (restoran.getDaftarMenu().isEmpty() || restoran.getDaftarPelanggan().isEmpty()) {
            throw new IllegalStateException("Data menu dan pelanggan harus ada dulu.");
        }

        String kodePelanggan = bacaTeks("Kode pelanggan");
        Pelanggan pelanggan = restoran.cariPelangganByKode(kodePelanggan);
        if (pelanggan == null) {
            throw new IllegalStateException("Pelanggan dengan kode " + kodePelanggan + " tidak ditemukan.");
        }

        int noMeja = bacaInt("Nomor meja");
        Pesanan pesanan = restoran.buatPesananBaru(pelanggan, noMeja);

        tampilkanSemuaMenu();
        System.out.println("Tambahkan menu ke pesanan. Ketik SELESAI pada kode menu untuk mengakhiri.");

        while (true) {
            String kodeMenu = bacaTeks("Kode menu");
            if (kodeMenu.equalsIgnoreCase("SELESAI")) {
                break;
            }
            Menu menu = restoran.cariMenuByKode(kodeMenu);
            if (menu == null) {
                System.out.println("Error: menu dengan kode " + kodeMenu + " tidak ditemukan.");
                continue;
            }
            int jumlah = bacaInt("Jumlah porsi");
            String catatan = bacaTeksOpsional("Catatan (boleh kosong)");
            try {
                pesanan.tambahItem(menu, jumlah, catatan);
                System.out.println("Ditambahkan: " + menu.getNama() + " x" + jumlah);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // kalau pesanan kosong atau riwayat penuh, simpanPesanan melempar exception
        restoran.simpanPesanan(pesanan);
        System.out.println("Pesanan berhasil disimpan:");
        pesanan.tampilkanStruk();
    }

    // ================= menu 4: cari =================

    private static void menuCari() {
        System.out.println("\n-- Cari Data --");
        System.out.println("1. Cari menu (kode/nama)");
        System.out.println("2. Cari pelanggan (kode/nama)");
        System.out.println("3. Cari pesanan (kode)");
        int pilih = bacaInt("Pilih");
        if (pilih < 1 || pilih > 3) {
            System.out.println("Error: pilihan tidak valid.");
            return;
        }
        String kunci = bacaTeks("Kata kunci");

        if (pilih == 1) {
            ArrayList<Menu> hasil = restoran.cariMenu(kunci);
            if (hasil.isEmpty()) {
                System.out.println("Menu tidak ditemukan.");
            }
            for (Menu m : hasil) {
                m.tampilkanInfo();
            }
        } else if (pilih == 2) {
            ArrayList<Pelanggan> hasil = restoran.cariPelanggan(kunci);
            if (hasil.isEmpty()) {
                System.out.println("Pelanggan tidak ditemukan.");
            }
            for (Pelanggan p : hasil) {
                p.tampilkanInfo();
            }
        } else {
            Pesanan p = restoran.cariPesananByKode(kunci);
            if (p == null) {
                System.out.println("Pesanan tidak ditemukan.");
            } else {
                p.tampilkanStruk();
            }
        }
    }

    // ================= menu 5: ADT stack =================

    private static void menuAdt() {
        RiwayatPesananStack riwayat = restoran.getRiwayat();
        System.out.println("\n-- Riwayat Pesanan (Stack) --");
        System.out.println("(push terjadi otomatis setiap pesanan berhasil disimpan)");
        System.out.println("1. Tampilkan isi stack");
        System.out.println("2. Peek (lihat pesanan teratas)");
        System.out.println("3. Pop (batalkan pesanan terakhir)");
        System.out.println("4. Cek isEmpty / isFull / size");
        System.out.println("5. Kosongkan riwayat");
        int pilih = bacaInt("Pilih");

        switch (pilih) {
            case 1:
                riwayat.tampilkan();
                break;
            case 2:
                Pesanan atas = riwayat.peek();
                if (atas == null) {
                    System.out.println("Riwayat kosong, tidak ada yang bisa dilihat.");
                } else {
                    atas.tampilkanStruk();
                }
                break;
            case 3:
                Pesanan batal = restoran.batalkanPesananTerakhir();
                System.out.println("Pesanan " + batal.getKode() + " dibatalkan, stok dikembalikan.");
                break;
            case 4:
                System.out.println("isEmpty : " + riwayat.isEmpty());
                System.out.println("isFull  : " + riwayat.isFull());
                System.out.println("size    : " + riwayat.size() + " dari " + riwayat.getKapasitas());
                break;
            case 5:
                riwayat.kosongkan();
                System.out.println("Riwayat dikosongkan (pesanan tetap tercatat di restoran).");
                break;
            default:
                System.out.println("Error: pilihan tidak valid.");
        }
    }

    // ================= data contoh =================

    private static void isiDataContoh() {
        restoran.tambahMenu(new Menu("M01", "Nasi Goreng", "Makanan", 20000, 10));
        restoran.tambahMenu(new Menu("M02", "Ayam Bakar", "Makanan", 25000, 8));
        restoran.tambahMenu(new Menu("M03", "Es Teh", "Minuman", 5000)); // stok default
        restoran.tambahMenu(new Menu("M04", "Jus Jeruk", "Minuman", 12000, 2));

        restoran.tambahPelanggan(new Pelanggan("P01", "Budi", "081234567890"));
        restoran.tambahPelanggan(new Pelanggan("P02", "Siti", "082345678901"));
    }

    // ================= pembaca input yang aman =================

    private static String bacaTeks(String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = input.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Error: input tidak boleh kosong.");
        }
    }

    private static String bacaTeksOpsional(String label) {
        System.out.print(label + ": ");
        return input.nextLine().trim();
    }

    private static int bacaInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = input.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Error: masukkan angka bulat yang valid.");
            }
        }
    }

    private static double bacaDouble(String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = input.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Error: masukkan angka yang valid.");
            }
        }
    }
}
