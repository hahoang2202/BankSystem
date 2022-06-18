package BankSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class NewAccount extends JInternalFrame implements ActionListener {

	private JPanel jpInfo = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbDeposit;
	private JTextField txtNo, txtName, txtDeposit;
	private JComboBox<String> cboMonth, cboDay, cboYear;
	private JButton btnSave, btnCancel;

	private int count = 0;
	private int rows = 0;
	private int total = 0;

	private String records[][] = new String[500][6];

	private String saves[][] = new String[500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	NewAccount() {
		super("Tạo tài khoản", false, true, false, true);
		setSize(335, 235);

		jpInfo.setBounds(0, 0, 500, 115);
		jpInfo.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 80, 25);
		lbDate = new JLabel("Ngày tạo:");
		lbDate.setForeground(Color.black);
		lbDate.setBounds(15, 90, 80, 25);
		lbDeposit = new JLabel("Số dư:");
		lbDeposit.setForeground(Color.black);
		lbDeposit.setBounds(15, 125, 80, 25);

		txtNo = new JTextField();
		txtNo.setHorizontalAlignment(JTextField.RIGHT);
		txtNo.setBounds(105, 20, 205, 25);
		txtName = new JTextField();
		txtName.setBounds(105, 55, 205, 25);
		txtDeposit = new JTextField();
		txtDeposit.setHorizontalAlignment(JTextField.RIGHT);
		txtDeposit.setBounds(105, 125, 205, 25);

		txtNo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				char c = ke.getKeyChar();
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

		String Months[] = { "January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December" };
		cboMonth = new JComboBox<>(Months);
		cboDay = new JComboBox<>();
		cboYear = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			String days = "" + i;
			cboDay.addItem(days);
		}
		for (int i = 2000; i <= 2025; i++) {
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

		jpInfo.add(lbNo);
		jpInfo.add(txtNo);
		jpInfo.add(lbName);
		jpInfo.add(txtName);
		jpInfo.add(lbDate);
		jpInfo.add(cboMonth);
		jpInfo.add(cboDay);
		jpInfo.add(cboYear);
		jpInfo.add(lbDeposit);
		jpInfo.add(txtDeposit);
		jpInfo.add(btnSave);
		jpInfo.add(btnCancel);

		getContentPane().add(jpInfo);

		setVisible(true);

	}

	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnSave) {
			if (txtNo.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp Id của khách hàng để tạo.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtNo.requestFocus();
			} else if (txtName.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp Name của khách hàng để tạo.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtName.requestFocus();
			} else if (txtDeposit.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng cung cấp số dư của khách hàng để tạo.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtDeposit.requestFocus();
			} else {
				populateArray();
				findRec();
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
				JOptionPane.showMessageDialog(this, "Account No. " + txtNo.getText() + " đã tồn tại.",
						"BankSystem - Error", JOptionPane.PLAIN_MESSAGE);
				txtClear();
				break;
			}
		}
		if (found == false) {
			saveArray();
		}

	}

	void saveArray() {

		saves[count][0] = txtNo.getText();
		saves[count][1] = txtName.getText();
		saves[count][2] = "" + cboMonth.getSelectedItem();
		saves[count][3] = "" + cboDay.getSelectedItem();
		saves[count][4] = "" + cboYear.getSelectedItem();
		saves[count][5] = txtDeposit.getText();
		saveFile();
		count++;

	}

	void saveFile() {

		try {
			FileOutputStream fos = new FileOutputStream("Bank.dat", true);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeUTF(saves[count][0]);
			dos.writeUTF(saves[count][1]);
			dos.writeUTF(saves[count][2]);
			dos.writeUTF(saves[count][3]);
			dos.writeUTF(saves[count][4]);
			dos.writeUTF(saves[count][5]);
			JOptionPane.showMessageDialog(this, "Bản ghi đã được lưu",
					"BankSystem - Record Saved", JOptionPane.PLAIN_MESSAGE);
			txtClear();
			dos.close();
			fos.close();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Error",
					"BankSystem - Problem", JOptionPane.PLAIN_MESSAGE);
		}

	}

	void txtClear() {

		txtNo.setText("");
		txtName.setText("");
		txtDeposit.setText("");
		txtNo.requestFocus();

	}
}