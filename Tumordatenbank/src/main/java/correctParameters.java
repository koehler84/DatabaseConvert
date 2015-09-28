import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.JProgressBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.CardLayout;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;

public class correctParameters extends JFrame {

	private final CardLayout pnCards_Layout;
	private JPanel contentPane;
	private JPanel panel_submitPatientendaten;
	private JPanel panel_submitFall;
	public JProgressBar progressBar;
	public JTable table_Patientendaten;
	private JTextField textField_Geburtsdatum;
	private JTextField textField_Vorname;
	private JTextField textField_Name;
	private JTextField textField_Strasse;
	private JTextField textField_Hausnummer;
	private JTextField textField_Land;
	private JTextField textField_PLZ;
	private JTextField textField_Ort;
	public JLabel lblConnected;
	private JCheckBox checkBox_Fehler;
	private boolean doubleCheck = false;
	private JPanel pnCards;
	private JMenuItem mntmOther;
	private JTable table_Fall;
	private JTextField textField_Geburtsdatum_1;
	private JTextField textField_Vorname_1;
	private JTextField textField_Name_1;
	private JTextField textField_Enummer;
	private JTextField textField_Eingangsdatum;
	private JTextField textField_Einsender;
	private JTextField textField_Arzt;
	private JComboBox<Befundtyp> comboBox_Befundtyp;
	private JCheckBox checkBox_Fehler_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					correctParameters frame = new correctParameters();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public correctParameters(/*Row row*/) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (start.cn != null && !start.cn.isClosed() && start.methodsCompleted) {
						start.cn.close();
						System.out.println("Datenbankverbindung beednet! WINDOW");
					}
				} catch (SQLException e1) {
					System.out.println("Fehler beim Beenden der Datenbankverbindung!");
				}
			}
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Fehler beim umstellen der UI.");
		}
		
		setTitle("Parameter korrigieren");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 200, 550, 450);
		setMinimumSize(new Dimension(550, 431));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnTest = new JMenu("Men\u00FC");
		mnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doubleCheck = false;
			}
		});
		menuBar.add(mnTest);
		
		JMenu mnNewMenu = new JMenu("New menu");
		mnTest.add(mnNewMenu);
		
		JMenuItem menu_showSubmitPat = new JMenuItem("Patientendaten bearbeiten");
		menu_showSubmitPat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.show(pnCards, "submitPatientendaten");
			}
		});
		mnTest.add(menu_showSubmitPat);
		
		mntmOther = new JMenuItem("F�lle bearbeiten");
		mntmOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.show(pnCards, "submitFall");
			}
		});
		mnTest.add(mntmOther);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//dispose();
				
				try {
					if (start.cn != null && !start.cn.isClosed() && start.methodsCompleted) {
						start.cn.close();
						System.out.println("Datenbankverbindung beednet! WINDOW");
					}
				} catch (SQLException e1) {
					System.out.println("Fehler beim Beenden der Datenbankverbindung!");
				}
				
				System.exit(0);
			}
		});
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
								
				try {
					if (start.cn != null && !start.cn.isClosed() && start.methodsCompleted) {
						start.cn.close();
						System.out.println("Datenbankverbindung beednet! WINDOW");
					}
				} catch (SQLException e1) {
					System.out.println("Fehler beim Beenden der Datenbankverbindung!");
				}
			}
		});
		
		JSeparator separator = new JSeparator();
		
		JLabel lblFortschritt = new JLabel("Fortschritt:");
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		
		lblConnected = new JLabel("connected");
		lblConnected.setVisible(false);
		
		pnCards_Layout = new CardLayout(0, 0);
		
		JButton btnNewButton = new JButton("switch Cards");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.next(pnCards);
				doubleCheck = false;
			}
		});
		
		pnCards = new JPanel();
		pnCards.setLayout(pnCards_Layout);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(1)
					.addComponent(lblFortschritt, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblConnected)
					.addPreferredGap(ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOk)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAbbrechen))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
					.addGap(1))
				.addComponent(pnCards, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 524, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(pnCards, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(2)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAbbrechen)
								.addComponent(btnOk)
								.addComponent(lblFortschritt, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblConnected)
								.addComponent(btnNewButton)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(4)
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)))
					.addGap(1))
		);
		
		panel_submitPatientendaten = new JPanel();
		pnCards.add(panel_submitPatientendaten, "submitPatientendaten");
		
		panel_submitFall = new JPanel();
		pnCards.add(panel_submitFall, "submitFall");
		
		JScrollPane scrollPane_Fall = new JScrollPane();
		
		JButton btnTabelleAktualisieren_Fall = new JButton("Tabelle aktualisieren");
		btnTabelleAktualisieren_Fall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBtoTable_Fall();
			}
		});
		
		JButton btnFertig_Fall = new JButton("Fertig");
		btnFertig_Fall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (table_Fall.getSelectedRow() != -1) {
					
					//If textFields Geburtsdatum, Name, Vorname are empty, skip update method
					if (textField_Geburtsdatum_1.getText().length() != 0 && textField_Name_1.getText().length() != 0 
							&& textField_Vorname_1.getText().length() != 0 && textField_Enummer.getText().length() != 0
							&& comboBox_Befundtyp.getSelectedIndex() != -1 && textField_Eingangsdatum.getText().length() != 0
							&& textField_Einsender.getText().length() != 0 && textField_Arzt.getText().length() != 0) {
						updateFallDB();						
					} else if (doubleCheck && textField_Enummer.getText().length() != 0 && comboBox_Befundtyp.getSelectedIndex() != -1) {
						updateFallDB();
						doubleCheck = false;
					} else {
						if (textField_Geburtsdatum_1.getText().length() == 0) {
							textField_Geburtsdatum_1.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Vorname_1.getText().length() == 0) {
							textField_Vorname_1.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Name_1.getText().length() == 0) {
							textField_Name_1.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Enummer.getText().length() == 0) {
							textField_Enummer.setBackground(Color.RED);
						}
						
						if (comboBox_Befundtyp.getSelectedIndex() == -1) {
							comboBox_Befundtyp.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
							//comboBox_Befundtyp
						}
						
						if (textField_Eingangsdatum.getText().length() == 0) {
							textField_Eingangsdatum.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Einsender.getText().length() == 0) {
							textField_Einsender.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Arzt.getText().length() == 0) {
							textField_Arzt.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
					}
					
				} else {
					System.out.println("Fehler: Keine Eingabe!");
				}
				
			}
		});
		
		JLabel lblGeburtsdatum_1 = new JLabel("Geburtsdatum:");
		
		JLabel lblVorname_1 = new JLabel("Vorname:");
		
		JLabel lblNachname_1 = new JLabel("Nachname:");
		
		textField_Geburtsdatum_1 = new JTextField();
		textField_Geburtsdatum_1.addKeyListener(resetDoubleCheck());
		textField_Geburtsdatum_1.setColumns(10);
		
		textField_Vorname_1 = new JTextField();
		textField_Vorname_1.addKeyListener(resetDoubleCheck());
		textField_Vorname_1.setColumns(10);
		
		textField_Name_1 = new JTextField();
		textField_Name_1.addKeyListener(resetDoubleCheck());
		textField_Name_1.setColumns(10);
		
		JLabel lblEnummer = new JLabel("E.-Nummer:");
		
		textField_Enummer = new JTextField();
		textField_Enummer.addKeyListener(resetDoubleCheck());
		textField_Enummer.setColumns(10);
		
		JLabel lblBefundtyp = new JLabel("Befundtyp:");
		
		comboBox_Befundtyp = new JComboBox<Befundtyp>();
		comboBox_Befundtyp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				doubleCheck = false;
			}
		});
		comboBox_Befundtyp.setModel(new DefaultComboBoxModel<Befundtyp>(Befundtyp.values()));
		comboBox_Befundtyp.setSelectedIndex(-1);
		
		textField_Eingangsdatum = new JTextField();
		textField_Eingangsdatum.addKeyListener(resetDoubleCheck());
		textField_Eingangsdatum.setColumns(10);
		
		JLabel lblEingangsdatum = new JLabel("Eingangsdatum:");
		
		textField_Einsender = new JTextField();
		textField_Einsender.addKeyListener(resetDoubleCheck());
		textField_Einsender.setColumns(10);
		
		JLabel lblEinsender = new JLabel("Einsender:");
		
		JLabel lblArzt = new JLabel("Arzt:");
		
		textField_Arzt = new JTextField();
		textField_Arzt.addKeyListener(resetDoubleCheck());
		textField_Arzt.setColumns(10);
		
		JLabel lblDatensatzIstVollstndig_1 = new JLabel("Datensatz ist vollst\u00E4ndig:");
		
		checkBox_Fehler_1 = new JCheckBox("");
		//TODO
		GroupLayout gl_panel_submitFall = new GroupLayout(panel_submitFall);
		gl_panel_submitFall.setHorizontalGroup(
			gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_submitFall.createSequentialGroup()
					.addComponent(lblGeburtsdatum_1)
					.addGap(45)
					.addComponent(lblVorname_1)
					.addGap(58)
					.addComponent(lblNachname_1, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel_submitFall.createSequentialGroup()
					.addGap(20)
					.addComponent(textField_Geburtsdatum_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Vorname_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Name_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(210, Short.MAX_VALUE))
				.addGroup(gl_panel_submitFall.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_submitFall.createSequentialGroup()
							.addComponent(lblEnummer)
							.addGap(49)
							.addComponent(lblBefundtyp)
							.addGap(80)
							.addComponent(lblEingangsdatum)
							.addGap(27)
							.addComponent(lblEinsender))
						.addGroup(gl_panel_submitFall.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.TRAILING)
								.addComponent(textField_Arzt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_Enummer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_submitFall.createSequentialGroup()
									.addComponent(comboBox_Befundtyp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(textField_Eingangsdatum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(textField_Einsender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_submitFall.createSequentialGroup()
									.addComponent(lblDatensatzIstVollstndig_1)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(checkBox_Fehler_1))))))
				.addGroup(Alignment.TRAILING, gl_panel_submitFall.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnTabelleAktualisieren_Fall, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(btnFertig_Fall, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane_Fall, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
				.addGroup(gl_panel_submitFall.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblArzt)
					.addContainerGap(490, Short.MAX_VALUE))
		);
		gl_panel_submitFall.setVerticalGroup(
			gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_submitFall.createSequentialGroup()
					.addGap(27)
					.addComponent(scrollPane_Fall, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_submitFall.createSequentialGroup()
							.addComponent(lblGeburtsdatum_1)
							.addGap(44)
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblEnummer)
								.addComponent(lblBefundtyp)
								.addComponent(lblEingangsdatum)
								.addComponent(lblEinsender)))
						.addGroup(gl_panel_submitFall.createSequentialGroup()
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
								.addComponent(lblVorname_1)
								.addComponent(lblNachname_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField_Geburtsdatum_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_Vorname_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField_Name_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_Enummer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox_Befundtyp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Eingangsdatum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Einsender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblArzt)
					.addGap(6)
					.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_submitFall.createSequentialGroup()
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField_Arzt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDatensatzIstVollstndig_1))
							.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
							.addGroup(gl_panel_submitFall.createParallelGroup(Alignment.LEADING)
								.addComponent(btnTabelleAktualisieren_Fall)
								.addComponent(btnFertig_Fall)))
						.addComponent(checkBox_Fehler_1))
					.addContainerGap())
		);
		
		table_Fall = new JTable();
		table_Fall.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//Put row content into textfields on double click
					rowToTextField("Fall");
					
					doubleCheck = false;
					
					textField_Geburtsdatum_1.setBackground(Color.WHITE);
					textField_Vorname_1.setBackground(Color.WHITE);
					textField_Name_1.setBackground(Color.WHITE);
					textField_Enummer.setBackground(Color.WHITE);
					comboBox_Befundtyp.setBorder(null);
					textField_Eingangsdatum.setBackground(Color.WHITE);
					textField_Einsender.setBackground(Color.WHITE);
					textField_Arzt.setBackground(Color.WHITE);
					
				}
			}
		});
		@SuppressWarnings("serial")
		DefaultTableModel tableModel_Fall = new DefaultTableModel(
				new String[]{"E.-Nummer", "Befundtyp", "Arzt", "Eingangsdatum", "Einsender", "Geburtsdatum", "Vorname", "Name"}, 0) {
			
			public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			}
		};
		table_Fall.setModel(tableModel_Fall);
		scrollPane_Fall.setViewportView(table_Fall);
		panel_submitFall.setLayout(gl_panel_submitFall);
		
		table_Patientendaten = new JTable();
		@SuppressWarnings({ "serial" })
		DefaultTableModel tableModel_Patientendaten = new DefaultTableModel(
				new String[]{"Geburtsdatum", "Vorname", "Name", "Stra�e", "Hausnummer", "Land", "PLZ", "Ort"}, 0) {
			
			public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			}
		};
		table_Patientendaten.setModel(tableModel_Patientendaten);
		table_Patientendaten.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//Put row content into textfields on double click
					rowToTextField("Patientendaten");
					
					doubleCheck = false;
					
					textField_Geburtsdatum.setBackground(Color.WHITE);
					textField_Vorname.setBackground(Color.WHITE);
					textField_Name.setBackground(Color.WHITE);
					textField_Strasse.setBackground(Color.WHITE);
					textField_Hausnummer.setBackground(Color.WHITE);
					textField_Land.setBackground(Color.WHITE);
					textField_PLZ.setBackground(Color.WHITE);
					textField_Ort.setBackground(Color.WHITE);
					
				}
			}
		});
		
		JScrollPane scrollPane_Patientendaten = new JScrollPane();
		
		JLabel lblGeburtsdatum = new JLabel("Geburtsdatum:");
		
		JLabel lblVorname = new JLabel("Vorname:");
		
		JLabel lblNachname = new JLabel("Nachname:");
		
		JLabel lblStrasse = new JLabel("Stra\u00DFe:");
		
		JLabel lblHausnummer = new JLabel("Hausnummer:");
		
		textField_Geburtsdatum = new JTextField();
		textField_Geburtsdatum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				doubleCheck = false;
				//Geburtsdatum, daher nur Zahlen und '-'
				if (!(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == '-')) {
					e.consume();
				}
			}
		});
		textField_Geburtsdatum.setColumns(10);
		
		textField_Vorname = new JTextField();
		textField_Vorname.addKeyListener(resetDoubleCheck());
		textField_Vorname.setColumns(10);
		
		textField_Name = new JTextField();
		textField_Name.addKeyListener(resetDoubleCheck());
		textField_Name.setColumns(10);
		
		textField_Strasse = new JTextField();
		textField_Strasse.addKeyListener(resetDoubleCheck());
		textField_Strasse.setColumns(10);
		
		textField_Hausnummer = new JTextField();
		textField_Hausnummer.addKeyListener(resetDoubleCheck());
		textField_Hausnummer.setColumns(10);
		
		textField_Ort = new JTextField();
		textField_Ort.addKeyListener(resetDoubleCheck());
		textField_Ort.setColumns(10);
		
		textField_PLZ = new JTextField();
		textField_PLZ.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				doubleCheck = false;
				//PLZ textField, daher nur Zahlen
				if (!(e.getKeyChar() >= '0' && e.getKeyChar() <= '9')) {
					e.consume();
				}
			}
		});
		textField_PLZ.setColumns(10);
		
		textField_Land = new JTextField();
		textField_Land.addKeyListener(resetDoubleCheck());
		textField_Land.setColumns(10);
		
		JLabel lblLand = new JLabel("Land:");
		
		JLabel lblPostleitzahl = new JLabel("Postleitzahl:");
		
		JLabel lblOrt = new JLabel("Ort:");
		
		JLabel lblDatensatzIstVollstndig = new JLabel("Datensatz ist vollst\u00E4ndig:");
		
		checkBox_Fehler = new JCheckBox("");
		
		JButton btnFertig = new JButton("Fertig");
		btnFertig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textField_Geburtsdatum.setBackground(Color.WHITE);
				textField_Vorname.setBackground(Color.WHITE);
				textField_Name.setBackground(Color.WHITE);
				textField_Strasse.setBackground(Color.WHITE);
				textField_Hausnummer.setBackground(Color.WHITE);
				textField_Land.setBackground(Color.WHITE);
				textField_PLZ.setBackground(Color.WHITE);
				textField_Ort.setBackground(Color.WHITE);
				
				if (table_Patientendaten.getSelectedRow() != -1) {
					
					//If textFields Geburtsdatum, Name, Vorname are empty, skip update method
					if (textField_Geburtsdatum.getText().length() != 0 && textField_Name.getText().length() != 0 
							&& textField_Vorname.getText().length() != 0 && textField_Strasse.getText().length() != 0
							&& textField_Hausnummer.getText().length() != 0 && textField_Land.getText().length() != 0
							&& textField_PLZ.getText().length() != 0 && textField_Ort.getText().length() != 0) {
						updatePatientenDB();						
					} else if (doubleCheck && textField_Geburtsdatum.getText().length() != 0 && textField_Name.getText().length() != 0 
							&& textField_Vorname.getText().length() != 0) {
						//writeInputToDB();
						updatePatientenDB();
						doubleCheck = false;
					} else {
						if (textField_Geburtsdatum.getText().length() == 0) {
							textField_Geburtsdatum.setBackground(Color.RED);
						}
						
						if (textField_Vorname.getText().length() == 0) {
							textField_Vorname.setBackground(Color.RED);
						}
						
						if (textField_Name.getText().length() == 0) {
							textField_Name.setBackground(Color.RED);
						}
						
						if (textField_Strasse.getText().length() == 0) {
							textField_Strasse.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Hausnummer.getText().length() == 0) {
							textField_Hausnummer.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Land.getText().length() == 0) {
							textField_Land.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_PLZ.getText().length() == 0) {
							textField_PLZ.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
						if (textField_Ort.getText().length() == 0) {
							textField_Ort.setBackground(Color.decode("#ff8080"));
							doubleCheck = true;
						}
						
					}
					
				} else {
					System.out.println("Fehler: Keine Eingabe!");
				}
								
			}
		});
		
		JButton btnTabelleAktualisieren = new JButton("Tabelle aktualisieren");
		btnTabelleAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBtoTable_Patientendaten();
			}
		});
		//TODO
		GroupLayout gl_panel_submitPatientendaten = new GroupLayout(panel_submitPatientendaten);
		gl_panel_submitPatientendaten.setHorizontalGroup(
			gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
					.addComponent(lblGeburtsdatum)
					.addGap(45)
					.addComponent(lblVorname)
					.addGap(58)
					.addComponent(lblNachname)
					.addGap(49)
					.addComponent(lblStrasse)
					.addGap(69)
					.addComponent(lblHausnummer))
				.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addGap(10)
							.addComponent(textField_Geburtsdatum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField_Vorname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField_Name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField_Strasse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField_Hausnummer, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addComponent(lblLand)
							.addGap(36)
							.addComponent(lblPostleitzahl)
							.addGap(47)
							.addComponent(lblOrt))
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addGap(10)
							.addComponent(textField_Land, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField_PLZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textField_Ort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblDatensatzIstVollstndig)
							.addGap(6)
							.addComponent(checkBox_Fehler)))
					.addGap(0, 44, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel_submitPatientendaten.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnTabelleAktualisieren, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(btnFertig, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane_Patientendaten, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
		);
		gl_panel_submitPatientendaten.setVerticalGroup(
			gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
					.addGap(27)
					.addComponent(scrollPane_Patientendaten, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblGeburtsdatum)
							.addComponent(lblVorname))
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNachname))
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblStrasse))
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblHausnummer)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_Geburtsdatum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Vorname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Strasse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Hausnummer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLand)
						.addComponent(lblPostleitzahl)
						.addComponent(lblOrt))
					.addGap(6)
					.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_Land, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_PLZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Ort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addGap(3)
							.addComponent(lblDatensatzIstVollstndig))
						.addComponent(checkBox_Fehler))
					.addPreferredGap(ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
					.addGroup(gl_panel_submitPatientendaten.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFertig)
						.addComponent(btnTabelleAktualisieren))
					.addContainerGap())
		);
		scrollPane_Patientendaten.setViewportView(table_Patientendaten);
		panel_submitPatientendaten.setLayout(gl_panel_submitPatientendaten);
		
		contentPane.setLayout(gl_contentPane);
		
		setVisible(true);
		toFront();
	}
	
	private KeyAdapter resetDoubleCheck() {
		
		return new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				doubleCheck = false;
			}
		};
	}
	
	private void rowToTextField(String tabelle) {
		
		if (tabelle.equals("Patientendaten")) {
			int row = table_Patientendaten.getSelectedRow();
			
			try {
				textField_Geburtsdatum.setText(table_Patientendaten.getValueAt(row, 0).toString());
			} catch (Exception e) {
				textField_Geburtsdatum.setText(null);
			}
			textField_Vorname.setText((String) table_Patientendaten.getValueAt(row, 1));
			textField_Name.setText((String) table_Patientendaten.getValueAt(row, 2));
			textField_Strasse.setText((String) table_Patientendaten.getValueAt(row, 3));
			textField_Hausnummer.setText((String) table_Patientendaten.getValueAt(row, 4));
			textField_Land.setText((String) table_Patientendaten.getValueAt(row, 5));
			textField_PLZ.setText((String) table_Patientendaten.getValueAt(row, 6));
			textField_Ort.setText((String) table_Patientendaten.getValueAt(row, 7));
		} else if (tabelle.equals("Fall")) {
			int row = table_Fall.getSelectedRow();
			
			try {
				textField_Geburtsdatum_1.setText(table_Fall.getValueAt(row, 5).toString());;
			} catch (Exception e) {
				textField_Geburtsdatum_1.setText(null);
			}
			textField_Vorname_1.setText((String) table_Fall.getValueAt(row, 6));
			textField_Name_1.setText((String) table_Fall.getValueAt(row, 7));
			textField_Enummer.setText((String) table_Fall.getValueAt(row, 0));
			comboBox_Befundtyp.setSelectedItem(Befundtyp.getBefundtyp(table_Fall.getValueAt(row, 1).toString()));
			textField_Einsender.setText((String) table_Fall.getValueAt(row, 4));;
			textField_Arzt.setText((String) table_Fall.getValueAt(row, 2));;
			
			try {
				textField_Eingangsdatum.setText(table_Fall.getValueAt(row, 3).toString());
			} catch (Exception e) {
				textField_Eingangsdatum.setText(null);
			}
			
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private void writeInputToDB() {
		
		try {
			PreparedStatement Pst = start.cn.prepareStatement("insert into patientendaten (`Geburtsdatum`, `Vorname`, `Name`, `Strasse`, `Hausnummer`, `Land`, `PLZ`, `Ort`)"
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? );");
			Pst.setString(1, textField_Geburtsdatum.getText());
			Pst.setString(2, textField_Vorname.getText());
			Pst.setString(3, textField_Name.getText());
			Pst.setString(4, textField_Strasse.getText());
			Pst.setString(5, textField_Hausnummer.getText());
			Pst.setString(6, textField_Land.getText());
			Pst.setInt(7, Integer.parseInt(textField_PLZ.getText()));
			Pst.setString(8, textField_Ort.getText());
			
			int changedRows = Pst.executeUpdate();			
			System.out.println("Manueller Input: Zeilen ge�ndert: " + changedRows);
			Pst.close();
			
			if (table_Patientendaten.getRowCount() > 0) {
				((DefaultTableModel)table_Patientendaten.getModel()).removeRow(0);
			}
			
			textField_Geburtsdatum.setText("");
			textField_Vorname.setText("");
			textField_Name.setText("");
			textField_Strasse.setText("");
			textField_Hausnummer.setText("");
			textField_Land.setText("");
			textField_PLZ.setText("");
			textField_Ort.setText("");
			
		} catch (SQLException e) {
			System.out.println("Fehler in writeInputToDB: Person ggf. schon erfasst!");
		}		
		
	}
	
	private void updatePatientenDB() {
		
		try {
			
			PreparedStatement Pst = start.cn.prepareStatement("update mydb.patientendaten set `Geburtsdatum` = ? , `Vorname` = ? , "
					+ "`Name` = ? , `Strasse` = ? , `Hausnummer` = ? , `Land` = ? , `PLZ` = ? , `Ort` = ?, `Fehler` = ? "
					+ " where `Geburtsdatum` = ? and `Vorname` = ? and `Name` = ? ;");
			
			Pst.setString(10, table_Patientendaten.getValueAt(table_Patientendaten.getSelectedRow(), 0).toString());		//where Geb
			Pst.setString(11, table_Patientendaten.getValueAt(table_Patientendaten.getSelectedRow(), 1).toString());		//where Vorname
			Pst.setString(12, table_Patientendaten.getValueAt(table_Patientendaten.getSelectedRow(), 2).toString());		//where Name
			
			Pst.setString(1, textField_Geburtsdatum.getText());	//set Geb
			Pst.setString(2, textField_Vorname.getText());	//set Vorname
			Pst.setString(3, textField_Name.getText());	//set Name
			
			if (!textField_Strasse.getText().equals("")) {
				Pst.setString(4, textField_Strasse.getText());	//set Strasse
			} else {
				Pst.setNull(4, java.sql.Types.NULL);
			}
			if (!textField_Hausnummer.getText().equals("")) {
				Pst.setString(5, textField_Hausnummer.getText());	//set Hausnummer
			} else {
				Pst.setNull(5, java.sql.Types.NULL);
			}
			if (!textField_Land.getText().equals("")) {
				Pst.setString(6, textField_Land.getText());	//set Land
			} else {
				Pst.setNull(6, java.sql.Types.NULL);
			}
			if (!textField_PLZ.getText().equals("")) {
				Pst.setInt(7, Integer.parseInt(textField_PLZ.getText()));	//set PLZ
			} else {
				Pst.setNull(7, java.sql.Types.NULL);
			}
			if (!textField_Ort.getText().equals("")) {
				Pst.setString(8, textField_Ort.getText());	//set Ort
			} else {
				Pst.setNull(8, java.sql.Types.NULL);
			}
			
			if (checkBox_Fehler.isSelected()) {
				Pst.setInt(9, 0);
			} else {
				Pst.setInt(9, 1);
			}
			
			System.out.println("Zeilen manuell ge�ndert: " + Pst.executeUpdate());
			
			Pst.close();
			
			((DefaultTableModel)table_Patientendaten.getModel()).removeRow(table_Patientendaten.getSelectedRow());
			
			textField_Geburtsdatum.setText("");
			textField_Vorname.setText("");
			textField_Name.setText("");
			textField_Strasse.setText("");
			textField_Hausnummer.setText("");
			textField_Land.setText("");
			textField_PLZ.setText("");
			textField_Ort.setText("");
			checkBox_Fehler.setSelected(false);
			
			textField_Geburtsdatum.setBackground(Color.WHITE);
			textField_Vorname.setBackground(Color.WHITE);
			textField_Name.setBackground(Color.WHITE);
			textField_Strasse.setBackground(Color.WHITE);
			textField_Hausnummer.setBackground(Color.WHITE);
			textField_Land.setBackground(Color.WHITE);
			textField_PLZ.setBackground(Color.WHITE);
			textField_Ort.setBackground(Color.WHITE);
			
		} catch (SQLException e) {
			System.out.println(e);
			//TODO Fenster "Diese Person existiert bereits in der Datenbank!"
		}
		
	}
	
	private void updateFallDB() {
		
		try {
			
			PreparedStatement Pst = start.cn.prepareStatement("update mydb.fall set `E.-Nummer` = ? , Befundtyp = ? ,  Arzt = ? , "
					+ "Eingangsdatum = ? , Einsender = ? , Patientendaten_PatientenID = (select PatientenID from mydb.Patientendaten "
					+ "where Geburtsdatum = ? and Vorname = ? and Name = ? ), Fehler = ? where `E.-Nummer` = ? and Befundtyp = ? ;");
			
			Pst.setString(10, table_Fall.getValueAt(table_Fall.getSelectedRow(), 0).toString());
			Pst.setInt(11, Befundtyp.getBefundtyp(table_Fall.getValueAt(table_Fall.getSelectedRow(), 1).toString()).getValue());
			
			Pst.setString(1, textField_Enummer.getText());
			Pst.setInt(2, ((Befundtyp) comboBox_Befundtyp.getSelectedItem()).getValue());
			
			if (!textField_Geburtsdatum_1.getText().equals("")) {
				Pst.setString(6, textField_Geburtsdatum_1.getText());
			} else {
				Pst.setNull(6, java.sql.Types.NULL);
			}
			if (!textField_Vorname_1.getText().equals("")) {
				Pst.setString(7, textField_Vorname_1.getText());
			} else {
				Pst.setNull(7, java.sql.Types.NULL);
			}
			if (!textField_Name_1.getText().equals("")) {
				Pst.setString(8, textField_Name_1.getText());
			} else {
				Pst.setNull(8, java.sql.Types.NULL);
			}
			if (!textField_Eingangsdatum.getText().equals("")) {
				Pst.setString(4, textField_Eingangsdatum.getText());
			} else {
				Pst.setNull(4, java.sql.Types.NULL);
			}
			if (!textField_Einsender.getText().equals("")) {
				Pst.setString(5, textField_Einsender.getText());
			} else {
				Pst.setNull(5, java.sql.Types.NULL);
			}
			if (!textField_Arzt.getText().equals("")) {
				Pst.setString(3, textField_Arzt.getText());
			} else {
				Pst.setNull(3, java.sql.Types.NULL);
			}
			
			if (checkBox_Fehler_1.isSelected()) {
				Pst.setInt(9, 0);
			} else {
				Pst.setInt(9, 1);
			}
			
			System.out.println("Zeilen manuell ge�ndert: " + Pst.executeUpdate());
			
			Pst.close();
			
			((DefaultTableModel)table_Fall.getModel()).removeRow(table_Fall.getSelectedRow());
			
			textField_Geburtsdatum_1.setText("");
			textField_Vorname_1.setText("");
			textField_Name_1.setText("");
			textField_Enummer.setText("");
			comboBox_Befundtyp.setSelectedIndex(-1);
			textField_Eingangsdatum.setText("");
			textField_Einsender.setText("");
			textField_Arzt.setText("");
			checkBox_Fehler_1.setSelected(false);
			
			textField_Geburtsdatum_1.setBackground(Color.WHITE);
			textField_Vorname_1.setBackground(Color.WHITE);
			textField_Name_1.setBackground(Color.WHITE);
			textField_Enummer.setBackground(Color.WHITE);
			comboBox_Befundtyp.setBorder(null);
			textField_Eingangsdatum.setBackground(Color.WHITE);
			textField_Einsender.setBackground(Color.WHITE);
			textField_Arzt.setBackground(Color.WHITE);
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	public void DBtoTable_Patientendaten() {
		
		try {
			Statement st = start.cn.createStatement();
			ResultSet res = st.executeQuery("select * from mydb.vPatientendaten_Hauptparameter where `Fehler` != 0");
			
			@SuppressWarnings("serial")
			DefaultTableModel tableModel = new DefaultTableModel(
					new String[]{"Geburtsdatum", "Vorname", "Name", "Stra�e", "Hausnummer", "Land", "PLZ", "Ort"}, 0) {
				
				public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				}
			};
			
			table_Patientendaten.setModel(tableModel);
			
			Object[] data = new Object[8];
			
			while (res.next()) {
								
				data[0] = res.getDate("Geburtsdatum");
				data[1] = res.getString("Vorname");
				data[2] = res.getString("Name");
				data[3] = res.getString("Strasse");
				data[4] = res.getString("Hausnummer");
				data[5] = res.getString("Land");
				data[6] = res.getString("PLZ");
				data[7] = res.getString("Ort");
				
				tableModel.addRow(data);
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception ex) {
			System.out.println(ex + " - ggf keine Verbindung zur Datenbank");
		}
		
	}
	
	public void DBtoTable_Fall() {
		
		try {
			Statement st = start.cn.createStatement();
			ResultSet res = st.executeQuery("select * from vFehlerFall;");
			
			@SuppressWarnings("serial")
			DefaultTableModel tableModel = new DefaultTableModel(
					new String[]{"E.-Nummer", "Befundtyp", "Arzt", "Eingangsdatum", "Einsender", "Geburtsdatum", "Vorname", "Name"}, 0) {
				
				public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				}
			};
			
			table_Fall.setModel(tableModel);
			
			Object[] data = new Object[8];
			
			while (res.next()) {
				
				data[0] = res.getString("E.-Nummer");
				data[1] = Befundtyp.getBefundtyp(res.getInt("Befundtyp")).name();
				data[2] = res.getString("Arzt");
				data[3] = res.getDate("Eingangsdatum");
				data[4] = res.getString("Einsender");
				data[5] = res.getDate("Geburtsdatum");
				data[6] = res.getString("Vorname");
				data[7] = res.getString("Name");
				
				tableModel.addRow(data);
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		} catch (Exception ex) {
			System.out.println(ex + " - ggf keine Verbindung zur Datenbank");
		}
		
	}
}
