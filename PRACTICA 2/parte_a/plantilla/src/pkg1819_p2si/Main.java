/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg1819_p2si;

import java.util.ArrayList;

/**
 *
 * @author fidel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Cargador de la BD de SI
        DBLoader ml = new DBLoader();
        ml.loadDBFromPath("./db");
        
        //Accedo a las imagenes de bolsos
        ArrayList d0imgs = ml.getImageDatabaseForDigit(1);
        
        //Y cojo el decimo bolso de la bd
        Imagen img = (Imagen) d0imgs.get(9);
        
        //La invierto para ilustrar como acceder a los pixels y imprimo los pixeles
        //en hexadecimal
        System.out.print("Image pixels: ");
        byte imageData[] = img.getImageData();
        for (int i = 0; i < imageData.length; i++)
        {
            imageData[i] = (byte) (255 - imageData[i]);
            System.out.format("%02X ", imageData[i]);
        }
        
        //Muestro la imagen invertida
        MostrarImagen imgShow = new MostrarImagen();
        imgShow.setImage(img);
        imgShow.mostrar();

        
    }
    
}
