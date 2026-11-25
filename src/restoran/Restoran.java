package restoran;

import java.util.ArrayList;

/**
 * Class utama yang mengelola data restoran: menu, pelanggan, dan pesanan.
 * Menyimpan daftar menu dan pelanggan (agregasi), serta pesanan yang pernah dibuat.
 * Riwayat pesanan terakhir disimpan di ADT Stack.
 */
public class Restoran {

    public static final int KAPASITAS_RIWAYAT = 5;

    private String nama;
    private String alamat;
    private ArrayList<Menu> daftarMenu;
    private ArrayList<Pelanggan> daftarPelanggan;
    private ArrayList<Pesanan> daftarPesanan;
    private RiwayatPesananStack riwayat;
    private int counterPesanan;

    public Restoran() {
        this("Restoran", "-");
    }

    public Restoran(String nama, String alamat) {
        this.nama = nama;
        this.alamat = alamat;
        this.daftarMenu = new ArrayList<>();
        this.daftarPelanggan = new ArrayList<>();
        this.daftarPesanan = new ArrayList<>();
        this.riwayat = new RiwayatPesananStack(KAPASITAS_RIWAYAT);
        this.counterPesanan = 0;
    }

    // ---------- getter ----------

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public ArrayList<Menu> getDaftarMenu() {
        return daftarMenu;
    }

    public ArrayList<Pelanggan> getDaftarPelanggan() {
        return daftarPelanggan;
    }

    public ArrayList<Pesanan> getDaftarPesanan() {
        return daftarPesanan;
    }

    public RiwayatPesananStack getRiwayat() {
        return riwayat;
    }

    // ---------- menu ----------

    public void tambahMenu(Menu menu) {
        if (cariMenuByKode(menu.getKode()) != null) {
            throw new IllegalStateException("Kode menu " + menu.getKode() + " sudah dipakai.");
        }
        daftarMenu.add(menu);
    }

    public Menu cariMenuByKode(String kode) {
        for (Menu m : daftarMenu) {
            if (m.getKode().equalsIgnoreCase(kode.trim())) {
                return m;
            }
        }
        return null;
    }

    /** Cari menu berdasarkan kode (persis) atau potongan nama. */
    public ArrayList<Menu> cariMenu(String kunci) {
        ArrayList<Menu> hasil = new ArrayList<>();
        String k = kunci.trim().toLowerCase();
        for (Menu m : daftarMenu) {
            if (m.getKode().toLowerCase().equals(k) || m.getNama().toLowerCase().contains(k)) {
                hasil.add(m);
            }
        }
        return hasil;
    }

    // ---------- pelanggan ----------

    public void tambahPelanggan(Pelanggan pelanggan) {
        if (cariPelangganByKode(pelanggan.getKode()) != null) {
            throw new IllegalStateException("Kode pelanggan " + pelanggan.getKode() + " sudah dipakai.");
        }
        daftarPelanggan.add(pelanggan);
    }

    public Pelanggan cariPelangganByKode(String kode) {
        for (Pelanggan p : daftarPelanggan) {
            if (p.getKode().equalsIgnoreCase(kode.trim())) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Pelanggan> cariPelanggan(String kunci) {
        ArrayList<Pelanggan> hasil = new ArrayList<>();
        String k = kunci.trim().toLowerCase();
        for (Pelanggan p : daftarPelanggan) {
            if (p.getKode().toLowerCase().equals(k) || p.getNama().toLowerCase().contains(k)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    // ---------- pesanan ----------

    /** Membuat objek pesanan baru (belum disimpan) dengan kode otomatis. */
    public Pesanan buatPesananBaru(Pelanggan pelanggan, int noMeja) {
        String kode = String.format("PSN-%03d", counterPesanan + 1);
        return new Pesanan(kode, pelanggan, noMeja);
    }

    /**
     * Menyimpan pesanan: cek aturan bisnis, kurangi stok, masukkan ke riwayat (push).
     */
    public void simpanPesanan(Pesanan pesanan) {
        if (pesanan.isKosong()) {
            throw new IllegalStateException("Pesanan harus memuat minimal 1 menu.");
        }
        if (riwayat.isFull()) {
            throw new IllegalStateException("Riwayat penuh (" + KAPASITAS_RIWAYAT
                    + " pesanan). Batalkan pesanan terakhir atau kosongkan riwayat dulu.");
        }

        // stok sudah dicek saat item ditambahkan, di sini baru benar-benar dikurangi
        for (ItemPesanan item : pesanan.getDaftarItem()) {
            item.getMenu().kurangiStok(item.getJumlah());
        }

        riwayat.push(pesanan);
        daftarPesanan.add(pesanan);
        pesanan.getPelanggan().tambahJumlahPesanan();
        counterPesanan++;
    }

    /** Membatalkan pesanan di puncak riwayat (pop), stok dikembalikan. */
    public Pesanan batalkanPesananTerakhir() {
        Pesanan pesanan = riwayat.pop();
        if (pesanan == null) {
            throw new IllegalStateException("Riwayat kosong, tidak ada pesanan yang bisa dibatalkan.");
        }
        for (ItemPesanan item : pesanan.getDaftarItem()) {
            item.getMenu().tambahStok(item.getJumlah());
        }
        pesanan.ubahStatus(Pesanan.STATUS_BATAL);
        pesanan.getPelanggan().kurangiJumlahPesanan();
        return pesanan;
    }

    public Pesanan cariPesananByKode(String kode) {
        for (Pesanan p : daftarPesanan) {
            if (p.getKode().equalsIgnoreCase(kode.trim())) {
                return p;
            }
        }
        return null;
    }

    // ---------- laporan ----------

    /** Total pendapatan dari pesanan yang tidak dibatalkan (sudah termasuk pajak). */
    public double hitungTotalPendapatan() {
        double total = 0;
        for (Pesanan p : daftarPesanan) {
            if (!Pesanan.STATUS_BATAL.equals(p.getStatus())) {
                total += p.hitungTotal();
            }
        }
        return total;
    }

    public void tampilkanRingkasan() {
        int aktif = 0;
        int batal = 0;
        double pajak = 0;
        for (Pesanan p : daftarPesanan) {
            if (Pesanan.STATUS_BATAL.equals(p.getStatus())) {
                batal++;
            } else {
                aktif++;
                pajak += p.hitungPajak();
            }
        }

        System.out.println("============ RINGKASAN " + nama.toUpperCase() + " ============");
        System.out.println("Jumlah menu          : " + daftarMenu.size());
        System.out.println("Jumlah pelanggan     : " + daftarPelanggan.size());
        System.out.println("Pesanan aktif        : " + aktif);
        System.out.println("Pesanan dibatalkan   : " + batal);
        System.out.println("Total pajak (10%)    : " + FormatUtil.rupiah(pajak));
        System.out.println("Total pendapatan     : " + FormatUtil.rupiah(hitungTotalPendapatan()));
        System.out.println("Riwayat (stack)      : " + riwayat.size() + "/" + riwayat.getKapasitas());
        System.out.println("=============================================");
    }
}
