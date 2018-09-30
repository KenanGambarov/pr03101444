package stat.pr03101444.ModelView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import stat.pr03101444.Pojo.HeaderFooter;
import stat.pr03101444.Pojo.TableRow;

/**
 *
 * @author Q.Kenan
 */
@Named(value = "Arayis")
@ConversationScoped
public class Arayis implements Serializable {

    @Resource(name = "jdbc/dummy")
    private DataSource ds;
    Connection conn = null;
//    ResultSet rs = null;
    private ResourceBundle properties = ResourceBundle.getBundle("stat.pr03101444.Pojo.messages");

    @Inject
    private Yekun_Bean yb;
    @Inject
    private Main_Bean bean;
    @Inject
    private TableRow row;

    @PostConstruct
    public void init() {
        try {
            if (bean.getConversation().isTransient()) { bean.redirectToPage("dummy.xhtml"); }
        } catch ( IOException ex ) { }
    }

    public List<Arayis> listfeal() {
        String d = "f";
        System.out.println("d = " + d);
        list = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        String sert = "";
        if(yb.getSelectiqtisadireg() != null){
        switch (yb.getSelectiqtisadireg()) {
            case "000":
                sert = " ";
                break;
            default:
                sert = "WHERE POLE = '" + yb.getSelectiqtisadireg() + "' OR kodarazi IN (" + yb.getSelectiqtisadireg() + ")";
        }
        }else{
            sert = "";
        }
        
        String SQL = "SELECT IFNULL(nt.ID,'000') id, IFNULL(nt.kodarazi,'000') zona,"
                + "       IF(nt.ID IS NULL AND nt.kodarazi IS NULL, '000', IF(nt.ID IS NOT NULL AND nt.kodarazi IS NULL, nt.POLE, nt.ZONA)) AS req,"
                + "       IF(nt.ID IS NULL,'Azərbaycan Respublikası', IF(nt.kodarazi IS NULL, CONCAT('  ',nt.REQION), CONCAT('   ',nt.b))) adi,"
                + "       feal, s1 cemi, s2 mues, s3 rsi, ROUND(s1/feal*100,1) f1, ROUND(s2/feal*100,1) f2, ROUND(s3/feal*100,1) f3, feal-s1 etmeyen, adam"
                + " FROM "
                + "   (SELECT  ts.*, SUM(ts.sayi) feal, SUM(IFNULL(fz.s1,0)) s1, SUM(IFNULL(fz.s2,0)) s2, SUM(IFNULL(fz.s3,0)) s3, SUM(IFNULL(fz.adam,0)) adam FROM"
                + "     (SELECT  LEFT(k.kodarazi,3) kodarazi, k.tab, k.mn, COUNT(DISTINCT k.kod) AS sayi, r.b, r.ZONA, ir.ID, ir.REQION, ir.pole"
                + "      FROM tesnifat.kataloq_2011 AS k, tesnifat.region AS r, tesnifat.iregion AS ir WHERE ir.ID=r.ID_IQTISADI_RAYON AND r.ZONA=LEFT(k.kodarazi,3) AND k.bazpr != '0' "
                + "      GROUP BY ir.ID,LEFT(k.kodarazi,3)) ts"
                + "   LEFT JOIN "
                + "    (SELECT  kodarazi, fn, mn, tab, SUM(s1) s1, SUM(s2) s2, SUM(s3) s3, SUM(adam) adam FROM"
                + "       (SELECT  DISTINCT(mkod) mkod, LEFT(arazi,3) kodarazi, feal fn, mulk mn, tab, status, 1 s1, IF(status=0 OR status=2, 1,0) s2,"
                + "             IF(status=1, 1,0) s3, adamsaat adam  FROM db03101444.afhes  GROUP BY mkod) hs GROUP BY hs.kodarazi) fz"
                + "     ON fz.kodarazi=ts.kodarazi GROUP BY ts.ID,ts.kodarazi WITH ROLLUP) nt " + sert + "  "
                + " ORDER BY id,kodarazi ";
        System.out.println("sql2 = " + SQL);
        try {

            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();
            while (rs.next()) {

                Arayis a = new Arayis();
                switch (rs.getString("adi")) {
                    case "Az?rbaycan Respublikas?":
                        a.adi = properties.getString("iqt");
                        break;
                    default:
                        a.adi = rs.getString("adi");
                }
                a.sut1 = rs.getString("feal");
                a.sut2 = rs.getString("cemi");
                a.sut3 = rs.getString("mues");
                a.sut4 = rs.getString("rsi");
                a.sut5 = rs.getString("f1");
                a.sut6 = rs.getString("f2");
                a.sut7 = rs.getString("f3");
                a.sut8 = rs.getString("etmeyen");
                a.sut9 = rs.getString("adam");
                a.sut10 = rs.getString("req");
                a.zona = rs.getString("zona");

                this.list.add(a);
            }

        } catch (SQLException e1) {
            System.out.println("sehv_Select1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_Select2 = " + e.getMessage());
            }
        }
        System.out.println("a = " + d);
        return this.list;
    }

    public List<Arayis> listfeal2() {
        list2 = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        String sert = "";
        String sert2 = "";
        String sert3 = "";

        switch (yb.getSelectmulkiyyet()) {
            case "0":
                sert = " ";
                break;
            case "1,2,3,4,5,6":
                sert = "AND id IN (2,3,4,5,6)";
                break;
            case "3,4,5,6":
                sert = "AND id IN (4,5,6)";
                break;
            default:
                sert = "AND id = '" + yb.getSelectmulkiyyet() + "' ";
        }
        if(yb.getSelectiqtisadireg() != null){
        switch (yb.getSelectiqtisadireg()) {
            case "000":
                sert2 = " ";
                sert3 = " ";
                break;
            default:
                sert2 = "AND LEFT(arazi, 3) IN (" + yb.getSelectiqtisadireg() + ")";
                sert3 = "AND LEFT(kodarazi, 3) IN (" + yb.getSelectiqtisadireg() + ")";
        }
        }else{
            sert = "";
        }

        String SQL = " SELECT "
                + "  IFNULL(w.id, '000') id,  IF(LENGTH(w.id) = 1, w.id, '000') req,"
                + "  IF(w.id = 000, 'Bütün mülkiyyət növləri', IF(w.id = '1' OR w.id = '1,2,3,4,5,6', CONCAT('', w.mad), IF(w.id = '3' OR w.id = '3,4,5,6', CONCAT('', w.mad), CONCAT('', w.mad)))) adi, "
                + "  feal, d1 cemi, d2 mues, d3 rsi, ROUND(d1 / feal * 100, 1) f1, ROUND(d2 / feal * 100, 1) f2, ROUND(d3 / feal * 100, 1) f3, w.feal - w.d1 etmeyen,  adam "
                + "  FROM (SELECT  pp.*, SUM(pp.count1) feal, SUM(IFNULL(ff.d1, 0)) d1, SUM(IFNULL(ff.d2, 0)) d2, SUM(IFNULL(ff.d3, 0)) d3, SUM(IFNULL(ff.adam, 0)) adam "
                + "  FROM (SELECT  IFNULL(id, '000') id, count1, mad "
                + "    FROM (SELECT  m.id AS id, COUNT(DISTINCT (k.kod)) AS count1, m.Ad AS mad "
                + "      FROM tesnifat.kataloq_2011 AS k, tesnifat.mulkiyyet m  WHERE k.mn IN (1, 2, 3, 4, 5, 6) AND m.id = k.mn AND k.bazpr != '0' " + sert3 + " GROUP BY m.id WITH ROLLUP "
                + "      UNION ALL "
                + "      SELECT  '1,2,3,4,5,6' AS id, COUNT(DISTINCT (k.kod)) AS count1, 'Qeyri dövlət mülkiyyəti' AS mad "
                + "      FROM tesnifat.kataloq_2011 AS k, tesnifat.mulkiyyet m  WHERE k.mn IN (2, 3, 4, 5, 6) AND m.id = k.mn AND k.bazpr != '0' " + sert3 + " "
                + "      UNION ALL "
                + "      SELECT  '3,4,5,6' AS id, COUNT(DISTINCT (k.kod)) AS count1, 'Xarici investisiyalı mülkiyyətlər' AS mad "
                + "      FROM tesnifat.kataloq_2011 AS k, tesnifat.mulkiyyet m  WHERE k.mn IN (4, 5, 6) AND m.id = k.mn AND k.bazpr != '0' " + sert3 + " GROUP BY id) pp) pp "
                + "    LEFT JOIN "
                + "      (SELECT  mkod, arazi kodarazi, feal fn, mn, tab, ik, Seksiya, htforma, SUM(d1) d1, SUM(d2) d2, SUM(d3) d3, SUM(adam) adam "
                + "       FROM "
                + "        (SELECT mkod, arazi, feal, IFNULL(mulk, '000') mn, tab, ik, Seksiya, htforma, status, SUM(d1) d1, SUM(d2) d2, SUM(d3) d3, SUM(adam) adam "
                + "         FROM "
                + "           (SELECT   mkod, LEFT(arazi, 3) arazi, feal, mulk, tab, ik, Seksiya, htforma, i.status, 1 d1, IF(status = 0 OR status = 2, 1, 0) d2, IF(status = 1, 1, 0) d3, adamsaat adam "
                + "            FROM db03101444.afhes i  WHERE mulk IN (1, 2, 3, 4, 5, 6) " + sert2 + " GROUP BY mkod) gg  GROUP BY mulk WITH ROLLUP "
                + "        UNION ALL "
                + "        SELECT  mkod, LEFT(arazi, 3) arazi, feal, '1,2,3,4,5,6' mulk, tab, ik, Seksiya, htforma, i.status, 1 d1, "
                + "                IF(status = 0 OR status = 2, 1, 0) d2, IF(status = 1, 1, 0) d3, adamsaat adam "
                + "        FROM db03101444.afhes i  WHERE mulk IN (2, 3, 4, 5, 6) " + sert2 + " GROUP BY mkod "
                + "        UNION ALL "
                + "        SELECT  mkod, LEFT(arazi, 3) arazi, feal, '3,4,5,6' mulk, tab, ik, Seksiya, htforma, i.status, 1 d1, "
                + "                IF(status = 0 OR status = 2, 1, 0) d2, IF(status = 1, 1, 0) d3, adamsaat adam "
                + "        FROM db03101444.afhes i  WHERE mulk IN (4, 5, 6) " + sert2 + "  GROUP BY mkod) pp "
                + "      GROUP BY pp.mn) ff "
                + "      ON ff.mn = pp.id "
                + "  GROUP BY pp.id) w WHERE feal <> 0 " + sert + " ORDER BY id ";

        try {

            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {

                Arayis b = new Arayis();
                switch (rs.getString("adi")) {
                    case "Bütün mülkiyy?t növl?ri":
                        b.adi = properties.getString("mulk1");
                        break;
                    case "Qeyri dövl?t mülkiyy?ti":
                        b.adi = properties.getString("mulk2");
                        break;
                    case "Xarici investisiyal? mülkiyy?tl?r":
                        b.adi = properties.getString("mulk3");
                        break;
                    default:
                        b.adi = rs.getString("adi");
                }
                b.sut1 = rs.getString("feal");
                b.sut2 = rs.getString("cemi");
                b.sut3 = rs.getString("mues");
                b.sut4 = rs.getString("rsi");
                b.sut5 = rs.getString("f1");
                b.sut6 = rs.getString("f2");
                b.sut7 = rs.getString("f3");
                b.sut8 = rs.getString("etmeyen");
                b.sut9 = rs.getString("adam");
                b.sut10 = rs.getString("id");

                this.list2.add(b);
            }

        } catch (Exception e1) {
            System.out.println("sehv_Select1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_Select2 = " + e.getMessage());
            }
        }
            return this.list2;
    }

    public List<Arayis> teqdimedenler() {
        list3 = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
        action = request.getParameter("action");
        id = request.getParameter("id");
        String sert = "";

        if(action != null && id != null){
        switch (action) {
            case "3":
                switch (id) {
                    case "000":
                        sert = " ) w ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w ";
                }
                break;
            case "4":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE status = 0 ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w WHERE status = 0 ";
                }
                break;
            case "5":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE status = 1 ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w WHERE status = 1 ";
                }
                break;
            case "6":
                switch (id) {
                    case "000":
                        sert = " ) w ";
                        break;
                    default:
                        sert = " AND mn IN (" + id + ") ) w ";
                }
                break;
            case "7":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE status = 0 ";
                        break;
                    default:
                        sert = " AND mn IN (" + id + ") ) w WHERE status = 0 ";
                }
                break;
            case "8":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE status = 1 ";
                        break;
                    default:
                        sert = " AND mn IN (" + id + ") ) w WHERE status = 1 ";
                }
                break;
            case "9":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE mulk = 1 ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w WHERE mulk = 1 ";
                }
                break;
            case "10":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE mulk != 1 ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w  WHERE mulk != 1";
                }
                break;
            case "11":
                switch (id) {
                    case "000":
                        sert = " ) w ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w ";
                }
                break;
            case "12":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE ik1 = 2";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w WHERE ik1 = 2 ";
                }
                break;
            case "13":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE ik1 = 3";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w WHERE ik1 = 3 ";
                }
                break;    
            case "14":
                switch (id) {
                    case "000":
                        sert = " ) w WHERE ik1 = 1";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w WHERE ik1 = 1 ";
                }
                break;    
            case "15":
                switch (id) {
                    case "000":
                        sert = " ) w ";
                        break;
                    default:
                        sert = " AND req IN (" + id + ") ) w ";
                }
  
        } 

        }else{
            sert = " ) w ";
        }
        
        String SQL = "SELECT *  FROM ("
                + " SELECT mkod, IF(status != 1, 0,status) status, a.ik1, a.arazi, a.seksiya, a.feal, a.tab, a.mulk, a.htforma, a.ik, date, time, k.ad, k.kod, k.req, k.mn FROM db03101444.afhes a, tesnifat.kataloq_2011 k "
                + " WHERE  a.mkod = k.kod " + sert + "  GROUP BY mkod";
        
        try {

            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                Arayis c = new Arayis();
                c.adi = rs.getString("ad");
                c.sut1 = rs.getString("mkod");
                c.sut2 = rs.getString("arazi");
                c.sut3 = rs.getString("seksiya");
                c.sut4 = rs.getString("feal");
                c.sut5 = rs.getString("tab");
                c.sut6 = rs.getString("mulk");
                c.sut7 = rs.getString("htforma");
                c.sut8 = rs.getString("ik");
                c.sut9 = rs.getString("time");
                c.sut10 = rs.getString("date");

                list3.add(c);
            }

        } catch (SQLException e1) {
            System.out.println("sehv_Teq1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_Teq2 = " + e.getMessage());
            }
        }
        return list3;
    }

    public List<Arayis> muessise() {
        muessise = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        String SQL = "";
        String sert = "";
        if(yb.getSelectiqtisadireg() != null){
        switch (yb.getSelectiqtisadireg()) {
            case "000":
                sert = " ";
                break;
            default:
                sert = "WHERE POLE = '" + yb.getSelectiqtisadireg() + "' OR kodarazi IN (" + yb.getSelectiqtisadireg() + ")";
        }
        }else{
            sert = "";
        }
        
        SQL = "SELECT IFNULL(nt.ID,'000') id, IFNULL(nt.kodarazi,'000') zona,"
                + "       IF(nt.ID IS NULL AND nt.kodarazi IS NULL, '000', IF(nt.ID IS NOT NULL AND nt.kodarazi IS NULL, nt.POLE, nt.ZONA)) AS req,"
                + "       IF(nt.ID IS NULL,'Azərbaycan Respublikası', IF(nt.kodarazi IS NULL, CONCAT('  ',nt.REQION), CONCAT('   ',nt.b))) adi,"
                + "       s1 dovlet, s2 qeyri_dovlet, s1+s2 cemi"
                + " FROM ("
                + "    SELECT  ts.*,  SUM(IFNULL(fz.s1, 0)) s1,  SUM(IFNULL(fz.s2, 0)) s2"
                + "     FROM ("
                + "       SELECT LEFT(k.kodarazi, 3) kodarazi, k.TAB, k.MN, COUNT(DISTINCT k.kod) AS sayi, r.b, r.zona, ir.id, ir.REQION, ir.POLE"
                + "         FROM "
                + "         tesnifat.kataloq_2011 AS k, tesnifat.region AS r, tesnifat.iregion AS ir WHERE ir.id = r.ID_IQTISADI_RAYON  AND "
                + "         r.zona = LEFT(k.kodarazi, 3) AND k.BAZPR != '0' GROUP BY ir.id, LEFT(k.kodarazi, 3)) ts"
                + "       LEFT JOIN (SELECT kodarazi, fn, MN, TAB, SUM(s1) s1, SUM(s2) s2 "
                + " FROM ( "
                + "         SELECT DISTINCT (mkod) mkod, LEFT(arazi, 3) kodarazi, feal fn, mulk MN, TAB, status,"
                + "         IF(mulk = 1, 1, 0) s1, IF(mulk != 1, 1, 0) s2"
                + "      FROM "
                + "        db03101444.afhes GROUP BY mkod) hs GROUP BY hs.kodarazi) fz ON fz.kodarazi = ts.kodarazi GROUP BY ts.id, ts.kodarazi WITH ROLLUP) nt " + sert
                + "   ORDER BY id,kodarazi ";

        try {

            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();
            System.out.println("sql = " + SQL);
            while (rs.next()) {
                Arayis c = new Arayis();
                switch (rs.getString("adi")) {
                    case "Az?rbaycan Respublikas?":
                        c.adi = properties.getString("iqt");
                        break;
                    default:
                        c.adi = rs.getString("adi");
                }
                c.sut1 = rs.getString("dovlet");
                c.sut2 = rs.getString("qeyri_dovlet");
                c.sut3 = rs.getString("cemi");
                c.sut4 = rs.getString("req");
                c.zona = rs.getString("zona");

                muessise.add(c);
            }

        } catch (SQLException e1) {
            System.out.println("sehv_mues1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_mues2 = " + e.getMessage());
            }
        }
        return muessise;
    }

    public List<Arayis> sahibkar() {
        sahibkar = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        String SQL = "";
        String sert = "";
        
        if(yb.getSelectiqtisadireg() != null){
        switch (yb.getSelectiqtisadireg()) {
            case "":
                sert = " ";
                break;
            case "000":
                sert = " ";
                break;
            default:
                sert = "WHERE POLE = '" + yb.getSelectiqtisadireg() + "' OR kodarazi IN (" + yb.getSelectiqtisadireg() + ")";
        }
        } else {
            sert = "";
        }
        
        SQL = "SELECT IFNULL(nt.ID,'000') id, IFNULL(nt.kodarazi,'000') zona,"
                + "       IF(nt.ID IS NULL AND nt.kodarazi IS NULL, '000', IF(nt.ID IS NOT NULL AND nt.kodarazi IS NULL, nt.POLE, nt.ZONA)) AS req,"
                + "       IF(nt.ID IS NULL,'Azərbaycan Respublikası', IF(nt.kodarazi IS NULL, CONCAT('  ',nt.REQION), CONCAT('   ',nt.b))) adi,"
                + "       s1 kicik, s3 orta, s2 iri,  s1+s2+s3 cemi"
                + " FROM ( "
                + "    SELECT  ts.*, SUM(IFNULL(fz.s1,0)) s1, SUM(IFNULL(fz.s2,0)) s2, SUM(IFNULL(fz.s3,0)) s3"
                + "     FROM ("
                + "       SELECT  LEFT(k.kodarazi,3) kodarazi, k.tab, k.mn, k.ik1, COUNT(DISTINCT k.kod) AS sayi, r.b, r.ZONA, ir.ID, ir.REQION, ir.pole"
                + "     FROM "
                + "       tesnifat.kataloq_2011 AS k, tesnifat.region AS r, tesnifat.iregion AS ir WHERE ir.ID=r.ID_IQTISADI_RAYON"
                + "       AND r.ZONA=LEFT(k.kodarazi,3) AND k.bazpr != '0' "
                + "       GROUP BY ir.ID,LEFT(k.kodarazi,3)) ts"
                + "  LEFT JOIN ("
                + "     SELECT  kodarazi, fn, mn, tab, SUM(s1) s1, SUM(s2) s2, SUM(s3) s3 "
                + "     FROM ("
                + "       SELECT DISTINCT(mkod) mkod, LEFT(arazi,3) kodarazi, feal fn, mulk mn, tab, ik1, status, IF(ik1 = 1,1,0) s1,"
                + "         IF(ik1 = 2, 1,0) s2, IF(ik1=3, 1,0) s3  "
                + "       FROM "
                + "        db03101444.afhes  GROUP BY mkod) hs GROUP BY hs.kodarazi) fz ON fz.kodarazi=ts.kodarazi GROUP BY ts.ID,ts.kodarazi WITH ROLLUP) nt " + sert
                + "   ORDER BY id,kodarazi ";

        try {

            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                Arayis c = new Arayis();
                switch (rs.getString("adi")) {
                    case "Az?rbaycan Respublikas?":
                        c.adi = properties.getString("iqt");
                        break;
                    default:
                        c.adi = rs.getString("adi");
                }
                c.sut1 = rs.getString("kicik");
                c.sut2 = rs.getString("orta");
                c.sut3 = rs.getString("iri");
                c.sut4 = rs.getString("cemi");
                c.sut5 = rs.getString("req");
                c.zona = rs.getString("zona");

                sahibkar.add(c);
            }

        } catch (SQLException e1) {
            System.out.println("sehv_mues1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_mues2 = " + e.getMessage());
            }
        }

        return sahibkar;
    }

    public String web() {
        if (yb.getValue().equals("1") && nov == 1) {
            return "Arayisfeal1.xhtml";
        } else if (yb.getValue().equals("1") && nov == 2) {
            return "Arayisfeal2.xhtml";
        } else if (yb.getValue().equals("2")) {
            return "Arayis_mues.xhtml";
        } else if (yb.getValue().equals("3")) {
            return "Arayis_sahibkar.xhtml";
        } else if (yb.getValue().equals("sut1") || yb.getValue().equals("sut2") || yb.getValue().equals("sut3") || yb.getValue().equals("sut4") || yb.getValue().equals("sut5") || yb.getValue().equals("sut6") || yb.getValue().equals("sut7")) {
            return "Cedvel.xhtml";
        } else if (yb.getValue().equals("4")) {
            return "Baxis.xhtml";  
        } else if (!yb.getValue().equals("0")) {
            return "Yekun.xhtml";    
        } else {
            return "";
        }
    }

