/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1819_p2si;

import java.io.*;

/**
 * 
 * @author Ivan
 */
public class Adaboost {
    
    final static int CDaUsar = 400;
    final static int PruAle = 500;
    
    
    class CD{
        
        private int pixel;
        private int umbral;
        private int direccion;
        private float alfa;
        
        CD(){
            this.pixel = (int)(Math.random()*784);
            this.umbral = (int)(Math.random()*255);
            this.direccion = (int)(Math.random()*784);
            
            if(this.direccion % 2 == 0){
                this.direccion = 1;
            }
            else{
                this.direccion = -1;
            }
            
            this.alfa = 0;
        }
        
        @Override
        public String toString(){
            return this.pixel+" "+this.umbral+" "+this.direccion+" "+this.alfa;
        }
        
        public void escribirEnFichero(CD cf, boolean switcheo, String nombrefichero){
            
            //PRIMER FICHERO
            if(switcheo){
                FileWriter fichero = null;
                try{
                    fichero = new FileWriter(nombrefichero);
                    PrintWriter pw = new PrintWriter(fichero);
                    pw.println(cf);
                }
                catch (IOException e) {} 
                finally {
                   try{
                   if (null != fichero)
                      fichero.close();
                   }
                   catch (IOException e2) {}
                }
            }
            //CONTINUAMOS ESCRIBIENDO EN EL FICHERO
            else{
                try(FileWriter fw = new FileWriter(nombrefichero, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)){
                    out.println(cf);
                }
                catch (IOException e) {}
            }
        }
        
        public void leerDeFichero(String cf){
            //-RUN  (LECTURA FICHERO)
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

            try {
               archivo = new File (cf);
               fr = new FileReader (archivo);
               br = new BufferedReader(fr);

               String linea;
               while((linea=br.readLine())!=null)
                  System.out.println(linea);
            }
            catch(IOException e){}
            finally{
               try{                    
                  if( null != fr ){   
                     fr.close();     
                  }                  
               }catch (IOException e2){}
            }
        }

    }
    
    
    public void Adaboost(){
        
        CD primero = new CD();
        
        
        
    }
    /*
    ** 
    
       BD -> 80%  TRAIN
          -> 20%  TEST
    
       CD
       +-------------------------+
       | Píxel     ->  0-784     |
       | Umbral    ->  0-255     |
       | Dirección -> +1 o -1    |
       | alfa      -> formula    |
       +-------------------------+
    
       D -> Vector de pesos (Inicializa a 1/N -> N=X.length)
       T -> Numero de CD a usar
       
       para i=1 hasta A-> Numero de pruebas aleatorias
            CD = Plano al azar;
            (El vector Err se supone que lo pasamos nosotros)
            actualizamos su Et haciendo un sumatorio de D * Err
            guardamos el clasificador debil con menos Et
       finpara
       
       obtener
    
    
    
    */
    
    
    
    
}
