package BankSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class DeleteCustomer extends JInternalFrame implements ActionListener {

	private JPanel jpDel = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal;
	private JButton btnDel, btnCancel;

	private int recCount = 0;
	private int rows = 0;
	private int total = 0;

	// Mảng 2 chiều để lưu trữ bản ghi dữ liệu.
	private String records[][] = new String[500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	DeleteCustomer() {
		super("Xóa tài khoản", false, true, false, true);
		setSize(350, 235);

		jpDel.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 90, 25);
		lbDate = new JLabel("Giao dịch cuối:");
		lbDate.setForeground(Color.black);
		lbDate.setBounds(15, 90, 100, 25);
		lbBal = new JLabel("Số dư:");
		lbBal.setForeground(Color.black);
		lbBal.setBounds(15, 125, 80, 25);

		txtNo = new JTextField();
		txtNo.setHorizontalAlignment(JTextField.RIGHT);
		txtNo.setBounds(125, 20, 200, 25);
		txtName = new JTextField();
		txtName.setEnabled(false);
		txtName.setBounds(125, 55, 200, 25);
		txtDate = new JTextField();
		txtDate.setEnabled(false);
		txtDate.setBounds(125, 90, 200, 25);
		txtBal = new JTextField();
		txtBal.setEnabled(false);
		txtBal.setHorizontalAlignment(JTextField.RIGHT);
		txtBal.setBounds(125, 125, 200, 25);

		// Căn chỉnh 2 nút.
		btnDel = new JButton("Delete");
		btnDel.setBounds(20, 165, 120, 25);
		btnDel.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(200, 165, 120, 25);
		btnCancel.addActionListener(this);

		// Thêm các nút vào bảng điều khiển.
		jpDel.add(lbNo);
		jpDel.add(txtNo);
		jpDel.add(lbName);
		jpDel.add(txtName);
		jpDel.add(lbDate);
		jpDel.add(txtDate);
		jpDel.add(lbBal);
		jpDel.add(txtBal);
		jpDel.add(btnDel);
		jpDel.add(btnCancel);

		// Hạn chế đầu vào của người dùng chỉ với các chữ số trong các văn bản dạng số.
		txtNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
				}
			}
		});
		// Kiểm tra số tài khoản do người dùng cung cấp.
		txtNo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent fe) {
				if (txtNo.getText().equals("")) {
				} else {
					rows = 0;
					populateArray(); // Tải tất cả các bản ghi hiện có vào bộ nhớ.
					findRec(); // Tìm tài khoản đã tồn tại hay chưa.
				}
			}
		});

		// Thêm bảng điều khiển vào cửa sổ.
		getContentPane().add(jpDel);

		populateArray(); // Tải tất cả các bản ghi hiện có vào bộ nhớ.

		// Hiển thị Cửa sổ Tài khoản Mới.
		setVisible(true);

	}

	// Thực hiện Hành động khi Người dùng nhấp vào Nút.
	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnDel) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp Id của khách hàng.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			} else {
				deletion(); // Xác nhận Xóa Bản ghi Hiện tại.
			}
		}
		if (obj == btnCancel) {
			txtClear();
			setVisible(false);
			dispose();
		}

	}

	// tải tất cả các Bản ghi từ Tệp.
	void populateArray() {

		try {
			fis = new FileInputStream("Bank.dat");
			dis = new DataInputStream(fis);
			// Loop to Populate the Array.
			while (true) {
				for (int i = 0; i < 6; i++) {
					records[rows][i] = dis.readUTF();
				}
				rows++;
			}
		} catch (Exception ex) {
			total = rows;
			if (total == 0) {
				JOptionPane.showMessageDialog(null, "Không có bản ghi.\nNhập Bản ghi trước để hiển thị.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				btnEnable();
			} else {
				try {
					dis.close();
					fis.close();
				} catch (Exception exp) {
				}
			}
		}

	}

	// Tìm bản ghi bằng cách so sánh với các bản ghi có trong mảng
	void findRec() {

		boolean found = false;
		for (int x = 0; x < total; x++) {
			if (records[x][0].equals(txtNo.getText())) {
				found = true;
				showRec(x);
				break;
			}
		}
		if (found == false) {
			String str = txtNo.getText();
			txtClear();
			JOptionPane.showMessageDialog(this, "Account No. " + str + " không tồn tại.",
					"BankSystem - Error", JOptionPane.PLAIN_MESSAGE);
		}

	}

	// hiển thị bản ghi.
	void showRec(int intRec) {

		txtNo.setText(records[intRec][0]);
		txtName.setText(records[intRec][1]);
		txtDate.setText(records[intRec][2] + ", " + records[intRec][3] + ", " + records[intRec][4]);
		txtBal.setText(records[intRec][5]);
		recCount = intRec;
	}

	// Xác nhận xóa
	void deletion() {

		try {
			int reply = JOptionPane.showConfirmDialog(this,
					"Are you Sure you want to Delete\n" + txtName.getText() + " Record From BankSystem?",
					"Bank System - Delete", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			// kiểm tra lựa chọn.
			if (reply == JOptionPane.YES_OPTION) {
				delRec();
			} else if (reply == JOptionPane.NO_OPTION) {
			}
		}

		catch (Exception e) {
		}

	}

	// Hàm để xóa một phần tử khỏi mảng.
	void delRec() {
		try {
			if (records != null) {
				for (int i = recCount; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						records[i][r] = records[i + 1][r];
						if (records[i][r] == null)
							break;
					}
				}
				total = total - 1;
				deleteFile();
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
		}

	}

	// Lưu bản ghi vào tệp sau khi xóa bản ghi của người dùng chọn.
	void deleteFile() {

		try {
			FileOutputStream fos = new FileOutputStream("Bank.dat");
			DataOutputStream dos = new DataOutputStream(fos);
			if (records != null) {
				for (int i = 0; i < total; i++) {
					for (int r = 0; r < 6; r++) {
						dos.writeUTF(records[i][r]);
						if (records[i][r] == null)
							break;
					}
				}
				JOptionPane.showMessageDialog(this, "Đã xóa bản ghi.",
						"BankSystem - Record Deleted", JOptionPane.PLAIN_MESSAGE);
				txtClear();
			} else {
			}
			dos.close();
			fos.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Error!!!",
					"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		}

	}

	// Xóa tất cả dữ liệu trong cửa sổ.
	void txtClear() {

		txtNo.setText("");
		txtName.setText("");
		txtDate.setText("");
		txtBal.setText("");
		txtNo.requestFocus();

	}

	// khóa cửa sổ điều khiển.
	void btnEnable() {

		txtNo.setEnabled(false);
		btnDel.setEnabled(false);

	}

}