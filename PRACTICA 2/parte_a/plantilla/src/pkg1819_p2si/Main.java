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
    
    private static final int training = 400;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DBLoader ml = new DBLoader();
        ml.loadDBFromPath("./db");
        
        ArrayList d0imgs = ml.getImageDatabaseForDigit(0);
        
        //Y cojo el decimo bolso de la bd
        Imagen img = (Imagen) d0imgs.get(0);
        System.out.println(img);
        
        //La invierto para ilustrar como acceder a los pixels y imprimo los pixeles
        //en hexadecimal
        System.out.print("Image pixels: ");
        //int max = -255;
        //int min = 255;
        byte imageData[] = img.getImageData();
        for (int i = 0; i < imageData.length; i++){
            imageData[i] = (byte) (255 - imageData[i]);
            //if(imageData[i]+128 < min) min = imageData[i]+128;
            //if(imageData[i]+128 > max) max = imageData[i]+128;
            System.out.print(imageData[i]+128+" ");
        }
        
        //System.out.println("JAP");
        //System.out.println(min);
        //System.out.println(max);

        //Muestro la imagen invertida
        MostrarImagen imgShow = new MostrarImagen();
        imgShow.setImage(img);
        imgShow.mostrar();
        
        
        vamosAlGymAda();
        
        /*
        
        switch(args.length){
            case 3 : if("-train".equals(args[0])){System.err.println("El comando -train debe tener 1 argumento solo");} else if("-run".equals(args[0])){ System.out.println("Analizando");}
            break;
            case 2 : if("-train".equals(args[0])){ System.out.println("Entrenando");} else if("-run".equals(args[0])){System.err.println("El comando -run debe tener 2 argumentos");}
            break;
            default : System.err.println("Error:\nModo de uso:\nPara entrenar Adaboost: Adaboost -train fichero.cf\nPara testear Adaboost: Adaboost -run fichero.cf imagen_prueba");
        }
            
    */
        
        //Adaboost(X,Y);   
        
        //Cargador de la BD de SI
        //DBLoader ml = new DBLoader();
        //ml.loadDBFromPath("./db");
        
        //Accedo a las imagenes de bolsos
        //ArrayList d0imgs = ml.getImageDatabaseForDigit(5);
        
        //Y cojo el decimo bolso de la bd
        //Imagen img = (Imagen) d0imgs.get(5);
        //System.out.println(img);
        
        //La invierto para ilustrar como acceder a los pixels y imprimo los pixeles
        //en hexadecimal
        //System.out.print("Image pixels: ");
        //int max = -255;
        //int min = 255;
        //byte imageData[] = img.getImageData();
        //for (int i = 0; i < imageData.length; i++){
        //    imageData[i] = (byte) (255 - imageData[i]);
        //    if(imageData[i]+128 < min) min = imageData[i]+128;
        //    if(imageData[i]+128 > max) max = imageData[i]+128;
        //    System.out.print(imageData[i]+128+" ");
        //}
        
        //System.out.println("JAP");
        //System.out.println(min);
        //System.out.println(max);

        //Muestro la imagen invertida
        //MostrarImagen imgShow = new MostrarImagen();
        //imgShow.setImage(img);
        //imgShow.mostrar();

        
    }
    
    
    public static void vamosAlGymAda(){
        
        //Cargador de la BD de SI
        DBLoader ml = new DBLoader();
        ml.loadDBFromPath("./db");
        
        //Rellenar X con el 80% de imagenes de la BD para el entrenamiento
        ArrayList<Imagen> X = new ArrayList<>();
        Imagen img;
        for(int i=0; i<8;i++){
            ArrayList d0imgs = ml.getImageDatabaseForDigit(i);
            for(int j=0;j<training;j++){
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
        
        
        //int counter = 1;
        //for(int i=0;i<Y_abrigos.length;i++){
        //    if(counter == 50) {System.out.println(Y_bolsos[i]+" "); counter=1;}
        //    else System.out.print(Y_bolsos[i]+" ");
        //    counter++;
        //}
        
        Adaboost abrigos = new Adaboost();
        abrigos.Adaboost(X, Y_abrigos);
    }
    
}
