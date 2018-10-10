
package tikape.runko.domain;


public class Vastaus {
    private String vastausteksti;
    private boolean oikein;

    public Vastaus(String vastausteksti, boolean oikein) {
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
    }

    public String getVastausteksti() {
        return vastausteksti;
    }

    public boolean isOikein() {
        return oikein;
    }

    public void setOikein(boolean oikein) {
        this.oikein = oikein;
    }

    public void setVastausteksti(String vastausteksti) {
        this.vastausteksti = vastausteksti;
    }
    
    
}
