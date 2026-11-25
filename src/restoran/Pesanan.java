package restoran;

import java.util.ArrayList;

/**
 * Pesanan seorang pelanggan. Terdiri dari beberapa ItemPesanan (komposisi),
 * dan dikaitkan dengan satu Pelanggan (asosiasi).
 * Pajak 10 persen dihitung otomatis dari subtotal.
 */
public class Pesanan {

    public static final double PAJAK = 0.10;
    public static final String STATUS_DIPESAN = "DIPESAN";
    public static final String STATUS_SELESAI = "SELESAI";
    public static final String STATUS_BATAL = "DIBATALKAN";

    private String kode;
    private Pelanggan pelanggan;
    private int noMeja;
    private String status;
    private ArrayList<ItemPesanan> daftarItem;

    public Pesanan() {
        this.kode = "-";
        this.pelanggan = null;
        this.noMeja = 1;
        this.status = STATUS_DIPESAN;
        this.daftarItem = new ArrayList<>();
    }

    public Pesanan(String kode, Pelanggan pelanggan, int noMeja) {
        setKode(kode);
        setPelanggan(pelanggan);
        setNoMeja(noMeja);
        this.status = STATUS_DIPESAN;
        this.daftarItem = new ArrayList<>();
    }

    // ---------- getter & setter ----------

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode pesanan tidak boleh kosong.");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Pesanan harus memiliki pelanggan.");
        }
        this.pelanggan = pelanggan;
    }

    public int getNoMeja() {
        return noMeja;
    }

    public void setNoMeja(int noMeja) {
        if (noMeja <= 0) {
            throw new IllegalArgumentException("Nomor meja harus lebih dari 0.");
        }
        this.noMeja = noMeja;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!STATUS_DIPESAN.equals(status) && !STATUS_SELESAI.equals(status)
                && !STATUS_BATAL.equals(status)) {
            throw new IllegalArgumentException("Status pesanan tidak dikenal: " + status);
        }
        this.status = status;
    }

    public ArrayList<ItemPesanan> getDaftarItem() {
        return daftarItem;
    }

    // ---------- perilaku ----------

    /**
     * Menambah menu ke pesanan. Kalau menu yang sama sudah ada, jumlahnya
     * digabung. Stok dicek di sini, tapi stok baru dikurangi saat pesanan disimpan.
     */
    public void tambahItem(Menu menu, int jumlah, String catatan) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu tidak ditemukan.");
        }
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah porsi harus lebih dari 0.");
        }

        ItemPesanan sudahAda = cariItem(menu);
        int jumlahSekarang = (sudahAda == null) ? 0 : sudahAda.getJumlah();

        if (jumlahSekarang + jumlah > menu.getStok()) {
            throw new IllegalStateException("Stok " + menu.getNama() + " tidak cukup (sisa "
                    + menu.getStok() + ", diminta " + (jumlahSekarang + jumlah) + ").");
        }

        if (sudahAda != null) {
            sudahAda.tambahJumlah(jumlah);
        } else {
            daftarItem.add(new ItemPesanan(menu, jumlah, catatan));
        }
    }

    private ItemPesanan cariItem(Menu menu) {
        for (ItemPesanan item : daftarItem) {
            if (item.getMenu() == menu) {
                return item;
            }
        }
        return null;
    }

    public boolean isKosong() {
        return daftarItem.isEmpty();
    }

    public double hitungSubtotal() {
        double total = 0;
        for (ItemPesanan item : daftarItem) {
            total += item.hitungSubtotal();
        }
        return total;
    }

    public double hitungPajak() {
        return hitungSubtotal() * PAJAK;
    }

    public double hitungTotal() {
        return hitungSubtotal() + hitungPajak();
    }

    public void ubahStatus(String statusBaru) {
        // pesanan yang sudah batal tidak boleh diubah lagi
        if (STATUS_BATAL.equals(this.status)) {
            throw new IllegalStateException("Pesanan " + kode + " sudah dibatalkan.");
        }
        setStatus(statusBaru);
    }

    public void tampilkanStruk() {
        System.out.println("--------------------------------------------");
        System.out.println("Pesanan   : " + kode + "  (" + status + ")");
        System.out.println("Pelanggan : " + pelanggan.getNama() + "   Meja: " + noMeja);
        System.out.println("--------------------------------------------");
        for (ItemPesanan item : daftarItem) {
            item.tampilkanInfo();
        }
        System.out.println("--------------------------------------------");
        System.out.printf("Subtotal       : %s%n", FormatUtil.rupiah(hitungSubtotal()));
        System.out.printf("Pajak (10%%)    : %s%n", FormatUtil.rupiah(hitungPajak()));
        System.out.printf("Total bayar    : %s%n", FormatUtil.rupiah(hitungTotal()));
        System.out.println("--------------------------------------------");
    }
}
