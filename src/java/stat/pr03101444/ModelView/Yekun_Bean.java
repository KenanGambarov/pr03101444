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
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
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
import stat.pr03101444.Pojo.TableRow;


@Named(value = "Yekun_Bean")
@ConversationScoped
public class Yekun_Bean implements Serializable {

    @Resource(name = "jdbc/dummy")
    private DataSource ds;
    private ResourceBundle properties = ResourceBundle.getBundle("stat.pr03101444.Pojo.messages");
    Connection conn = null;

    @Inject
    private Main_Bean main_Bean;
    
    @Inject
    private TableRow lst;

    public ArrayList<Yekun_Bean> getIqtisadireg() throws SQLException {
        iqtisadireg = new ArrayList<>();
        ResultSet result = null;
        conn = null;
        String sert = " ";
        if (main_Bean.getStatus() == 1) {
            sert = "WHERE zona = SUBSTRING('" + main_Bean.getAreacode() + "',1,3) ";
        }
        
        String SQL = "SELECT IF(id IS NULL, '000', id) AS iq,"
                + "   IF(zona IS NULL, '000', zona) AS zona1,"
                + "   IF(id IS NULL AND zona IS NULL, '000', IF(zona IS NULL, pole, zona)) AS zona,"
                + "   IF(id IS NULL AND zona IS NULL, 'Bütün ərazilər',IF(zona IS NULL, reqion, IF(zona in (201,202), concat(b,'(Gəncə)'),b))) AS araziadi,"
                + "   IF(id IS NULL AND zona IS NULL, '', IF(zona IS NULL, '&nbsp;&nbsp;&nbsp;&nbsp;', '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;')) AS probel "
                + "   FROM  "
                + "     (SELECT  id, zona, reqion, b, pole FROM tesnifat.region, tesnifat.iregion WHERE ID_IQTISADI_RAYON = ID GROUP BY id, zona WITH ROLLUP ) a "
                + sert + "ORDER BY iq, zona1";
       
        try {
            conn = ds.getConnection();
            result = conn.prepareStatement(SQL).executeQuery();

            while (result.next()) {

                Yekun_Bean yb = new Yekun_Bean();
                yb.zona1 = result.getString("zona");
                switch (result.getString("araziadi")) {
                    case "Bütün ?razil?r":
                        yb.araziadi = properties.getString("word1");
                        break;
                    case "Nizami rayonu(G?nc?)":
                        yb.araziadi = properties.getString("word2");
                        break;
                    case "Kəpəz rayonu(G?nc?)":
                        yb.araziadi = properties.getString("word3");
                        break;
                    default:
                        yb.araziadi = result.getString("araziadi");
                }
                yb.zona = result.getString("zona");
                iqtisadireg.add(yb);
            }
            } catch (Exception e1) {
            System.out.println("sehv3 =  " + e1.getMessage());
        } finally { 
            try {
                if (result != null) {
                    result.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv4 = " + e.getMessage());
            }
        }
        return iqtisadireg;
    }

    public ArrayList<Yekun_Bean> getFnkod() throws SQLException {
        fnkod = new ArrayList<>();
        ResultSet result = null;
        conn = null;
        String SQL = "SELECT * FROM db03101444.feal_sahe ORDER BY seksiya";
       
        try {
            conn = ds.getConnection();
            result = conn.prepareStatement(SQL).executeQuery();
            
            while (result.next()) {

                Yekun_Bean yb = new Yekun_Bean();
                yb.sahe = result.getString("sahe");
                yb.seksiya = result.getString("seksiya");
                fnkod.add(yb);
            }
         } catch (Exception e1) {
            System.out.println("sehv3 =  " + e1.getMessage());
        } finally { 
            try {
                if (result != null) {
                    result.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv4 = " + e.getMessage());
            }
        }
        return fnkod;
    }

    public ArrayList<Yekun_Bean> getMulkiyyet() throws SQLException {
        mulkiyyet = new ArrayList<>();
        ResultSet result = null;
        conn = null;
        String sert = "";
        String SQL = "SELECT ID, AD FROM tesnifat.mulkiyyet "
                + "      UNION "
                + "      SELECT '1,2,3,4,5,6' AS ID, 'Qeyri dövlət mülkiyyəti' AS AD "
                + "      FROM tesnifat.mulkiyyet GROUP BY ID "
                + "      UNION "
                + "      SELECT '3,4,5,6' AS ID, 'Xarici investisiyalı mülkiyyətlər' AS AD "
                + "      FROM tesnifat.mulkiyyet GROUP BY ID "
                + "   ORDER BY ID";
        try {
            conn = ds.getConnection();
            result = conn.prepareStatement(SQL).executeQuery();
            
            while (result.next()) {
                Yekun_Bean yb = new Yekun_Bean();
                switch (result.getString("AD")) {
                    case "Bütün mülkiyy?t növl?ri":
                        yb.mulk = properties.getString("mulk1");
                        break;
                    case "Qeyri dövl?t mülkiyy?ti":
                        yb.mulk = properties.getString("mulk2");
                        break;
                    case "Xarici investisiyal? mülkiyy?tl?r":
                        yb.mulk = properties.getString("mulk3");
                        break;
                    default:
                        yb.mulk = result.getString("AD");
                }
                yb.id = result.getString("ID");
                mulkiyyet.add(yb);
            }
         } catch (Exception e1) {
            System.out.println("sehv3 =  " + e1.getMessage());
        } finally { 
            try {
                if (result != null) {
                    result.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv4 = " + e.getMessage());
            }
        }
        return mulkiyyet;
    }

    public List<TableRow> listyek() {
        list = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        String sert = "";
        String sert2 = "";
        String sert3 = "";
        String where = "";
        switch (value) {
            case "":
                value = "sut1";
                break;
            case "0":
                value = "sut1";
                break;
            case "1":
                value = "sut1";
                break;
            case "2":
                value = "sut1";
                break;
            case "3":
                value = "sut1";
                break;
            case "4":
                value = "sut1";    
        }
        
        if (fk != null) {
            switch (fk) {
                case "2":
                    sert3 = "LEFT(feal,2) feal";
                    break;
                case "3":
                    sert3 = "LEFT(feal,3) feal";
                    break;
                case "4":
                    sert3 = "LEFT(feal,4) feal";
                    break;
                default:
                    sert3 = "LEFT(feal,5) feal";
                    break;
            }
        } else {
            sert3 = "feal";
        }

        if (!selectfnkod.equals("0") && !selectfnkod.equals("")) {
            sert = " seksiya IN ('" + selectfnkod + "') ";
        } else {
            selectfnkod = "";
        }
        

        if (!mn.equals("") && !mn.equals("0") && !mn.equals("4")) {
            if(!sert.equals("")) {
                sert = sert + " AND ik IN (" + mn + ") ";                
            } else{
                sert = " ik IN (" + mn + ") ";  
            }      
       } else {
            mn = "";
        }   
            
        if (!ik.equals("") && !ik.equals("0") && !ik.equals("4")) {
            if (!sert.equals("")) {
                sert = sert + " AND ik1 IN (" + ik + ") ";                  
            } else{
                sert = " ik1 IN (" + ik + ") ";
            }
        } else {
            ik = "";    
        }
        
        switch (selectiqtisadireg) {
            case "":
                selectiqtisadireg = "";
                break;
            case "000":
                selectiqtisadireg = "";
                break;
            default:
                if (!sert.equals("")) {
                    sert = sert + " AND LEFT (arazi,3) IN (" + selectiqtisadireg + ") ";
                } else {
                    sert = " LEFT (arazi,3) IN (" + selectiqtisadireg + ") ";
                }
        }

        if (!selectmulkiyyet.equals("0") && (!selectmulkiyyet.equals(""))) {
            if (!sert.equals("")) {
                if(!selectmulkiyyet.equals("1")){
                  sert = sert + " AND mulk = '1' ";  
                } else {
                  sert = sert + " AND mulk IN (2,3,4,5,6) ";  
                }   
            } else if (sert.equals("")) {
                if(!selectmulkiyyet.equals("1")){
                  sert = " mulk = '1' ";  
                } else {
                  sert = " mulk IN (2,3,4,5,6) ";  
                }
            }
        } else {
            selectmulkiyyet = "";
        }


        if (!sert.equals("")) {
            where = "WHERE" + sert;
        } else {
            where = "";
        }   

        
//            String SQL = "  SELECT a.sira, a.adi, ad2, a.mehs_kodu, mehs_koduh, feal, "
//                + "IFNULL(h." + value + ", 0.0) " + value + " "
//                + "     FROM ( "
//                + "  (SELECT " + sert3 + ", mehs_koduh, SUM(" + value + ") " + value + ", sira_h, mkod, seksiya, arazi FROM db03101444.afhes " + where + " GROUP BY feal,sira_h) h "
//                + "   LEFT JOIN "
//                + "  (SELECT sira, adi, mehs_kodu FROM db03101444.af) a ON a.sira = h.sira_h "
//                + "  LEFT JOIN "
//                + "   (SELECT adi AS ad2, mehs_kodu FROM db03101444.af1 ) m ON h.mehs_koduh = m.mehs_kodu) "
//                + " WHERE " + value + "<>0 "
//                + " ORDER BY sira,feal ";
        String  SQL = "SELECT h.arazi, a.adi, h.mehs_koduh,  h.feal, IFNULL(h." + value + ", 0.0) " + value + " " 
                    + " FROM " 
                    + "  (SELECT seksiya, arazi, feal, mehs_koduh, SUM(" + value + ") " + value + "  FROM db03101444.afhes " + where + "  GROUP BY arazi, mehs_koduh, feal) h," 
                    + "  (SELECT adi, mehs_kodu FROM db03101444.af UNION SELECT adi, mehs_kodu FROM db03101444.af1) a " 
                    + " WHERE  a.mehs_kodu = h.mehs_koduh AND " + value + " <> 0  ORDER BY arazi,mehs_koduh,feal ";
        System.out.println("sql1 = " + SQL);
        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {

                map = new LinkedHashMap<>();
                map2 = new LinkedHashMap<>();
                if (value.equals("sut7")) {
                    map2.put(rs.getString("feal"), rs.getInt(value));
                } else {
                    map.put(rs.getString("feal"), rs.getDouble(value));
                }
                list.add(new TableRow(rs.getString("adi"), rs.getString("mehs_koduh"), map, map2));

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
        return list;
    }

    public int getfnCount() {
        return feal_list().size();
    }

    public List<String> feal_list() {

        list2 = new ArrayList<>();
        conn = null;
        ResultSet rs1 = null;
        String sert = "";

        if (fk != null) {
            switch (fk) {
                case "2":
                    sert = "LEFT(feal,2)";
                    break;
                case "3":
                    sert = "LEFT(feal,3)";
                    break;
                case "4":
                    sert = "LEFT(feal,4)";
                    break;
                default:
                    sert = "LEFT(feal,5)";
            }
        } else {
            sert = "feal";
        }

        String SQL1 = "SELECT DISTINCT (" + sert + ") feal FROM db03101444.afhes ORDER BY feal";

        try {
            conn = ds.getConnection();
            rs1 = conn.prepareStatement(SQL1).executeQuery();
            System.out.println("sqlyek = " + SQL1);
            while (rs1.next()) {

                list2.add(rs1.getString("feal"));

            }

        } catch (Exception e1) {
            System.out.println("sehv5 =  " + e1.getMessage());
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv6 = " + e.getMessage());
            }
        }
        return list2;
    }

    public void chap_pdf()
            throws ServletException, IOException, SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.responseComplete();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        OutputStream out = response.getOutputStream();
        
        if(listyek().isEmpty()){
            return;
        }
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
            int x;
            int i;
            int z;
            int a;
            int y;
            doc.open();
            for (x = 0; x < list2.size(); x += 7) {
                par = new Paragraph(new Paragraph(getBasliq() + "\n\n", font2));
                par.setAlignment(Element.ALIGN_CENTER);
                doc.add(par);
////----------------------------------------------------
                float[] colsWidth1 = {11, 3, 3, 3, 3, 3, 3, 3, 3};
                PdfPTable table = new PdfPTable(colsWidth1);
                y = 7;
                if (x + 7 > list2.size()) {
                    y = list2.size() - x;
                    table = new PdfPTable(2 + y);
                    switch (y) {
                        case 3:
                            colsWidth1 = new float[]{11, 3, 3, 3, 3};
                            table = new PdfPTable(colsWidth1);
                            break;
                        case 4:
                            colsWidth1 = new float[]{11, 3, 3, 3, 3, 3};
                            table = new PdfPTable(colsWidth1);
                            break;
                        case 5:
                            colsWidth1 = new float[]{11, 3, 3, 3, 3, 3, 3};
                            table = new PdfPTable(colsWidth1);
                            break;
                        case 6:
                            colsWidth1 = new float[]{11, 3, 3, 3, 3, 3, 3, 3};
                            table = new PdfPTable(colsWidth1);
                    }
                }
                table.setWidthPercentage(100);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//            table.setHeaderRows(2);

                cell = new PdfPCell(new Paragraph("Göstəricinin adı", font1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Məsrəflərin kodu", font1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                a = 7;
                cell = new PdfPCell(new Paragraph("Fəaliyyət növləri", font1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (x + 7 > list2.size()) {
                    a = list2.size() - x;
                }
                cell.setColspan(a);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", font1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", font1));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);

                i = x;
                while (i < x + 7 && i < list2.size()) {
                    cell = new PdfPCell(new Paragraph(list2.get(i), font1));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                    i++;
                }

                Iterator<TableRow> itr = this.list.iterator();
                while (itr.hasNext()) {
                    TableRow tbl = itr.next();

                    String ad1 = tbl.getAdi();
                    if (tbl.getAdi() == null) {
                        ad1 = "";
                    }
                    switch (tbl.getKod()) {
                        case "00000":
                            cell = new PdfPCell(new Paragraph(ad1 + "\n\n o cümlədən əsas vəsaitlərin növləri üzrə:", font1));
                            cell.setHorizontalAlignment(row);
                            cell.setBorder(0);
                            table.addCell(cell);
                            break;
                        default:
                            cell = new PdfPCell(new Paragraph(ad1, font1));
                            cell.setHorizontalAlignment(row);
                            cell.setBorder(0);
                            table.addCell(cell);
                    }

                    String kod2 = tbl.getKod();
                    if (tbl.getKod() == null) {
                        kod2 = "";
                    }else if (tbl.getKod().equals("00000")) {
                        kod2 = "X";
                    }
                    cell = new PdfPCell(new Paragraph(kod2, font1));
                    cell.setHorizontalAlignment(0);
                    cell.setBorder(0);
                    table.addCell(cell);

                    z = x;
                    while (z < x + 7 && z < list2.size()) {
                        Double dbl = tbl.getMap(list2.get(z));
                        Integer kod = tbl.getMap2(list2.get(z));
                        if (tbl.getMap(list2.get(z)) == null) {
                            dbl = 0.0;
                        }
                        if (tbl.getMap2(list2.get(z)) == null) {
                            kod = 0;
                        }
                        if (value.equals("sut7")) {
                            cell = new PdfPCell(new Paragraph("" + kod, font1));
                        } else {
                            cell = new PdfPCell(new Paragraph("" + dbl, font1));
                        }
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBorder(0);
                        table.addCell(cell);
                        z++;

                    }

                }
                doc.add(table);
//                }
            }
            doc.close();
            pwriter.close();
        } catch (DocumentException ex) {
            System.out.println(ex);
        }
    }

    public String elave(){
        if(value.equals("0") || value.equals("1") || value.equals("2") || value.equals("3") || value.equals("4")){
            return null;
        }else{
            return null;
        }
    }
    
    public void hes_excel() throws DocumentException, IOException {
        
        if(listyek().isEmpty()){
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFFont font1 = createFont(workbook, (short) 11, "Times New Roman", (short) 400);
        HSSFFont font2 = createFont(workbook, (short) 12, "Times New Roman", (short) 700);
        HSSFSheet sheet = workbook.createSheet("Əsas vəsaitlərin hərəkəti və əsaslı təmiri (torpaq və faydalı qazıntılar istisna olunmaqla)\n\n");
        sheet.setColumnWidth(0, 9700);
        sheet.setColumnWidth(1, 3200);
        sheet.setColumnWidth(1 + getfnCount(), 3000);

        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        HSSFCell cell = createCell(workbook, row, 0, (short) 1, (short) 2, font2, false, false);
        cell.setCellValue(getBasliq());

        row = sheet.createRow(sheet.getLastRowNum() + 2);
        cell = createCell(workbook, row, 0, (short) 2, (short) 1, font2, true, true);
        cell.setCellValue("Göstəricinin adı");
        cell = createCell(workbook, row, 1, (short) 2, (short) 1, font2, true, true);
        cell.setCellValue("Məsrəflərin kodu");
        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 2, 1 + getfnCount()));
        for (int i = 2; i < 2 + getfnCount(); i++) {
            cell = createCell(workbook, row, i, (short) 2, (short) 1, font2, true, true);
            cell.setCellValue("Fəaliyyət növləri");
        }

        row = sheet.createRow(sheet.getLastRowNum() + 1);
        cell = createCell(workbook, row, 0, (short) 2, (short) 1, font2, true, true);
        cell.setCellValue(" ");
        cell = createCell(workbook, row, 1, (short) 2, (short) 1, font2, true, true);
        cell.setCellValue(" ");
        for (int i = 2; i < 2 + getfnCount(); i++) {
            cell = createCell(workbook, row, i, (short) 2, (short) 1, font2, true, true);
            cell.setCellValue(list2.get(i - 2));
        }

        Iterator<TableRow> itr = this.list.iterator();
        while (itr.hasNext()) {
            TableRow tbl = itr.next();

            row = sheet.createRow(sheet.getLastRowNum() + 1);
            cell = createCell(workbook, row, 0, (short) 1, (short) 1, font1, false, false);
            String ad1 = tbl.getAdi();
            String ad2 = tbl.getAd();
            if (tbl.getAdi() == null) {
                ad1 = "";
            }
            cell.setCellValue(ad1);

            cell = createCell(workbook, row, 1, (short) 1, (short) 1, font1, false, false);
            String kod5 = tbl.getKod();
            if(tbl.getKod().equals("00000")){
                kod5 = "X";
            }
            if (tbl.getAdi() == null) {
                kod5 = "";
            }
            cell.setCellValue(kod5);

            for (int i = 2; i < 2 + getfnCount(); i++) {
                cell = createCell(workbook, row, i, (short) 3, (short) 1, font1, false, false);
                Double dbl = tbl.getMap(list2.get(i - 2));
                if (tbl.getMap(list2.get(i - 2)) == null) {
                    dbl = 0.0;
                }
                cell.setCellValue(dbl);
            }
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.responseComplete();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "inline;filename=FA.xls");
        response.setHeader("Cache-Control", "no-cache");
        OutputStream out = response.getOutputStream();

        try {
            workbook.write(out);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

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

    private void redirectToPage(String toUrl) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ExternalContext extContext = ctx.getExternalContext();
            String url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/" + toUrl));
            extContext.redirect(url);
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    public void cixis() {
        redirectToPage("//localhost:8080/loginControl/?param=pr03101444/");
    }

    public List<TableRow> baxis() throws SQLException {
        baxis = new ArrayList<>();
        conn = null;
        String sert1 = "";
        String sert2 = "";
        String sert3 = "";
        String sert4 = "";
        String sert5 = "";
        String sert6 = "";
        String sert7 = "";
        ResultSet rs = null;


        if (kod != null) {
            if (!kod.equals("")) {
                sert1 = " mkod IN (" + kod + ") AND";
            }
        } else {
            kod = "";
        }

        if (sira != null) {
            if (!sira.equals("")) {
                sert2 = " sira_h IN (" + sira + ") AND ";
            }
        } else {
            sira = "";
        }

//        if (iqt != null) {
//            if (!iqt.equals("") && !iqt.equals("0") && !iqt.equals("000")) {
//                sert3 = " LEFT (arazi,3) IN (" + iqt + ") AND ";
//            }
//        } else {
//            iqt = "";
//        }
     if (iqt != null) {
        switch (iqt) {
            case "":
                iqt = "";
                break;
            case "000":
                iqt = "";
                break;
            default:
                    sert3 = " LEFT (arazi,3) IN (" + iqt + ") AND";        
        }
        } else {
              iqt = "";  
        }
        
        if (feal != null) {
            if (!feal.equals("") && !feal.equals("0")) {
                sert4 = " feal IN (" + feal + ") AND ";
            }
        } else {
            feal = "";
        }

        if (mn != null) {
            if (!mn.equals("") && !mn.equals("0")) {
                sert5 = " mulk IN (" + mn + ") AND ";
            }
        } else {
            mn = "";
        }

        if (sek != null) {
            if (!sek.equals("") && !sek.equals("0")) {
                sert6 = " h.seksiya IN ('" + sek + "') AND ";
            }
        } else {
            sek = "";
        }

        if (tab != null) {
            if (!tab.equals("") && !tab.equals("0") ) {
                sert7 = " h.tab IN (" + tab + ") AND ";
            }
        } else {
            tab = "";
        }

        if (main_Bean.getStatus() == 1 && !sert1.equals("")) {
            sert4 = "";
            sert5 = "";
            sert6 = "";
            sert7 = "";
        } else if (!sert1.equals("")){
            sert3 = "";
            sert4 = "";
            sert5 = "";
            sert6 = "";
            sert7 = "";
        } else {
            
        }

//        String SQL = "  SELECT '0' nomre, sira_h, adi, ad2, m.mehs_kodu, mehs_koduh, feal, ' ' arazi, mkod,"
//                + " IFNULL(h.sut1,0) sut1, IFNULL(h.sut2,0) sut2, IFNULL(h.sut3,0) sut3, IFNULL(h.sut4,0) sut4, IFNULL(h.sut5,0) sut5,"
//                + " IFNULL(h.sut6,0) sut6, IFNULL(h.sut7,0) sut7"
//                + " FROM ("
//                + " (SELECT ad, q.* FROM db03101444.afhes q, tesnifat.kataloq_2011 y WHERE mkod IN (kod)) h "
//                + " LEFT JOIN "
//                + " (SELECT sira, adi, mehs_kodu FROM db03101444.af) a ON a.sira = h.sira_h "
//                + " LEFT JOIN "
//                + " (SELECT adi AS ad2, mehs_kodu FROM db03101444.af1 ) m ON h.mehs_koduh = m.mehs_kodu)"
//                + " WHERE " + sert1 + sert2 + sert3 + sert4 + sert5 + sert6 + sert7 + " sut1 + sut2 + sut3 + sut4 +sut5 + sut6 + sut7 <> 0 "
//                + " GROUP BY mkod,sira_h "
//                + " UNION ALL"
//                + "(SELECT '1' nomre, ' ' sira_h, ad, ' ' ad2, ' ' mehs_kodu, ' ' mehs_koduh, ' ' feal, LEFT (arazi,3) arazi, mkod,"
//                + " '0' sut1, '0' sut2, '0' sut3, '0' sut4, '0' sut5,"
//                + " '0' sut6, '0' sut7"
//                + "  FROM db03101444.afhes h, tesnifat.kataloq_2011 q WHERE " + sert1 + sert2 + sert3 + sert4 + sert5 + sert6 + sert7 + " mkod = kod GROUP BY mkod) ORDER BY mkod,sira_h";
        
                String SQL = "SELECT '1' nomre, sira_h, adi, mehs_koduh, feal, LEFT(arazi,3) arazi, mkod, "
                + "       IFNULL(h.sut1, 0) sut1, IFNULL(h.sut2, 0) sut2, IFNULL(h.sut3, 0) sut3, IFNULL(h.sut4, 0) sut4, "
                + "       IFNULL(h.sut5, 0) sut5, IFNULL(h.sut6, 0) sut6, IFNULL(h.sut7, 0) sut7 "
                + " FROM ( "
                + "    (SELECT Ad, q.*  FROM db03101444.afhes q, tesnifat.kataloq_2011 y WHERE mkod IN (kod)) h "
                + "     LEFT JOIN  (SELECT sira, adi, mehs_kodu FROM db03101444.af UNION SELECT sira, adi, mehs_kodu FROM db03101444.af1) m "
                + "     ON h.mehs_koduh = m.mehs_kodu)  WHERE " + sert1 + sert2 + sert3 + sert4 + sert5 + sert6 + sert7 + " sut1 + sut2 + sut3 + sut4 + sut5 + sut6 + sut7 <> 0  GROUP BY mkod,sira_h "
                + " UNION ALL "
                + " (SELECT  '0' nomre, ' ' sira_h, Ad, ' ' mehs_koduh, ' ' feal, LEFT(arazi, 3) arazi, mkod, "
                + "         '0' sut1, '0' sut2, '0' sut3, '0' sut4, '0' sut5, '0' sut6, '0' sut7 "
                + "  FROM db03101444.afhes h,  tesnifat.kataloq_2011 q WHERE " + sert1 + sert2 + sert3 + sert4 + sert5 + sert6 + sert7 + " mkod = kod  GROUP BY mkod) "
                + " ORDER BY arazi, mkod, sira_h";
        System.out.println("sql = " + SQL);
        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                lst = new TableRow();
                lst.setSira(rs.getString("sira_h"));
                lst.setAdi(rs.getString("adi"));
//                lst.setMehs_kodu(rs.getString("mehs_kodu"));
                lst.setKod(rs.getString("mehs_koduh"));
//                lst.setAd(rs.getString("ad2"));
                lst.setNomre(rs.getString("nomre"));
                lst.setKod_c(rs.getString("mkod"));
                lst.setAdi_h(rs.getString("arazi"));
                lst.setSut1(rs.getDouble("sut1"));
                lst.setSut2(rs.getDouble("sut2"));
                lst.setSut3(rs.getDouble("sut3"));
                lst.setSut4(rs.getDouble("sut4"));
                lst.setSut5(rs.getDouble("sut5"));
                lst.setSut6(rs.getDouble("sut6"));
                lst.setSut7(rs.getInt("sut7"));

                baxis.add(lst);
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

        return baxis;
    }

    public List<Yekun_Bean> rayon() {
        rayon = new ArrayList<>();
        conn = null;
        ResultSet rs = null;
        String SQL = "SELECT ad, kod FROM tesnifat.kataloq_2011 WHERE  LEFT (kodarazi,3) = SUBSTRING('" + main_Bean.getAreacode() + "',1,3) AND bazpr != '0' ";

        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                Yekun_Bean yb = new Yekun_Bean();
                yb.kod = rs.getString("kod");
                yb.ad = rs.getString("ad");
              
                rayon.add(yb);

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
        return rayon;
    }

//    public int getStatus() {
//        conn = null;
//        ResultSet rs = null;
//        String SQL = "SELECT status FROM  tesnifat.accesskey";
//
//        try {
//            conn = ds.getConnection();
//            rs = conn.prepareStatement(SQL).executeQuery();
//
//            while (rs.next()) {
//                status = rs.getInt("status");
//
//            }
//
//        } catch (Exception e1) {
//            System.out.println("sehv_Select1 =  " + e1.getMessage());
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException e) {
//                System.out.println("sehv_Select2 = " + e.getMessage());
//            }
//        }
//        return status;
//    }

    public String Ray(){
         conn = null;
        ResultSet rs = null;
        String SQL = "SELECT B FROM tesnifat.region WHERE KOD = " + main_Bean.getAreacode() + " ";
       
        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ray = rs.getString("B");

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
        return ray;
    }
    
    public String getRay() {
        return ray;
    }

//    
//    public String getParam(FacesContext fc) {
//        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
//        areacode = params.get("arazi");
//        return areacode;
//    }
    
    private List<TableRow> list;
    private List<String> list2;
    private List<Double> list3;
    private List<TableRow> baxis;
    private List<Yekun_Bean> rayon;
    private ArrayList<Yekun_Bean> fnkod;
    private ArrayList<Yekun_Bean> mulkiyyet;
    private ArrayList<Yekun_Bean> iqtisadireg;
    private Map<String, Double> map;
    private Map<String, Integer> map2;
    private String selectfnkod;
    private String selectmulkiyyet;
    private String selectiqtisadireg;
    private String value;
    private int val;
    private String zona;
    private String zona1;
    private String araziadi;
    private String seksiya;
    private String sahe;
    private String mulk;
    private String fn;
    private int nov;
    private String id;
    private String iqt;
    private String kod;
    private String sira;
    private String ad;
    private String kod2;
    private String kod3;
    private String feal;
    private String tab;
    private String sek;
    private String mn;
    private String mn2;
    private String ik;
    private String fk;
    private String basliq;
    private String ray;
    private int seh = 1;
    

    public Main_Bean getMain_Bean() {
        return main_Bean;
    }

    public void setMain_Bean(Main_Bean main_Bean) {
        this.main_Bean = main_Bean;
    }

    public TableRow getLst() {
        return lst;
    }

    public void setLst(TableRow lst) {
        this.lst = lst;
    }

    public List<TableRow> getList() {
        return list;
    }

    public List<String> getList2() {
        return list2;
    }

    public List<Double> getList3() {
        return list3;
    }

    public List<TableRow> getBaxis() {
        return baxis;
    }

    public void setBaxis(List<TableRow> baxis) {
        this.baxis = baxis;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSelectfnkod() {
        return selectfnkod;
    }

    public void setSelectfnkod(String selectfnkod) {
        this.selectfnkod = selectfnkod;
    }

    public String getSelectmulkiyyet() {
        return selectmulkiyyet;
    }

    public void setSelectmulkiyyet(String selectmulkiyyet) {
        this.selectmulkiyyet = selectmulkiyyet;
    }

    public String getSelectiqtisadireg() {
        return selectiqtisadireg;
    }

    public void setSelectiqtisadireg(String selectiqtisadireg) {
        this.selectiqtisadireg = selectiqtisadireg;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getZona() {
        return zona;
    }

    public String getZona1() {
        return zona1;
    }

    public String getAraziadi() {
        return araziadi;
    }

    public String getSeksiya() {
        return seksiya;
    }

    public String getSahe() {
        return sahe;
    }

    public String getMulk() {
        return mulk;
    }

    public int getNov() {
        return nov;
    }

    public void setNov(int nov) {
        this.nov = nov;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIqt() {
        return iqt;
    }

    public void setIqt(String iqt) {
        this.iqt = iqt;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getSira() {
        return sira;
    }

    public void setSira(String sira) {
        this.sira = sira;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getKod2() {
        return kod2;
    }

    public void setKod2(String kod2) {
        this.kod2 = kod2;
    }

    public String getKod3() {
        return kod3;
    }

    public void setKod3(String kod3) {
        this.kod3 = kod3;
    }

    public String getFeal() {
        return feal;
    }

    public void setFeal(String feal) {
        this.feal = feal;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getSek() {
        return sek;
    }

    public void setSek(String sek) {
        this.sek = sek;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getFk() {
        return fk;
    }

    public void setFk(String fk) {
        this.fk = fk;
    }


//    public void setLogin(String login) {
//        this.login = login;
//    }

    public String getBasliq() {
        switch (value) {
            case "sut1":
                basliq = "Maddi əsas vəsaitlərin əldə edilməsinə çəkilmiş xərclər";
                break;
            case "sut2":
                basliq = "Ölkədə istehsal edilmiş yeni əsas vəsaitlər";
                break;
            case "sut3":
                basliq = "İdxal olunmuş yeni və istifadə edilmiş əsas vəsaitlər";
                break;
            case "sut4":
                basliq = "Əsas vəsaitlərin əsaslı təmiri üzrə kənar müəssisə və təşkilatların göstərdikləri xidmətlərin dəyəri";
                break;
            case "sut5":
                basliq = "Xarici ölkələrin hüquqi şəxslərinin göstərdikləri xidmətlər";
                break;
            case "sut6":
                basliq = "Silinmiş əsas vəsaitlərin bazar qiyməti";
                break;
            case "sut7":
                basliq = "Silinmiş əsas vəsaitlərin yaşı (tam rəqəmlə)";
        }
        return basliq;
    }

    public void setBasliq(String basliq) {
        this.basliq = basliq;
    }

    public void setRay(String ray) {
        this.ray = ray;
    }

    public String getIk() {
        return ik;
    }

    public void setIk(String ik) {
        this.ik = ik;
    }

    public String getMn2() {
        return mn2;
    }

    public void setMn2(String mn2) {
        this.mn2 = mn2;
    }

    public int getSeh() {
        return seh;
    }

    public void setSeh(int seh) {
        this.seh = seh;
    }


}
