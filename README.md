# 🕌 ZISWAF DIGITAL COUNTER
### Kalkulator Syariat & Kasir Zakat, Infaq, Shadaqah Terpadu
**IT Incubation 2026 • Divisi Object-Oriented Programming (OOP)**  
**Komunitas Fun Java — Jurusan Teknik Informatika, Fakultas Sains dan Teknologi**  
**Universitas Islam Negeri Maulana Malik Ibrahim Malang**

> **Motto:** *Learn the logic. Practice the code. Build something.*  
> **Tema Kategori 7:** *Layanan Sosial, Filantropi & Keislaman (Kekhasan Kampus Islami Ulul Albab UIN Maliki Malang).*

---

## 📌 1. Latar Belakang & Deskripsi Kasus

Di lingkungan kampus Islami seperti **UIN Maulana Malik Ibrahim Malang**, lembaga amil zakat masjid kampus (**LAZIS Masjid Tarbiyah**) melayani ratusan muzakki dan munfiq dari kalangan dosen, tenaga kependidikan, mahasiswa ma'had, hingga jamaah masyarakat umum. 

Tantangan utama yang dihadapi adalah:
1. **Penghitungan Manual yang Rentan:** Banyak muzakki belum memahami cara menghitung nisab zakat mal (tabungan & emas) dan zakat profesi bulanan sesuai fatwa syariah.
2. **Akad Zakat Fitrah Ganda:** Pilihan pembayaran zakat fitrah dalam bentuk uang tunai (Rp 45.000/jiwa) maupun beras (2.5 kg/jiwa) sering membingungkan pencatatan pembukuan konvensional.
3. **Tuntutan Transparansi Publik:** Diperlukan pemisahan rekapitulasi dana kas antara **Zakat**, **Infaq Gedung/Sarana**, dan **Shadaqah Sosial** secara transparan dan akuntabel.
4. **Aspek Spiritual Serah Terima:** Sesuai sunnah Rasulullah SAW (QS. At-Taubah: 103), petugas amil wajib melafalkan doa serah terima zakat secara khusyuk saat muzakki menyerahkan dana.

**ZISWAF Digital Counter** hadir sebagai aplikasi desktop interaktif berbasis **Java OOP & GUI NetBeans Swing** yang menyelesaikan seluruh tantangan tersebut dengan elegan, akurat, dan berestetika tinggi.

---

## 🏗️ 2. Struktur Arsitektur Perangkat Lunak

Mengikuti cetak biru standar komunitas Fun Java (*Modul 3 Part 6*), kode program dirancang dengan pemisahan *layer* (Model-Storage-Util-View) yang bersih:

```
studi-case/
├── bin/                             <-- Folder output biner (.class)
├── src/
│   └── funjava/
│       ├── model/
│       │   ├── Muzakki.java         <-- OOP: Class Entitas Donatur & Enkapsulasi
│       │   └── TransaksiZakat.java  <-- OOP: Class Transaksi ZISWAF & Relasi Agregasi
│       ├── storage/
│       │   └── TransactionStorage.java <-- Data Persistence (File I/O buku_kas_ziswaf.txt)
│       ├── util/
│       │   └── ReceiptUtil.java     <-- Format Rupiah, Bobot Beras, Doa Amil, Generator Kuitansi
│       ├── view/
│       │   ├── DoaDialog.java       <-- GUI Modal: Dialog Doa Amil Zakat & Akad
│       │   └── MainFrame.java       <-- GUI Utama NetBeans Swing (Kasir, Buku Kas, Kalkulator)
│       ├── TestBackend.java         <-- Unit Test Verifikasi Logika & File I/O
│       └── ZiswafApp.java           <-- Main Entry Point (Look & Feel + Anti-Aliasing)
├── buku_kas_ziswaf.txt              <-- Berkas Persistensi Data Rekapitulasi Kas Non-Volatil
├── studi_kasus.md                   <-- Spesifikasi Studi Kasus Resmi
├── run.bat                          <-- Script Peluncur Sekali Klik (Windows Batch)
├── run.ps1                          <-- Script Peluncur Sekali Klik (PowerShell)
└── README.md                        <-- Dokumentasi Lengkap Proyek
```

