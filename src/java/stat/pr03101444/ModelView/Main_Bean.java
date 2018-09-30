package stat.pr03101444.ModelView;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.enterprise.context.Conversation;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import stat.tools.AccessCalendar;

@Named(value = "Main_Bean")
@ConversationScoped
public class Main_Bean implements Serializable {

    @Inject
    private Conversation conversation;
    @Resource(name = "jdbc/dummy")
    private DataSource dst;
    private ResourceBundle properties = ResourceBundle.getBundle("stat.pr03101444.Pojo.messages");

    

    public void setKey(String key) throws SQLException, IOException {
        if (!key.matches("^[0-9]{19,19}$")) {
            this.messages = "Sizin bu hesabatla işləmək səlahiyyətiniz yoxdur";
            redirectToPage("message.xhtml");
            return;
        }

        this.key = key;
        Connection conn = dst.getConnection();
        Statement stmt = null;
        ResultSet result = null;

        try {          // Reading information from "accesskey" table
            stmt = conn.createStatement();
            result = stmt.executeQuery("SELECT login, areacode, status, uip FROM tesnifat.accesskey WHERE rkey='" + key + "'");
            if (!result.first()) {
                return;
            }

            login = result.getString(1);
            areacode = result.getString(2);
            status = result.getInt(3);
            ip = result.getString(4);
            selectfactorycode = this.login;

            
            if (status != 0 && status != 1 && status != 2 && status != 3) {
                errmes = properties.getString("errmes1");   // Sizin bu hesabatla işləmək səlahiyyətiniz yoxdur
                return;
            }

//            System.out.println("key=" + key + "***login=" + login + "***areacode=" + areacode + "***status=" + status);
//            Calendar c1 = Calendar.getInstance();
//            int il1 = c1.get(Calendar.YEAR);
//            int dovr1 = c1.get(Calendar.MONTH) + 1;
//            if (dovr1 > 1) {
//                --dovr1;
//            } else {
//                dovr1 = 12;
//                il1--;
//            }
//            // Hesabat dövrünün yüklənməsi
//            AccessCalendar acc = new AccessCalendar(dst, "03112220");
//            String period[] = acc.getPeriod();
//            ryear = period[0];
////            rperiod = period[3];     // hesabat dövrü köhnə AccessCalendar
////            rcomment = period[5];    // messaj köhnə AccessCalendar
//            rperiod = period[4];   // hesabat dövrü yeni AccessCalendar
//            rcomment = period[6];  // messaj yeni AccessCalendar
//            System.out.println("ryear+rperiod=" + ryear +" "+ rperiod);
//
//            if (ryear.equals("0") || rperiod.equals("0")) {
//                ryear = Integer.toString(il1);
//                rperiod = Integer.toString(dovr1);         
//            }
//            
//            selectyear = ryear;
//            selectperiod = rperiod;
            

        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException ex) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
            conn.close();
        }

        if (conversation.isTransient()) {
            conversation.begin();
        }

       
        switch (status) {
            case 0:
                redirectToPage("Hesabat.xhtml");
                break;
            case 1:
                redirectToPage("rayonlar.xhtml");
                break;
            default:
                redirectToPage("Yekun.xhtml");
                break;
        }
    }
    
    public void param (HttpServletRequest request) throws IOException{
        param = request.getParameter("Key");
        if(param == null){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("http://localhost:8080/loginControl/?param=pr03101444/");
        }
    }

