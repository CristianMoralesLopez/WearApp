package com.example.myapplication;

import java.util.ArrayList;

public class Rutina {

    private ArrayList <Integer> valoresPulso;
    private String horaInicio;
    private String horaFin;
    private String duracion;

    public Rutina(ArrayList<Integer> valoresPulso, String horaInicio, String horaFin, String duracion) {
        this.valoresPulso = valoresPulso;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.duracion = duracion;
    }
    public Rutina (){

    }

    public ArrayList<Integer> getValoresPulso() {
        return valoresPulso;
    }

    public void setValoresPulso(ArrayList<Integer> valoresPulso) {
        this.valoresPulso = valoresPulso;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