---

## 💎 3. Pemenuhan Rancangan OOP Sesuai Panduan

### 3.1 Class `Muzakki` (`funjava.model.Muzakki`)
* **Atribut Private:** `nikNip`, `namaLengkap`, `noHp`, `alamat`, `kategori` (Dosen/Tendik, Mahasiswa UIN, Masyarakat Umum).
* **Enkapsulasi Penuh:** Seluruh atribut hanya dapat diakses melalui *getter* dan *setter* tervalidasi:
  * Jika nama atau NIK kosong, setter secara otomatis memberikan nilai *default* aman sehingga tidak memicu error `NullPointerException`.

### 3.2 Class `TransaksiZakat` (`funjava.model.TransaksiZakat`)
* **Atribut Private:** `idZiswaf`, `muzakki` (*Objek Muzakki*), `jenisDana`, `kategori` (`ZAKAT`, `INFAQ`, `SHADAQAH`), `nominalRupiah`, `berasKg`, `jumlahJiwa`, `keterangan`, `doaAkad`, `waktuTransaksi`.
* **Prinsip Relasi Agregasi:** Menggabungkan objek `Muzakki` ke dalam objek `TransaksiZakat`.
* **Serialisasi Berkas:** Menyediakan method `toRecordString()` dan `fromRecordString()` untuk menyimpan dan membaca data dari file teks dengan format delimiter pipe (`|`).

---

## 🎨 4. Desain GUI & Palet Identitas Visual Fun Java

Antarmuka dibangun menggunakan **Java Swing** dengan menerapkan palet warna resmi Komunitas Fun Java (*Modul 3 Part 4*):

| Elemen UI | Kode HEX | Nilai RGB | Penggunaan dalam Aplikasi |
| :--- | :---: | :---: | :--- |
| **Fun Java Primary Accent** | `#E57F3E` | `RGB(229, 127, 62)` | Tombol Simpan Transaksi, Badge Nominal, Tab Highlight |
| **Deep Roast Coffee** | `#1F1008` | `RGB(31, 16, 8)` | Latar Header Banner, Header Tabel `JTable` |
| **Warm Foam / Cream** | `#FDF3E7` | `RGB(253, 243, 231)` | Latar Belakang Form & Seleksi Baris Tabel |
| **Clean White** | `#FFFFFF` | `RGB(255, 255, 255)` | Kartu Form Konten, Panel Input Form |
| **Islamic Emerald Accent** | `#1B5E20` | `RGB(27, 94, 32)` | Tombol Doa Amil Zakat, Status Memenuhi Nisab |

