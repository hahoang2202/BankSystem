package BankSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class WithdrawMoney extends JInternalFrame implements ActionListener {

	private JPanel jpWith = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbWithdraw;
	private JTextField txtNo, txtName, txtWithdraw;
	private JComboBox<String> cboMonth, cboDay, cboYear;
	private JButton btnSave, btnCancel;

	private int recCount = 0;
	private int rows = 0;
	private int total = 0;
	private int curr;
	private int withdraw;

	private String records[][] = new String[500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	WithdrawMoney() {

		super("Rút tiền", false, true, false, true);
		setSize(335, 235);

		jpWith.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 80, 25);
		lbDate = new JLabel("With. Date:");
		lbDate.setForeground(Color.black);
		lbDate.setBounds(15, 90, 80, 25);
		lbWithdraw = new JLabel("With. Amount:");
		lbWithdraw.setForeground(Color.black);
		lbWithdraw.setBounds(15, 125, 80, 25);

		txtNo = new JTextField();
		txtNo.setHorizontalAlignment(JTextField.RIGHT);

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
		txtNo.setBounds(105, 20, 205, 25);

		txtName = new JTextField();
		txtName.setEnabled(false);
		txtName.setBounds(105, 55, 205, 25);
		txtWithdraw = new JTextField();
		txtWithdraw.setHorizontalAlignment(JTextField.RIGHT);
		txtWithdraw.setBounds(105, 125, 205, 25);

		txtNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
				}
			}
		});
		txtWithdraw.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
				if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE)))) {
					getToolkit().beep();
					ke.consume();
				}
			}
		});

		String Months[] = { "January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December" };
		cboMonth = new JComboBox<>(Months);
		cboDay = new JComboBox<>();
		cboYear = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			String days = "" + i;
			cboDay.addItem(days);
		}
		for (int i = 2011; i <= 2025; i++) {
			String years = "" + i;
			cboYear.addItem(years);
		}

		cboMonth.setBounds(105, 90, 92, 25);
		cboDay.setBounds(202, 90, 43, 25);
		cboYear.setBounds(250, 90, 60, 25);

		btnSave = new JButton("Save");
		btnSave.setBounds(20, 165, 120, 25);
		btnSave.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(185, 165, 120, 25);
		btnCancel.addActionListener(this);

		jpWith.add(lbNo);
		jpWith.add(txtNo);
		jpWith.add(lbName);
		jpWith.add(txtName);
		jpWith.add(lbDate);
		jpWith.add(cboMonth);
		jpWith.add(cboDay);
		jpWith.add(cboYear);
		jpWith.add(lbWithdraw);
		jpWith.add(txtWithdraw);
		jpWith.add(btnSave);
		jpWith.add(btnCancel);

		getContentPane().add(jpWith);

		populateArray();

		setVisible(true);

	}

	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnSave) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp Id của khách hàng để tìm kiếm.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();//ưu tiên thứ tự
			} else if (txtWithdraw.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp số tiền rút",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtWithdraw.requestFocus();
			} else {
				withdraw = Integer.parseInt(txtWithdraw.getText());
				if (curr == 0) {
					JOptionPane.showMessageDialog(this, txtName.getText() + " không đủ số dư.",
							"BankSystem - EmptyAccount", JOptionPane.PLAIN_MESSAGE);
					txtClear();
				} else if (withdraw > curr) {
					JOptionPane.showMessageDialog(this, "Số tiền rút tiền không được lớn hơn số dư.",
							"BankSystem - Large Amount", JOptionPane.PLAIN_MESSAGE);
					txtWithdraw.setText("");
					txtWithdraw.requestFocus();
				} else {
					editRec();
				}
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

	public void showRec(int intRec) {

		txtNo.setText(records[intRec][0]);
		txtName.setText(records[intRec][1]);
		curr = Integer.parseInt(records[intRec][5]);
		recCount = intRec;

	}

	void txtClear() {

		txtNo.setText("");
		txtName.setText("");
		txtWithdraw.setText("");
		txtNo.requestFocus();

	}

	public void editRec() {

		records[recCount][0] = txtNo.getText();
		records[recCount][1] = txtName.getText();
		records[recCount][2] = "" + cboMonth.getSelectedItem();
		records[recCount][3] = "" + cboDay.getSelectedItem();
		records[recCount][4] = "" + cboYear.getSelectedItem();
		records[recCount][5] = "" + (curr - withdraw);
		editFile();

	}

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
				JOptionPane.showMessageDialog(this, "Bản ghi đã cập nhật",
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

	void btnEnable() {

		txtNo.setEnabled(false);
		cboMonth.setEnabled(false);
		cboDay.setEnabled(false);
		cboYear.setEnabled(false);
		txtWithdraw.setEnabled(false);
		btnSave.setEnabled(false);

	}

}