//
//    public String getParam(FacesContext fc) {
//        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
//        return params.get("action");
//    }
//
//    public void attrListener(ActionEvent event) {
//        action = (String) event.getComponent().getAttributes().get("action");
//    }
    public void arayis_pdf() throws ServletException, IOException, SQLException {
        list1 = new ArrayList<>();
        Connection con = null;
        ResultSet rs = null;
        String SQL = "";
        String sert = "";
        String sert2 = "";

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
        action = request.getParameter("action");
        id = request.getParameter("id");
        ad = request.getParameter("ad");
        
        switch (nov) {
            case 1:
                switch (id) {
                    case "000":
                        sert = "";
                        break;
                    default:
                        sert = "req IN (" + id + ") AND";
                }
                break;
            case 2:
                switch (id) {
                    case "000":
                        sert = "";
                        break;
                    case "1,2,3,4,5,6":
                        sert = "mn IN (2,3,4,5,6) AND";
                        break;
                    case "3,4,5,6":
                        sert = "mn IN (4,5,6) AND";
                        break;
                    default:
                        sert = "mn IN (" + id + ") AND";                
                 }
                
        }
        
        if(bean.getStatus() == 1){
            if(yb.getSelectiqtisadireg() != null){
                switch (yb.getSelectiqtisadireg()) {
                    case "000":
                    sert2 = "";    
                    break;
                    default:
                    sert2 = " LEFT(kodarazi, 3) IN (" + yb.getSelectiqtisadireg() + ") AND";
                }
            }
        }

        switch (action) {
            case "1":
                SQL = "SELECT ad, kod, kodarazi, seksiya, fn, tab, mn, htforma, ik FROM tesnifat.kataloq_2011 WHERE " + sert + sert2 + " bazpr != '0' ";
                break;
            case "2":
                SQL = " SELECT k.kod, kodarazi, k.seksiya, fn, k.tab, mn, k.htforma, k.ik, k.ad FROM tesnifat.kataloq_2011 k "
                        + " WHERE " + sert + sert2 + " k.kod NOT IN (SELECT mkod FROM db03101444.afhes a) AND bazpr != '0' "; 
        }
        System.out.println("SQLp = " + SQL);
//        FacesContext fc = FacesContext.getCurrentInstance();
//        action = request.getParameter("action");
//                action = request.getParameter("action");
        try {

            con = ds.getConnection();
            rs = con.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                Arayis a = new Arayis();
                a.adi = rs.getString("ad");
                a.sut1 = rs.getString("kod");
                a.sut2 = rs.getString("kodarazi");
                a.sut3 = rs.getString("seksiya");
                a.sut4 = rs.getString("fn");
                a.sut5 = rs.getString("tab");
                a.sut6 = rs.getString("mn");
                a.sut7 = rs.getString("htforma");
                a.sut8 = rs.getString("ik");

                list1.add(a);
            }

        } catch (Exception e1) {
            System.out.println("sehv_pdf_arayis1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_pdf_arayis2 = " + e.getMessage());
            }
        }

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

            switch (action) {
                case "1":
                    id1 = ad + " üzrə fəaliyyətdə olan müəssisələr";
                    break;
                case "2":
                    id1 = ad + " üzrə hesabat təqdim etmeyen müəssisələr";
            }

            par = new Paragraph(new Paragraph(id1 + "\n\n", font2));
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);
////----------------------------------------------------
            float[] colsWidth1 = {2.3f, 3f, 11f, 3.5f, 2.5f, 2.5f, 3f, 2.5f, 2.7f, 2f};
            PdfPTable table = new PdfPTable(colsWidth1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.setHeaderRows(2);

            cell = new PdfPCell(new Paragraph("Say", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Kod", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Ad", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Ərazi kodu", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Seksiya", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Fəaliy-yət kodu", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Tabelik", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Mül-kiyyət", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Hüquqi forma", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("İri, kiçik", font1));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            say = 0;
            Iterator<Arayis> it = this.list1.iterator();
            while (it.hasNext()) {
                Arayis a = it.next();
                cell = new PdfPCell(new Paragraph("" + getSay(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut1, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(a.adi, font1));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut2, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut3, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut4, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut5, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut6, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut7, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + a.sut8, font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            }

            doc.add(table);

            doc.close();
            pwriter.close();
        } catch (DocumentException ex) {
            System.out.println(ex);
        }
    }

    public void teqdimeden_pdf()
            throws ServletException, IOException {
        list4 = new ArrayList<>();
        conn = null;
        ResultSet rs = null;

        String SQL = " SELECT sira, adi, a.mehs_kodu, ad, "
                + "   IFNULL(h.sut1,0) sut1, IFNULL(h.sut2,0) sut2, IFNULL(h.sut3,0) sut3, IFNULL(h.sut4,0) sut4, IFNULL(h.sut5,0) sut5, "
                + "   IFNULL(h.sut6,0) sut6, IFNULL(h.sut7,0) sut7, m.mehs_kodu, sira_h "
                + "    FROM ("
                + " (SELECT  sira, adi, mehs_kodu  FROM db03101444.af) a "
                + " LEFT JOIN "
                + " (SELECT * FROM db03101444.afhes WHERE mkod = '" + kod1 + "') h ON a.sira=h.sira_h "
                + " LEFT JOIN "
                + "(SELECT adi AS ad, mehs_kodu FROM db03101444.af1) m ON h.mehs_koduh = m.mehs_kodu) "
                + " ORDER BY sira";

        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                row = new TableRow();
                row.setSira(rs.getString("sira"));
                row.setAdi(rs.getString("adi"));
                row.setAd(rs.getString("ad"));
                row.setKod(rs.getString("m.mehs_kodu"));
                row.setMehs_kodu(rs.getString("mehs_kodu"));
                row.setSut1(rs.getDouble("sut1"));
                row.setSut2(rs.getDouble("sut2"));
                row.setSut3(rs.getDouble("sut3"));
                row.setSut4(rs.getDouble("sut4"));
                row.setSut5(rs.getDouble("sut5"));
                row.setSut6(rs.getDouble("sut6"));
                row.setSut7(rs.getInt("sut7"));

                list4.add(row);
            }

        } catch (Exception e1) {
            System.out.println("sehv_Select1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_Select2 = " + e.getMessage());
            }
        }
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

            par = new Paragraph(new Paragraph("Əsas vəsaitlərin hərəkəti və əsaslı təmiri (torpaq və faydalı qazıntılar istisna olunmaqla)\n\n", font2));
            par.setAlignment(Element.ALIGN_CENTER);
            doc.add(par);
