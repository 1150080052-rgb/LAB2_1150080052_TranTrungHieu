package week_02;

public class frm_RC4 extends SimpleCipherForm {

    public frm_RC4() {
        super("RC4 Cipher - Trần Trung Hiếu - 1150080052",
                "MẬT MÃ DÒNG RC4",
                "RC4",
                "Key",
                "1 - 256 byte; bản mã hiển thị dạng HEX",
                "rc4_ciphertext.txt",
                RC4Cipher::encrypt,
                RC4Cipher::decrypt);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frm_RC4().setVisible(true));
    }
}