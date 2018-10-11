
package tikape.runko.domain;


public class Vastaus {
    private Integer id;
    private Integer kysymys_id;
    private String vastausteksti;
    private boolean oikein;

    public Vastaus(Integer id, Integer kysymys_id, String vastausteksti, boolean oikein) {
        this.id = id;
        this.kysymys_id = kysymys_id;
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
    }

    public Integer getId() {
        return id;
    }

    public Integer getKysymys_id() {
        return kysymys_id;
    }

    public String getVastausteksti() {
        return vastausteksti;
    }

    public boolean isOikein() {
        return oikein;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKysymys_id(Integer kysymys_id) {
        this.kysymys_id = kysymys_id;
    }

    public void setOikein(boolean oikein) {
        this.oikein = oikein;
    }

    public void setVastausteksti(String vastausteksti) {
        this.vastausteksti = vastausteksti;
    }
    
    
}
