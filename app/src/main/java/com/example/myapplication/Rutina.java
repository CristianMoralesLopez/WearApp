package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

public class Rutina implements Serializable {

    private ArrayList <Integer> valoresPulso;
    private ArrayList <Integer> valoresPulso2;


    private String horaInicio;
    private String horaFin;
    private String duracion;
    private String horaInicio1;
    private String horaFin1;
    private String pasos;
    private String calorias;
    private String promedioPulso;
    private String menorPulso;
    private String mayorPulso;
    private String modaPulso;




    public Rutina(ArrayList<Integer> valoresPulso, ArrayList<Integer> valoresPulso2, String horaInicio, String horaFin, String duracion) {
        this.valoresPulso = valoresPulso;
        this.valoresPulso2 = valoresPulso2;
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

    public ArrayList<Integer> getValoresPulso2() {
        return valoresPulso2;
    }

    public void setValoresPulso2(ArrayList<Integer> valoresPulso2) {
        this.valoresPulso2 = valoresPulso2;
    }

    public String getHoraInicio1() {
        return horaInicio1;
    }

    public void setHoraInicio1(String horaInicio1) {
        this.horaInicio1 = horaInicio1;
    }

    public String getHoraFin1() {
        return horaFin1;
    }

    public void setHoraFin1(String horaFin1) {
        this.horaFin1 = horaFin1;
    }


    public String getPasos() {
        return pasos;
    }

    public void setPasos(String pasos) {
        this.pasos = pasos;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getPromedioPulso() {
        return promedioPulso;
    }

    public void setPromedioPulso(String promedioPulso) {
        this.promedioPulso = promedioPulso;
    }

    public String getMenorPulso() {
        return menorPulso;
    }

    public void setMenorPulso(String menorPulso) {
        this.menorPulso = menorPulso;
    }

    public String getMayorPulso() {
        return mayorPulso;
    }

    public void setMayorPulso(String mayorPulso) {
        this.mayorPulso = mayorPulso;
    }

    public String getModaPulso() {
        return modaPulso;
    }

    public void setModaPulso(String modaPulso) {
        this.modaPulso = modaPulso;
    }
}
