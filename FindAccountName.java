package BankSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class FindAccountName extends JInternalFrame implements ActionListener {

	private JPanel jpFind = new JPanel();
	private JLabel lbNo, lbName, lbDate, lbBal;
	private JTextField txtNo, txtName, txtDate, txtBal;
	private JButton btnFind, btnCancel;

	private int rows = 0;
	private int total = 0;

	private String records[][] = new String[500][6];

	private FileInputStream fis;
	private DataInputStream dis;

	FindAccountName() {
		super("Tìm kiểm tài khoản", false, true, false, true);
		setSize(350, 235);

		jpFind.setLayout(null);

		lbNo = new JLabel("Account No:");
		lbNo.setForeground(Color.black);
		lbNo.setBounds(15, 20, 80, 25);
		lbName = new JLabel("Person Name:");
		lbName.setForeground(Color.black);
		lbName.setBounds(15, 55, 80, 25);
		lbDate = new JLabel("Giao dịch cuối:");
		lbDate.setForeground(Color.black);
		lbDate.setBounds(15, 90, 100, 25);
		lbBal = new JLabel("Số dư:");
		lbBal.setForeground(Color.black);
		lbBal.setBounds(15, 125, 80, 25);

		txtNo = new JTextField();
		txtNo.setEnabled(false);
		txtNo.setHorizontalAlignment(JTextField.RIGHT);
		txtNo.setBounds(125, 20, 200, 25);
		txtName = new JTextField();
		txtName.setBounds(125, 55, 200, 25);
		txtDate = new JTextField();
		txtDate.setEnabled(false);
		txtDate.setBounds(125, 90, 200, 25);
		txtBal = new JTextField();
		txtBal.setHorizontalAlignment(JTextField.RIGHT);
		txtBal.setEnabled(false);
		txtBal.setBounds(125, 125, 200, 25);

		btnFind = new JButton("Search");
		btnFind.setBounds(20, 165, 120, 25);
		btnFind.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(200, 165, 120, 25);
		btnCancel.addActionListener(this);

		jpFind.add(lbNo);
		jpFind.add(txtNo);
		jpFind.add(lbName);
		jpFind.add(txtName);
		jpFind.add(lbDate);
		jpFind.add(txtDate);
		jpFind.add(lbBal);
		jpFind.add(txtBal);
		jpFind.add(btnFind);
		jpFind.add(btnCancel);

		getContentPane().add(jpFind);

		populateArray();

		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == btnFind) {
			if (txtName.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Vui lòng điền Name khách hàng để tìm kiếm.",
						"BankSystem - Empty", JOptionPane.PLAIN_MESSAGE);
				txtName.requestFocus();
			} else {
				rows = 0;
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
			if (records[x][1].equalsIgnoreCase(txtName.getText())) {
				found = true;
				showRec(x);
				break;
			}
		}
		if (found == false) {
			JOptionPane.showMessageDialog(this, "Customer Name " + txtName.getText() + " không tồn tại.",
					"BankSystem - Error", JOptionPane.PLAIN_MESSAGE);
			txtClear();
		}

	}

	public void showRec(int intRec) {

		txtNo.setText(records[intRec][0]);
		txtName.setText(records[intRec][1]);
		txtDate.setText(records[intRec][2] + ", " + records[intRec][3] + ", " + records[intRec][4]);
		txtBal.setText(records[intRec][5]);

	}

	void txtClear() {

		txtNo.setText("");
		txtName.setText("");
		txtDate.setText("");
		txtBal.setText("");
		txtName.requestFocus();

	}

	void btnEnable() {

		txtName.setEnabled(false);
		btnFind.setEnabled(false);

	}

}