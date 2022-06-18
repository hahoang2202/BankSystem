package BankSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class DepositMoney extends JInternalFrame implements ActionListener {

	private JPanel jpDep = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbDeposit;
	private JTextField txtNo, txtName, txtDeposit;
	private JComboBox<String> cboMonth, cboDay, cboYear;
	private JButton btnSave, btnCancel;

	private int recCount = 0;
	private int rows = 0;
	private int total = 0;
	private int curr;
	private int deposit;

	// Mảng 2 chiều để lưu trữ bản ghi dữ liệu..
	private String records[][] = new String[500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	DepositMoney() {
		super("Gửi tiền", false, true, false, true);
		setSize(335, 235);

		jpDep.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 80, 25);
		lbDate = new JLabel("Ngày gửi:");
		lbDate.setForeground(Color.black);
		lbDate.setBounds(15, 90, 80, 25);
		lbDeposit = new JLabel("Số tiền:");
		lbDeposit.setForeground(Color.black);
		lbDeposit.setBounds(15, 125, 80, 25);

		txtNo = new JTextField();
		txtNo.setHorizontalAlignment(JTextField.RIGHT);
		txtNo.setBounds(105, 20, 205, 25);
		txtName = new JTextField();
		txtName.setEnabled(false);
		txtName.setBounds(105, 55, 205, 25);
		txtDeposit = new JTextField();
		txtDeposit.setHorizontalAlignment(JTextField.RIGHT);
		txtDeposit.setBounds(105, 125, 205, 25);

		// Tạo mục ngày.
		String Months[] = { "January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December" };
		cboMonth = new JComboBox<>(Months);
		cboDay = new JComboBox<>();
		cboYear = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			String days = "" + i;
			cboDay.addItem(days);
		}
		for (int i = 2000; i <= 2015; i++) {
			String years = "" + i;
			cboYear.addItem(years);
		}

		// Đặt vị trí cho nút.
		cboMonth.setBounds(105, 90, 92, 25);
		cboDay.setBounds(202, 90, 43, 25);
		cboYear.setBounds(250, 90, 60, 25);

		// Tạo nút.
		btnSave = new JButton("Save");
		btnSave.setBounds(20, 165, 120, 25);
		btnSave.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(185, 165, 120, 25);
		btnCancel.addActionListener(this);

		// Hạn chế đầu vào của người dùng chỉ với các chữ số trong các văn bản dạng số.
		txtNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();// Unic.
				if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
				}
			}
		});
		txtDeposit.addKeyListener(new KeyAdapter() {
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
					populateArray();
					findRec();
				}
			}
		});

		// Thêm các nút điều khiển.
		jpDep.add(lbNo);
		jpDep.add(txtNo);
		jpDep.add(lbName);
		jpDep.add(txtName);
		jpDep.add(lbDate);
		jpDep.add(cboMonth);
		jpDep.add(cboDay);
		jpDep.add(cboYear);
		jpDep.add(lbDeposit);
		jpDep.add(txtDeposit);
		jpDep.add(btnSave);
		jpDep.add(btnCancel);

		// Thêm bảng điều khiển vào cửa sổ.
		getContentPane().add(jpDep);

		populateArray();

		setVisible(true);

	}

	// Thực hiện Hành động khi Người dùng nhấp vào Nút.
	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnSave) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp Id của khách hàng.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			} else if (txtDeposit.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp số tiền của khách hàng",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtDeposit.requestFocus();
			} else {
				editRec(); // Cập nhật vào bản ghi.
			}
		}
		if (obj == btnCancel) {
			txtClear();
			setVisible(false);
			dispose();
		}

	}

	void populateArray() {

		try {
			fis = new FileInputStream("Bank.dat");
			dis = new DataInputStream(fis);
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

	// Hiển thị bản ghi.
	public void showRec(int intRec) {

		txtNo.setText(records[intRec][0]);
		txtName.setText(records[intRec][1]);
		curr = Integer.parseInt(records[intRec][5]);
		recCount = intRec;

	}

	// Xác nhận xóa
	void txtClear() {

		txtNo.setText("");
		txtName.setText("");
		txtDeposit.setText("");
		txtNo.requestFocus();

	}

	// Chỉnh sửa giá trị phần tử của mảng.
	public void editRec() {

		deposit = Integer.parseInt(txtDeposit.getText());
		records[recCount][0] = txtNo.getText();
		records[recCount][1] = txtName.getText();
		records[recCount][2] = "" + cboMonth.getSelectedItem();
		records[recCount][3] = "" + cboDay.getSelectedItem();
		records[recCount][4] = "" + cboYear.getSelectedItem();
		records[recCount][5] = "" + (curr + deposit);
		editFile(); // Lưu lại.

	}

	// Lưu bản ghi vào tệp sau khi sửa bản ghi của người dùng chọn.
	public void editFile() {

		try {
			FileOutputStream fos = new FileOutputStream("Bank.dat");
			DataOutputStream dos = new DataOutputStream(fos);
			if (records != null) {
				for (int i = 0; i < total; i++) {
					for (int c = 0; c < 6; c++) {
						dos.writeUTF(records[i][c]);
						if (records[i][c] == null)
							break;
					}
				}
				JOptionPane.showMessageDialog(this, "Đã cập nhật",
						"BankSystem - Record Saved", JOptionPane.PLAIN_MESSAGE);
				txtClear();
				dos.close();
				fos.close();
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Error!!!",
					"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		}

	}

	// khóa cửa sổ điều khiển.
	void btnEnable() {

		txtNo.setEnabled(false);
		cboMonth.setEnabled(false);
		cboDay.setEnabled(false);
		cboYear.setEnabled(false);
		txtDeposit.setEnabled(false);
		btnSave.setEnabled(false);

	}

}