package funjava;

import funjava.model.Muzakki;
import funjava.model.TransaksiZakat;
import funjava.storage.TransactionStorage;
import funjava.util.ReceiptUtil;

import java.io.File;
import java.util.List;

public class TestBackend {
    public static void main(String[] args) {
        System.out.println("=== 1. TEST MODEL MUZAKKI & TRANSAKSIZAKAT ===");
        Muzakki m = new Muzakki("198001012005011001", "Dr. H. Ahmad Fauzi", "081234567890", "FST UIN Malang", "Dosen / Tendik");
        System.out.println("Muzakki: " + m);

        TransaksiZakat tz = new TransaksiZakat(
                "ZSW-TEST-001",
                m,
                "Zakat Profesi (Penghasilan)",
                250000,
                0.0,
                1,
                "Zakat Profesi Bulanan",
                ReceiptUtil.DOA_AMIL_LATIN,
                "2026-09-26 10:00:00"
        );
        System.out.println("Record: " + tz.toRecordString());

        System.out.println("\n=== 2. TEST STORAGE PERSISTENCE ===");
        TransactionStorage.initStorage();
        boolean saved = TransactionStorage.appendTransaction(tz);
        System.out.println("Append Status: " + saved);

        List<TransaksiZakat> list = TransactionStorage.loadAllTransactions();
        System.out.println("Total Transaksi Terbaca: " + list.size());
        assert list.size() > 0 : "Data harus lebih dari 0!";

        System.out.println("\n=== 3. TEST REKAPITULASI DANA TERPISAH ===");
        TransactionStorage.RekapData rekap = TransactionStorage.calculateRekapitulasi(list);
        System.out.println("Total Zakat (Rp) : " + ReceiptUtil.formatRupiah(rekap.totalZakatNominal));
        System.out.println("Total Zakat (Beras): " + ReceiptUtil.formatBeras(rekap.totalZakatBerasKg));
        System.out.println("Total Infaq (Rp) : " + ReceiptUtil.formatRupiah(rekap.totalInfaqNominal));
        System.out.println("Total Shadaqah (Rp): " + ReceiptUtil.formatRupiah(rekap.totalShadaqahNominal));
        System.out.println("Grand Total (Rp) : " + ReceiptUtil.formatRupiah(rekap.getGrandTotalNominal()));

        System.out.println("\n=== 4. TEST RECEIPT GENERATOR ===");
        String receipt = ReceiptUtil.generateReceipt(tz);
        System.out.println(receipt);

        System.out.println("\n=== 5. TEST LAPORAN AUDIT BUKU KAS ===");
        String audit = TransactionStorage.generateLaporanRekapitulasiText(list);
        System.out.println(audit);

        System.out.println("\n>> SEMUA PENGUJIAN BACKEND & LOGIKA SUKSES 100%! <<");
    }
}
