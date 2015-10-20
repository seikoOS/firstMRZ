/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package neuro;
/**
 * return
 * W and WT for debug
 * @author NafanyaVictorovna
 */
public class Neuron {
    
    private double[][] X_list;
    private double[] X;                       //one vector of colors in rectangle(part of picture) n*m*3(R,G,B)
    private double[][] W;                     //W matrix for first layer
    private double[][] WT;                    //WT matrix for second layer
    private double[][] new_X_list;
   
    
    private final int N;
    private final int L;
    
    public Neuron(int n, int m, int l){
        N = 3*n*m;
        L = l;
        X_list = new double[L][N];
        new_X_list = new double[L][N];
    }
    
    public void createX(){
        X = new double[N];
    }
    
    public void addToX(double color, int coord){
        X[coord] = color;
    }
    
    public void addToXList(int coord){
        X_list[coord] = X;
    }
    
    public void createMatrix(int p){
        W = new double[p][N];
        WT = new double[N][p];
        for(int i=0; i<N; i++){
            for(int j=0; j<p; j++){
                W[j][i] = WT[i][j] = (Math.random()*(1-(-1))) - 1;
            }
        }
    }
    
    private void normalizeW(int p) {
        for (int j=0; j < p; j++) {
            double s = 0;
            for (int i=0; i < N; i++) {
                s += Math.pow(W[j][i], 2);
            }
            s = Math.sqrt(s);
            for (int i=0; i < N; i++) {
                W[j][i] /= s;
            }
        }
    }
    
    private void normalizeWT(int p) {
        for (int i=0; i < N; i++) {
            double s = 0;
            for (int j=0; j < p; j++) {
                s += Math.pow(WT[i][j], 2);
            }
            s = Math.sqrt(s);
            for (int j=0; j < p; j++) {
                WT[i][j] /= s;
            }
        }
    }
    
    private void YStage(int p, double[] x, double[] y) {
        for (int j=0; j<p; j++) {
            y[j] = 0;  
            for (int i=0; i<N; i++) {
                y[j] += WT[i][j] * x[i];
            }
        }
    }
    
    private void NewXStage(int p, double[] x, double[] y) {
        for (int i=0; i<N; i++) {
            x[i] = 0;   
            for (int j=0; j<p; j++) {
                x[i] += W[j][i] * y[j];
            }
        }
    }
    
//    private double alphaX(double[] newX){
//        double alphaX = 0;
//        for(int i =0; i<N; i++){
//            alphaX += Math.pow(newX[i], 2);
//        }
//        return alphaX = 1./(alphaX); 
//    }
//    
//    private double alphaY(int p, double[] Y){
//        double alphaY = 0;
//        for(int j=0; j<p; j++){
//            alphaY += Math.pow(Y[j],2);
//        }
//        return alphaY = 1./(alphaY);
//    }
    
    public double error(int p, int s){
        double[] Y, newX, gamma;
        double alphaX = 0.0001, alphaY = 0.0001, g, e = 0;
        Y = new double[p]; 
        newX = new double[N];
        gamma = new double[p];
        //Y vector
        this.YStage(p, X_list[s], Y);
        //result X
        this.NewXStage(p, newX, Y);
        //adaptive step for inside layer 
        //this.alphaY(p,Y);
        //adaptive step for result layer
        //this.alphaX(newX);
        //gamma
        for(int j=0; j<p; j++){ 
            g = 0;
            for(int i=0; i<N; i++){
                g += W[j][i] * (newX[i] - X_list[s][i]);
            }
            gamma[j] = g;
        }
        //correct WT, W
        for(int i=0; i<N; i++){
            for(int j=0; j<p; j++){
                WT[i][j] -= alphaX * X_list[s][i] * gamma[j];
                W[j][i] -= alphaY * (newX[i] - X_list[s][i]) * Y[j];
            }
        }        
        this.normalizeW(p);
        this.normalizeWT(p);       
        //calculation E
        for(int i=0; i<N; i++){
            e += Math.pow((newX[i] - X_list[s][i]), 2);
        }
        //write ending X list
        new_X_list[s] = newX;     
        return e / 2;
    }
                    
    public float calcZ(int p){
        return ((N+L)*p+2)/(N*L);
    }
    
    public double[] getNewX(int s){
        return new_X_list[s];
    }
//end of class    
//
//    public double[][][] getW() {
//        return W_list;
//    }
//    
//    public double[][][] getWT(){
//        return WT_list;
//    }
}
