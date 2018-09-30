package stat.pr03101444.Pojo;

import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;


@Named (value = "Kat")
@ConversationScoped
public class Katalog
  implements Serializable
{
  private String ad;
  private String kod;
  private String fn;
  private String req;
  private String ik;
  private String bucce;
  private String mn;
  private String seksiya;
  private String htforma;
  private String tab;
  private String say;
  private String dev;
  private String muesseadi;
  private String muessekod;
  private String arazi;
  private String status;
  
  public Katalog() {}
  
  public String getAd()
  {
    return this.ad;
  }
  
  public void setAd(String ad)
  {
    this.ad = ad;
  }
  
  public String getBucce()
  {
    return this.bucce;
  }
  
  public void setBucce(String bucce)
  {
    this.bucce = bucce;
  }
  
  public String getDev()
  {
    return this.dev;
  }
  
  public void setDev(String dev)
  {
    this.dev = dev;
  }
  
  public String getFn()
  {
    return this.fn;
  }
  
  public void setFn(String fn)
  {
    this.fn = fn;
  }
  
  public String getHtforma()
  {
    return this.htforma;
  }
  
  public void setHtforma(String htforma)
  {
    this.htforma = htforma;
  }
  
  public String getIk()
  {
    return this.ik;
  }
  
  public void setIk(String ik)
  {
    this.ik = ik;
  }
  
  public String getKod()
  {
    return this.kod;
  }
  
  public void setKod(String kod)
  {
    this.kod = kod;
  }
  
  public String getMn()
  {
    return this.mn;
  }
  
  public void setMn(String mn)
  {
    this.mn = mn;
  }
  
  public String getReq()
  {
    return this.req;
  }
  
  public void setReq(String req)
  {
    this.req = req;
  }
  
  public String getSay()
  {
    return this.say;
  }
  
  public void setSay(String say)
  {
    this.say = say;
  }
  
  public String getSeksiya()
  {
    return this.seksiya;
  }
  
  public void setSeksiya(String seksiya)
  {
    this.seksiya = seksiya;
  }
  
  public String getTab()
  {
    return this.tab;
  }
  
  public void setTab(String tab)
  {
    this.tab = tab;
  }
  
  public String getArazi()
  {
    return this.arazi;
  }
  
  public void setArazi(String arazi)
  {
    this.arazi = arazi;
  }
  
  public String getMuesseadi()
  {
    return this.muesseadi;
  }
  
  public void setMuesseadi(String muesseadi)
  {
    this.muesseadi = muesseadi;
  }
  
  public String getMuessekod()
  {
    return this.muessekod;
  }
  
  public void setMuessekod(String muessekod)
  {
    this.muessekod = muessekod;
  }
  
  public String getStatus()
  {
    return this.status;
  }
  
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  public Katalog(String ad, String kod, String fn, String req, String ik, String bucce, String mn, String seksiya, String htforma, String tab, String say, String dev)
  {
    this.ad = ad;
    this.kod = kod;
    this.fn = fn;
    this.req = req;
    this.ik = ik;
    this.bucce = bucce;
    this.mn = mn;
    this.seksiya = seksiya;
    this.htforma = htforma;
    this.tab = tab;
    this.say = say;
    this.dev = dev;
  }
  
  public Katalog(String muessekod, String muesseadi, String fn, String mn, String say, String seksiya, String arazi, String tab, String ik, String status, String htforma)
  {
    this.muesseadi = muesseadi;
    this.muessekod = muessekod;
    this.fn = fn;
    this.arazi = arazi;
    this.mn = mn;
    this.seksiya = seksiya;
    this.say = say;
    this.tab = tab;
    this.ik = ik;
    this.status = status;
    this.htforma = htforma;
  }

}
