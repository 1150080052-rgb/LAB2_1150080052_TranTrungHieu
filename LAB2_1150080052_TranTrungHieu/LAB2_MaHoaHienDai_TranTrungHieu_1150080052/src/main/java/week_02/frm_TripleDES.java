package week_02;

public class frm_TripleDES extends SimpleCipherForm {

    public frm_TripleDES() {
        super("TripleDES Cipher - Trần Trung Hiếu - 1150080052",
                "MẬT MÃ TRIPLEDES (3DES)",
                "TripleDES",
                "123456789012345678901234",
                "Đúng 24 ký tự (24 byte)",
                "tripledes_ciphertext.txt",
                TripleDESCipher::encrypt,
                TripleDESCipher::decrypt);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frm_TripleDES().setVisible(true));
    }
}