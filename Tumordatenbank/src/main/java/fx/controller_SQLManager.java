package fx;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import tableMasks.Prototyp_universell;

public class controller_SQLManager implements Initializable {

	@FXML public static AnchorPane mainPanel;
	@FXML public TextField txtField_Statement;
	@FXML public ComboBox<String> cmbBox_Statement;
	@FXML public TableView<Prototyp_universell> table;
	//private ObservableList<Prototyp_universell> data;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("controller SQLManager");
		
		table.getColumns().setAll(Prototyp_universell.getColumns());
	}
	
	public void executeStatement() {

//		System.out.println(cmbBox_Statement.getSelectionModel().getSelectedItem());
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = FX_Main.cn.createStatement();
			//rs = st.executeQuery(cmbBox_Statement.getSelectionModel().getSelectedItem());
			rs = st.executeQuery("select patdaten.`Name`, patdaten.Vorname, patdaten.Geburtsdatum ,mydb.fall.`E.-Nummer`, mydb.klassifikation.ER, mydb.klassifikation.PR,  mydb.klassifikation.`Her2/neu`, mydb.klassifikation.lage, mydb.klassifikation.Tumorart,\r\n" + 
					" klassifikation.G, klassifikation.T, klassifikation.N, klassifikation.N, klassifikation.M, klassifikation.L, klassifikation.V, klassifikation.R\r\n" + 
					"from mydb.klassifikation as klassifikation\r\n" + 
					"join mydb.fall as fall \r\n" + 
					"join mydb.patientendaten as patdaten on klassifikation.`Fall_E.-Nummer` = fall.`E.-Nummer` and klassifikation.Fall_Befundtyp = fall.Befundtyp and patdaten.PatientenID = fall.Patientendaten_PatientenID;");
			
			while (rs.next()) {
				LocalDate geburtsdatum = null;
				if (rs.getDate("Geburtsdatum") != null) geburtsdatum = rs.getDate("Geburtsdatum").toLocalDate();
				
				Prototyp_universell data = new Prototyp_universell(geburtsdatum, rs.getString("Vorname"), rs.getString("Name"),
						rs.getString("E.-Nummer"), rs.getString("ER"), rs.getString("PR"), rs.getString("Her2/neu"), 
						rs.getString("Tumorart"), rs.getString("T"), rs.getString("N"), rs.getString("M"), 
						rs.getInt("G"), rs.getInt("L"), rs.getInt("R"), rs.getInt("V"));
				
				table.getItems().add(data);
			}
		} catch (SQLException e) {
			System.err.println(e + "\nFehler beim Statement/ResultSet in executeStatement() in " + getClass().getName());
			return;
		}
	}

}
