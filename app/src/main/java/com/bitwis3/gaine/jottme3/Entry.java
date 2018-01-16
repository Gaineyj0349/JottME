package com.bitwis3.gaine.jottme3;

/**
 * Created by gaine on 11/8/2017.
 */

public class Entry {
    String entryBody = "";
    String entryDate = "";
    public void setEntryBody(String entryBody) {
        this.entryBody = entryBody;
    }

    public String getEntryBody() {
        return entryBody;
    }



    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
    public String getEntryDate() {
        return entryDate;
    }

    public void clear(){
         entryBody = "";
         entryDate = "";
    }
}
