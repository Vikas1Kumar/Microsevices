package question.bank.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.w3c.dom.Node;

public class ExtractDataFromWordFile {

	// static File stylesheet = new File("/WEB-INF/resources/OMML2MML.XSL");
	// new File("C:\\Program Files\\Microsoft\\Office\\root\\Office16\\OMML2MML.XSL");
	public File stylesheet = null;
	static TransformerFactory tFactory = TransformerFactory.newInstance();
	static StreamSource stylesource = null;
	ArrayList<String> al = null;
	ArrayList<ArrayList<String>> tableRow = null;
	ArrayList<ArrayList<ArrayList<String>>> tables = new ArrayList<ArrayList<ArrayList<String>>>();

	public ArrayList<ArrayList<ArrayList<String>>> getDataFromWord(String file, String xmlFilePath) {
		try {
			stylesheet = new File(xmlFilePath);
			stylesource = new StreamSource(stylesheet);
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(fis);

			// using a StringBuffer for appending all the content as HTML
			StringBuffer allHTML = new StringBuffer();

			// loop over all IBodyElements - should be self explained
			for (IBodyElement ibodyelement : document.getBodyElements()) {
				if (ibodyelement.getElementType().equals(BodyElementType.PARAGRAPH)) {
					XWPFParagraph paragraph = (XWPFParagraph) ibodyelement;
					allHTML.append("<p>");
					allHTML.append(getTextAndFormulas(paragraph));
					allHTML.append("</p>");
				} else if (ibodyelement.getElementType().equals(BodyElementType.TABLE)) {
					XWPFTable table = (XWPFTable) ibodyelement;
					allHTML.append("<table border=1>");
					tableRow = new ArrayList<ArrayList<String>>();
					for (XWPFTableRow row : table.getRows()) {
						allHTML.append("<tr>");
						al = new ArrayList<String>();
						for (XWPFTableCell cell : row.getTableCells()) {
							allHTML.append("<td>");
							int i = 0;
							String data = "";
							for (XWPFParagraph paragraph : cell.getParagraphs()) {
								allHTML.append("<p>");
								System.out.println("Cell paragraph " + i);
								i++;
								allHTML.append(getTextAndFormulas(paragraph));
								data += getTextAndFormulas(paragraph);
								allHTML.append("</p>");
							}
							al.add(data);
							allHTML.append("</td>");
						}
						tableRow.add(al);
						allHTML.append("</tr>");
					}
					allHTML.append("</table>");
					tables.add(tableRow);
				}
			}

			document.close();
			// System.out.println(allHTML.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return tables;
	}

	// method for getting MathML from oMath
	static String getMathML(CTOMath ctomath) throws Exception {
		Transformer transformer = tFactory.newTransformer(stylesource);

		Node node = ctomath.getDomNode();

		DOMSource source = new DOMSource(node);
		StringWriter stringwriter = new StringWriter();
		StreamResult result = new StreamResult(stringwriter);
		transformer.setOutputProperty("omit-xml-declaration", "yes");
		transformer.transform(source, result);

		String mathML = stringwriter.toString();
		stringwriter.close();

		// The native OMML2MML.XSL transforms OMML into MathML as XML having special
		// name spaces.
		// We don't need this since we want using the MathML in HTML, not in XML.
		// So ideally we should changing the OMML2MML.XSL to not do so.
		// But to take this example as simple as possible, we are using replace to get
		// rid of the XML specialities.
		mathML = mathML.replaceAll("xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"", "");
		mathML = mathML.replaceAll("xmlns:mml", "xmlns");
		mathML = mathML.replaceAll("mml:", "");

		return mathML;
	}

	// method for getting HTML including MathML from XWPFParagraph
	static String getTextAndFormulas(XWPFParagraph paragraph) throws Exception {
		int isImage = 0;
		StringBuffer textWithFormulas = new StringBuffer();
		System.out.println("paragraph.getRuns() " + paragraph.getRuns().size());
		for (XWPFRun run : paragraph.getRuns()) {
			if (run.getEmbeddedPictures().size() > 0) {
				isImage = 1;
				for (XWPFPicture pic : run.getEmbeddedPictures()) {
					byte[] pictureData = pic.getPictureData().getData();
					int pictureType = pic.getPictureData().getPictureType();
					String base64 = Base64.getEncoder().encodeToString(pictureData);
					textWithFormulas.append("<img src='data:image/" + pictureType + ";base64," + base64 + "'/>");
				}
			}
		}

		/*
		 * if(paragraph.getRuns().size() > 0 &&
		 * paragraph.getRuns().get(0).getEmbeddedPictures().size() > 0) { XWPFPicture
		 * picture = paragraph.getRuns().get(0).getEmbeddedPictures().get(0); byte[]
		 * pictureData = picture.getPictureData().getData(); int pictureType =
		 * picture.getPictureData().getPictureType(); String base64 =
		 * Base64.getEncoder().encodeToString(pictureData);
		 * textWithFormulas.append("<img src='data:image/" + pictureType + ";base64," +
		 * base64 + "'/>"); }
		 */

		// using a cursor to go through the paragraph from top to down
		if (isImage == 0) {
			XmlCursor xmlcursor = paragraph.getCTP().newCursor();

			while (xmlcursor.hasNextToken()) {
				XmlCursor.TokenType tokentype = xmlcursor.toNextToken();
				if (tokentype.isStart()) {
					// System.out.println("getLocalPart: " + xmlcursor.getName().getLocalPart());
					if (xmlcursor.getName().getPrefix().equalsIgnoreCase("w")
							&& xmlcursor.getName().getLocalPart().equalsIgnoreCase("r")) {
						// elements w:r are text runs within the paragraph
						// simply append the text data
						textWithFormulas.append(xmlcursor.getTextValue());
					} else if (xmlcursor.getName().getLocalPart().equalsIgnoreCase("oMath")) {
						// we have oMath
						// append the oMath as MathML
						textWithFormulas.append(getMathML((CTOMath) xmlcursor.getObject()));
						// System.out.println("getTextValue: "+xmlcursor.getTextValue());
					}
				} else if (tokentype.isEnd()) {
					// we have to check whether we are at the end of the paragraph
					xmlcursor.push();
					xmlcursor.toParent();
					if (xmlcursor.getName().getLocalPart().equalsIgnoreCase("p")) {
						break;
					}
					xmlcursor.pop();
				}
			}
		}
		return textWithFormulas.toString();
	}
}
