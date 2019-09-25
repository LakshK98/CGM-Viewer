package application;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.imageio.ImageIO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class MainController {
	@FXML
	private Button selectCGMBtn;
	@FXML
	private TextField selectedFileTextField;
	@FXML
	private Button displayBtn, saveBtn;
	@FXML
	private TextArea editTextArea;
	@FXML
	private Label invalidFileLabel, editLabel, saveLabel, invalidCGMLabel;
	File selectedFile = null;

	@FXML
	public void initialize() {
		editTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				System.out.println("text change");
				saveLabel.setText("Unsaved");

			}
		});
	}

	public static boolean isPureAscii(String v) {
		return Charset.forName("US-ASCII").newEncoder().canEncode(v);
	}

	public void selectCGMAction(ActionEvent event) {

		System.out.println("Reached");

		FileChooser fc = new FileChooser();
		selectedFile = fc.showOpenDialog(null);

		if (selectedFile != null && isCGM(selectedFile.getName())) {

			selectedFileTextField.setText(selectedFile.getName());
			try (InputStream inputStream = new BufferedInputStream(new FileInputStream(selectedFile));) {

				byte[] buffer = new byte[10];

				int bytesRead = 0;
				int countUn = 0, countPure = 0, countLen = 0, countUnder = 0, newLine = 5;
				StringBuilder sb = new StringBuilder();
				editTextArea.setText("");
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					if (bytesRead != buffer.length) {
						buffer = new byte[bytesRead];
					}
					String editString = new String(buffer, StandardCharsets.US_ASCII);

					if (!isPureAscii(editString)) {

						editString = "l29K" + Base64.getEncoder().encodeToString(buffer) + "K92l";
						countUn++;
					} else {
						countPure++;
						StringBuilder constructString = new StringBuilder();
						for (int byteIndex = 0; byteIndex < buffer.length; byteIndex++) {
							if (buffer[byteIndex] < 32 || buffer[byteIndex] == 127)
								constructString.append("°" + buffer[byteIndex] + "°");
							else
								constructString.append((char) buffer[byteIndex]);
						}

						countLen += constructString.length();

						
						editString = constructString.toString();
					}
					sb.append(editString);
					newLine--;
					if (newLine == 0) {
						newLine = 5;
						sb.append("\n");
					}

				}
				System.out.println("Pure:" + countPure);
				System.out.println("ImPure:" + countUn);
				System.out.println("Len:" + countLen);
				System.out.println("Under 32:" + countUnder);

				editTextArea.setText(sb.toString());
				saveLabel.setText("Saved");

				

			} catch (IOException ex) {
				ex.printStackTrace();
			}
			invalidFileLabel.setVisible(false);
			editLabel.setVisible(true);
			editTextArea.setVisible(true);
			saveLabel.setVisible(true);
			saveBtn.setVisible(true);
			displayBtn.setVisible(true);

		} else {
			selectedFileTextField.setText("");
			System.out.println("File invalid");
			invalidFileLabel.setVisible(true);
			editLabel.setVisible(false);
			editTextArea.setVisible(false);
			saveLabel.setVisible(false);
			saveBtn.setVisible(false);
			displayBtn.setVisible(false);
		}
		invalidCGMLabel.setVisible(false);

	}

	public boolean isCGM(String filename) {
//		return false;
		return (filename.substring(filename.length() - 3, filename.length()).equalsIgnoreCase("cgm"));
	}

	public void displayBtnAction(ActionEvent event) {
		try {
			File cgmFile = selectedFile;
			File outFile = new File("cgmImage.png");
			BufferedImage image = ImageIO.read(cgmFile);
			ImageIO.write(image, "PNG", outFile);
			System.out.println("written");

			FileWriter fw = new FileWriter("cgmView.html");
			fw.write("<div><h2>" + selectedFile.getName() + "</h2><img src=cgmImage.png><div>");
			fw.close();
			File htmlFile = new File("cgmView.html");

			Desktop.getDesktop().browse(htmlFile.toURI());

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			invalidCGMLabel.setVisible(true);
		}

	}

	public void saveBtnAction(ActionEvent event) {
		String editedString = editTextArea.getText().replace("\n", "");
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(selectedFile));) {
			int countUn = 0, countPure = 0, countLen = 0;

			while (editedString.length() != 0) {

				if (editedString.substring(0, 4).equals("l29K")) {

					editedString = editedString.substring(4);
					int endIndex = editedString.indexOf("K92l");
					String convertString = editedString.substring(0, endIndex);
					editedString = editedString.substring(endIndex + 4);
					byte[] decodedBytes = Base64.getDecoder().decode(convertString);
					for (int byteindex = 0; byteindex < decodedBytes.length; byteindex++) {
						outputStream.write(decodedBytes[byteindex]);

					}
					countUn++;

				} else {
					countPure++;
					int endIndex = editedString.indexOf("l29K");
					if (endIndex == -1)
						endIndex = editedString.length();
					String convertString = editedString.substring(0, endIndex);
					countLen += convertString.length();

					editedString = editedString.substring(endIndex);

					byte outByte;
					while (convertString.length() != 0) {
						if (convertString.charAt(0) == '°') {
							convertString = convertString.substring(1);

							int stringEndIndex = convertString.indexOf('°');
							int convertToByte = Integer.parseInt(convertString.substring(0, stringEndIndex));
							convertString = convertString.substring(stringEndIndex + 1);
							outByte = (byte) convertToByte;
						} else {
							outByte = (byte) convertString.charAt(0);
							convertString = convertString.substring(1);

						}

						outputStream.write(outByte);

					}
				}

			}
			System.out.println("Pure:" + countPure);
			System.out.println("ImPure:" + countUn);
			System.out.println("Len:" + countLen);
			saveLabel.setText("Saved");
		}

		catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}