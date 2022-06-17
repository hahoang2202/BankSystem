package BankSystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalTheme;

import BankSystem.Theme.AquaTheme;
import BankSystem.Theme.GrayTheme;
import BankSystem.Theme.GreenTheme;
import BankSystem.Theme.MilkyTheme;
import BankSystem.Theme.SandTheme;
import BankSystem.Theme.SolidTheme;

public class BankSystem extends JFrame implements ActionListener, ItemListener {

	/**
	 *
	 */
	private static final int CTRL_MASK = Event.CTRL_MASK;

	// Giao diện chính.
	private JDesktopPane desktop = new JDesktopPane();

	// Thanh Menu.
	private JMenuBar bar;

	// Các chương trình trong Menu.
	private JMenu mnuFile, mnuEdit, mnuView, mnuOpt, mnuWin, mnuHelp;

	private JMenuItem addNew, printRec, end; // Các tùy chọn dành cho File.
	private JMenuItem deposit, withdraw, delRec, search, searchName; // Các tùy chọn dành cho Edit.
	private JMenuItem oneByOne, allCustomer; // Các tùy chọn dành cho View.
	private JMenuItem change, style, theme; // Các tùy chọn dành cho Option.
	private JMenuItem close, closeAll; // Các tùy chọn dành cho Window.
	private JMenuItem about; // Các tùy chọn dành cho Help.

	// Menu thu nhỏ.
	private JPopupMenu popMenu = new JPopupMenu();

	// Menu hình ảnh cho các thao tác.
	private JMenuItem open, report, dep, with, del, find, all;

	// Thanh công cụ.
	private JToolBar toolBar;

	// Các nút trên thanh công cụ.
	private JButton btnNew, btnDep, btnWith, btnRec, btnDel, btnSrch;

	// Thanh Status hiển thị tên chương trình và thông báo chào mừng.
	private JPanel statusBar = new JPanel();

	// Hiện thị trên thanh Status
	private JLabel welcome;

	// Menu giao diện.
	private String strings[] = { "1. Metal", "2. Motif", "3. Windows" };
	private UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();
	private ButtonGroup group = new ButtonGroup();
	private JRadioButtonMenuItem radio[] = new JRadioButtonMenuItem[strings.length];

	// Lấy ngày hiện tại.
	private java.util.Date currDate = new java.util.Date();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
	private String d = sdf.format(currDate);

	// Các biến được sử dụng.
	// Sử dụng các biến và lưu trữ nó vào mảng.
	private int rows = 0;
	private int total = 0;

	// Mảng 2 chiều để lưu trữ bản ghi dữ liệu.
	private String records[][] = new String[500][6];

	// Biến để đọc bản ghi.
	private FileInputStream fis;
	private DataInputStream dis;

	// Xây dựng chương trình để đồng bộ tất cả biến.

