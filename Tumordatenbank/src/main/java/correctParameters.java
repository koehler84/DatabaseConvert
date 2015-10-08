import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

@SuppressWarnings("serial")
public class correctParameters extends JFrame {

	private final CardLayout pnCards_Layout;
	private JPanel contentPane;
	private JPanel panel_submitPatientendaten;
	private JPanel panel_submitFall;
	private JPanel panel_SQLManager;
	private JPanel panel_ChangePersonFall;
	public JProgressBar progressBar;
	private JTable table_Patientendaten;
	private JTable table_Fall;
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
	private JTextField textField_Geburtsdatum_1;
	private JTextField textField_Vorname_1;
	private JTextField textField_Name_1;
	private JTextField textField_Enummer;
	private JTextField textField_Eingangsdatum;
	private JTextField textField_Einsender;
	private JTextField textField_Arzt;
	private JComboBox<Befundtyp> comboBox_Befundtyp;
	private JCheckBox checkBox_Fehler_1;
	private JTextField textField_SQLStatement;
	private JTable table_SQL;
	private JTextField textField_Geburtsdatum_ChangePersonFall;
	private JTextField textField_Vorname_ChangePersonFall;
	private JTextField textField_Name_ChangePersonFall;
	private JTextField textField_ENummer_ChangePersonFall;
	private JTable table_ChangePersonFall;
	private JTextField textField_Geburtsdatum_ChangePersonFall_2;
	private JTextField textField_Vorname_ChangePersonFall_2;
	private JTextField textField_Name_ChangePersonFall_2;

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
		