////----------------------------------------------------
            float[] colsWidth1 = {1.5f, 11f, 3.5f, 3.5f, 2.5f, 2.5f, 3f, 2.5f, 2.7f, 2.7f};
            PdfPTable table = new PdfPTable(colsWidth1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.setHeaderRows(2);

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

//            for (TableRow lst : list4) {
            Iterator<TableRow> itr = this.list4.iterator();
            while (itr.hasNext()) {
                TableRow lst = itr.next();
                cell = new PdfPCell(new Paragraph(lst.getSira(), font1));
                cell.setHorizontalAlignment(0);
                cell.setBorder(0);
                table.addCell(cell);

                String ad1 = lst.getAdi();
                String ad2 = lst.getAd();
                if (lst.getAdi() == null) {
                    ad1 = "";
                }
                if (lst.getAd() == null) {
                    ad2 = "";
                }
                switch (lst.getSira()) {
                    case "1":
                        cell = new PdfPCell(new Paragraph(ad1 + "\n\n o cümlədən əsas vəsaitlərin növləri üzrə:", font1));
                        cell.setHorizontalAlignment(row);
                        cell.setBorder(0);
                        table.addCell(cell);
                        break;
                    default:
                        cell = new PdfPCell(new Paragraph(ad1 + ad2, font1));
                        cell.setHorizontalAlignment(row);
                        cell.setBorder(0);
                        table.addCell(cell);
                }

                String kod1 = lst.getMehs_kodu();
                String kod2 = lst.getKod();
                if (lst.getMehs_kodu() == null) {
                    kod1 = "";
                }
                if (lst.getKod() == null) {
                    kod2 = "";
                }
                cell = new PdfPCell(new Paragraph(kod1 + kod2, font1));
                cell.setHorizontalAlignment(0);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut1(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut2(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut3(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut4(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut5(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut6(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("" + lst.getSut7(), font1));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(0);
                table.addCell(cell);

            }
            doc.add(table);

            doc.close();
            pwriter.close();
        } catch (DocumentException ex) {
            System.out.println(ex);
        }
    }

    public void redirectToPage1(String pagename) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        request.getSession();
        ExternalContext extContext = fc.getExternalContext();
        String url = extContext.encodeActionURL(fc.getApplication().getViewHandler().getActionURL(fc, "/" + pagename));
        extContext.redirect(url);
    }

    private List<Arayis> list;
    private List<Arayis> list1;
    private List<Arayis> list2;
    private List<Arayis> list3;
    private List<Arayis> muessise;
    private List<Arayis> sahibkar;
    private List<TableRow> list4;
    private String adi;
    private String sut1;
    private String sut2;
    private String sut3;
    private String sut4;
    private String sut5;
    private String sut6;
    private String sut7;
    private String sut8;
    private String sut9;
    private String sut10;
    private String id;
    private String id1;
    private String ad;
    private int nov;
    private int say = 0;
    private String action;
    private String kod1;
    private String zona;

    public List<Arayis> getList() {
        return list;
    }

    public String getAdi() {
        return adi;
    }

    public String getSut1() {
        return sut1;
    }

    public String getSut2() {
        return sut2;
    }

    public String getSut3() {
        return sut3;
    }

    public String getSut4() {
        return sut4;
    }

    public String getSut5() {
        return sut5;
    }

    public String getSut6() {
        return sut6;
    }

    public String getSut7() {
        return sut7;
    }

    public String getSut8() {
        return sut8;
    }

    public String getSut9() {
        return sut9;
    }

    public String getSut10() {
        return sut10;
    }

    public void setSut10(String sut10) {
        this.sut10 = sut10;
    }

    public void setSut1(String sut1) {
        this.sut1 = sut1;
    }

    public void setSut2(String sut2) {
        this.sut2 = sut2;
    }

    public void setSut3(String sut3) {
        this.sut3 = sut3;
    }

    public void setSut4(String sut4) {
        this.sut4 = sut4;
    }

    public void setSut5(String sut5) {
        this.sut5 = sut5;
    }

    public void setSut6(String sut6) {
        this.sut6 = sut6;
    }

    public void setSut7(String sut7) {
        this.sut7 = sut7;
    }

    public void setSut8(String sut8) {
        this.sut8 = sut8;
    }

    public void setSut9(String sut9) {
        this.sut9 = sut9;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSay() {
        say++;
        return say;
    }

    public void setSay(int say) {
        this.say = say;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getNov() {
        return nov;
    }

    public void setNov(int nov) {
        this.nov = nov;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public void setKod1(String kod1) {
        this.kod1 = kod1;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public List<Arayis> getList1() {
        return list1;
    }

    public List<Arayis> getList2() {
        return list2;
    }

    public List<Arayis> getList3() {
        return list3;
    }

    public List<TableRow> getList4() {
        return list4;
    }

}
