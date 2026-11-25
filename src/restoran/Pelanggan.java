package restoran;

/**
 * Pelanggan restoran. Pelanggan yang sudah memesan minimal 3 kali
 * ditandai sebagai member.
 */
public class Pelanggan {

    private static final int BATAS_MEMBER = 3;

    private String kode;
    private String nama;
    private String noTelepon;
    private int jumlahPesanan;
    private boolean member;

    // Konstruktor default
    public Pelanggan() {
        this.kode = "-";
        this.nama = "Tamu";
        this.noTelepon = "00000000";
        this.jumlahPesanan = 0;
        this.member = false;
    }

    // Konstruktor berparameter
    public Pelanggan(String kode, String nama, String noTelepon) {
        setKode(kode);
        setNama(nama);
        setNoTelepon(noTelepon);
        this.jumlahPesanan = 0;
        this.member = false;
    }

    // ---------- getter & setter ----------

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode pelanggan tidak boleh kosong.");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        // hanya angka, panjang 8 sampai 15 digit
        if (noTelepon == null || !noTelepon.trim().matches("\\d{8,15}")) {
            throw new IllegalArgumentException("Nomor telepon harus berupa 8-15 digit angka.");
        }
        this.noTelepon = noTelepon.trim();
    }

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public boolean isMember() {
        return member;
    }

    // ---------- perilaku ----------

    public void tambahJumlahPesanan() {
        jumlahPesanan++;
        perbaruiStatusMember();
    }

    public void kurangiJumlahPesanan() {
        if (jumlahPesanan > 0) {
            jumlahPesanan--;
        }
        perbaruiStatusMember();
    }

    private void perbaruiStatusMember() {
        member = jumlahPesanan >= BATAS_MEMBER;
    }

    public void tampilkanInfo() {
        System.out.printf("%-6s | %-20s | %-15s | pesanan: %d | %s%n",
                kode, nama, noTelepon, jumlahPesanan, member ? "MEMBER" : "reguler");
    }
}
