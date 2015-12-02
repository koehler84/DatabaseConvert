package fx;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.mysql.jdbc.CommunicationsException;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class controller_Main implements Initializable {

	@FXML public AnchorPane centerPanel;
	@FXML public ProgressBar progressBar;
	@FXML private ImageView imgConnected;
	@FXML private ImageView imgDisconnected;
	@FXML private Label lblConnected;
	private static Label static_lblConnected;
	private static ImageView static_imgConnected;
	private static ImageView static_imgDisconnected;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//access to non-static variable!
		static_lblConnected = lblConnected;
		static_imgConnected = imgConnected;
		static_imgDisconnected = imgDisconnected;
		
		imgConnected.setVisible(false);
		lblConnected.setVisible(false);
		System.out.println("init");

		setCenterPanel(controller_Logo.getMainPanel());
		
		Task<Boolean> task_connect = FX_Main.connect();
		new Thread(task_connect).start();
		
	}

	static Label getStatic_lblConnected() {
		return static_lblConnected;
	}

	public void abbrechen_Button(ActionEvent e) {
		try {
			if (FX_Main.cn != null && !FX_Main.cn.isClosed() && FX_Main.methodsCompleted) {
				FX_Main.cn.close();
				System.out.println("Datenbankverbindung beednet! WINDOW");
			}
		} catch (SQLException e1) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}

		System.exit(0);
	}

	public void ok_Button(ActionEvent e) {				
		try {
			if (FX_Main.cn != null && !FX_Main.cn.isClosed() && FX_Main.methodsCompleted) {
				FX_Main.cn.close();
				System.out.println("Datenbankverbindung beednet! WINDOW");
			}
		} catch (SQLException e1) {
			System.out.println("Fehler beim Beenden der Datenbankverbindung!");
		}

		FX_Window.window.close();
	}
	
	public void setCenterPanel(Pane pane) {
		centerPanel.getChildren().setAll(pane);
		pane.prefWidthProperty().bind(centerPanel.widthProperty());
		pane.prefHeightProperty().bind(centerPanel.heightProperty());
	}

	public void setCenterPanel_Patientendaten() {
		setCenterPanel(controller_Patientendaten.getMainPanel());
	}

	public void setCenterPanel_Fall() {
		setCenterPanel(controller_Fall.getMainPanel());
	}

	public void setCenterPanel_SQLManager() {
		setCenterPanel(controller_SQLManager.getMainPanel());
	}

	public void rebuildDB() {
		try {
			Statement st = FX_Main.cn.createStatement();
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

		} catch (CommunicationsException ex) {
			controller_Main.getStatic_lblConnected().setVisible(false);
		} catch (SQLException e) {
			System.out.println(e);
		}

	}
	
	/**
	 * Method returns whether the Connection indicator (Label/picture) shows the database connection as connected or disconnected
	 * @return <b>true</b> - Connection indicator shows connected state<br>
	 * <b>false</b> - Connection indicator shows disconnected state
	 */
	public static boolean getConnectionIndicatorState() {
		if (static_lblConnected.isVisible()) return true;
		return false;
	}
	
	/**
	 * Changes the appearance of the Connection indicator (Label/picture). If it shows the database connection as connected, 
	 * it will change it to show a disconnected state and vice versa. 
	 */
	public static void changeConnectionIndicatorState() {
		boolean state = getConnectionIndicatorState();
		
		if (state) {
			static_imgConnected.setVisible(false);
			static_imgDisconnected.setVisible(true);
		} else {
			static_imgDisconnected.setVisible(false);
			static_imgConnected.setVisible(true);
		}
		
		if (state) {
			static_lblConnected.setVisible(false);
		} else {
			static_lblConnected.setVisible(true);
		}		
	}
	
	/**
	 * Method sets Connection indicator to certain state. 
	 * @param b <b>true</b> - Connection indicator will show connected state<br>
	 * <b>false</b> - Connection indicator will show disconnected state
	 */
	public static void setConnectionIndicatorState(boolean b) {
		if (b) {
			static_lblConnected.setVisible(true);
			static_imgDisconnected.setVisible(false);
			static_imgConnected.setVisible(true);
		} else {
			static_lblConnected.setVisible(false);
			static_imgConnected.setVisible(false);
			static_imgDisconnected.setVisible(true);
		}
	}

	public void setProgress() {
		progressBar.progressProperty().unbind();

		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {

				for (long i = 0; i < 300000; i++) {
					System.out.println("Erg: " + i);
					updateProgress(i, 300000);
				}

				return null;
			}
		};

		progressBar.progressProperty().bind(task.progressProperty());
		Thread thread = new Thread(task);
		thread.start();

	}

	/**
	 * Method starts analyzing an excel file and putting data into the database. This method opens a <i>FileChooser</i> 
	 * to let the user choose a path.
	 */
	public void datenAnalyse() {

		final File file = new FileChooser().showOpenDialog(FX_Window.window);

		if (file != null && file.exists() && FX_Main.cn != null) {

			Task<XSSFSheet> loadSheet = FX_Main.loadExcel(file);
			progressBar.setProgress(-1);
			new Thread(loadSheet).start();
			
			while (!loadSheet.isDone()) {
			}

			Task<Void> startTask = FX_Main.excelToPatient(loadSheet);
			progressBar.progressProperty().bind(startTask.progressProperty());
			new Thread(startTask).start();
			System.out.println("test");
			
			Task<Void> continueTask = FX_Main.excelToFall(loadSheet);
			progressBar.progressProperty().bind(continueTask.progressProperty());
			new Thread(continueTask).start();
			
		} else if (FX_Main.cn == null) {

		} else {
			//user nerven
		}

	}
	
	public void connect() {
		try {
			new Thread().start();
			if (FX_Main.cn.isClosed()) {		
			}
		} catch (SQLException e) {
			System.err.println(e.getLocalizedMessage() + " - Failed to reconnect.");
		} catch (NullPointerException e) {
			new Thread().start();
		} /*finally {
			setConnectionIndicatorState(true);
		}*/
	}
	
	public void disconnect() {
		try {
			if (!FX_Main.cn.isClosed()) {
				FX_Main.cn.close();
				setConnectionIndicatorState(false);
			}
		} catch (SQLException e) {
			System.err.println(e.getLocalizedMessage() + " - Failed to disconnect.");
		} catch (NullPointerException e) {
			System.err.println(e.getLocalizedMessage() + " - FX_Main.cn is null.");
		} /*finally {
			setConnectionIndicatorState(false);
		}*/
	}

}