	public BankSystem() {

		// Tên chương trình
		super("BankSystem by Java.");

		UIManager.addPropertyChangeListener(new UISwitchListener((JComponent) getRootPane()));

		// Tại Menu.
		bar = new JMenuBar();

		// Thiết lập Cửa sổ chính của Chương trình.
		setIconImage(getToolkit().getImage("Images/Bank.gif"));
		setSize(700, 550);
		setJMenuBar(bar);

		// Đóng giao diện.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				quitApp();
			}
		});

		// Setting vị trí.
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

		// Tạo Menu phím tắt.
		mnuFile = new JMenu("File");
		mnuFile.setMnemonic((int) 'F');
		mnuEdit = new JMenu("Edit");
		mnuEdit.setMnemonic((int) 'E');
		mnuView = new JMenu("View");
		mnuView.setMnemonic((int) 'V');
		mnuOpt = new JMenu("Options");
		mnuOpt.setMnemonic((int) 'O');
		mnuWin = new JMenu("Window");
		mnuWin.setMnemonic((int) 'W');
		mnuHelp = new JMenu("Help");
		mnuHelp.setMnemonic((int) 'H');

		// Tạo MenuItems cho File.
		addNew = new JMenuItem("Tạo acc mới", new ImageIcon("Images/Open.gif"));
		addNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, CTRL_MASK));
		addNew.setMnemonic((int) 'N');
		addNew.addActionListener(this);
		printRec = new JMenuItem("Số dư tài khoản", new ImageIcon("Images/New.gif"));
		printRec.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, CTRL_MASK));
		printRec.setMnemonic((int) 'R');
		printRec.addActionListener(this);
		end = new JMenuItem("Quit BankSystem ?", new ImageIcon("Images/export.gif"));
		end.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, CTRL_MASK));
		end.setMnemonic((int) 'Q');
		end.addActionListener(this);

		// Tạo MenuItems cho Edit.
		deposit = new JMenuItem("Gửi tiền");
		deposit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, CTRL_MASK));
		deposit.setMnemonic((int) 'T');
		deposit.addActionListener(this);
		withdraw = new JMenuItem("Rút tiền");
		withdraw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, CTRL_MASK));
		withdraw.setMnemonic((int) 'W');
		withdraw.addActionListener(this);
		delRec = new JMenuItem("Xóa tài khoản", new ImageIcon("Images/Delete.gif"));
		delRec.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, CTRL_MASK));
		delRec.setMnemonic((int) 'D');
		delRec.addActionListener(this);
		search = new JMenuItem("Tìm kiếm bằng No.", new ImageIcon("Images/find.gif"));
		search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK));
		search.setMnemonic((int) 'S');
		search.addActionListener(this);
		searchName = new JMenuItem("Tìm kiếm bằng Name");
		searchName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, CTRL_MASK));
		searchName.setMnemonic((int) 'M');
		searchName.addActionListener(this);

		// Tạo MenuItems cho View.
		oneByOne = new JMenuItem("Xem từng tài khoản");
		oneByOne.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, CTRL_MASK));
		oneByOne.setMnemonic((int) 'O');
		oneByOne.addActionListener(this);
		allCustomer = new JMenuItem("Xem tất cả", new ImageIcon("Images/refresh.gif"));
		allCustomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, CTRL_MASK));
		allCustomer.setMnemonic((int) 'A');
		allCustomer.addActionListener(this);

		// Tạo MenuItems cho Option.
		change = new JMenuItem("Đổi màu nền");
		change.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, CTRL_MASK));
		change.setMnemonic((int) 'B');
		change.addActionListener(this);

		// Menu Để thay đổi bố trí của Chương trình.
		style = new JMenu("Đổi kiểu bố trí");
		style.setMnemonic((int) 'L');
		for (int i = 0; i < radio.length; i++) { // Tạo Menu con.
			radio[i] = new JRadioButtonMenuItem(strings[i]); // Tạo mảng các Layout.
			radio[i].addItemListener(this); // Setting hoạt động.
			group.add(radio[i]); // Nhóm lại.
			style.add(radio[i]); // Thêm vào MenuOption.
		}
		// Tạo menu cho MenuTheme
		MetalTheme[] themes = { new DefaultMetalTheme(), new GreenTheme(), new AquaTheme(),
				new SandTheme(), new SolidTheme(), new MilkyTheme(), new GrayTheme() };
		theme = new MetalThemeMenu("Apply Theme", themes); // Thêm vào chương trình.
		theme.setMnemonic((int) 'M');

		// Tạo MenuItems cho Windown.
		close = new JMenuItem("Đóng cửa sổ hiện tại");
		close.setMnemonic((int) 'C');
		close.addActionListener(this);
		closeAll = new JMenuItem("Đóng tất cả cửa sổ");
		closeAll.setMnemonic((int) 'A');
		closeAll.addActionListener(this);

		// MenuItems cho Help.
		about = new JMenuItem("Thông tin", new ImageIcon("Images/Save.gif"));
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, CTRL_MASK));
		about.setMnemonic((int) 'C');
		about.addActionListener(this);

		// File Menu Items.
		mnuFile.add(addNew);
		mnuFile.addSeparator();
		mnuFile.add(printRec);
		mnuFile.addSeparator();
		mnuFile.add(end);

		// Edit Menu Items.
		mnuEdit.add(deposit);
		mnuEdit.add(withdraw);
		mnuEdit.addSeparator();
		mnuEdit.add(delRec);
		mnuEdit.addSeparator();
		mnuEdit.add(search);
		mnuEdit.add(searchName);

		// View Menu Items.
		mnuView.add(oneByOne);
		mnuView.addSeparator();
		mnuView.add(allCustomer);

		// Options Menu Items.
		mnuOpt.add(change);
		mnuOpt.addSeparator();
		mnuOpt.add(style);
		mnuOpt.addSeparator();
		mnuOpt.add(theme);

		// Window Menu Items.
		mnuWin.add(close);
		mnuWin.add(closeAll);

		// Help Menu Items.
		mnuHelp.add(about);

		// Thêm Menu vào Thanh.
		bar.add(mnuFile);
		bar.add(mnuEdit);
		bar.add(mnuView);
		bar.add(mnuOpt);
		bar.add(mnuWin);
		bar.add(mnuHelp);

		// Menu nhỏ.
		open = new JMenuItem("Tạo tài khoản", new ImageIcon("Images/Open.gif"));
		open.addActionListener(this);
		report = new JMenuItem("In ra các bản ghi", new ImageIcon("Images/New.gif"));
		report.addActionListener(this);
		dep = new JMenuItem("Gửi tiền");
		dep.addActionListener(this);
		with = new JMenuItem("Rút tiền");
		with.addActionListener(this);
		del = new JMenuItem("Xóa khách hàng", new ImageIcon("Images/Delete.gif"));
		del.addActionListener(this);
		find = new JMenuItem("Tìm kiếm Customer", new ImageIcon("Images/find.gif"));
		find.addActionListener(this);
		all = new JMenuItem("Xem tất cả Customer", new ImageIcon("Images/refresh.gif"));
		all.addActionListener(this);

		// Thêm các menu vào Menu nhỏ.
		popMenu.add(open);
		popMenu.add(report);
		popMenu.add(dep);
		popMenu.add(with);
		popMenu.add(del);
		popMenu.add(find);
		popMenu.add(all);

		// Quy trình hiển thị PopupMenu của Chương trình.
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				checkMouseTrigger(me);
			}

			public void mouseReleased(MouseEvent me) {
				checkMouseTrigger(me);
			}

			private void checkMouseTrigger(MouseEvent me) {
				if (me.isPopupTrigger())
					popMenu.show(me.getComponent(), me.getX(), me.getY());
			}
		});

		// Tạo nút Chương trình của Thanh công cụ.
		btnNew = new JButton(new ImageIcon("Images/NotePad.gif"));
		btnNew.setToolTipText("Tạo tài khoản");
		btnNew.addActionListener(this);
		btnDep = new JButton(new ImageIcon("Images/ImationDisk.gif"));
		btnDep.setToolTipText("Gửi tiền");
		btnDep.addActionListener(this);
		btnWith = new JButton(new ImageIcon("Images/SuperDisk.gif"));
		btnWith.setToolTipText("Rút tiền");
		btnWith.addActionListener(this);
		btnRec = new JButton(new ImageIcon("Images/Paproll.gif"));
		btnRec.setToolTipText("In số dư khách hàng");
		btnRec.addActionListener(this);
		btnDel = new JButton(new ImageIcon("Images/Toaster.gif"));
		btnDel.setToolTipText("Xóa khách hàng");
		btnDel.addActionListener(this);
		btnSrch = new JButton(new ImageIcon("Images/Search.gif"));
		btnSrch.setToolTipText("Tìm kiếm Customer");
		btnSrch.addActionListener(this);

		// Tạo Thanh công cụ của Chương trình.
		toolBar = new JToolBar();
		toolBar.add(btnNew);
		toolBar.addSeparator();
		toolBar.add(btnDep);
		toolBar.add(btnWith);
		toolBar.addSeparator();
		toolBar.add(btnRec);
		toolBar.addSeparator();
		toolBar.add(btnDel);
		toolBar.addSeparator();
		toolBar.add(btnSrch);
		toolBar.addSeparator();

		// Tạo Thanh Trạng thái của Chương trình.
		welcome = new JLabel("Welcome Today is " + d + " ", JLabel.RIGHT);
		welcome.setForeground(Color.black);
		welcome.setToolTipText("Welcoming the User & System Current Date");
		statusBar.setLayout(new BorderLayout());
		statusBar.add(welcome, BorderLayout.EAST);

		// Tăng tốc độ kéo.
		desktop.putClientProperty("JDesktopPane.dragMode", "outline");

		// Nội dung Chương trình.
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(desktop, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		// Hiển thị hình thức.
		setVisible(true);
	}

	// Chức năng thực hiện các hành động khác nhau theo menu của chương trình.

	public void actionPerformed(ActionEvent ae) {

		Object obj = ae.getSource();

		if (obj == addNew || obj == open || obj == btnNew) {

			boolean b = openChildWindow("Tạo tài khoản");
			if (b == false) {
				NewAccount newAcc = new NewAccount();
				desktop.add(newAcc);
				newAcc.show();
			}

		} else if (obj == printRec || obj == btnRec || obj == report) {

			getAccountNo();

		} else if (obj == end) {

			quitApp();

		} else if (obj == deposit || obj == dep || obj == btnDep) {

			boolean b = openChildWindow("Gửi tiền");
			if (b == false) {
				DepositMoney depMon = new DepositMoney();
				desktop.add(depMon);
				depMon.show();
			}

		} else if (obj == withdraw || obj == with || obj == btnWith) {

			boolean b = openChildWindow("Rút tiền");
			if (b == false) {
				WithdrawMoney withMon = new WithdrawMoney();
				desktop.add(withMon);
				withMon.show();
			}

		} else if (obj == delRec || obj == del || obj == btnDel) {

			boolean b = openChildWindow("Xóa Tài khoản");
			if (b == false) {
				DeleteCustomer delCus = new DeleteCustomer();
				desktop.add(delCus);
				delCus.show();
			}

		} else if (obj == search || obj == find || obj == btnSrch) {

			boolean b = openChildWindow("Tìm kiếm Customer [By No.]");
			if (b == false) {
				FindAccount fndAcc = new FindAccount();
				desktop.add(fndAcc);
				fndAcc.show();
			}

		} else if (obj == searchName) {

			boolean b = openChildWindow("Tìm kiếm Customer [By Name]");
			if (b == false) {
				FindName fndNm = new FindName();
				desktop.add(fndNm);
				fndNm.show();
			}

		} else if (obj == oneByOne) {

			boolean b = openChildWindow("Xem tài khoản");
			if (b == false) {
				ViewOne vwOne = new ViewOne();
				desktop.add(vwOne);
				vwOne.show();
			}

		} else if (obj == allCustomer || obj == all) {

			boolean b = openChildWindow("Xem Tất cả Tài khoản");
			if (b == false) {
				ViewCustomer viewCus = new ViewCustomer();
				desktop.add(viewCus);
				viewCus.show();
			}

		} else if (obj == change) {

			Color cl = new Color(153, 153, 204);
			cl = JColorChooser.showDialog(this, "Chọn Background", cl);
			if (cl == null) {
			} else {
				desktop.setBackground(cl);
				desktop.repaint();
			}

		} else if (obj == close) {

			try {
				desktop.getSelectedFrame().setClosed(true);
			} catch (Exception CloseExc) {
			}

		} else if (obj == closeAll) {

			JInternalFrame Frames[] = desktop.getAllFrames(); // Nhận tất cả các khung mở.
			for (int getFrameLoop = 0; getFrameLoop < Frames.length; getFrameLoop++) {
				try {
					Frames[getFrameLoop].setClosed(true); // Đóng khung.
				} catch (Exception CloseExc) {
				}
			}

		}

		else if (obj == about) {

			String msg = "BankSystem By Java.\n\n" + "Created & Designed By:\n" +
					"hae\n\n" + "E-mail:\n haduong2072008@gmail.com";
			JOptionPane.showMessageDialog(this, msg, "About BankSystem", JOptionPane.PLAIN_MESSAGE);

		}

	}

	// Chức năng Menu hình ảnh.

	public void itemStateChanged(ItemEvent e) {

		for (int i = 0; i < radio.length; i++)
			if (radio[i].isSelected()) {
				changeLookAndFeel(i);
			}

	}

	// Hàm đóng Chương trình.

	private void quitApp() {

		try {
			// Hộp thoại Xác nhận.
			int reply = JOptionPane.showConfirmDialog(this,
					"Are you really want to exit\nFrom BankSystem?",
					"BankSystem - Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
			// Kiểm tra lựa chọn.
			if (reply == JOptionPane.YES_OPTION) {
				setVisible(false); // Thoát giao diện.
				dispose(); // giải phóng tài nguyên.
				System.out.println("Thanks for Using BankSystem\nAuthor - Hae");
				System.exit(0); // Đóng.
			} else if (reply == JOptionPane.NO_OPTION) {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}

		catch (Exception e) {
		}

	}

	// Chức năng thay đổi giao diện của chương trình.

	public void changeLookAndFeel(int val) {

		try {
			UIManager.setLookAndFeel(looks[val].getClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}

	}

	// Vòng lặp để mở tác vụ.

	private boolean openChildWindow(String title) {

		JInternalFrame[] childs = desktop.getAllFrames();
		for (int i = 0; i < childs.length; i++) {
			if (childs[i].getTitle().equalsIgnoreCase(title)) {
				childs[i].show();
				return true;
			}
		}
		return false;

	}

	// Chức năng để in Bản ghi.

	void getAccountNo() {

		String printing;
		rows = 0;
		boolean b = populateArray();
		if (b == false) {
		} else {
			try {
				printing = JOptionPane.showInputDialog(this, "Nhập Account No. to in số dư khách hàng.\n" +
						"(Tip: only Digits)", "BankSystem - In bản ghi",
						JOptionPane.PLAIN_MESSAGE);
				if (printing == null) {
				}
				if (printing.equals("")) {
					JOptionPane.showMessageDialog(this, "Nhập Account No. to in.",
							"BankSystem - Trống", JOptionPane.PLAIN_MESSAGE);
					getAccountNo();
				} else {
					findRec(printing);
				}
			} catch (Exception e) {
			}
		}

	}

	// Tải tất cả bản ghi vào Database

	boolean populateArray() {

		boolean b = false;
		try {
			fis = new FileInputStream("Bank.dat");
			dis = new DataInputStream(fis);
			// Điền vào mảng.
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
				b = false;
			} else {
				b = true;
				try {
					dis.close();
					fis.close();
				} catch (Exception exp) {
				}
			}
		}
		return b;

	}

	// Hàm tìm bản ghi

	void findRec(String rec) {

		boolean found = false;
		for (int x = 0; x < total; x++) {
			if (records[x][0].equals(rec)) {
				found = true;
				printRecord(makeRecordPrint(x));
				break;
			}
		}
		if (found == false) {
			JOptionPane.showMessageDialog(this, "Account No. " + rec + " không tồn tại.",
					"BankSystem - Error", JOptionPane.PLAIN_MESSAGE);
			getAccountNo();
		}

	}

	// Tạo bản ghi.

	String makeRecordPrint(int rec) {

		String data;
		String data0 = "               BankSystem by Java.               \n"; // Page Title.
		String data1 = "               Customer Balance Report.              \n\n"; // Page Header.
		String data2 = "  Account No.:       " + records[rec][0] + "\n";
		String data3 = "  Customer Name:     " + records[rec][1] + "\n";
		String data4 = "  Giao dịch cuối:  " + records[rec][2] + ", " + records[rec][3] + ", " + records[rec][4]
				+ "\n";
		String data5 = "  Số dư hiện tại:   " + records[rec][5] + "\n\n";
		String sep0 = " -----------------------------------------------------------\n";
		String sep1 = " -----------------------------------------------------------\n";
		String sep2 = " -----------------------------------------------------------\n";
		String sep3 = " -----------------------------------------------------------\n";
		String sep4 = " -----------------------------------------------------------\n\n";

		data = data0 + sep0 + data1 + data2 + sep1 + data3 + sep2 + data4 + sep3 + data5 + sep4;
		return data;
	}

	// Hàm in bản ghi.

	void printRecord(String rec) {

		StringReader sr = new StringReader(rec);
		LineNumberReader lnr = new LineNumberReader(sr);
		Font typeface = new Font("Times New Roman", Font.PLAIN, 12);
		Properties p = new Properties();
		PrintJob pJob = getToolkit().getPrintJob(this, "In số dư khách hàng", p);

		if (pJob != null) {
			Graphics gr = pJob.getGraphics();
			if (gr != null) {
				FontMetrics fm = gr.getFontMetrics(typeface);
				int margin = 20;
				int pageHeight = pJob.getPageDimension().height - margin;
				int fontHeight = fm.getHeight();
				int fontDescent = fm.getDescent();
				int curHeight = margin;
				String nextLine;
				gr.setFont(typeface);

				try {
					do {
						nextLine = lnr.readLine();
						if (nextLine != null) {
							if ((curHeight + fontHeight) > pageHeight) { // New Page.
								gr.dispose();
								gr = pJob.getGraphics();
								curHeight = margin;
							}
							curHeight += fontHeight;
							if (gr != null) {
								gr.setFont(typeface);
								gr.drawString(nextLine, margin, curHeight - fontDescent);
							}
						}
					} while (nextLine != null);
				} catch (EOFException eof) {
				} catch (Throwable t) {
				}
			}
			gr.dispose();
		}
		if (pJob != null)
			pJob.end();
	}

	public static void main(String args[]) {
		new BankSystem();
	}

}