//    public ArrayList<SelectItem> getYears() throws SQLException {
//        years = new ArrayList<>();
//        Connection conn = getDst().getConnection();
//        Statement stmt = null;
//        ResultSet result = null;
//        try {
//            stmt = conn.createStatement();
//            result = stmt.executeQuery("SELECT DISTINCT il FROM db03112220.iller ORDER BY il");
//            String lastyear = "0000";
//            while (result.next()) {
//                lastyear = result.getString(1);
//                years.add(new SelectItem(lastyear));
//            }
////            System.out.println("lastyear=" + lastyear);
////            System.out.println("getRyear=" + ryear);
//            if (Integer.parseInt(lastyear) < Integer.parseInt(ryear)) {
//                years.add(new SelectItem(ryear));
//                stmt.executeUpdate("INSERT INTO db03112220.iller (il) value ('" + ryear + "')");
//                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS db03112220.ypxhes_" + ryear + " LIKE db03112220.ypxhes");
//            }
//        } finally {
//            if (result != null) {
//                try {
//                    result.close();
//                } catch (SQLException ex) {
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException ex) {
//                }
//            }
//            conn.close();
//        }
//        return years;
//    }

//    public ArrayList<SelectItem> getYearsItems() throws SQLException {
//        years = new ArrayList<>();
//        Connection conn = getDst().getConnection();
//        Statement stmt = null;
//        ResultSet result = null;
//        try {
//            stmt = conn.createStatement();
//            result = stmt.executeQuery("SELECT DISTINCT il FROM db03112220.iller ORDER BY il");
//            String lastyear = "0000";
//            while (result.next()) {
//                lastyear = result.getString(1);
//                years.add(new SelectItem(lastyear));
//            }
//            System.out.println("lastyear=" + lastyear);
//            System.out.println("getRyear=" + getRyear());
//            if (Integer.parseInt(lastyear) < Integer.parseInt(getRyear())) {
//                years.add(new SelectItem(getRyear()));
//                stmt.executeUpdate("INSERT INTO db03112220.iller (il) value ('" + ryear + "')");
//                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS db03112220.ypxhes_" + ryear + " LIKE db03112220.ypxhes");
//            }
//        } finally {
//            if (result != null) {
//                try {
//                    result.close();
//                } catch (SQLException ex) {
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException ex) {
//                }
//            }
//            conn.close();
//        }
//        return years;
//    }
//    public ArrayList<SelectItem> getPeriods() throws SQLException {
//        periods = new ArrayList<>();
//        Connection conn = getDst().getConnection();
//        Statement stmt = null;
//        ResultSet result = null;
//        try {
//            stmt = conn.createStatement();
//            result = stmt.executeQuery("SELECT period, periodname FROM tesnifat.calendar WHERE forma='03112220' ORDER BY period");
//            while (result.next()) {
//                periods.add(new SelectItem(result.getString(1), result.getString(2)));
//            }
//        } finally {
//            if (result != null) {
//                try {
//                    result.close();
//                } catch (SQLException ex) {
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException ex) {
//                }
//            }
//            conn.close();
//        }
//        return periods;
//    }

//    public ArrayList<SelectItem> getPeriodsItems() throws SQLException {
//        periods = new ArrayList<>();
//        Connection conn = getDst().getConnection();
//        Statement stmt = null;
//        ResultSet result = null;
//        try {
//            stmt = conn.createStatement();
//            result = stmt.executeQuery("SELECT period, periodname FROM tesnifat.calendar WHERE forma='03112220' ORDER BY period");
//            while (result.next()) {
//                periods.add(new SelectItem(result.getString(1), result.getString(2)));
//            }
//        } finally {
//            if (result != null) {
//                try {
//                    result.close();
//                } catch (SQLException ex) {
//                }
//            }
//            if (stmt != null) {
//                try {
//                    stmt.close();
//                } catch (SQLException ex) {
//                }
//            }
//            conn.close();
//        }
//        return periods;
//    }
   
    public void redirectToPage(String pagename) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        request.getSession();
        ExternalContext extContext = fc.getExternalContext();
        String url = extContext.encodeActionURL(fc.getApplication().getViewHandler().getActionURL(fc, "/" + pagename));
        extContext.redirect(url);
    }

    public void newSelect() {
        errlist = "";
    }
    
  
    private String errlist;
    private String key;
    private String login;
    private String areacode;
    private int status;
    private String ip;
    private boolean readonly;
    private String ryear;        // Hesabat ili
    private String rperiod;      // Hesabat dövrü
    private String rcomment;     // Hesabat dövrü haqqında bildiriş
    private String selectyear;
    private String selectperiod;
    private String selectfactorycode;
    private String messages;
    private String param;
    private ArrayList<SelectItem> periods;
    private ArrayList<SelectItem> years;
    private String errmes = properties.getString("errmes0");
    private String outcome;// Siz bu proqramı düzgün yükləmirsiniz

    public String getKey() {
        return key;
    }
    
    //Getters and setters
    public Conversation getConversation() {
        return conversation;
    }

    public DataSource getDst() {
        return dst;
    }

    public String getRcomment() {
        return rcomment;
    }

    public String getLogin() {
        return this.login;
    }

    public int getStatus() {
        return this.status;
    }

    public String getRyear() {
        return ryear;
    }

    public String getRperiod() {
        return rperiod;
    }

    public String getSelectperiod() {
        return selectperiod;
    }

    public String getSelectfactorycode() {
        return this.selectfactorycode;
    }

    public String getSelectyear() {
        return selectyear;
    }

    public String getErrmes() {
        return errmes;
    }

    public String getIp() {
        return ip;
    }

    public String getMessages() {
        return this.messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void setSelectyear(String selectyear) {
        this.selectyear = selectyear;
    }

    public void setSelectperiod(String selectperiod) {
        this.selectperiod = selectperiod;
    }

    public void setPeriods(ArrayList<SelectItem> periods) {
        this.periods = periods;
    }

    public void setYears(ArrayList<SelectItem> years) {
        this.years = years;
    }

    public void setRyear(String ryear) {
        this.ryear = ryear;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getAreacode() {
        return this.areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
