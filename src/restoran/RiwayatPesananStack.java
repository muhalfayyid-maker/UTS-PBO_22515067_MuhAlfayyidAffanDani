package restoran;

/**
 * ADT Stack (LIFO) untuk menyimpan riwayat pesanan terakhir.
 * Dipakai untuk fitur "batalkan pesanan terakhir".
 *
 * Isi stack disimpan dalam array Pesanan berkapasitas tetap.
 * Semua atribut private, akses hanya lewat operasi publik di bawah.
 * Kondisi penuh/kosong ditangani lewat nilai kembalian, bukan dengan crash.
 */
public class RiwayatPesananStack {

    private final Pesanan[] data;
    private final int kapasitas;
    private int top; // indeks elemen teratas, -1 artinya kosong

    public RiwayatPesananStack(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas stack harus lebih dari 0.");
        }
        this.kapasitas = kapasitas;
        this.data = new Pesanan[kapasitas];
        this.top = -1;
    }

    /** Menaruh pesanan di puncak stack. Mengembalikan false kalau stack penuh. */
    public boolean push(Pesanan pesanan) {
        if (pesanan == null) {
            throw new IllegalArgumentException("Pesanan yang di-push tidak boleh null.");
        }
        if (isFull()) {
            return false;
        }
        top++;
        data[top] = pesanan;
        return true;
    }

    /** Mengambil dan membuang pesanan teratas. Mengembalikan null kalau kosong. */
    public Pesanan pop() {
        if (isEmpty()) {
            return null;
        }
        Pesanan hasil = data[top];
        data[top] = null; // lepas referensi supaya tidak menggantung
        top--;
        return hasil;
    }

    /** Melihat pesanan teratas tanpa membuangnya. Null kalau kosong. */
    public Pesanan peek() {
        if (isEmpty()) {
            return null;
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == kapasitas - 1;
    }

    public int size() {
        return top + 1;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    /** Mengosongkan seluruh isi stack. */
    public void kosongkan() {
        while (!isEmpty()) {
            pop();
        }
    }

    /** Menampilkan isi stack dari puncak ke dasar. */
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("(riwayat kosong)");
            return;
        }
        System.out.println("Isi riwayat (atas -> bawah), terisi " + size() + "/" + kapasitas + ":");
        for (int i = top; i >= 0; i--) {
            Pesanan p = data[i];
            String penanda = (i == top) ? "[TOP] " : "      ";
            System.out.printf("%s%s | %-15s | meja %d | %s%n", penanda, p.getKode(),
                    p.getPelanggan().getNama(), p.getNoMeja(), FormatUtil.rupiah(p.hitungTotal()));
        }
    }
}
