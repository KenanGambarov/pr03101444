package stat.pr03101444.Pojo;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import stat.pr03101444.ModelView.Hesabat_Bean;
import stat.pr03101444.ModelView.Yekun_Bean;


@Named(value = "Row")
@ConversationScoped
public class TableRow implements Serializable {

    @Resource(name = "jdbc/dummy")
    private DataSource ds;
    Connection conn = null;
    ResultSet rs = null;

    @Inject
    private Hesabat_Bean bean;
    @Inject
    private TableRow row1;
    @Inject
    private Yekun_Bean yb;

    public List<TableRow> listhes() {
        list = new ArrayList<>();
        conn = null;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ip = request.getRemoteAddr();
        rs = null;
        System.out.println("code4 = " + bean.getSelectfactorycode());
        String SQL = " SELECT sira, adi, mehs_kodu,"
                + "   IFNULL(h.sut1,0) sut1, IFNULL(h.sut2,0) sut2, IFNULL(h.sut3,0) sut3, IFNULL(h.sut4,0) sut4, IFNULL(h.sut5,0) sut5, "
                + "   IFNULL(h.sut6,0) sut6, IFNULL(h.sut7,0) sut7, adamsaat, ip "
                + "    FROM ("
                + " (SELECT  sira, adi, mehs_kodu  FROM db03101444.af) a "
                + " LEFT JOIN "
                + " (SELECT * FROM db03101444.afhes WHERE mkod = '" + bean.getSelectfactorycode() + "') h ON a.sira=h.sira_h) "
                + " ORDER BY sira";

        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                row1 = new TableRow();
                row1.sira = rs.getString("sira");
                row1.adi = rs.getString("adi");
                row1.mehs_kodu = rs.getString("mehs_kodu");
                row1.sut1 = rs.getDouble("sut1");
                row1.sut2 = rs.getDouble("sut2");
                row1.sut3 = rs.getDouble("sut3");
                row1.sut4 = rs.getDouble("sut4");
                row1.sut5 = rs.getDouble("sut5");
                row1.sut6 = rs.getDouble("sut6");
                row1.sut7 = rs.getInt("sut7");

                list.add(row1);
                bean.setList(list);
                if (rs.getDouble("adamsaat") != 0 && rs.getString("adamsaat") != null) {
                    bean.setAdamsaat(rs.getString("adamsaat"));
                    ip = rs.getString("ip");
                }
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
        return list;
    }


    public void setSelectmehsul(String selectmehsul) {
        this.selectmehsul = selectmehsul;
    }

