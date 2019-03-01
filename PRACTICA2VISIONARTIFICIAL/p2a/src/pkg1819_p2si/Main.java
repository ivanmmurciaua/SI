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
    public static int entrenamiento = 400;
    public static int tes = 100;

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
    
    /*
    *  Metodos inutiles
    */
    
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
            case 7: for(int i=res*7;i<Y.length;i++) Y[i]=1;
            break;
        }
        return Y;
    }
    
    public static int eightypcent(int categoria){
        ArrayList d0imgs = ml.getImageDatabaseForDigit(categoria);
        double eightypcent = d0imgs.size()*0.8;
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
    
     /*
    *  FIN Metodos inutiles
    */

    public static void gymAda(String cf){
        //Rellenar X con el 80% de imagenes de la BD para el entrenamiento
        ArrayList<Imagen> X = new ArrayList<>();
        Imagen img;
        for(int i=0; i<8;i++){
            ArrayList d0imgs = ml.getImageDatabaseForDigit(i);
            for(int j=0;j<entrenamiento;j++){
                img = (Imagen) d0imgs.get(j);
                X.add(img);
            }
        }

        int[] Y_abrigos = new int[3200];
        Arrays.fill(Y_abrigos,-1);
        for(int i=0;i<400;i++) Y_abrigos[i]=1;
        
        int[] Y_bolsos = new int[3200];
        Arrays.fill(Y_bolsos,-1);
        for(int i=400;i<800;i++) Y_bolsos[i]=1;
        
        int[] Y_camisetas = new int[3200];
        Arrays.fill(Y_camisetas,-1);
        for(int i=800;i<1200;i++) Y_camisetas[i]=1;
        
        int[] Y_pantalones = new int[3200];
        Arrays.fill(Y_pantalones,-1);
        for(int i=1200;i<1600;i++) Y_pantalones[i]=1;
        
        int[] Y_sueters = new int[3200];
        Arrays.fill(Y_sueters,-1);
        for(int i=1600;i<2000;i++) Y_sueters[i]=1;
        
        int[] Y_vestidos = new int[3200];
        Arrays.fill(Y_vestidos,-1);
        for(int i=2000;i<2400;i++) Y_vestidos[i]=1;
        
        int[] Y_zapatillas = new int[3200];
        Arrays.fill(Y_zapatillas,-1);
        for(int i=2400;i<2800;i++) Y_zapatillas[i]=1;
        
        int[] Y_zapatos = new int[3200];
        Arrays.fill(Y_zapatos,-1);
        for(int i=2800;i<3200;i++) Y_zapatos[i]=1;
     
        Adaboost ada = new Adaboost();
        
        ada.Adaboost(X, Y_abrigos,cf);
        ada.Adaboost(X, Y_bolsos,cf);
        ada.Adaboost(X, Y_camisetas,cf);
        ada.Adaboost(X, Y_pantalones,cf);
        ada.Adaboost(X, Y_sueters,cf);
        ada.Adaboost(X, Y_vestidos,cf);
        ada.Adaboost(X, Y_zapatillas,cf);
        ada.Adaboost(X, Y_zapatos,cf);
        
        
        //for(int i=0;i<8;i++){
            //ArrayList<Imagen> X = sacarX(i);
            //int[] Y = sacarY(i);
            //ada.Adaboost(X,,cf);
        //}
        
        //TEST
        ArrayList<Imagen> p = new ArrayList<Imagen>();
        Adaboost test = new Adaboost();
 
        for(int i=0;i<8;i++){
            int con = 0;
            ArrayList<Imagen> d0imgs = ml.getImageDatabaseForDigit(i);
            for(int j=d0imgs.size()-1;con<Main.tes;j--){
                img = (Imagen) d0imgs.get(j);
                p.add(img);   
                con++;
            }
        }

            int control = 1;
            int cat = 0;
            int aciertos=0;
            
            for(int s=0;s<p.size()-1;s++){
                double[] tests = test.AdaboostRun(p.get(s),cf);
                if(tests[cat]>0){aciertos++;}
                control++;
                
                if(control==Main.tes){
                    System.out.println(aciertos+"/"+Main.tes);
                    System.out.println((double)aciertos/Main.tes+"% de acierto de la categoria "+cat);
                    control=0;
                    cat++;
                    aciertos=0;
                }
            }
            
    }
    
    
    /*
    *  Test para Adaboost   
    */
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
            System.out.println("Categoria"+i+": "+salida[i]);
        
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