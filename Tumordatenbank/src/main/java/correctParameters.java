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

public class correctParameters extends JFrame {

	private JPanel contentPane;
	private JPanel panel_submitPatientendaten;
	public JProgressBar progressBar;
	public JTable table;
	public JScrollPane scrollPane;
	private JTextField textField_Geburtsdatum;
	private JTextField textField_Vorname;
	private JTextField textField_Name;
	private JTextField textField_Strasse;
	private JTextField textField_Hausnummer;
	private JTextField textField_Land;
	private JTextField textField_PLZ;
	private JTextField textField_Ort;
	public JLabel lblConnected;
	private JLabel lblGeburtsdatum;
	private JLabel lblVorname;
	private JLabel lblNachname;
	private JLabel lblStrae;
	private JLabel lblHausnummer;
	private JLabel lblLand;
	private JLabel lblPostleitzahl;
	private JLabel lblOrt;
	private JCheckBox checkBox_Vollstaendig;
	private boolean doubleCheck = false;

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
		setMinimumSize(new Dimension(550, 400));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnTest = new JMenu("Test");
		menuBar.add(mnTest);
		
		JMenu mnNewMenu = new JMenu("New menu");
		mnTest.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnTest.add(mntmNewMenuItem);
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
		
		panel_submitPatientendaten = new JPanel();
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (panel_submitPatientendaten.isVisible()) {
					panel_submitPatientendaten.setVisible(false);
				} else {
					panel_submitPatientendaten.setVisible(true);
				}
			}
		});
		
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
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_submitPatientendaten, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
					.addGap(1))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel_submitPatientendaten, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
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
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//Put row content into textfields on double click
					rowToTextField();
					
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
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		
		lblGeburtsdatum = new JLabel("Geburtsdatum:");
		
		lblVorname = new JLabel("Vorname:");
		
		lblNachname = new JLabel("Nachname:");
		
		lblStrae = new JLabel("Stra\u00DFe:");
		
		lblHausnummer = new JLabel("Hausnummer:");
		
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
		
		lblLand = new JLabel("Land:");
		
		lblPostleitzahl = new JLabel("Postleitzahl:");
		
		lblOrt = new JLabel("Ort:");
		
		JLabel lblDatensatzIstVollstndig = new JLabel("Datensatz ist vollst\u00E4ndig:");
		
		checkBox_Vollstaendig = new JCheckBox("");
		
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
				
				if (table.getSelectedRow() != -1) {
					
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
							doubleCheck = true;
						}
						
						if (textField_Vorname.getText().length() == 0) {
							textField_Vorname.setBackground(Color.RED);
							doubleCheck = true;
						}
						
						if (textField_Name.getText().length() == 0) {
							textField_Name.setBackground(Color.RED);
							doubleCheck = true;
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
		GroupLayout gl_panel_submitPatientendaten = new GroupLayout(panel_submitPatientendaten);
		gl_panel_submitPatientendaten.setHorizontalGroup(
			gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
				.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
					.addGap(209)
					.addComponent(lblNachname)
					.addGap(49)
					.addComponent(lblStrae)
					.addGap(69)
					.addComponent(lblHausnummer)
					.addContainerGap())
				.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
					.addComponent(lblGeburtsdatum)
					.addGap(33)
					.addComponent(lblVorname)
					.addGap(372))
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
							.addGap(15)
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
							.addComponent(checkBox_Vollstaendig)))
					.addContainerGap(43, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel_submitPatientendaten.createSequentialGroup()
					.addContainerGap(452, Short.MAX_VALUE)
					.addComponent(btnFertig)
					.addContainerGap())
		);
		gl_panel_submitPatientendaten.setVerticalGroup(
			gl_panel_submitPatientendaten.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
					.addGap(27)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
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
							.addComponent(lblStrae))
						.addGroup(gl_panel_submitPatientendaten.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblHausnummer)))
					.addPreferredGap(ComponentPlacement.RELATED)
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
						.addComponent(checkBox_Vollstaendig))
					.addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
					.addComponent(btnFertig)
					.addContainerGap())
		);
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
	
	private void rowToTextField() {
		
		int row = table.getSelectedRow();
		
		textField_Geburtsdatum.setText(table.getValueAt(row, 0).toString());
		textField_Vorname.setText((String) table.getValueAt(row, 1));
		textField_Name.setText((String) table.getValueAt(row, 2));
		textField_Strasse.setText((String) table.getValueAt(row, 3));
		textField_Hausnummer.setText((String) table.getValueAt(row, 4));
		textField_Land.setText((String) table.getValueAt(row, 5));
		textField_PLZ.setText((String) table.getValueAt(row, 6));
		textField_Ort.setText((String) table.getValueAt(row, 7));
		
	}
	
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
			System.out.println("Manueller Input: Zeilen ge‰ndert: " + changedRows);
			Pst.close();
			
			if (table.getRowCount() > 0) {
				((DefaultTableModel)table.getModel()).removeRow(0);
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
			
			if (table.getSelectedRow() != -1) {
				Pst.setString(10, table.getValueAt(table.getSelectedRow(), 0).toString());		//where Geb
				Pst.setString(11, table.getValueAt(table.getSelectedRow(), 1).toString());		//where Vorname
				Pst.setString(12, table.getValueAt(table.getSelectedRow(), 2).toString());		//where Name
			} else {
				Pst.setString(10, table.getValueAt(0, 0).toString());		//where Geb
				Pst.setString(11, table.getValueAt(0, 1).toString());		//where Vorname
				Pst.setString(12, table.getValueAt(0, 2).toString());		//where Name
			}
			
			Pst.setString(1, textField_Geburtsdatum.getText());	//set Geb
			Pst.setString(2, textField_Vorname.getText());	//set Vorname
			Pst.setString(3, textField_Name.getText());	//set Name
			Pst.setString(4, textField_Strasse.getText());	//set Strasse
			Pst.setString(5, textField_Hausnummer.getText());	//set Hausnummer
			Pst.setString(6, textField_Land.getText());	//set Land
			Pst.setString(8, textField_Ort.getText());	//set Ort
			
			if (textField_PLZ.getText().length() != 0) {
				Pst.setInt(7, Integer.parseInt(textField_PLZ.getText()));
			} else {
				Pst.setString(7, null);
			}
			
			if (checkBox_Vollstaendig.isSelected()) {
				Pst.setInt(9, 0);
			} else {
				Pst.setInt(9, 1);
			}
			
			System.out.println("Zeilen manuell ge‰ndert: " + Pst.executeUpdate());
			
			Pst.close();
			
			if (table.getRowCount() > 0 && table.getSelectedRow() != -1) {
				((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
			} else {
				((DefaultTableModel)table.getModel()).removeRow(0);
			}
			
			textField_Geburtsdatum.setText("");
			textField_Vorname.setText("");
			textField_Name.setText("");
			textField_Strasse.setText("");
			textField_Hausnummer.setText("");
			textField_Land.setText("");
			textField_PLZ.setText("");
			textField_Ort.setText("");
			checkBox_Vollstaendig.setSelected(false);
			
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
		}
		
	}
	
	@SuppressWarnings("serial")
	public void insertModel() {
		
		try {
			Statement st = start.cn.createStatement();
			ResultSet res = st.executeQuery("select * from mydb.vPatientendaten_Hauptparameter where `Fehler` != 0");
			
			DefaultTableModel tableModel = new DefaultTableModel(
					new String[]{"Geburtsdatum", "Vorname", "Name", "Straﬂe", "Hausnummer", "Land", "PLZ", "Ort"}, 0) {
				
				public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				}
			};
			
			table.setModel(tableModel);
			
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
		}
		
	}
}