    public String getSira_h() {
        sira_h = null;
        rs = null;
        String SQL = "SELECT sira_h FROM db03101444.afhes WHERE mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {

                if (rs.getInt("sira_h") > 36) {
                    sira_h = rs.getString("sira_h");
                } else {
                    sira_h = "36";
                }

            }
        } catch (Exception e1) {
            System.out.println("sehv_ad10 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad10.5 = " + e.getMessage());
            }
        }
        return sira_h;
    }

     public void addAction() {
        TableRow tableRow = new TableRow(sira, adi, mehs_kodu, sut1, sut2, sut3, sut4, sut5, sut6, sut7);
        list.add(tableRow);
    }
    
    public TableRow(String sira, String adi, String mehs_kodu, double sut1, double sut2, double sut3, double sut4, double sut5, double sut6, int sut7) {
        this.sira = sira;
        this.adi = adi;
        this.mehs_kodu = mehs_kodu;
        this.sut1 = sut1;
        this.sut2 = sut2;
        this.sut3 = sut3;
        this.sut4 = sut4;
        this.sut5 = sut5;
        this.sut6 = sut6;
        this.sut7 = sut7;
    }

    public TableRow(){
        map = new LinkedHashMap<>();
        map2 = new LinkedHashMap<>();
    }
    
    public Double getMap(String fn){ 
        return map.get(fn);
    }
    
    public Integer getMap2(String fn){
        return map2.get(fn);
    }
    
    public TableRow(String adi, String kod, Map<String,Double>map, Map<String,Integer>map2) {
        this.adi = adi;
        this.kod = kod;
        this.map = map;
        this.map2 = map2;
    }

    
    public String getSira_c() {
        if (getSira_h() == null) {
            sira_c = sira_v;
        } else {
            sira_c = sira_h;
        }
        return sira_c;
    }

    public void setSira_c(String sira_c) {
        this.sira_c = sira_c;
    }

    public double sut(int i) {
        switch (i) {
            case 1:
                return sut1;
            case 2:
                return sut2;
            case 3:
                return sut3;
            case 4:
                return sut4;
            case 5:
                return sut5;
            case 6:
                return sut6;
        }
        return 0;
    }

    public int getSay() {
        say++;
        return say;
    }

    public void setSay(int say) {
        this.say = say;
    }


    private String sira;
    private String adi;
    private String mehs_kodu;
    private String adi_h;
    private String mehs_koduh1;
    private double sut1;
    private double sut2;
    private double sut3;
    private double sut4;
    private double sut5;
    private double sut6;
    private int sut7;
    private String selectmehsul;
    private List<TableRow> list;
    private List<TableRow> adkod;
    private String adamsaat;
    private String ip;
    private String kod;
    private String kod_c;
    private String kod_b;
    private String nomre;
    private int say = 0;
    private List<TableRow> mehsul34;
    private String kod1;
    private String kod2;
    private String kod3;
    private String kod4;
    private String kod5;
    private String kod6;
    private String kod7;
    private String kod8;
    private String kod9;
    private String kod10;
    private String ad;
    private String ad1;
    private String ad2;
    private String ad3;
    private String ad4;
    private String ad5;
    private String ad6;
    private String ad7;
    private String ad8;
    private String ad9;
    private String ad10;
    private String mehs_koduh2;
    private String mehs_koduh3;
    private String mehs_koduh4;
    private String mehs_koduh5;
    private String mehs_koduh6;
    private String mehs_koduh7;
    private String mehs_koduh8;
    private String mehs_koduh9;
    private String mehs_koduh10;
    private String sira_h;
    private String sira_v = "36";
    private String sira_c;
    private ArrayList<SelectItem> mehsul;
    private ArrayList<SelectItem> mehsul1;
    private ArrayList<SelectItem> mehsul2;
    private ArrayList<SelectItem> mehsul3;
    private ArrayList<SelectItem> mehsul4;
    private ArrayList<SelectItem> mehsul5;
    private ArrayList<SelectItem> mehsul6;
    private ArrayList<SelectItem> mehsul7;
    private ArrayList<SelectItem> mehsul8;
    private ArrayList<SelectItem> mehsul9;
    private ArrayList<SelectItem> mehsul10;
    private ArrayList<SelectItem> mehsul11;
    private ArrayList<SelectItem> mehsul12;
    private ArrayList<SelectItem> mehsul13;
    private ArrayList<SelectItem> mehsul14;
    private ArrayList<SelectItem> mehsul15;
    private ArrayList<SelectItem> mehsul16;
    private ArrayList<SelectItem> mehsul17;
    private ArrayList<SelectItem> mehsul18;
    private ArrayList<SelectItem> mehsul19;
    private ArrayList<SelectItem> mehsul20;
    private ArrayList<SelectItem> mehsul21;
    private ArrayList<SelectItem> mehsul22;
    private ArrayList<SelectItem> mehsul23;
    private ArrayList<SelectItem> mehsul24;
    private ArrayList<SelectItem> mehsul25;
    private ArrayList<SelectItem> mehsul26;
    private ArrayList<SelectItem> mehsul27;
    private ArrayList<SelectItem> mehsul28;
    private ArrayList<SelectItem> mehsul29;
    private ArrayList<SelectItem> mehsul30;
    private ArrayList<SelectItem> mehsul31;
    private ArrayList<String> mehsul32;
    private ArrayList<String> mehsul33;
    private Map<String,Double>map;
    private Map<String,Integer>map2;

    public TableRow getRow1() {
        return row1;
    }

    public void setRow1(TableRow row1) {
        this.row1 = row1;
    }

    public String getSira() {
        return sira;
    }

    public void setSira(String sira) {
        this.sira = sira;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getMehs_kodu() {
        return mehs_kodu;
    }

    public void setMehs_kodu(String mehs_kodu) {
        this.mehs_kodu = mehs_kodu;
    }

    public String getAdi_h() {
        return adi_h;
    }

    public String getNomre() {
        return nomre;
    }

    public void setNomre(String nomre) {
        this.nomre = nomre;
    }


    public void setAdi_h(String adi_h) {
        this.adi_h = adi_h;
    }

    public double getSut1() {
        return sut1;
    }

    public void setSut1(double sut1) {
        this.sut1 = sut1;
    }

    public double getSut2() {
        return sut2;
    }

    public void setSut2(double sut2) {
        this.sut2 = sut2;
    }

    public double getSut3() {
        return sut3;
    }

    public void setSut3(double sut3) {
        this.sut3 = sut3;
    }

    public double getSut4() {
        return sut4;
    }

    public void setSut4(double sut4) {
        this.sut4 = sut4;
    }

    public double getSut5() {
        return sut5;
    }

    public void setSut5(double sut5) {
        this.sut5 = sut5;
    }

    public double getSut6() {
        return sut6;
    }

    public void setSut6(double sut6) {
        this.sut6 = sut6;
    }

    public int getSut7() {
        return sut7;
    }

    public void setSut7(int sut7) {
        this.sut7 = sut7;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public String getAdamsaat() {
        return adamsaat;
    }

    public void setAdamsaat(String adamsaat) {
        this.adamsaat = adamsaat;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSelectmehsul() {
        return selectmehsul;
    }

    public List<TableRow> getList() {
        return list;
    }

    public List<TableRow> getAdkod() {
        return adkod;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getKod_c() {
        return kod_c;
    }

    public void setKod_c(String kod_c) {
        this.kod_c = kod_c;
    }

    public String getKod_b() {
        return kod_b;
    }

    public void setKod_b(String kod_b) {
        this.kod_b = kod_b;
    }

    public String getSira_v() {
        return sira_v;
    }

    public void setSira_v(String sira_v) {
        this.sira_v = sira_v;
    }

    public String getMehs_koduh1() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '37' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh1 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod1.5 = " + e.getMessage());
            }
        }
        return mehs_koduh1;
    }

    public void setMehs_koduh1(String mehs_koduh1) {
        this.mehs_koduh1 = mehs_koduh1;
    }

    public String getMehs_koduh2() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '38' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh2 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod2 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod2.5 = " + e.getMessage());
            }
        }
        return mehs_koduh2;
    }

    public void setMehs_koduh2(String mehs_koduh2) {
        this.mehs_koduh2 = mehs_koduh2;
    }

    public String getMehs_koduh3() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '39' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                setMehs_koduh3(rs.getString("mehs_koduh"));
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod3 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod3.5 = " + e.getMessage());
            }
        }
        return mehs_koduh3;
    }

    public void setMehs_koduh3(String mehs_koduh3) {
        this.mehs_koduh3 = mehs_koduh3;
    }

    public String getMehs_koduh4() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '40' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh4 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod4 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod4.5 = " + e.getMessage());
            }
        }
        return mehs_koduh4;
    }

    public void setMehs_koduh4(String mehs_koduh4) {
        this.mehs_koduh4 = mehs_koduh4;
    }

    public String getMehs_koduh5() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '41' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh5 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod5 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod5.5 = " + e.getMessage());
            }
        }
        return mehs_koduh5;
    }

    public void setMehs_koduh5(String mehs_koduh5) {
        this.mehs_koduh5 = mehs_koduh5;
    }

    public String getMehs_koduh6() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '42' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh6 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod6 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod6.5 = " + e.getMessage());
            }
        }
        return mehs_koduh6;
    }

    public void setMehs_koduh6(String mehs_koduh6) {
        this.mehs_koduh6 = mehs_koduh6;
    }

    public String getMehs_koduh7() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '43' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh7 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod7 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod7.5 = " + e.getMessage());
            }
        }
        return mehs_koduh7;
    }

    public void setMehs_koduh7(String mehs_koduh7) {
        this.mehs_koduh7 = mehs_koduh7;
    }

    public String getMehs_koduh8() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '44' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh8 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod8 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod8.5 = " + e.getMessage());
            }
        }
        return mehs_koduh8;
    }

    public void setMehs_koduh8(String mehs_koduh8) {
        this.mehs_koduh8 = mehs_koduh8;
    }

    public String getMehs_koduh9() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '45' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh9 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod9 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod9.5 = " + e.getMessage());
            }
        }
        return mehs_koduh9;
    }

    public void setMehs_koduh9(String mehs_koduh9) {
        this.mehs_koduh9 = mehs_koduh9;
    }

    public String getMehs_koduh10() {
        rs = null;
        String SQL = "SELECT mehs_koduh FROM db03101444.afhes WHERE sira_h = '46' AND mkod = '" + bean.getSelectfactorycode() + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                mehs_koduh10 = rs.getString("mehs_koduh");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod10 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod10.5 = " + e.getMessage());
            }
        }
        return mehs_koduh10;
    }

    public void setMehs_koduh10(String mehs_koduh10) {
        this.mehs_koduh10 = mehs_koduh10;
    }

    public String getKod1() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh1 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod1 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod1.5 = " + e.getMessage());
            }
        }
        return kod1;
    }

    public void setKod1(String kod1) {
        this.kod1 = kod1;
    }

    public String getKod2() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh2 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod2 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod2 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod2.5 = " + e.getMessage());
            }
        }
        return kod2;
    }

    public void setKod2(String kod2) {
        this.kod2 = kod2;
    }

    public String getKod3() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh3 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod3 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod3 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod3.5 = " + e.getMessage());
            }
        }
        return kod3;
    }

    public void setKod3(String kod3) {
        this.kod3 = kod3;
    }

    public String getKod4() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh4 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod4 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod4 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod4.5 = " + e.getMessage());
            }
        }
        return kod4;
    }

    public void setKod4(String kod4) {
        this.kod4 = kod4;
    }

    public String getKod5() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh5 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod5 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod5 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod5.5 = " + e.getMessage());
            }
        }
        return kod5;
    }

    public void setKod5(String kod5) {
        this.kod5 = kod5;
    }

    public String getKod6() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh6 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod6 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod6 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod6.5 = " + e.getMessage());
            }
        }
        return kod6;
    }

    public void setKod6(String kod6) {
        this.kod6 = kod6;
    }

    public String getKod7() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh7 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod7 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod7 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod7.5 = " + e.getMessage());
            }
        }
        return kod7;
    }

    public void setKod7(String kod7) {
        this.kod7 = kod7;
    }

    public String getKod8() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh8 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod8 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod8 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod8.5 = " + e.getMessage());
            }
        }
        return kod8;
    }

    public void setKod8(String kod8) {
        this.kod8 = kod8;
    }

    public String getKod9() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu = '" + mehs_koduh9 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod9 = rs.getString("mehs_kodu");
            }
        } catch (Exception e1) {
            System.out.println("sehv_kod9 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod9.5 = " + e.getMessage());
            }
        }
        return kod9;
    }

    public void setKod9(String kod9) {
        this.kod9 = kod9;
    }

    public String getKod10() {
        rs = null;
        String SQL = "SELECT mehs_kodu FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh10 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                kod10 = rs.getString("mehs_kodu");
            }

        } catch (Exception e1) {
            System.out.println("sehv_kod10 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_kod10.5 = " + e.getMessage());
            }
        }
        return kod10;
    }

    public void setKod10(String kod10) {
        this.kod10 = kod10;
    }

    public String getAd1() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh1 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad1 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad1.5 = " + e.getMessage());
            }
        }
        return ad1;
    }

    public void setAd1(String ad1) {
        this.ad1 = ad1;
    }

    public String getAd2() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh2 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad2 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad2 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad2.5 = " + e.getMessage());
            }
        }
        return ad2;
    }

    public void setAd2(String ad2) {
        this.ad2 = ad2;
    }

    public String getAd3() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh3 + "'";
        try {
           
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad3 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad3 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad3.5 = " + e.getMessage());
            }
        }
        return ad3;
    }

    public void setAd3(String ad3) {
        this.ad3 = ad3;
    }

    public String getAd4() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh4 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad4 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad4 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad4.5 = " + e.getMessage());
            }
        }
        return ad4;
    }

    public void setAd4(String ad4) {
        this.ad4 = ad4;
    }

    public String getAd5() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh5 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad5 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad5 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad5.5 = " + e.getMessage());
            }
        }
        return ad5;
    }

    public void setAd5(String ad5) {
        this.ad5 = ad5;
    }

    public String getAd6() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh6 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad6 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad6 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad6.5 = " + e.getMessage());
            }
        }
        return ad6;
    }

    public void setAd6(String ad6) {
        this.ad6 = ad6;
    }

    public String getAd7() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh7 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad7 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad7 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad7.5 = " + e.getMessage());
            }
        }
        return ad7;
    }

    public void setAd7(String ad7) {
        this.ad7 = ad7;
    }

    public String getAd8() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh8 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad8 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad8 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad8.5 = " + e.getMessage());
            }
        }
        return ad8;
    }

    public void setAd8(String ad8) {
        this.ad8 = ad8;
    }

    public String getAd9() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + mehs_koduh9 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad9 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad9 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad9.5 = " + e.getMessage());
            }
        }
        return ad9;
    }

    public void setAd9(String ad9) {
        this.ad9 = ad9;
    }

    public String getAd10() {
        rs = null;
        String SQL = "SELECT adi FROM db03101444.af1 WHERE mehs_kodu ='" + this.mehs_koduh10 + "'";
        try {
            
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                ad10 = rs.getString("adi");
            }
        } catch (Exception e1) {
            System.out.println("sehv_ad10 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_ad10.5 = " + e.getMessage());
            }
        }
        return ad10;
    }

    public void setSira_h(String sira_h) {
        this.sira_h = sira_h;
    }

    public void setAd10(String ad10) {
        this.ad10 = ad10;
    }

    public ArrayList<SelectItem> getMehsul1() throws SQLException {
        mehsul1 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='25100'");
            while (rs.next()) {
                mehsul1.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul1;
    }

    public ArrayList<SelectItem> getMehsul() throws SQLException {
        mehsul = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='01400'");
            while (rs.next()) {
                mehsul.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul;
    }

    public ArrayList<SelectItem> getMehsul2() throws SQLException {
        mehsul2 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='25200'");
            while (rs.next()) {
                mehsul2.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul2;
    }

    public ArrayList<SelectItem> getMehsul3() throws SQLException {
        mehsul3 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='25300'");
            while (rs.next()) {
                mehsul3.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul3;
    }

    public ArrayList<SelectItem> getMehsul4() throws SQLException {
        mehsul4 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='25400'");
            while (rs.next()) {
                mehsul4.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul4;
    }

    public ArrayList<SelectItem> getMehsul5() throws SQLException {
        mehsul5 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='25900'");
            while (rs.next()) {
                mehsul5.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul5;
    }

    public ArrayList<SelectItem> getMehsul6() throws SQLException {
        mehsul6 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26200'");
            while (rs.next()) {
                mehsul6.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul6;
    }

    public ArrayList<SelectItem> getMehsul7() throws SQLException {
        mehsul7 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26300'");
            while (rs.next()) {
                mehsul7.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul7;
    }

    public ArrayList<SelectItem> getMehsul8() throws SQLException {
        mehsul8 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26400'");
            while (rs.next()) {
                mehsul8.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul8;
    }

    public ArrayList<SelectItem> getMehsul9() throws SQLException {
        mehsul9 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26500'");
            while (rs.next()) {
                mehsul9.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul9;
    }

    public ArrayList<SelectItem> getMehsul10() throws SQLException {
        mehsul10 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26600'");
            while (rs.next()) {
                mehsul10.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul10;
    }

    public ArrayList<SelectItem> getMehsul11() throws SQLException {
        mehsul11 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26700'");
            while (rs.next()) {
                mehsul11.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul11;
    }

    public ArrayList<SelectItem> getMehsul12() throws SQLException {
        mehsul12 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='26800'");
            while (rs.next()) {
                mehsul12.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul12;
    }

    public ArrayList<SelectItem> getMehsul13() throws SQLException {
        mehsul13 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='27100'");
            while (rs.next()) {
                mehsul13.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul13;
    }

    public ArrayList<SelectItem> getMehsul14() throws SQLException {
        mehsul14 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='27200'");
            while (rs.next()) {
                mehsul14.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul14;
    }

    public ArrayList<SelectItem> getMehsul15() throws SQLException {
        mehsul15 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='27300'");
            while (rs.next()) {
                mehsul15.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul15;
    }

    public ArrayList<SelectItem> getMehsul16() throws SQLException {
        mehsul16 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='27400'");
            while (rs.next()) {
                mehsul16.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul16;
    }

    public ArrayList<SelectItem> getMehsul17() throws SQLException {
        mehsul17 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='27500'");
            while (rs.next()) {
                mehsul17.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul17;
    }

    public ArrayList<SelectItem> getMehsul18() throws SQLException {
        mehsul18 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='27900'");
            while (rs.next()) {
                mehsul18.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul18;
    }

    public ArrayList<SelectItem> getMehsul19() throws SQLException {
        mehsul19 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='28100'");
            while (rs.next()) {
                mehsul19.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul19;
    }

    public ArrayList<SelectItem> getMehsul20() throws SQLException {
        mehsul20 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='28200'");
            while (rs.next()) {
                mehsul20.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul20;
    }

    public ArrayList<SelectItem> getMehsul21() throws SQLException {
        mehsul21 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='28300'");
            while (rs.next()) {
                mehsul21.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul21;
    }

    public ArrayList<SelectItem> getMehsul22() throws SQLException {
        mehsul22 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='28400'");
            while (rs.next()) {
                mehsul22.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul22;
    }

    public ArrayList<SelectItem> getMehsul23() throws SQLException {
        mehsul23 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='28900'");
            while (rs.next()) {
                mehsul23.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul23;
    }

    public ArrayList<SelectItem> getMehsul24() throws SQLException {
        mehsul24 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='29100'");
            while (rs.next()) {
                mehsul24.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul24;
    }

    public ArrayList<SelectItem> getMehsul25() throws SQLException {
        mehsul25 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='29200'");
            while (rs.next()) {
                mehsul25.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul25;
    }

    public ArrayList<SelectItem> getMehsul26() throws SQLException {
        mehsul26 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='30100'");
            while (rs.next()) {
                mehsul26.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul26;
    }

    public ArrayList<SelectItem> getMehsul27() throws SQLException {
        mehsul27 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='30200'");
            while (rs.next()) {
                mehsul27.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul27;
    }

    public ArrayList<SelectItem> getMehsul28() throws SQLException {
        mehsul28 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='30300'");
            while (rs.next()) {
                mehsul28.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul28;
    }

    public ArrayList<SelectItem> getMehsul29() throws SQLException {
        mehsul29 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='30400'");
            while (rs.next()) {
                mehsul29.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul29;
    }

    public ArrayList<SelectItem> getMehsul30() throws SQLException {
        mehsul30 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='30900'");
            while (rs.next()) {
                mehsul30.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul30;
    }

    public ArrayList<SelectItem> getMehsul31() throws SQLException {
        mehsul31 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='31000'");
            while (rs.next()) {
                mehsul31.add(new SelectItem(rs.getString("c_kod") + " - " + rs.getString("c_ad")));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul31;
    }

    public ArrayList<String> getMehsul32() throws SQLException {
        mehsul32 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='32000'");
            while (rs.next()) {
                mehsul32.add(rs.getString("c_kod") + " - " + rs.getString("c_ad"));
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul32;
    }

    public ArrayList<String> getMehsul33() throws SQLException {
        mehsul33 = new ArrayList<>();
        conn = ds.getConnection();
        Statement stmt = null;
        rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT c_kod,c_ad FROM db03101444.1af_hesabat_ucun_acar WHERE b_kod='41000'");
            while (rs.next()) {
                mehsul33.add(rs.getString("c_kod") + " - " + rs.getString("c_ad"));

            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            }
            conn.close();
        }
        return mehsul33;
    }

    public List<TableRow> getMehsul34() {
        mehsul34 = new ArrayList<>();
        rs = null;
        String SQL = "SELECT MNT3, MNT4, Ad FROM db03101444.1af_mehsul WHERE MNT3 >= '42100'";

        try {
            conn = ds.getConnection();
            rs = conn.prepareStatement(SQL).executeQuery();

            while (rs.next()) {
                row1 = new TableRow();
                row1.kod_c = rs.getString("MNT3");
                row1.ad = rs.getString("Ad");
                row1.kod_b = rs.getString("MNT4");
                mehsul34.add(row1);

            }
        } catch (Exception e1) {
            System.out.println("sehv_mehsul1 =  " + e1.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("sehv_mehsul1.5 = " + e.getMessage());
            }
        }
        return mehsul34;
    }

}
