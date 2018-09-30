package stat.pr03101444.ModelView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import stat.pr03101444.Pojo.HeaderFooter;
import stat.pr03101444.Pojo.Katalog;
import stat.pr03101444.Pojo.TableRow;

@Named(value = "Bean")
@ConversationScoped
public class Hesabat_Bean implements Serializable {

    @Resource(name = "jdbc/dummy")
    private DataSource ds;
    private ResourceBundle properties = ResourceBundle.getBundle("stat.pr03101444.Pojo.messages");
    Connection conn = null;
    ResultSet rs = null;

    @Inject
    private Main_Bean main_Bean;
    @Inject
    private TableRow tr;
    @Inject
    private TableRow tlr;
    @Inject
    private Katalog k;
    @Inject
    private Conversation conversation;

    public void SaveHes() {

        int row = 46;
        int col = 6;
        int count = 0;
        this.message = "";
        double[][] dat = new double[row + 1][col + 1];
        double sum = 0;

        for (int i = 1; i <= col; i++) {
            for (int j = 1; j <= row; j++) {
                dat[j][i] = 0;
            }
        }

        int i2 = 0;
        Iterator<TableRow> itr = this.list.iterator();
        while (itr.hasNext()) {
            i2 = i2 + 1;
            tr = itr.next();
            for (int j = 1; j <= 6; j++) {
                dat[i2][j] = tr.sut(j);
            }
        }

        for (int j = 1; j <= 6; j++) {
            for (int i = 2; i <= 36; i++) {
                sum = sum + dat[i][j];
            }
            if (dat[1][j] - sum != 0) {
                count++;
                this.message = this.message + "\n Sütun " + j + " :  Sətir 1  Sətir (2+3+4+5+....+35+36) " + bold(dat[1][j], sum, " != ");
            }
            sum = 0;
        }

        for (int j = 1; j <= 46; j++) {
            sum = dat[j][2] + dat[j][3];
            if (dat[j][1] != sum) {
                count++;
                this.message = this.message + "\n Sətir " + j + "  Sütun 01 = Sütun 02 + Sütun 03 " + bold(dat[j][1], sum, " != ");
            }
            sum = 0;
        }

        for (int j = 1; j <= 46; j++) {
            if (dat[j][4] < dat[j][5]) {
                count++;
                this.message = this.message + "\n Sətir " + j + "  Sütun 04 >= Sütun 05 " + bold(dat[j][4], dat[j][5], " < ");
            }
        }

        for (int j = 1; j <= 6; j++) {
            for (int i = 37; i <= 46; i++) {
                sum = sum + dat[i][j];
            }

            if (dat[36][j] - sum != 0) {
                count++;
                this.message = this.message + "\n Sütun " + j + " :  Sətir 36  Sətir (37+38+39+....+45+46) " + bold(dat[36][j], sum, " != ");
            }
            sum = 0;
        }

        for (int j = 1; j <= 6; j++) {
            for (int i = 1; i <= 46; i++) {
                sum = sum + dat[i][j];
            }
        }
        
        if (sum == 0) {
            this.message = "Hesabat boş ola bilməz";
        }

        if (!this.message.isEmpty()) {
            return;
        }

//        if(main_Bean.getStatus() == 1){
//            selectfactorycode = code;
//        }

        conn = null;

        try {

            String SQL = " DELETE FROM db03101444.afhes WHERE mkod = '" + selectfactorycode + "'";
            conn = ds.getConnection();
            conn.prepareStatement(SQL).executeUpdate();

            itr = this.list.iterator();
            while (itr.hasNext()) {
                tr = itr.next();
                if (tr.getSut1() + tr.getSut2() + tr.getSut3() + tr.getSut4() + tr.getSut5() + tr.getSut6() + tr.getSut7() != 0) {
                    switch (tr.getSira()) {
                        case "37":
                            String SQL1 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh1() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL1).executeUpdate();
                            break;
                        case "38":
                            String SQL2 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh2() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL2).executeUpdate();
                            break;
                        case "39":
                            String SQL3 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh3() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL3).executeUpdate();
                            break;
                        case "40":
                            String SQL4 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh4() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL4).executeUpdate();
                            break;
                        case "41":
                            String SQL5 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh5() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL5).executeUpdate();
                            break;
                        case "42":
                            String SQL6 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh6() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL6).executeUpdate();
                            break;
                        case "43":
                            String SQL7 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh7() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL7).executeUpdate();
                            break;
                        case "44":
                            String SQL8 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh8() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL8).executeUpdate();
                            break;
                        case "45":
                            String SQL9 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh9() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL9).executeUpdate();
                            break;
                        case "46":
                            String SQL10 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tlr.getMehs_koduh10() + "',"
                                    + "'" + main_Bean.getStatus() + "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL10).executeUpdate();
                            break;
                        default:
                            String SQL11 = "INSERT INTO db03101444.afhes(mkod,sira_h,mehs_koduh,status,sut1,sut2,sut3,sut4,sut5,sut6,sut7,feal,seksiya,mulk,ik,ik1,tab,htforma,arazi,adamsaat,date,time,ip) values ("
                                    + "'" + selectfactorycode + "',"
                                    + "'" + tr.getSira() + "',"
                                    + "'" + tr.getMehs_kodu() + "',"
                                    + "'" + main_Bean.getStatus()+ "',"
                                    + "'" + tr.getSut1() + "',"
                                    + "'" + tr.getSut2() + "',"
                                    + "'" + tr.getSut3() + "',"
                                    + "'" + tr.getSut4() + "',"
                                    + "'" + tr.getSut5() + "',"
                                    + "'" + tr.getSut6() + "',"
                                    + "'" + tr.getSut7() + "',"
                                    + "'" + fn + "',"
                                    + "'" + seksiya + "',"
                                    + "'" + mn + "',"
                                    + "'" + ik + "',"
                                    + "'" + ik1 + "',"
                                    + "'" + tab + "',"
                                    + "'" + htforma + "',"
                                    + "'" + arazi + "',"
                                    + "'" + adamsaat + "'"
                                    + ",now(),now(),'" + tlr.getIp() + "')";

                            conn.prepareStatement(SQL11).executeUpdate();
                    }
                }
            }
        } catch (Exception e1) {
            System.out.println("sehv_Insert1 = " + e1.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_Insert2 = " + e.getMessage());
            }
        }
        this.message = "Məlumat qeydə alındı";
    }

    private int errorcount;
    private boolean result;
    private String errormessage;

    public String format(int number, String message) {
        return "<tr><td width=\"16\"><img src=\"./images/control_error.png\" width=\"16\" height=\"16\" /></td><td width=\"16\"><b>&nbsp;" + number + "</b></td><td class=zaqalovka>" + message + "</td></tr> <br>";
    }

    public String bold(Object d1, Object d2, String priznak) {
        return "(" + d1 + priznak + d2 + ")";
    }

    public String bold1(Object d1, Object d2, String priznak) {
        return "<b>" + d1 + priznak + d2 + "</b>";
    }

    private String message = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Katalog> Rows() throws SQLException {
        kl = new ArrayList<>();
        conn = ds.getConnection();
        rs = null;
        switch (main_Bean.getStatus()) {
            case 0:
                selectfactorycode = main_Bean.getSelectfactorycode();
                break;
            case 1:
                selectfactorycode = code;
                break;
            case 2:
                selectfactorycode = this.mkod;
                break;
        }
        String SQL = "SELECT ad, seksiya, htforma, bucce, ik, ik1, say, fn, mn, kodarazi, tab FROM tesnifat.kataloq_2011 WHERE kod='" + selectfactorycode + "'";
        try {

            rs = conn.prepareStatement(SQL).executeQuery();
            while (rs.next()) {
                k = new Katalog();
                k.setAd(rs.getString("ad"));
                k.setBucce(rs.getString("bucce"));
                k.setSay(rs.getString("say"));
//            k.setMuessekod(rs.getString("kod"));
                kl.add(k);
                seksiya = rs.getString("seksiya");
                ik = rs.getString("ik");
                ik1 = rs.getString("ik1");
                htforma = rs.getString("htforma");
//            mkod = rs.getString("kod");
                fn = rs.getString("fn");
                mn = rs.getString("mn");
                arazi = rs.getString("kodarazi");
                tab = rs.getString("tab");
            }
        } catch (Exception e1) {
            System.out.println("sehv3 =  " + e1.getMessage());
        } finally { 
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv4 = " + e.getMessage());
            }
        }
        return kl;
    }

    public void chap_pdf()
            throws ServletException, IOException, SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.responseComplete();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        OutputStream out = response.getOutputStream();
        int seh = 1;
        int row = 0;

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline;filename=FA.pdf");
            response.setHeader("Cache-Control", "no-cache");
            Document doc = new Document(PageSize.A4, 25, 20, 40, 40);
            BaseFont bfTimes = BaseFont.createFont("font/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font1 = new Font(bfTimes, 11, Font.NORMAL);
            Font font2 = new Font(bfTimes, 12, Font.BOLD);
            Paragraph par;
            PdfPCell cell = null;
            PdfWriter pwriter = PdfWriter.getInstance(doc, out);
            pwriter.setPageEvent(new HeaderFooter(seh));

            doc.open();

////----------------------------------------------------
            float[] colsWidth1 = {1.5f, 11f, 3.5f, 3.5f, 2.5f, 2.5f, 3f, 2.5f, 2.7f, 2.7f};
            PdfPTable table = new PdfPTable(colsWidth1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.setHeaderRows(2);

            par = new Paragraph(new Paragraph("R Ə S M İ   S T A T İ S T İ K A   H E S A B A T I\n\n", font2));
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);

            par = new Paragraph(new Paragraph("\"Əsas vəsaitlərin hərəkəti və əsaslı təmiri (torpaq və faydalı qazıntılar istisna olunmaqla)\n\n", font2));
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);

            par = new Paragraph(new Paragraph("FA forması hesabatı\n\n", font2));
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);
            PdfPTable tablex0 = new PdfPTable(2);

            PdfPCell cell0 = null;

            float[] colsWidth0 = {4f, 6f};
            PdfPTable tablex00 = new PdfPTable(colsWidth0);
            tablex0.setWidthPercentage(100);
            tablex0.setHeaderRows(1);
            tablex0.getDefaultCell().setBorder(0);
            tablex0.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            String mm[] = {"", "Müəssisənin adı:", "Adamsaat:", "IP:"};
            cell0 = new PdfPCell(new Paragraph(mm[1], font1));
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablex00.addCell(cell0);

            Iterator<Katalog> kat = this.kl.iterator();
            while (kat.hasNext()) {
                Katalog ka = kat.next();
                cell0 = new PdfPCell(new Paragraph(ka.getAd() + "", font1));
                cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
                tablex00.addCell(cell0);
            }

            cell0 = new PdfPCell(new Paragraph(mm[2], font1));
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablex00.addCell(cell0);

            cell0 = new PdfPCell(new Paragraph(adamsaat, font1));
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablex00.addCell(cell0);

            cell0 = new PdfPCell(new Paragraph(mm[3], font1));
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablex00.addCell(cell0);

            cell0 = new PdfPCell(new Paragraph(tlr.getIp() + "", font1));
            //cell0 = new PdfPCell(new Paragraph(dateFormat.format(date) + "\n", font1));
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablex00.addCell(cell0);

            doc.add(tablex00);

            par = new Paragraph(new Paragraph("\n\n", font2));
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);

            cell = new PdfPCell(new Paragraph("Sət-rin №-si", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Göstəricinin adı", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Məhsulun kodu", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Hesabat dövrü ərzində maddi əsas vəsaitlərin əldə edilməsinə çəkilmiş xərclər (torpaq və faydalı qazıntılar istisna olunmaqla)", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("o cümlədən:", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Əsas vəsaitlərin əsaslı təmiri üzrə kənar müəssisə və təşkilat ların göstərdikləri xidmətlə-rin dəyəri", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("o cümlə-dən, xarici ölkələ-rin hüquqi şəxslərinin göstər-dikləri xidmət-lər", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Silinmiş əsas vəsaitlər bazar qiyməti ilə", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Silinmiş əsas vəsaitlə-rin yaşı (tam rəqəmlə)", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("ölkədə istehsal edilmiş yeni (istifa-dədə olma- mış) əsas vəsait-lər", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("idxal olun-muş yeni və istifadə edilmiş əsas vəsait-lər", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            Iterator<TableRow> itr = this.list.iterator();
            while (itr.hasNext()) {
                tr = itr.next();
                cell = new PdfPCell(new Paragraph(tr.getSira(), font1));
                cell.setHorizontalAlignment(0);
                cell.setBorder(0);
                table.addCell(cell);

                switch (tr.getSira()) {
                    case "01":
                        cell = new PdfPCell(new Paragraph(tr.getAdi() + "\n\n o cümlədən əsas vəsaitlərin növləri üzrə:", font1));
                        cell.setHorizontalAlignment(row);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "37":
                        cell = new PdfPCell(new Paragraph(tlr.getAd1(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "38":
                        cell = new PdfPCell(new Paragraph(tlr.getAd2(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "39":
                        cell = new PdfPCell(new Paragraph(tlr.getAd3(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "40":
                        cell = new PdfPCell(new Paragraph(tlr.getAd4(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "41":
                        cell = new PdfPCell(new Paragraph(tlr.getAd5(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "42":
                        cell = new PdfPCell(new Paragraph(tlr.getAd6(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "43":
                        cell = new PdfPCell(new Paragraph(tlr.getAd7(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "44":
                        cell = new PdfPCell(new Paragraph(tlr.getAd8(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "45":
                        cell = new PdfPCell(new Paragraph(tlr.getAd9(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "46":
                        cell = new PdfPCell(new Paragraph(tlr.getAd10(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    default:
                        cell = new PdfPCell(new Paragraph(tr.getAdi(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                }

                switch (tr.getSira()) {
                    case "37":
                        cell = new PdfPCell(new Paragraph(tlr.getKod1(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "38":
                        cell = new PdfPCell(new Paragraph(tlr.getKod2(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "39":
                        cell = new PdfPCell(new Paragraph(tlr.getKod3(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "40":
                        cell = new PdfPCell(new Paragraph(tlr.getKod4(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "41":
                        cell = new PdfPCell(new Paragraph(tlr.getKod5(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "42":
                        cell = new PdfPCell(new Paragraph(tlr.getKod6(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "43":
                        cell = new PdfPCell(new Paragraph(tlr.getKod7(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "44":
                        cell = new PdfPCell(new Paragraph(tlr.getKod8(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "45":
                        cell = new PdfPCell(new Paragraph(tlr.getKod9(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    case "46":
                        cell = new PdfPCell(new Paragraph(tlr.getKod10(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    default:
                        cell = new PdfPCell(new Paragraph(tr.getMehs_kodu(), font1));
                        cell.setHorizontalAlignment(0);
                        cell.setBorder(0);
                        table.addCell(cell);
                }

                cell = new PdfPCell(new Paragraph("" + tr.getSut1(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + tr.getSut2(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + tr.getSut3(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + tr.getSut4(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + tr.getSut5(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + tr.getSut6(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + tr.getSut7(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            }
            doc.add(table);
            returnOver(pwriter, bfTimes);

            doc.close();
            pwriter.close();
        } catch (DocumentException ex) {
            System.out.println(ex);
        }
    }

    private PdfContentByte returnOver(PdfWriter pwriter, BaseFont bfTimes) {
        PdfContentByte over = pwriter.getDirectContent();
        over.saveState();
        over.beginText();
        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        over.setLineWidth(1.2f);
        over.setRGBColorStroke(0xFF, 0x00, 0x00);
        over.setRGBColorFill(0xFF, 0xFF, 0xFF);
        over.setFontAndSize(bfTimes, 20);
        over.setTextMatrix(1f, 2f, -2, 1, 120, 70);
        over.showText("H E S A B A T    Q E Y D Ə    A L I N D I !");
        over.endText();
        over.restoreState();
        return over;
    }

//    public void hes_excel() throws DocumentException, IOException, SQLException {
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFFont font1 = createFont(workbook, (short) 11, "Times New Roman", (short) 400);
//        HSSFFont font2 = createFont(workbook, (short) 12, "Times New Roman", (short) 700);
//        HSSFSheet sheet = workbook.createSheet("Əsas vəsaitlərin hərəkəti və əsaslı təmiri (torpaq və faydalı qazıntılar istisna olunmaqla)\n\n");
//        sheet.setColumnWidth(0, 1700);
//        sheet.setColumnWidth(1, 9700);
//        sheet.setColumnWidth(2, 3000);
//        sheet.setColumnWidth(3, 6000);
//        sheet.setColumnWidth(4, 6000);
//        sheet.setColumnWidth(5, 6000);
//        sheet.setColumnWidth(6, 6000);
//        sheet.setColumnWidth(7, 5000);
//        sheet.setColumnWidth(8, 5000);
//        sheet.setColumnWidth(9, 5000);
//
//        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
//        HSSFCell cell = createCell(workbook, row, 0, (short) 1, (short) 2, font2, false, false);
//        cell.setCellValue("Əsas vəsaitlərin hərəkəti və əsaslı təmiri (torpaq və faydalı qazıntılar istisna olunmaqla)");
//
//        row = sheet.createRow(sheet.getLastRowNum() + 2);
//        cell = createCell(workbook, row, 0, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Sətrin №-si");
//        cell = createCell(workbook, row, 1, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Göstəricinin adı");
//        cell = createCell(workbook, row, 2, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Məhsulun kodu");
//        cell = createCell(workbook, row, 3, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Hesabat dövrü ərzində maddi əsas vəsaitlərin əldə edilməsinə çəkilmiş xərclər (torpaq və faydalı qazıntılar istisna olunmaqla)");
//        cell = createCell(workbook, row, 4, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("o cümlədən:");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 4, 5));
//        cell = createCell(workbook, row, 5, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        cell = createCell(workbook, row, 6, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Əsas vəsaitlərin əsaslı təmiri üzrə kənar müəssisə və təşkilatların göstərdikləri xidmətlərin dəyəri");
//        cell = createCell(workbook, row, 7, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("o cümlədən, xarici ölkələrin hüquqi şəxslərinin göstərdikləri xidmətlər");
//        cell = createCell(workbook, row, 8, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Silinmiş əsas vəsaitlərin bazar qiyməti ilə");
//        cell = createCell(workbook, row, 9, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("Silinmiş əsas vəsaitlərin yaşı (tam rəqəmlə)");
//
//        row = sheet.createRow(sheet.getLastRowNum() + 1);
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 0, 0));
//        cell = createCell(workbook, row, 0, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 1, 1));
//        cell = createCell(workbook, row, 1, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 2, 2));
//        cell = createCell(workbook, row, 2, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 3, 3));
//        cell = createCell(workbook, row, 3, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        cell = createCell(workbook, row, 4, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("ölkədə istehsal edilmiş yeni (istifadədə olmamış) əsas vəsaitlər");
//        cell = createCell(workbook, row, 5, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("idxal olunmuş yeni və istifadə edilmiş əsas vəsaitlər");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 6, 6));
//        cell = createCell(workbook, row, 6, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        cell = createCell(workbook, row, 7, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 7, 7));
//        cell = createCell(workbook, row, 8, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 8, 8));
//        cell = createCell(workbook, row, 9, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("");
//        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - 1, sheet.getLastRowNum(), 9, 9));
//
//        row = sheet.createRow(sheet.getLastRowNum() + 1);
//        cell = createCell(workbook, row, 0, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("A");
//        cell = createCell(workbook, row, 1, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("B");
//        cell = createCell(workbook, row, 2, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("C");
//        cell = createCell(workbook, row, 3, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("1");
//        cell = createCell(workbook, row, 4, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("2");
//        cell = createCell(workbook, row, 5, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("3");
//        cell = createCell(workbook, row, 6, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("4");
//        cell = createCell(workbook, row, 7, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("5");
//        cell = createCell(workbook, row, 8, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("6");
//        cell = createCell(workbook, row, 9, (short) 2, (short) 1, font2, true, true);
//        cell.setCellValue("7");
//
//        Iterator<TableRow> itr = this.list.iterator();
//        while (itr.hasNext()) {
//            tr = itr.next();
//
//            row = sheet.createRow(sheet.getLastRowNum() + 1);
//            cell = createCell(workbook, row, 0, (short) 1, (short) 2, font1, false, false);
//            cell.setCellValue(tr.getSira());
//
//            switch (tr.getSira()) {
//                case "37":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd1());
//                    break;
//                case "38":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd2());
//                    break;
//                case "39":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd3());
//                    break;
//                case "40":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd4());
//                    break;
//                case "41":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd5());
//                    break;
//                case "42":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd6());
//                    break;
//                case "43":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd7());
//                    break;
//                case "44":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd8());
//                    break;
//                case "45":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd9());
//                    break;
//                case "46":
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getAd10());
//                    break;
//                default:
//                    cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tr.getAdi());
//            }
//
//            switch (tr.getSira()) {
//                case "37":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod1());
//                    break;
//                case "38":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod2());
//                    break;
//                case "39":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod3());
//                    break;
//                case "40":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod4());
//                    break;
//                case "41":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod5());
//                    break;
//                case "42":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod6());
//                    break;
//                case "43":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod7());
//                    break;
//                case "44":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod8());
//                    break;
//                case "45":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod9());
//                    break;
//                case "46":
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tlr.getKod10());
//                    break;
//                default:
//                    cell = createCell(workbook, row, 2, (short) 1, (short) 1, font1, false, false);
//                    cell.setCellValue(tr.getMehs_kodu());
//            }
//
//            cell = createCell(workbook, row, 3, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut1());
//            cell = createCell(workbook, row, 4, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut2());
//            cell = createCell(workbook, row, 5, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut3());
//            cell = createCell(workbook, row, 6, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut4());
//            cell = createCell(workbook, row, 7, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut5());
//            cell = createCell(workbook, row, 8, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut6());
//            cell = createCell(workbook, row, 9, (short) 3, (short) 1, font1, false, false);
//            cell.setCellValue(tr.getSut7());
//        }
//
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.responseComplete();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-Disposition", "inline;filename=FA.xls");
//        response.setHeader("Cache-Control", "no-cache");
//        OutputStream out = response.getOutputStream();
//
//        try {
//            workbook.write(out);
//        } finally {
//            if (out != null) {
//                out.flush();
//                out.close();
//            }
//        }
//    }

    private HSSFFont createFont(HSSFWorkbook workbook, short fontheight, String fontname, short boldweight) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(fontheight);
        font.setFontName(fontname);
        font.setBoldweight(boldweight);
        return font;
    }

    private HSSFCell createCell(HSSFWorkbook workbook, HSSFRow row, int column, short halign, short valign, HSSFFont font, boolean wrap, boolean borders) {
        HSSFCell cell = row.createCell(column);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setFont(font);
        cellStyle.setWrapText(wrap);
        if (borders) {
            cellStyle.setBorderBottom((short) 1);
            cellStyle.setBorderLeft((short) 1);
            cellStyle.setBorderRight((short) 1);
            cellStyle.setBorderTop((short) 1);
        }
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public void GoHes() throws IOException, SQLException {
        this.mkod = this.selectcode;
        this.login = this.selectcode;

        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        String SQL = "SELECT kod FROM tesnifat.kataloq_2011 WHERE bazpr != 0 AND kod='" + this.mkod + "'";
//        System.out.println("mkod=" + mkod);
        try {

//            result = stmt.executeQuery("SELECT COUNT(*) FROM tesnifat.kataloq_2011 WHERE kod='" + login + "' AND tab='1413'");
//            result.first();
//            if (result.getInt(1) == 0) {
//                this.errmes = "Müəssisə bu hesabatı təqdim etməməlidir";
//                return;
//            }
            
            rs = conn.prepareStatement(SQL).executeQuery();
            if (!rs.first()) {

                errmes = properties.getString("errmes4");
                errmes2 = properties.getString("errmes5");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errmes, errmes2));
                return;
            }
        } catch (Exception e1) {
            System.out.println("sehv3 =  " + e1.getMessage());
        } finally { 
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv4 = " + e.getMessage());
            }
        
        }

        if (conversation.isTransient()) {
            conversation.begin();
        }
//        this.errmes = "";
        main_Bean.redirectToPage("Hesabat.xhtml");
//        this.selectcode = " ";
//        return errmes;
    }

    public void go() throws IOException{
        main_Bean.redirectToPage("Yekun.xhtml");
    }
    
    private List<TableRow> list;
    private List<Katalog> kl;
    private String adamsaat = "0";
    private int status;
    private String ip;
    private String mn;
    private String fn;
    private String arazi;
    private String tab;
    private String mkod;
    private String selectfactorycode;
    private String seksiya;
    private String htforma;
    private String ik;
    private String ik1;
    private String login;
    private String selectcode;
    private String errmes;
    private String errmes2;
    private String code;
    private String areacode;

    public Main_Bean getMain_Bean() {
        return main_Bean;
    }

    public void setMain_Bean(Main_Bean main_Bean) {
        this.main_Bean = main_Bean;
    }

    public Katalog getK() {
        return k;
    }

    public void setK(Katalog k) {
        this.k = k;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Katalog> getKl() {
        return kl;
    }

    public void setKl(List<Katalog> kl) {
        this.kl = kl;
    }

    public String getAdamsaat() {
        return adamsaat;
    }

    public void setAdamsaat(String adamsaat) {
        this.adamsaat = adamsaat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getArazi() {
        return arazi;
    }

    public void setArazi(String arazi) {
        this.arazi = arazi;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getMkod() {
        return mkod;
    }

    public void setMkod(String mkod) {
        this.mkod = mkod;
    }

    public String getSelectfactorycode() {
        return selectfactorycode;
    }

    public void setSelectfactorycode(String selectfactorycode) {
        this.selectfactorycode = selectfactorycode;
    }

    public String getSeksiya() {
        return seksiya;
    }

    public String getHtforma() {
        return htforma;
    }

    public String getIk() {
        return ik;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSelectcode() {
        return selectcode;
    }

    public void setSelectcode(String selectcode) {
        this.selectcode = selectcode;
    }

    public String getErrmes() {
        return errmes;
    }

    public void setErrmes(String errmes) {
        this.errmes = errmes;
    }

    public List<TableRow> getList() {
        return list;
    }

    public void setList(List<TableRow> list) {
        this.list = list;
    }

    public String getErrmes2() {
        return errmes2;
    }

    public void setErrmes2(String errmes2) {
        this.errmes2 = errmes2;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getIk1() {
        return ik1;
    }

    public void setIk1(String ik1) {
        this.ik1 = ik1;
    }

}
