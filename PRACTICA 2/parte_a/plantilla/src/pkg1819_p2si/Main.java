/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1819_p2si;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author fidel
 */
public class Main {
    
    public static DBLoader ml;

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        //Carga DB
        ml = new DBLoader();
        ml.loadDBFromPath("./db");
 
        //Control de arg
        if("Adaboost".equals(args[0])){
            switch(args.length){
                case 3: if("-run".equals(args[1])){System.err.println("Error:\nModo de uso:\nPara entrenar Adaboost: Adaboost -train fichero.cf\nPara testear Adaboost: Adaboost -run fichero.cf imagen_prueba");} 
                        else{gymAda(args[2]);}
                break;
                case 4: if("-train".equals(args[1])){System.err.println("Error:\nModo de uso:\nPara entrenar Adaboost: Adaboost -train fichero.cf\nPara testear Adaboost: Adaboost -run fichero.cf imagen_prueba");}
                        else{teStAda(args[2],args[3]);}
                break;
            }
        }
        else{System.err.println("Error:\nModo de uso:\nPara entrenar Adaboost: Adaboost -train fichero.cf\nPara testear Adaboost: Adaboost -run fichero.cf imagen_prueba");}
        
    }
    
    public static int[] sacarY(int categoria){
        int e = eightypcent(categoria);
        int res = e/7;
        int[] Y = new int[e+e];
        Arrays.fill(Y,-1);
        switch(categoria){
            case 0: for(int i=0;i<e;i++) Y[i]=1;
            break;
            case 1: for(int i=res*1;i<e;i++) Y[i]=1;
            break;
            case 2: for(int i=res*2;i<e;i++) Y[i]=1;
            break;
            case 3: for(int i=res*3;i<e;i++) Y[i]=1;
            break;
            case 4: for(int i=res*4;i<e;i++) Y[i]=1;
            break;
            case 5: for(int i=res*5;i<e;i++) Y[i]=1;
            break;
            case 6: for(int i=res*6;i<e;i++) Y[i]=1;
            break;
            case 7: for(int i=res*7;i<e;i++) Y[i]=1;
            break;
        }
        return Y;
    }
    
    public static int eightypcent(int categoria){
        ArrayList d0imgs = ml.getImageDatabaseForDigit(categoria);
        double eightypcent = d0imgs.size()*0.75;
        return (int)eightypcent;
    }
    
    public static ArrayList<Imagen> sacarX(int categoria){
        
        ArrayList<Imagen> X = new ArrayList<>();
        Imagen img;
        
        int eightypcent = eightypcent(categoria);
        double restante = eightypcent/7;
        int total = eightypcent*2;

        for(int i=0;i<8;i++){
            ArrayList<Imagen> d0imgs = ml.getImageDatabaseForDigit(i);
            if(i==categoria){
                for(int j=0;j<(int)eightypcent;j++){
                    img = (Imagen) d0imgs.get(j);
                    X.add(img);
                }
            }
            else{
                for(int k=0;k<(int)restante;k++){
                    img = (Imagen) d0imgs.get(k);
                    X.add(img);
                }
            }
            if(i==7){
                if(total-X.size()>0){
                    int resante = total-X.size();
                    for(int h=d0imgs.size()-1;resante>0;resante--){
                        img = (Imagen) d0imgs.get(h);
                        X.add(img);
                        h--;
                    }
                }
            }
        }
        return X;
    }

    public static void gymAda(String cf){
     
        Adaboost ada = new Adaboost();
        for(int i=0;i<8;i++){
            ArrayList<Imagen> X = sacarX(i);
            int[] Y = sacarY(i);
            ada.Adaboost(X,Y,cf);
        }
        
        //TEST
        Imagen img;
        ArrayList<Imagen> X = new ArrayList<Imagen>();
        Adaboost test = new Adaboost();
        
        
        for(int i=0;i<8;i++){
            int pesto = 0;
            ArrayList<Imagen> d0imgs = ml.getImageDatabaseForDigit(i);
            for(int j=d0imgs.size()-1;pesto<40;j--){
                img = (Imagen) d0imgs.get(j);
                X.add(img);   
                pesto++;
            }
        }

            int pepe = 1;
            int cat = 0;
            int aciertos=0;
            double aciertostotales = 0.0;
            
            for(int s=0;s<X.size()-1;s++){
                double[] tests = test.AdaboostRun(X.get(s),cf);
                if(tests[cat]>0){aciertos++;}
                pepe++;
                
                if(pepe==40){
                    System.out.println(aciertos+"/40");
                    aciertostotales += aciertos;
                    pepe=0;
                    cat++;
                    aciertos=0;
                }
            }
            System.out.println((double)aciertostotales/320+"% de acierto");
            
            
                
                /*for(int e=0;e<tests.length;e++){
                    System.out.print("<"+tests[e]+" ");
                }
                System.out.println(">");*/
            
        
    }
    
    public static void teStAda(String cf, String imge){
       
       Adaboost ada = new Adaboost();
       Imagen img = new Imagen();
        img.loadFromPath(imge);
        byte[] imageData = img.getImageData();
        int[] imagen = new int[784];
        
        for (int i = 0; i < imageData.length; i++){
            imageData[i] = (byte) (255 - imageData[i]);
            imagen[i] = (int)imageData[i]+128;
        }

        double max = (double)-232215.2131;
        int caete = 0;
        double[] salida = new double[8];
        salida = ada.AdaboostRun(imagen,cf);
       
        for(int i=0;i<salida.length;i++)
            System.out.println(salida[i]);
        
        for(int i=0;i<salida.length;i++)
           if(salida[i]>max) {max=salida[i]; caete=i;}
        
        switch(caete){
            case 0 : System.out.println("Adaboost: La imagen es un abrigo");
            break;
            case 1 : System.out.println("Adaboost: La imagen es un bolso");
            break;
            case 2 : System.out.println("Adaboost: La imagen es una camiseta");
            break;
            case 3 : System.out.println("Adaboost: La imagen es un pantalon");
            break;
            case 4 : System.out.println("Adaboost: La imagen es un suéter");
            break;
            case 5 : System.out.println("Adaboost: La imagen es un vestido");
            break;
            case 6 : System.out.println("Adaboost: La imagen es una zapatilla");
            break;
            case 7 : System.out.println("Adaboost: La imagen es un zapato");
            break;
        }
   }
    
}
