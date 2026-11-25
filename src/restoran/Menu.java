package restoran;

/**
 * Menu makanan atau minuman yang dijual di restoran.
 * Menyimpan kode, nama, kategori, harga, dan stok porsi.
 */
public class Menu {

    private static final int STOK_AWAL_DEFAULT = 10;

    private String kode;
    private String nama;
    private String kategori;
    private double harga;
    private int stok;

    // Konstruktor default
    public Menu() {
        this.kode = "-";
        this.nama = "Tanpa Nama";
        this.kategori = "Umum";
        this.harga = 1000;
        this.stok = 0;
    }

    // Konstruktor berparameter (lengkap). Validasi dilewatkan lewat setter.
    public Menu(String kode, String nama, String kategori, double harga, int stok) {
        setKode(kode);
        setNama(nama);
        setKategori(kategori);
        setHarga(harga);
        setStok(stok);
    }

    // Overloading: kalau stok tidak diisi, dipakai stok awal default
    public Menu(String kode, String nama, String kategori, double harga) {
        this(kode, nama, kategori, harga, STOK_AWAL_DEFAULT);
    }

    // ---------- getter & setter ----------

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode menu tidak boleh kosong.");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama menu tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        if (kategori == null || kategori.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategori menu tidak boleh kosong.");
        }
        this.kategori = kategori.trim();
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        if (harga <= 0) {
            throw new IllegalArgumentException("Harga menu harus lebih dari 0.");
        }
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }
        this.stok = stok;
    }

    // ---------- perilaku ----------

    /** Menu dianggap tersedia kalau stoknya masih ada. */
    public boolean isTersedia() {
        return stok > 0;
    }

    /** Mengurangi stok saat pesanan disimpan. */
    public void kurangiStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pengurangan stok harus lebih dari 0.");
        }
        if (jumlah > stok) {
            throw new IllegalStateException("Stok " + nama + " tidak cukup (sisa " + stok + ").");
        }
        stok -= jumlah;
    }

    /** Mengembalikan stok, dipakai saat pesanan dibatalkan. */
    public void tambahStok(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah penambahan stok harus lebih dari 0.");
        }
        stok += jumlah;
    }

    public void tampilkanInfo() {
        System.out.printf("%-6s | %-20s | %-10s | %10s | stok: %d%n",
                kode, nama, kategori, FormatUtil.rupiah(harga), stok);
    }
}