		JMenu mnMenu = new JMenu("Men\u00FC");
		mnMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doubleCheck = false;
			}
		});
		menuBar.add(mnMenu);
		
		JMenu mnNewMenu = new JMenu("Datenbankeinstellungen");
		mnMenu.add(mnNewMenu);
		
		JMenuItem mntmSchemaErneuern = new JMenuItem("Schema erneuern");
		mntmSchemaErneuern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (start.cn != null) {
					
					int value = JOptionPane.showConfirmDialog(start.UIFenster1,
							"Das ändern des Datenbankschemas kann zu Datenverlust führen! Fortfahren?",
							"Datenbankschema ändern", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					
					if (value == 0) {
						
						if (updateDBScheme()) {
							//true
							JOptionPane.showMessageDialog(start.UIFenster1, "Das Datenbankschema wurde erfolgreich übernommen.",
									"Update Erfolgreich", JOptionPane.PLAIN_MESSAGE);
						} else {
							//false
							JOptionPane.showMessageDialog(start.UIFenster1,
									"Beim Übertragen des Schemas auf die Datenbank ist ein Fehler aufgetreten!",
									"Fehler beim Update", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(start.UIFenster1, "Es besteht keine Verbindung zu einer Datenbank.",
							"Fehlende Verbindung", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		mnNewMenu.add(mntmSchemaErneuern);
		
		JMenuItem mntmRunSqlStatement = new JMenuItem("Run SQL Statement");
		mntmRunSqlStatement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.show(pnCards, "SQL Manager");
			}
		});
		mnNewMenu.add(mntmRunSqlStatement);
		
		JMenuItem menu_showSubmitPat = new JMenuItem("Patientendaten bearbeiten");
		menu_showSubmitPat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.show(pnCards, "submitPatientendaten");
			}
		});
		mnMenu.add(menu_showSubmitPat);
		
		JMenuItem mntmPersonenVerknpfen = new JMenuItem("Personen verkn\u00FCpfen");
		mntmPersonenVerknpfen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.show(pnCards, "Change Person Fall");
			}
		});
		
		mntmOther = new JMenuItem("Fälle bearbeiten");
		mntmOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pnCards_Layout.show(pnCards, "submitFall");
			}
		});
		mnMenu.add(mntmOther);
		mnMenu.add(mntmPersonenVerknpfen);
		
		JMenuItem mntmDatenErneutEinfgen = new JMenuItem("Daten erneut einf\u00FCgen");
		mntmDatenErneutEinfgen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (start.methodsCompleted) {
					start.restart();
				}
			}
		});
		mnMenu.add(mntmDatenErneutEinfgen);
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
		
		panel_SQLManager = new JPanel();
		pnCards.add(panel_SQLManager, "SQL Manager");
		
		panel_ChangePersonFall = new JPanel();
		pnCards.add(panel_ChangePersonFall, "Change Person Fall");
		
		JLabel lblGeburtsdatum_2 = new JLabel("Geburtsdatum:");
		
		textField_Geburtsdatum_ChangePersonFall = new JTextField();
		textField_Geburtsdatum_ChangePersonFall.setColumns(10);
		
		textField_Vorname_ChangePersonFall = new JTextField();
		textField_Vorname_ChangePersonFall.setColumns(10);
		
		JLabel lblVorname_2 = new JLabel("Vorname:");
		
		textField_Name_ChangePersonFall = new JTextField();
		textField_Name_ChangePersonFall.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		
		textField_ENummer_ChangePersonFall = new JTextField();
		textField_ENummer_ChangePersonFall.setColumns(10);
		
		JLabel lblEnummer_1 = new JLabel("E.-Nummer:");
		
		JScrollPane scrollPane_ChangePersonFall = new JScrollPane();
		
		JButton btnSuchen = new JButton("Suchen");
		btnSuchen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (start.cn != null) {
					ChangePersonFallSelect();
				} else if (start.cn == null) {
					JOptionPane.showMessageDialog(start.UIFenster1, "Sie sind nicht mit der Datenbank verbunden!",
							"Keine Verbindung", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		JLabel lblGeburtsdatum_3 = new JLabel("Geburtsdatum:");
		
		textField_Geburtsdatum_ChangePersonFall_2 = new JTextField();
		textField_Geburtsdatum_ChangePersonFall_2.setColumns(10);
		
		textField_Vorname_ChangePersonFall_2 = new JTextField();
		textField_Vorname_ChangePersonFall_2.setColumns(10);
		
		textField_Name_ChangePersonFall_2 = new JTextField();
		textField_Name_ChangePersonFall_2.setColumns(10);
		
		JLabel lblVorname_3 = new JLabel("Vorname:");
		
		JLabel lblName_1 = new JLabel("Name:");
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				ChangePersonFall();
			}
		});
		GroupLayout gl_panel_ChangePersonFall = new GroupLayout(panel_ChangePersonFall);
		gl_panel_ChangePersonFall.setHorizontalGroup(
			gl_panel_ChangePersonFall.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_ChangePersonFall.createSequentialGroup()
					.addComponent(lblEnummer_1)
					.addGap(48)
					.addComponent(lblGeburtsdatum_2)
					.addGap(33)
					.addComponent(lblVorname_2)
					.addGap(59)
					.addComponent(lblName)
					.addGap(178))
				.addGroup(gl_panel_ChangePersonFall.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField_ENummer_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Geburtsdatum_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Vorname_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Name_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
					.addComponent(btnSuchen))
				.addComponent(scrollPane_ChangePersonFall, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
				.addGroup(gl_panel_ChangePersonFall.createSequentialGroup()
					.addComponent(lblGeburtsdatum_3, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(lblVorname_3)
					.addGap(57)
					.addComponent(lblName_1)
					.addGap(269))
				.addGroup(gl_panel_ChangePersonFall.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField_Geburtsdatum_ChangePersonFall_2, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Vorname_ChangePersonFall_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField_Name_ChangePersonFall_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
					.addComponent(btnUpdate))
		);
		gl_panel_ChangePersonFall.setVerticalGroup(
			gl_panel_ChangePersonFall.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_ChangePersonFall.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_ChangePersonFall.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEnummer_1)
						.addComponent(lblVorname_2)
						.addComponent(lblName)
						.addComponent(lblGeburtsdatum_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_ChangePersonFall.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSuchen)
						.addComponent(textField_ENummer_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Geburtsdatum_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Vorname_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Name_ChangePersonFall, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addComponent(scrollPane_ChangePersonFall, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel_ChangePersonFall.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGeburtsdatum_3)
						.addComponent(lblVorname_3)
						.addComponent(lblName_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_ChangePersonFall.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_Geburtsdatum_ChangePersonFall_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Vorname_ChangePersonFall_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_Name_ChangePersonFall_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnUpdate))
					.addContainerGap(113, Short.MAX_VALUE))
		);
		
		table_ChangePersonFall = new JTable();
		table_ChangePersonFall.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					rowToTextField("ChangePersonFall");
				}
			}
		});
		DefaultTableModel tableModel = new DefaultTableModel(
				new String[]{"E.-Nummer", "PatientenID", "Geburtsdatum", "Vorname", "Name"}, 0) {
			
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};
		table_ChangePersonFall.setModel(tableModel);
		scrollPane_ChangePersonFall.setViewportView(table_ChangePersonFall);
		panel_ChangePersonFall.setLayout(gl_panel_ChangePersonFall);
		
		textField_SQLStatement = new JTextField();
		textField_SQLStatement.setText("select * from ");
		textField_SQLStatement.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					SQL_ManagerToTable();
				}
			}
		});
		textField_SQLStatement.setColumns(10);
		
		JScrollPane scrollPane_SQL = new JScrollPane();
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (start.cn == null) {
					JOptionPane.showMessageDialog(start.UIFenster1, "Sie sind nicht mit der Datenbank verbunden!",
							"Keine Verbindung", JOptionPane.WARNING_MESSAGE);
				} else {
					SQL_ManagerToTable();
				}
			}
		});
		
		GroupLayout gl_panel_SQLManager = new GroupLayout(panel_SQLManager);
		gl_panel_SQLManager.setHorizontalGroup(
			gl_panel_SQLManager.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_SQLManager.createSequentialGroup()
					.addComponent(textField_SQLStatement, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnExecute))
				.addComponent(scrollPane_SQL, GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
		);
		gl_panel_SQLManager.setVerticalGroup(
			gl_panel_SQLManager.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_SQLManager.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_SQLManager.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_SQLStatement, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnExecute))
					.addGap(42)
					.addComponent(scrollPane_SQL, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
					.addGap(52))
		);
		
		table_SQL = new JTable();
		scrollPane_SQL.setViewportView(table_SQL);
		panel_SQLManager.setLayout(gl_panel_SQLManager);
		
		JScrollPane scrollPane_Fall = new JScrollPane();
		
		JButton btnTabelleAktualisieren_Fall = new JButton("Tabelle aktualisieren");
		btnTabelleAktualisieren_Fall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (start.cn == null) {
					JOptionPane.showMessageDialog(start.UIFenster1, "Sie sind nicht mit der Datenbank verbunden!",
							"Keine Verbindung", JOptionPane.WARNING_MESSAGE);
				} else {
					DBtoTable_Fall();					
				}
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
		
		DefaultTableModel tableModel_Patientendaten = new DefaultTableModel(
				new String[]{"Geburtsdatum", "Vorname", "Name", "Straße", "Hausnummer", "Land", "PLZ", "Ort"}, 0) {
			
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
				
				if (start.cn == null) {
					JOptionPane.showMessageDialog(start.UIFenster1, "Sie sind nicht mit der Datenbank verbunden!",
							"Keine Verbindung", JOptionPane.WARNING_MESSAGE);
				} else {
					DBtoTable_Patientendaten();
				}
			}
		});
		
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
	
	private void ChangePersonFall() {
		
		int selectedRow = table_ChangePersonFall.getSelectedRow();
		String geb = textField_Geburtsdatum_ChangePersonFall_2.getText();
		String vorname = textField_Vorname_ChangePersonFall_2.getText();
		String name = textField_Name_ChangePersonFall_2.getText();
		String gebOld = table_ChangePersonFall.getValueAt(selectedRow, 2).toString();
		String vornameOld = (String)table_ChangePersonFall.getValueAt(selectedRow, 3);
		String nameOld = (String)table_ChangePersonFall.getValueAt(selectedRow, 4);
		long patIDOld = (long) table_ChangePersonFall.getValueAt(selectedRow, 1);;
		int deleteOld = -1;
		
		if (geb.length() == 0 || vorname.length() == 0 || name.length() == 0) {
			JOptionPane.showMessageDialog(start.UIFenster1, "Bitte geben sie etwas ein!", "Fehler: Keine Eingabe", JOptionPane.PLAIN_MESSAGE);
			return;
		} else if (geb.equals(gebOld) && vorname.equals(vornameOld) && name.equals(nameOld)) {
			//Personen sind gleich
			JOptionPane.showMessageDialog(start.UIFenster1, "Die eingegebene Person stimmt mit der bereits eingetragenen Person überein.", 
					"Gleiche Person", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		try {
			PreparedStatement Pst_in = start.cn.prepareStatement("insert into mydb.patientendaten (Geburtsdatum, Vorname, Name) "
					+ "values ( ? , ? , ? );");
			
			Pst_in.setString(1, geb);
			Pst_in.setString(2, vorname);
			Pst_in.setString(3, name);			
			
			Pst_in.executeUpdate();
			Pst_in.close();
		} catch (SQLException e) {
			//Wenn die Person bereits existiert kommt es zur Exeption
			//System.out.println(e);
		}
		
		try {
			PreparedStatement Pst_up = start.cn.prepareStatement("update mydb.fall set Patientendaten_PatientenID = "
					+ "(select PatientenID from mydb.patientendaten where Geburtsdatum = ? and Vorname = ? and Name = ? ) where "
					+ "Patientendaten_PatientenID = ? ;");
			
			Pst_up.setString(1, geb);
			Pst_up.setString(2, vorname);
			Pst_up.setString(3, name);
			Pst_up.setLong(4, patIDOld);
			
			Pst_up.executeUpdate();
			Pst_up.close();
			
			deleteOld = JOptionPane.showConfirmDialog(start.UIFenster1, "Alle Fälle von " + vornameOld + " " + nameOld
					+ " wurden erfolgreich " + vorname + " " + name + " zugewiesen.\n"
					+ "Weitere Daten wurden ggf. noch nicht übertragen.\n\nSoll Person A gelöscht werden?",
					"Zuweisen erfolgreich", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			//0 - Ja, 1 - Nein, -1 - Fenster gesclossen
			
			textField_Geburtsdatum_ChangePersonFall_2.setText("");
			textField_Vorname_ChangePersonFall_2.setText("");
			textField_Name_ChangePersonFall_2.setText("");
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		ChangePersonFallSelect();
		
		if (deleteOld != 0) {
			return;
		} else if (deleteOld == 1) {
			
			try {
				PreparedStatement Pst_del = start.cn.prepareStatement("delete from mydb.patientendaten where PatientenID = ? and "
						+ "Geburtsdatum = ? and Vorname = ? and Name = ? ;");
				
				Pst_del.setLong(1, patIDOld);
				Pst_del.setString(2, gebOld);
				Pst_del.setString(2, vornameOld);
				Pst_del.setString(2, nameOld);
				
				Pst_del.executeUpdate();
				Pst_del.close();
			} catch (SQLException e) {
				System.out.println(e);
			}
			
		}
	}
	
	private void ChangePersonFallSelect() {
		
		String Gebdt = textField_Geburtsdatum_ChangePersonFall.getText();
		String vorname = textField_Vorname_ChangePersonFall.getText();
		String name = textField_Name_ChangePersonFall.getText();
		String eNR = textField_ENummer_ChangePersonFall.getText();
		
		DefaultTableModel tableModel = new DefaultTableModel(
				new String[]{"E.-Nummer", "PatientenID", "Geburtsdatum", "Vorname", "Name"}, 0) {
			
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};
		table_ChangePersonFall.setModel(tableModel);
		
		if (Gebdt.length() == 0 && vorname.length() == 0 && name.length() == 0 && eNR.length() == 0) {
			JOptionPane.showMessageDialog(start.UIFenster1, "Bitte geben sie etwas ein.", "Fehler: Keine Eingabe", JOptionPane.PLAIN_MESSAGE);
		} else {

			if (vorname.length() != 0) vorname += "%";
			if (name.length() != 0) name += "%";
			if (eNR.length() != 0) eNR += "%";
			
			try {

				ResultSet rs = null;
				Statement st = start.cn.createStatement();

				if (Gebdt.length() != 0) {
					//Gebdt eingegeben
					if (vorname.length() != 0) {
						//vorname eingegeben
						if (name.length() != 0) {
							//name eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and Vorname like \"" + vorname
										+ "\" and Name like \"" + name + "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and Vorname like \"" + vorname
										+ "\" and Name like \"" + name + "\";");
							}
						} else if (name.length() == 0) {
							//name nicht eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and Vorname like \"" + vorname
										+ "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and Vorname like \"" + vorname + "\";");
							}
						}
					} else if (vorname.length() == 0) {
						//vorname nicht eingegeben
						if (name.length() != 0) {
							//name eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and Name like \"" + name + "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and Name like \"" + name + "\";");
							}
						} else if (name.length() == 0) {
							//name nicht eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Geburtsdatum = \"" + Gebdt + "\";");
							}
						}
					}
				} else if (Gebdt.length() == 0) {
					//Gebdt nicht eingegeben
					if (vorname.length() != 0) {
						//vorname eingegeben
						if (name.length() != 0) {
							//name eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Vorname like \"" + vorname + "\" and Name like \"" + name + "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Vorname like \"" + vorname + "\" and Name like \"" + name + "\";");
							}
						} else if (name.length() == 0) {
							//name nicht eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Vorname like \"" + vorname + "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Vorname like \"" + vorname + "\";");
							}
						}
					} else if (vorname.length() == 0) {
						//vorname nicht eingegeben
						if (name.length() != 0) {
							//name eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Name like \"" + name + "\" and `E.-Nummer` like \"" + eNR + "\";");
							} else if (eNR.length() == 0) {
								//E-Nummer nicht eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where Name like \"" + name + "\";");
							}
						} else if (name.length() == 0) {
							//name nicht eingegeben
							if (eNR.length() != 0) {
								//E-Nummer eingegeben
								rs = st.executeQuery("select `E.-Nummer`, `PatientenID`, `Geburtsdatum`, `Vorname`, `Name` from mydb.vfallmitNamen "
										+ "where `E.-Nummer` like \"" + eNR + "\";");
							}
						}
					}
				}

				while (rs.next()) {
					Object[] data = new Object[5];

					for (int i = 0; i < data.length; i++) {
						data[i] = rs.getObject(i+1);
					}
					tableModel.addRow(data);
				}

				st.close();
				rs.close();

			} catch (SQLException e) {
				System.out.println(e);
			}

		}
	}
	
	private void SQL_ManagerToTable() {
		
		if (textField_SQLStatement.getText().length() != 0 && start.cn != null) {
			
			try {
				Statement st = start.cn.createStatement();
				ResultSet rs = st.executeQuery(textField_SQLStatement.getText());
				ResultSetMetaData rsMeta = rs.getMetaData();
				String[] columnNames = new String[rsMeta.getColumnCount()];
				
				for (int i = 1; i < rsMeta.getColumnCount()+1; i++) {
					columnNames[i-1] = rsMeta.getColumnLabel(i);
				}
				
				DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
				table_SQL.setModel(tableModel);
				
				while (rs.next()) {
					Object[] array = new Object[rsMeta.getColumnCount()];
					
					for (int i = 1; i < array.length+1; i++) {
						
						array[i-1] = rs.getObject(i);
						
					}
					tableModel.addRow(array);
				}
				
				rs.close();
				
			} catch (SQLException e1) {
				System.out.println(e1);
			}
		}
		
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
		} else if (tabelle.equals("ChangePersonFall")) {
			//TODO
			int row = table_ChangePersonFall.getSelectedRow();
			
			try {
				textField_Geburtsdatum_ChangePersonFall_2.setText(table_ChangePersonFall.getValueAt(row, 2).toString());
			} catch (Exception e) {
				textField_Geburtsdatum_ChangePersonFall_2.setText(null);
			}
			textField_Vorname_ChangePersonFall_2.setText((String) table_ChangePersonFall.getValueAt(row, 3));
			textField_Name_ChangePersonFall_2.setText((String) table_ChangePersonFall.getValueAt(row, 4));
			
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
			System.out.println("Manueller Input: Zeilen geändert: " + changedRows);
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
			
			System.out.println("Zeilen manuell geändert: " + Pst.executeUpdate());
			
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
			
			System.out.println("Zeilen manuell geändert: " + Pst.executeUpdate());
			
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
			
			DefaultTableModel tableModel = new DefaultTableModel(
					new String[]{"Geburtsdatum", "Vorname", "Name", "Straße", "Hausnummer", "Land", "PLZ", "Ort"}, 0) {
				
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
	
	private boolean updateDBScheme() {
		
		try {
			
			Statement st = start.cn.createStatement();
			st.execute("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
			st.execute("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
			st.execute("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';");
			st.execute("DROP SCHEMA IF EXISTS `mydb` ;");
			st.execute("CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;");
			st.execute("USE `mydb` ;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`patientendaten` (\r\n" + 
					"  `PatientenID` INT(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '',\r\n" + 
					"  `Name` VARCHAR(100) NOT NULL COMMENT '',\r\n" + 
					"  `Vorname` VARCHAR(100) NOT NULL COMMENT '',\r\n" + 
					"  `Geburtsdatum` DATE NOT NULL COMMENT '',\r\n" + 
					"  `Verstorben (Quelle)` VARCHAR(45) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Verstorben (Datum)` DATE NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Bemerkung Tod` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Follow-up` DATE NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Follow-up Status` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `EE-Status` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Strasse` VARCHAR(100) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Hausnummer` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Land` VARCHAR(3) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `PLZ` VARCHAR(6) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Ort` VARCHAR(45) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Fehler` INT(1) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`PatientenID`)  COMMENT '')\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"AUTO_INCREMENT = 0\r\n" + 
					"DEFAULT CHARACTER SET = utf8\r\n" + 
					"PACK_KEYS = 0;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`fall` (\r\n" + 
					"  `E.-Nummer` VARCHAR(15) NOT NULL COMMENT '',\r\n" + 
					"  `Befundtyp` INT(1) NOT NULL COMMENT '',\r\n" + 
					"  `Patientendaten_PatientenID` INT(11) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Arzt` VARCHAR(45) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Eingangsdatum` DATE NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Einsender` VARCHAR(15) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Kryo` TINYINT(1) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `OP-Datum` DATE NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Mikroskopie` TEXT NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Fehler` INT(1) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`E.-Nummer`, `Befundtyp`)  COMMENT '',\r\n" + 
					"  INDEX `Fall_FKIndex1` (`Patientendaten_PatientenID` ASC)  COMMENT '',\r\n" + 
					"  INDEX `Fall_FKIndex2` (`Patientendaten_PatientenID` ASC)  COMMENT '',\r\n" + 
					"  CONSTRAINT `fk_{464EC141-8213-44DD-8551-7BA9ACCDC20A}`\r\n" + 
					"    FOREIGN KEY (`Patientendaten_PatientenID`)\r\n" + 
					"    REFERENCES `mydb`.`patientendaten` (`PatientenID`)\r\n" + 
					"    ON DELETE NO ACTION\r\n" + 
					"    ON UPDATE NO ACTION)\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"DEFAULT CHARACTER SET = utf8\r\n" + 
					"PACK_KEYS = 0;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`tumor` (\r\n" + 
					"  `TumorID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '',\r\n" + 
					"  `R/L` VARCHAR(10) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Materialart` VARCHAR(15) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Größe (cm)` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Tumorprogress` DATE NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Lokalisation` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`TumorID`)  COMMENT '')\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"DEFAULT CHARACTER SET = utf8\r\n" + 
					"PACK_KEYS = 0;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`tumor` (\r\n" + 
					"  `TumorID` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '',\r\n" + 
					"  `R/L` VARCHAR(10) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Materialart` VARCHAR(15) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Größe (cm)` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Tumorprogress` DATE NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Lokalisation` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`TumorID`)  COMMENT '')\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"DEFAULT CHARACTER SET = utf8\r\n" + 
					"PACK_KEYS = 0;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`fall_has_tumor` (\r\n" + 
					"  `Fall_E.-Nummer` VARCHAR(15) NOT NULL COMMENT '',\r\n" + 
					"  `Fall_Nachbericht` INT(2) NOT NULL COMMENT '',\r\n" + 
					"  `Tumor_TumorID` INT(11) UNSIGNED NOT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`Fall_E.-Nummer`, `Fall_Nachbericht`, `Tumor_TumorID`)  COMMENT '',\r\n" + 
					"  INDEX `fk_Fall_has_Tumor_Tumor1_idx` (`Tumor_TumorID` ASC)  COMMENT '',\r\n" + 
					"  INDEX `fk_Fall_has_Tumor_Fall1_idx` (`Fall_E.-Nummer` ASC, `Fall_Nachbericht` ASC)  COMMENT '',\r\n" + 
					"  CONSTRAINT `fk_Fall_has_Tumor_Fall1`\r\n" + 
					"    FOREIGN KEY (`Fall_E.-Nummer` , `Fall_Nachbericht`)\r\n" + 
					"    REFERENCES `mydb`.`fall` (`E.-Nummer` , `Befundtyp`)\r\n" + 
					"    ON DELETE NO ACTION\r\n" + 
					"    ON UPDATE CASCADE,\r\n" + 
					"  CONSTRAINT `fk_Fall_has_Tumor_Tumor1`\r\n" + 
					"    FOREIGN KEY (`Tumor_TumorID`)\r\n" + 
					"    REFERENCES `mydb`.`tumor` (`TumorID`)\r\n" + 
					"    ON DELETE NO ACTION\r\n" + 
					"    ON UPDATE NO ACTION)\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"DEFAULT CHARACTER SET = utf8;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`klassifikation` (\r\n" + 
					"  `Fall_E.-Nummer` VARCHAR(15) NOT NULL COMMENT '',\r\n" + 
					"  `Fall_Befundtyp` INT(1) NOT NULL COMMENT '',\r\n" + 
					"  `Tumorklassifizierung` VARCHAR(15) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Quadrant` VARCHAR(10) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `G` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `T` VARCHAR(3) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `N` VARCHAR(3) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `M` VARCHAR(3) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `L` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `V` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `R` INT(2) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `N gesamt` INT(3) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `N meta` INT(3) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `ER` VARCHAR(2) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `ER IRS` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `PR` VARCHAR(2) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `PR IRS` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Her2/neu` VARCHAR(2) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Her2/neu-Score` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Ki67` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" +
					"  `Lage` VARCHAR(10) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Tumorart` VARCHAR(20) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`Fall_E.-Nummer`, `Fall_Befundtyp`)  COMMENT '',\r\n" + 
					"  CONSTRAINT `fk_Klassifikation_Fall1`\r\n" + 
					"    FOREIGN KEY (`Fall_E.-Nummer` , `Fall_Befundtyp`)\r\n" + 
					"    REFERENCES `mydb`.`fall` (`E.-Nummer` , `Befundtyp`)\r\n" + 
					"    ON DELETE NO ACTION\r\n" + 
					"    ON UPDATE CASCADE)\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"DEFAULT CHARACTER SET = utf8\r\n" + 
					"PACK_KEYS = 0;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`medikation` (\r\n" + 
					"  `Patientendaten_PatientenID` INT(11) UNSIGNED ZEROFILL NOT NULL COMMENT '',\r\n" + 
					"  `Tamoxifen` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Chemo` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `Radiatio` INT(11) UNSIGNED NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  `AromataseH` INT(11) NULL DEFAULT NULL COMMENT '',\r\n" + 
					"  PRIMARY KEY (`Patientendaten_PatientenID`)  COMMENT '',\r\n" + 
					"  CONSTRAINT `fk_Medikation_Patientendaten1`\r\n" + 
					"    FOREIGN KEY (`Patientendaten_PatientenID`)\r\n" + 
					"    REFERENCES `mydb`.`patientendaten` (`PatientenID`)\r\n" + 
					"    ON DELETE NO ACTION\r\n" + 
					"    ON UPDATE NO ACTION)\r\n" + 
					"ENGINE = InnoDB\r\n" + 
					"DEFAULT CHARACTER SET = utf8\r\n" + 
					"PACK_KEYS = 0;");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`vfallmitnamen` (`E.-Nummer` INT, `Befundtyp` INT, `Arzt` INT,"
					+ " `Eingangsdatum` INT, `Einsender` INT, `Fehler` INT, `PatientenID` INT, `Geburtsdatum` INT, `Vorname` INT, `Name` INT);");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`vfehlerfall` (`E.-Nummer` INT, `Befundtyp` INT, `Arzt` INT,"
					+ " `Eingangsdatum` INT, `Einsender` INT, `Fehler` INT, `PatientenID` INT, `Geburtsdatum` INT, `Vorname` INT, `Name` INT);");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`vpatientendaten_hauptparameter` (`PatientenID` INT, `Vorname` INT, `Name` INT,"
					+ " `Geburtsdatum` INT, `Strasse` INT, `Hausnummer` INT, `Land` INT, `PLZ` INT, `Ort` INT, `Fehler` INT);");
			st.execute("CREATE TABLE IF NOT EXISTS `mydb`.`vpatientenkeyelements` (`PatientenID` INT, `Geburtsdatum` INT, `Vorname` INT, `Name` INT);");
			st.execute("DROP TABLE IF EXISTS `mydb`.`vfallmitnamen`;");
			st.execute("USE `mydb`;");
			st.execute("CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `mydb`.`vfallmitnamen`"
					+ " AS select `mydb`.`fall`.`E.-Nummer` AS `E.-Nummer`,`mydb`.`fall`.`Befundtyp` AS `Befundtyp`,"
					+ "`mydb`.`fall`.`Arzt` AS `Arzt`,`mydb`.`fall`.`Eingangsdatum` AS `Eingangsdatum`,`mydb`.`fall`.`Einsender` AS `Einsender`,"
					+ "`mydb`.`fall`.`Fehler` AS `Fehler`,`mydb`.`fall`.`Patientendaten_PatientenID` AS `PatientenID`,"
					+ "`pat`.`Geburtsdatum` AS `Geburtsdatum`,`pat`.`Vorname` AS `Vorname`,`pat`.`Name` AS `Name`"
					+ " from (`mydb`.`fall` left join `mydb`.`patientendaten` `pat` on((`mydb`.`fall`.`Patientendaten_PatientenID` = "
					+ "`pat`.`PatientenID`)));");
			st.execute("DROP TABLE IF EXISTS `mydb`.`vfehlerfall`;");
			st.execute("USE `mydb`;");
			st.execute("CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `mydb`.`vfehlerfall` "
					+ "AS select `mydb`.`fall`.`E.-Nummer` AS `E.-Nummer`,`mydb`.`fall`.`Befundtyp` AS `Befundtyp`,"
					+ "`mydb`.`fall`.`Arzt` AS `Arzt`,`mydb`.`fall`.`Eingangsdatum` AS `Eingangsdatum`,`mydb`.`fall`.`Einsender` "
					+ "AS `Einsender`,`mydb`.`fall`.`Fehler` AS `Fehler`,`mydb`.`fall`.`Patientendaten_PatientenID` AS `PatientenID`,"
					+ "`pat`.`Geburtsdatum` AS `Geburtsdatum`,`pat`.`Vorname` AS `Vorname`,`pat`.`Name` AS `Name` "
					+ "from (`mydb`.`fall` left join `mydb`.`patientendaten` `pat` on((`mydb`.`fall`.`Patientendaten_PatientenID` = "
					+ "`pat`.`PatientenID`))) where (`mydb`.`fall`.`Fehler` <> 0);");
			st.execute("DROP TABLE IF EXISTS `mydb`.`vpatientendaten_hauptparameter`;");
			st.execute("USE `mydb`;");
			st.execute("CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW"
					+ " `mydb`.`vpatientendaten_hauptparameter` AS select `mydb`.`patientendaten`.`PatientenID` AS `PatientenID`,"
					+ "`mydb`.`patientendaten`.`Vorname` AS `Vorname`,`mydb`.`patientendaten`.`Name` AS `Name`,"
					+ "`mydb`.`patientendaten`.`Geburtsdatum` AS `Geburtsdatum`,`mydb`.`patientendaten`.`Strasse` AS `Strasse`,"
					+ "`mydb`.`patientendaten`.`Hausnummer` AS `Hausnummer`,`mydb`.`patientendaten`.`Land` AS `Land`,"
					+ "`mydb`.`patientendaten`.`PLZ` AS `PLZ`,`mydb`.`patientendaten`.`Ort` AS `Ort`,`mydb`.`patientendaten`.`Fehler` "
					+ "AS `Fehler` from `mydb`.`patientendaten`;");
			st.execute("DROP TABLE IF EXISTS `mydb`.`vpatientenkeyelements`;");
			st.execute("USE `mydb`;");
			st.execute("CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW"
					+ " `mydb`.`vpatientenkeyelements` AS select `mydb`.`patientendaten`.`PatientenID` AS `PatientenID`,"
					+ "`mydb`.`patientendaten`.`Geburtsdatum` AS `Geburtsdatum`,`mydb`.`patientendaten`.`Vorname` AS `Vorname`,"
					+ "`mydb`.`patientendaten`.`Name` AS `Name` from `mydb`.`patientendaten`;");
			st.execute("USE `mydb`;");
			st.execute("CREATE\r\n" +
					"DEFINER=`root`@`localhost`\r\n" + 
					"TRIGGER `mydb`.`trDeletePatient`\r\n" + 
					"BEFORE DELETE ON `mydb`.`patientendaten`\r\n" + 
					"FOR EACH ROW\r\n" + 
					"BEGIN\r\n" + 
					"	update mydb.fall set Patientendaten_PatientenID = null, Fehler = 1 where Patientendaten_PatientenID = OLD.PatientenID;\r\n" + 
					"END;");
			st.execute("USE `mydb`;");
			st.execute("CREATE\r\n" + 
					"DEFINER=`root`@`localhost`\r\n" + 
					"TRIGGER `mydb`.`trDoppeltePatienten`\r\n" + 
					"BEFORE INSERT ON `mydb`.`patientendaten`\r\n" + 
					"FOR EACH ROW\r\n" + 
					"BEGIN\r\n" + 
					"	declare count INT;\r\n" + 
					"    set count = (select count(*) from mydb.patientendaten where Name = NEW.Name and Vorname = NEW.Vorname and Geburtsdatum = NEW.Geburtsdatum);\r\n" + 
					"    if count > 0 then\r\n" + 
					"		set NEW.`Name` = null;\r\n" + 
					"    end if;\r\n" + 
					"END;");
			st.execute("USE `mydb`;");
			st.execute("CREATE\r\n" + 
					"DEFINER=`root`@`localhost`\r\n" + 
					"TRIGGER `mydb`.`trUpdateDoppeltePatienten`\r\n" + 
					"BEFORE UPDATE ON `mydb`.`patientendaten`\r\n" + 
					"FOR EACH ROW\r\n" + 
					"BEGIN\r\n" + 
					"	declare count INT;\r\n" + 
					"    set count = (select count(*) from mydb.patientendaten where Name = NEW.Name and Vorname = NEW.Vorname and Geburtsdatum = NEW.Geburtsdatum and not PatientenID = OLD.PatientenID);\r\n" + 
					"    if count > 0 then\r\n" + 
					"		set NEW.`Name` = null;\r\n" + 
					"    end if;\r\n" + 
					"END;");
			st.execute("USE `mydb`;");
			st.execute("CREATE\r\n" + 
					"DEFINER=`root`@`localhost`\r\n" + 
					"TRIGGER `mydb`.`trDeleteFall`\r\n" + 
					"BEFORE DELETE ON `mydb`.`fall`\r\n" + 
					"FOR EACH ROW\r\n" + 
					"BEGIN\r\n" + 
					"	delete from mydb.klassifikation where `Fall_E.-Nummer` = OLD.`E.-Nummer` and Fall_Befundtyp = OLD.Befundtyp;\r\n" + 
					"END;");
			st.execute("USE `mydb`;");
			st.execute("CREATE\r\n" + 
					"DEFINER=`root`@`localhost`\r\n" + 
					"TRIGGER `mydb`.`trPatientenIDnull`\r\n" + 
					"BEFORE INSERT ON `mydb`.`fall`\r\n" + 
					"FOR EACH ROW\r\n" + 
					"BEGIN\r\n" + 
					"	if NEW.`Patientendaten_PatientenID` is null then\r\n" + 
					"    set NEW.`Fehler` = 1;\r\n" + 
					"    end if;\r\n" + 
					"END;");
			st.execute("SET SQL_MODE=@OLD_SQL_MODE;");
			st.execute("SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;");
			st.execute("SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;");
			
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
}
