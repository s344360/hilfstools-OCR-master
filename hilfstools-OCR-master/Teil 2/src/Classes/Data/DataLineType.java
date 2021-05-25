package Classes.Data;

/**
 * Represents the type of the DataLine.
 *
 * To add new DataLineTypes they must be added
 * - as an ENUM-value (incl. short & long name)
 * - in the DataLineType.fromString(...) function
 */
public enum DataLineType {
	/**
	 * Possible types.
	 * SHORT-NAME is used for display in Train/Test list.
	 * LONG-NAME is used in detail view when an item in Train/Test set is selected.
	 *
	 * Format:
	 * ENUM-NAME (SHORT-NAME, LONG-NAME)
	 */
	PARAGRAPH("PAR", "Paragraph"),
	HEADING("HDG", "Heading"),
	CAPTION("CAP", "Caption"),
	HEADER("HDR", "Header"),
	FOOTER("FTR", "Footer"),
	PAGENUMBER("PNR", "Pagenumber"),
	DROPCAPITAL("DPC", "Drop capital"),
	CREDIT("CRE", "Credit"),
	FLOATING("FLT", "Floating"),
	SIGNATUREMARK("SMK", "Signature mark"),
	CATCHWORD("CWD", "Catchword"),
	MARGINALIA("MAR", "Marginalia"),
	FOOTNOTE("FTN", "Footnote"),
	FOOTNOTECONT("FTN", "Footnote continued"),
	ENDNOTE("END", "endnote"),
	TOCENTRY("TOC", "TOC entry"),
	OTHER("OTH", "Other");

	private String typeStringShort;
	private String typeString;

	/**
	 * Creates a new DataLineType instance.
	 *
	 * @param typeStringShort short name
	 * @param typeString long / full name
	 */
	DataLineType(String typeStringShort, String typeString) {
		this.typeStringShort = typeStringShort;
		this.typeString = typeString;
	}

	/**
	 * Gets the short name of the DataLineType.
	 *
	 * @return short name
	 */
	public String toString() {
		return typeStringShort;
	}

	/**
	 * Gets the full name of the DataLineType.
	 *
	 * @return full name
	 */
	public String toStringLong() {
		return this.typeString;
	}

	/**
	 * Converts a String to a DataLineType.
	 * Used by import functions to convert file name to DataLine.
	 *
	 * @param typeStr String that should be converted to a DataLineType.
	 *
	 * @return DataLineType represented by the typeStr.
	 */
	public static DataLineType fromString(String typeStr)
	{
		switch (typeStr) {
			case "catch-word":
				return DataLineType.CATCHWORD;
			case "heading":
				return DataLineType.HEADING;
			case "caption":
				return DataLineType.CAPTION;
			case "header":
				return DataLineType.HEADER;
			case "footer":
				return DataLineType.FOOTER;
			case "marginalia":
				return DataLineType.MARGINALIA;
			case "page-number":
				return DataLineType.PAGENUMBER;
			case "drop-capital":
				return DataLineType.DROPCAPITAL;
			case "credit":
				return DataLineType.CREDIT;
			case "floating":
				return DataLineType.FLOATING;
			case "paragraph":
				return DataLineType.PARAGRAPH;
			case "signature-mark":
				return DataLineType.SIGNATUREMARK;
			case "footnote":
				return DataLineType.FOOTNOTE;
			case "footnote-continues":
				return DataLineType.FOOTNOTECONT;
			case "endnote":
				return DataLineType.ENDNOTE;
			case "toc-entry":
				return DataLineType.TOCENTRY;
			default:
				return DataLineType.OTHER;
		}
	}
}