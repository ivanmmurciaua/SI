/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1819_p2si;

import java.util.ArrayList;
import java.util.Arrays;
import static pkg1819_p2si.Adaboost.*;

/**
 *
 * @author fidel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
        
        switch(args.length){
            case 3 : if("-train".equals(args[0])){System.err.println("El comando -train debe tener 1 argumento solo");} else if("-run".equals(args[0])){ System.out.println("Analizando");}
            break;
            case 2 : if("-train".equals(args[0])){ System.out.println("Entrenando");} else if("-run".equals(args[0])){System.err.println("El comando -run debe tener 2 argumentos");}
            break;
            default : System.err.println("Error:\nModo de uso:\nPara entrenar Adaboost: Adaboost -train fichero.cf\nPara testear Adaboost: Adaboost -run fichero.cf imagen_prueba");
        }
            
    */
        
        //int[] Y = new int[3200];  //0->3199
        //int[] Y_abrigos = new int[3200];
        //int[] Y_bolsos = new int[3200];
        //int[] Y_camisetas = new int[3200];
        //int[] Y_pantalones = new int[3200];
        //int[] Y_sueters = new int[3200];
        //int[] Y_vestidos = new int[3200];
        //int[] Y_zapatillas = new int[3200];
        //int[] Y_zapatos = new int[3200];
        //Arrays.fill(Y,-1);
        Adaboost p1 = new Adaboost();
        p1.Adaboost();
        
        //Abrigos
        //for(int i=0;i<401;i++) Y[i]=1;
        //Bolsos
        //for(int i=400;i<801;i++) Y[i]=1;
        //Camisetas
        //for(int i=800;i<1201;i++) Y[i]=1;
        //Pantalones
        //for(int i=1200;i<1601;i++) Y[i]=1;
        //Sueters
        //for(int i=1600;i<2001;i++) Y[i]=1;
        //Vestidos
        //for(int i=2000;i<2401;i++) Y[i]=1;
        //Zapatillas
        //for(int i=2400;i<2801;i++) Y[i]=1;
        //Zapatos
        //for(int i=2800;i<3201;i++) Y[i]=1;
        
        
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
    
}
