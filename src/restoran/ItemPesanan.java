package restoran;

/**
 * Satu baris dalam pesanan: sebuah menu beserta jumlahnya.
 * Harga satuan disalin saat item dibuat supaya perubahan harga menu
 * di kemudian hari tidak mengubah pesanan yang sudah ada.
 */
public class ItemPesanan {

    private Menu menu;
    private int jumlah;
    private double hargaSatuan;
    private String catatan;

    public ItemPesanan() {
        this.menu = null;
        this.jumlah = 1;
        this.hargaSatuan = 0;
        this.catatan = "";
    }

    public ItemPesanan(Menu menu, int jumlah, String catatan) {
        setMenu(menu);
        setJumlah(jumlah);
        this.hargaSatuan = menu.getHarga();
        setCatatan(catatan);
    }

    // ---------- getter & setter ----------

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu pada item pesanan tidak boleh kosong.");
        }
        this.menu = menu;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah porsi harus lebih dari 0.");
        }
        this.jumlah = jumlah;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        // catatan boleh kosong, tapi tidak boleh null
        this.catatan = (catatan == null) ? "" : catatan.trim();
    }

    // ---------- perilaku ----------

    public double hitungSubtotal() {
        return hargaSatuan * jumlah;
    }

    public void tambahJumlah(int tambahan) {
        if (tambahan <= 0) {
            throw new IllegalArgumentException("Jumlah tambahan harus lebih dari 0.");
        }
        this.jumlah += tambahan;
    }

    public void tampilkanInfo() {
        System.out.printf("  %-20s x%-3d %12s%s%n",
                menu.getNama(), jumlah, FormatUtil.rupiah(hitungSubtotal()),
                catatan.isEmpty() ? "" : "  (" + catatan + ")");
    }
}
