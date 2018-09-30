package stat.pr03101444.Pojo;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper {

    public int pagenumber = 0;

    public int getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }

    public HeaderFooter(int pagenumber) {
        setPagenumber(pagenumber - 1);
    }

    public HeaderFooter() {
    }

    public void onEndPage(PdfWriter pwriter, Document pdocument) {
        PdfContentByte cb = pwriter.getDirectContent();
        Phrase footer = new Phrase("" + (pagenumber + pwriter.getPageNumber()), FONT[3]);
        ColumnText.showTextAligned(cb, com.itextpdf.text.Element.ALIGN_CENTER, footer, (pdocument.left() + pdocument.right()) / 2, pdocument.top() + 15, 0);
    }
    public static final Font[] FONT = new Font[4];

    static {
        FONT[0] = new Font(FontFamily.HELVETICA, 24);
        FONT[1] = new Font(FontFamily.HELVETICA, 18);
        FONT[2] = new Font(FontFamily.HELVETICA, 14);
        FONT[3] = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    }
}
