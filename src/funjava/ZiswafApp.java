package funjava;

import funjava.view.MainFrame;

import javax.swing.*;

/**
 * ZiswafApp - Main Entry Point
 * IT Incubation 2026 • Komunitas Fun Java
 * Fakultas Sains dan Teknologi • UIN Maulana Malik Ibrahim Malang
 * 
 * Kasir & Kalkulator Zakat, Infaq, Shadaqah (ZISWAF Digital Counter)
 */
public class ZiswafApp {
    public static void main(String[] args) {
        // Aktifkan rendering teks font halus (Anti-Aliasing)
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set Look and Feel (Modern Windows / Nimbus / System)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName()) || "Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Gunakan system fallback
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        // Jalankan MainFrame pada Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
