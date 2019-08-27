package com.example.kardex.ui.login;

public class Cant {

    int cel;
    int mau;
    int pan;
    int par;
    int por;
    int tec;

    public Cant(String cel, String mau, String pan, String par, String por, String tec) {
        this.cel = Integer.parseInt(cel);
        this.mau = Integer.parseInt(mau);
        this.pan = Integer.parseInt(mau);
        this.par = Integer.parseInt(par);
        this.por = Integer.parseInt(por);
        this.tec = Integer.parseInt(tec);
    }


    public int getCel() {
        return cel;
    }

    public int getMau() {
        return mau;
    }

    public int getPan() {
        return pan;
    }

    public int getPar() {
        return par;
    }

    public int getPor() {
        return por;
    }

    public int getTec() {
        return tec;
    }





}
