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

public class SimpleCipherForm extends JFrame {

    public interface Op {
        String run(String input, String key) throws Exception;
    }

    protected final JTextArea txt_plaintext = new JTextArea(3, 50);
    protected final JTextField txt_key;
    protected final JTextArea txt_ciphertext = new JTextArea(3, 50);
    protected final JButton btn_encrypt = new JButton("Encrypt");
    protected final JButton btn_decrypt = new JButton("Decrypt");
    protected final JButton btn_save = new JButton("Save File");
    protected final JButton btn_open = new JButton("Open File");
    protected final JLabel lbl_status = new JLabel("Sẵn sàng.");

    private final String algName;
    private final String defaultFileName;
    private final Op encryptOp;
    private final Op decryptOp;

    protected SimpleCipherForm(String windowTitle, String heading, String algName, String defaultKey,
            String keyHint, String defaultFileName, Op encryptOp, Op decryptOp) {
        this.algName = algName;
        this.defaultFileName = defaultFileName;
        this.encryptOp = encryptOp;
        this.decryptOp = decryptOp;
        this.txt_key = new JTextField(defaultKey, 26);

        setTitle(windowTitle);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi(heading, keyHint);
        btn_encrypt.addActionListener(e -> doEncrypt());
        btn_decrypt.addActionListener(e -> doDecrypt());
        btn_save.addActionListener(e -> doSave());
        btn_open.addActionListener(e -> doOpen());
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUi(String heading, String keyHint) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;

        JLabel title = new JLabel(heading);
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
        keyRow.add(new JLabel(keyHint));
        c.gridx = 0;
        c.gridy = 2;
        p.add(new JLabel("Key:"), c);
        c.gridx = 1;
        p.add(keyRow, c);

        c.gridx = 0;
        c.gridy = 3;
        p.add(new JLabel("Ciphertext:"), c);
        c.gridx = 1;
        p.add(new JScrollPane(txt_ciphertext), c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        buttons.add(btn_save);
        buttons.add(btn_open);
        c.gridx = 1;
        c.gridy = 4;
        p.add(buttons, c);

        c.gridx = 0;
        c.gridy = 5;
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
            String ciphertext = encryptOp.run(plaintext, txt_key.getText());
            txt_ciphertext.setText(ciphertext);
            lbl_status.setText("<html>Đã mã hóa " + algName + ". " + HistoryDao.tryInsert(algName, "ENCRYPT", null,
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
            String plaintext = decryptOp.run(ciphertext, txt_key.getText());
            txt_plaintext.setText(plaintext);
            lbl_status.setText("<html>Đã giải mã " + algName + ". " + HistoryDao.tryInsert(algName, "DECRYPT", null,
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
        String path = FileTextIO.saveText(this, txt_ciphertext, defaultFileName);
        if (path != null) {
            lbl_status.setText("Đã lưu tệp: " + path);
        }
    }

    private void doOpen() {
        String path = FileTextIO.openText(this, txt_ciphertext);
        if (path != null) {
            lbl_status.setText("Đã mở tệp: " + path);
        }
    }
}