### Fitur Antarmuka Utama:
1. **Tab 1: 🕌 Kasir & Penyetoran ZISWAF**
   * Form Data Muzakki (NIK/NIP/NIM, Nama, No. HP, Alamat, Status Sivitas).
   * Tombol `⚡ Isi Data Cepat Sivitas UIN` untuk mempermudah peragaan demo di hadapan juri.
   * **Kalkulator Zakat Mal Otomatis:** Input saldo tabungan/emas $\times 2.5\%$, otomatis mengecek nisab 85 gram emas (Rp 119.000.000).
   * **Pilihan Zakat Fitrah Dinamis:** Memilih pembayaran uang tunai (Rp 45.000/jiwa) atau beras (2.5 kg/jiwa) dengan spinner jiwa yang mengalkulasi subtotal secara langsung.
   * **Simulasi Zakat Profesi Dosen/Karyawan:** Menghitung $2.5\% \times (\text{Penghasilan} - \text{Pengeluaran Pokok})$.
   * **Pilihan Infaq Gedung & Sedekah Sosial Mahasiswa:** Input nominal bebas dengan pilihan peruntukan (Pembangunan Masjid, Al-Qur'an, Bantuan UKT Mahasiswa Dhuafa).
   * **Tombol Visual "Tampilkan Doa Amil Zakat" & Pop-up Kuitansi:** Membuka doa serah terima dan tanda terima resmi.
2. **Tab 2: 📊 Buku Kas & Rekapitulasi Transparan**
   * **4 Kartu Metrik Ringkasan:** Menampilkan total terpisah untuk Dana Zakat (Rp & Kg Beras), Dana Infaq (Rp), Dana Shadaqah (Rp), dan Grand Total Kas Terkumpul.
   * **Filter & Pencarian Instan:** Memfilter berdasarkan kategori (Semua/Zakat/Infaq/Shadaqah) dan mencari nama donatur atau ID transaksi secara *real-time*.
   * **Tabel Interaktif `JTable`:** Menampilkan histori transaksi yang dimuat dari berkas fisik.
   * **Tombol Cetak Ulang Bukti Setor & Ekspor Laporan Audit:** Menghasilkan laporan audit keuangan siap cetak.
3. **Tab 3: 🧮 Kalkulator Simulasi Syariah (Expo Live Station)**
   * Disediakan khusus bagi pengunjung stan Expo yang ingin menguji kewajiban zakat penghasilan mereka.
   * Dilengkapi dalil Al-Qur'an & rujukan fiqih (Fatwa MUI No. 3 Tahun 2003, QS. At-Taubah: 103, QS. Al-Baqarah: 261).
   * Fitur `➡️ Terapkan Nilai ini ke Tab Kasir` untuk langsung mentransfer hasil simulasi ke kasir transaksi.

---

## 💾 5. Data Persistence (`buku_kas_ziswaf.txt`)

Data tidak akan hilang saat komputer dimatikan atau aplikasi ditutup. Seluruh catatan tersimpan dalam berkas:
`buku_kas_ziswaf.txt`

Mekanisme persistensi dirancang dengan ketahanan tinggi:
* **Writing Mode (Append):** Menggunakan `PrintWriter` dan `FileOutputStream(..., true)` ber-encoding `UTF-8`.
* **Reading Mode:** Membaca baris demi baris menggunakan `BufferedReader`, mengabaikan baris komentar (`#`), dan mem-parsing data kembali menjadi objek `TransaksiZakat`.
* **Separate Balance Calculation:** Class `TransactionStorage` memiliki method khusus `calculateRekapitulasi()` dan `generateLaporanRekapitulasiText()` yang memisahkan pembukuan dana Zakat, Infaq, dan Shadaqah demi transparansi amil.

---

## 🚀 6. Cara Menjalankan Aplikasi

### Cara 1: Menggunakan Script Cepat (Windows)
Cukup klik ganda berkas `run.bat` di folder proyek, atau jalankan perintah berikut di terminal:
```powershell
.\run.bat
```
Atau menggunakan PowerShell:
```powershell
.\run.ps1
```

### Cara 2: Kompilasi & Jalankan Manual via Terminal
```bash
# 1. Buat folder bin
mkdir bin

# 2. Kompilasi seluruh file Java
javac -d bin -encoding UTF-8 src/funjava/model/*.java src/funjava/storage/*.java src/funjava/util/*.java src/funjava/view/*.java src/funjava/*.java

# 3. Jalankan aplikasi GUI
java -cp bin funjava.ZiswafApp
```

### Cara 3: Buka di Apache NetBeans IDE
1. Buka **Apache NetBeans IDE**.
2. Pilih menu **File $\rightarrow$ Open Project...**
3. Arahkan ke folder `studi-case`.
4. Klik kanan pada `ZiswafApp.java` $\rightarrow$ **Run File** (atau tekan `Shift + F6`).

---

## 🌟 7. Skenario WOW Factor Demo Expo 3 Menit (Di Depan Juri & Dosen Penguji)

Ikuti panduan berikut saat mendemonstrasikan aplikasi di stan pameran:

* **Menit 00.00 – 00.30 (The Hook & Identitas Nilai Keislaman):**  
  *"Assalamu'alaikum wr. wb. Selamat datang di Stan Kelompok Fun Java. Kami mempersembahkan **ZISWAF Digital Counter**, aplikasi kasir dan kalkulator syariat terpadu yang dirancang khusus untuk mewujudkan digitalisasi filantropi Islam di Masjid Tarbiyah UIN Maliki Malang dengan arsitektur OOP Java murni."*
* **Menit 00.30 – 01.30 (Live Demo Alur Transaksi & Kalkulator Syariat):**  
  1. Klik tombol `⚡ Isi Data Cepat Sivitas UIN` (data Dosen FST langsung terisi otomatis).
  2. Pilih menu **Zakat Profesi (Penghasilan)**.
  3. Masukkan gaji Rp 12.000.000 dan pengeluaran Rp 4.000.000. Tunjukkan bahwa sistem secara otomatis menghitung nisab bulanan (Rp 7.000.000) dan kadar zakat $2.5\%$ secara instan (Rp 200.000).
  4. Klik tombol `💾 Simpan Transaksi & Cetak Bukti Setor`.
  5. Tunjukkan dialog **Doa Amil Zakat** berharakat Arab dan terjemahan yang menyejukkan hati.
  6. Tampilkan **Bukti Setor ZISWAF Elektronik** lengkap dengan kode verifikasi sistem.
* **Menit 01.30 – 02.15 (The Persistence & Transparency WOW Factor):**  
  1. Buka tab **Buku Kas & Rekapitulasi Transparan**.
  2. Perlihatkan kepada dosen penguji bagaimana angka pada kartu metrik Zakat, Infaq, dan Shadaqah terpisah secara otomatis.
  3. **Tutup aplikasi di hadapan dosen penguji**. Buka kembali aplikasinya $\rightarrow$ Data transaksi yang baru diinput **tetap utuh dan terbaca kembali dari file `buku_kas_ziswaf.txt`**.
* **Menit 02.15 – 03.00 (Tanya Jawab Teknis):**  
  Jawab pertanyaan seputar OOP, Enkapsulasi, dan relasi Class dengan percaya diri.

---

## ❓ 8. Bank Pertanyaan Kritis Dosen Penguji & Juri Expo

> ❓ **Dosen Penguji:** *"Mana bukti nyata penerapan prinsip Enkapsulasi pada kode kalian?"*  
> 💡 **Jawaban Tim:**  
> *"Dapat dilihat pada file `Muzakki.java` dan `TransaksiZakat.java`. Seluruh atribut dideklarasikan dengan modifier `private`. Nilai atribut tidak dapat diubah sembarangan dari luar, melainkan harus melewati method setter yang telah dilengkapi validasi seperti pemeriksaan nilai kosong dan pencegahan nominal negatif `Math.max(0, nominal)`."*

> ❓ **Dosen Penguji:** *"Bagaimana aplikasi membedakan pencatatan Zakat, Infaq, dan Shadaqah?"*  
> 💡 **Jawaban Tim:**  
> *"Di dalam Class `TransaksiZakat`, terdapat atribut `kategori` yang secara otomatis mengklasifikasikan transaksi menjadi `ZAKAT`, `INFAQ`, atau `SHADAQAH`. Saat dibaca oleh `TransactionStorage.java`, method `calculateRekapitulasi()` menghitung masing-masing dana secara terpisah sehingga tidak tercampur antara dana zakat yang terikat mustahik syariat dan dana infaq/sedekah."*

> ❓ **Dosen Penguji:** *"Bagaimana jika file `buku_kas_ziswaf.txt` tidak sengaja terhapus?"*  
> 💡 **Jawaban Tim:**  
> *"Class `TransactionStorage` memiliki method `initStorage()` yang mendeteksi ketiadaan file secara otomatis saat aplikasi dibuka, lalu langsung membuatkan struktur file baru lengkap dengan header penjelas dan proteksi `try-catch` sehingga program tidak akan pernah mengalami force-close atau crash."*

---
**Komunitas Fun Java — Jurusan Teknik Informatika FST UIN Maulana Malik Ibrahim Malang**  
*“Mencetak Software Engineer Muda yang Berkarakter, Kompeten, dan Berdaya Saing Global.”*
# study_kasus_fun_java
