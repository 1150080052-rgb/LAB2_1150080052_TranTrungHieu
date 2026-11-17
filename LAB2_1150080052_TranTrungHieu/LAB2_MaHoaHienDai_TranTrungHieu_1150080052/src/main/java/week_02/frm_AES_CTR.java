package week_02;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class frm_AES_CTR extends JFrame {

    private final JTextArea txt_plaintext = new JTextArea(3, 50);
    private final JTextField txt_key = new JTextField(40);
    private final JButton btn_genkey = new JButton("Generate Key");
    private final JTextField txt_nonce = new JTextField(40);
    private final JButton btn_gennonce = new JButton("Generate Nonce");
    private final JTextArea txt_ciphertext = new JTextArea(3, 50);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_save = new JButton("Save File");
    private final JButton btn_open = new JButton("Open File");
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");

    public frm_AES_CTR() {
        setTitle("AES-CTR Cipher - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
        btn_genkey.addActionListener(e -> {
            txt_key.setText(AESCTRCipher.newKey());
            lbl_status.setText("Đã tạo khóa ngẫu nhiên 256 bit.");
        });
        btn_gennonce.addActionListener(e -> {
            txt_nonce.setText(AESCTRCipher.newNonce());
            lbl_status.setText("Đã tạo nonce ngẫu nhiên 96 bit. Mỗi lần mã hóa nên tạo nonce mới.");
        });
        btn_encrypt.addActionListener(e -> doEncrypt());
        btn_decrypt.addActionListener(e -> doDecrypt());
        btn_save.addActionListener(e -> doSave());
        btn_open.addActionListener(e -> doOpen());
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;

        JLabel title = new JLabel("BÀI NÂNG CAO 01 - AES-256 CHẾ ĐỘ CTR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        p.add(title, c);
        c.gridwidth = 1;

        txt_plaintext.setLineWrap(true);
        txt_ciphertext.setLineWrap(true);

        c.gridx = 0;
        c.gridy = 1;
        p.add(new JLabel("Plaintext:"), c);
        c.gridx = 1;
        p.add(new JScrollPane(txt_plaintext), c);

        JPanel keyRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        keyRow.add(txt_key);
        keyRow.add(btn_genkey);
        c.gridx = 0;
        c.gridy = 2;
        p.add(new JLabel("Key (Base64):"), c);
        c.gridx = 1;
        p.add(keyRow, c);

        JPanel nonceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        nonceRow.add(txt_nonce);
        nonceRow.add(btn_gennonce);
        c.gridx = 0;
        c.gridy = 3;
        p.add(new JLabel("Nonce (Base64):"), c);
        c.gridx = 1;
        p.add(nonceRow, c);

        c.gridx = 0;
        c.gridy = 4;
        p.add(new JLabel("Ciphertext:"), c);
        c.gridx = 1;
        p.add(new JScrollPane(txt_ciphertext), c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        buttons.add(btn_save);
        buttons.add(btn_open);
        c.gridx = 1;
        c.gridy = 5;
        p.add(buttons, c);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        p.add(lbl_status, c);

        setContentPane(p);
    }

    private void doEncrypt() {
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            String ciphertext = AESCTRCipher.encrypt(plaintext, txt_key.getText(), txt_nonce.getText());
            txt_ciphertext.setText(ciphertext);
            lbl_status.setText("<html>Đã mã hóa AES-CTR. " + HistoryDao.tryInsert("AES-CTR", "ENCRYPT", null,
                    plaintext.length(), ciphertext.length()) + "</html>");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Mã hóa thất bại:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doDecrypt() {
        String ciphertext = txt_ciphertext.getText();
        if (ciphertext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext trước khi giải mã.");
            return;
        }
        try {
            String plaintext = AESCTRCipher.decrypt(ciphertext, txt_key.getText(), txt_nonce.getText());
            txt_plaintext.setText(plaintext);
            lbl_status.setText("<html>Đã giải mã AES-CTR. " + HistoryDao.tryInsert("AES-CTR", "DECRYPT", null,
                    ciphertext.length(), plaintext.length()) + "</html>");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Giải mã thất bại: sai Key hoặc bản mã bị sửa.\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doSave() {
        if (txt_ciphertext.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Chưa có bản mã để lưu.");
            return;
        }
        String content = AESCTRCipher.toFile(txt_nonce.getText(), txt_ciphertext.getText());
        String path = FileTextIO.saveString(this, content, "aes_ctr_ciphertext.txt");
        if (path != null) {
            lbl_status.setText("Đã lưu nonce + bản mã (không lưu Key) vào: " + path);
        }
    }

    private void doOpen() {
        String content = FileTextIO.openString(this);
        if (content == null) {
            return;
        }
        try {
            String[] parts = AESCTRCipher.fromFile(content);
            txt_nonce.setText(parts[0]);
            txt_ciphertext.setText(parts[1]);
            lbl_status.setText("Đã mở tệp. Nhập đúng Key rồi bấm Decrypt.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Tệp không hợp lệ", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frm_AES_CTR().setVisible(true));
    }
}