package Classes.Data;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Class representing a Line of the GT.
 */
public class DataLine {
	private String text;
	private DataLineType type;
	private Image image;
	private File imgFile;

	/**
	 * Creates a new DataLine.
	 *
	 * @param text Text of the GT.
	 * @param type Type of the GT.
	 * @param file File containing the image corresponding to the GT item.
	 */
	public DataLine(String text, DataLineType type, File file) {
		this.text = text;
		this.type = type;
		this.imgFile = file;
		image = new Image("file:///" + imgFile.getAbsolutePath());
	}

	/**
	 * Gets the text of the DataLine.
	 *
	 * @return Text of the GT.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Gets the type of the DataLine. (e.g. Paragraph, Heading, etc.)
	 *
	 * @return Type of the DataLine.
	 */
	public DataLineType getType() {
		return this.type;
	}

	/**
	 * Gets the Image of the DataLine.
	 *
	 * @return Image of the DataLine.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Gets the file containing the Image of the DataLine.
	 *
	 * @return Image-File of the DataLine.
	 */
	public File getImageFile() {
		return imgFile;
	}

	/**
	 * Returns the DataLine as a formatted String.
	 *
	 * (e.g. "[TPE] Text" where TPE stands for the 3-char abbreviation of the Type name)
	 *
	 * @return formatted String
	 */
	public String toString() {
		return "[" + this.type.toString() + "] " + this.text;
	}
}