/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AEstrella;
import static java.lang.Math.*;
import java.util.*;


/**
 *
 * @author mirse
 */
public class AEstrella {
 
    //Mundo sobre el que se debe calcular A*
    Mundo mundo;
    
    //Camino
    public char camino[][];
    
    //Casillas expandidas
    int camino_expandido[][];
    
    //Número de nodos expandidos
    int expandidos;
    
    //Coste del camino
    float coste_total;
    
    public AEstrella(){
        expandidos = 0;
        mundo = new Mundo();
    }
    
    public AEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    //COLUMNA=X FILA=Y
    class Nodo{
            
        //f(n)
        int f;
        //Total del peso hasta el momento
        int g;
        //f(h) - Heurística
        int h;
        //Coordenada x
        int x;
        //Coordenada y
        int y;
        //Coordenda z
        int z;
        
        Nodo padre;
        
        Nodo(int x, int y, int z){
            this.f = 0;
            this.g = 0;
            this.h = 0;
            this.x = x;
            this.y = y;
            this.z = z;
            this.padre = new Nodo();
        }
        
        Nodo(){
            this.f = 0;
            this.g = 0;
            this.h = 0;
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        
        Nodo transformarACubicas(Coordenada c){
            this.x = c.getX() - (c.getY() + (c.getY()&1)) / 2;
            this.z = c.getY();
            this.y = -this.x-this.z;
            return this;
        }
        
        
        //Cube(+1, -1, 0), Cube(+1, 0, -1), Cube(0, +1, -1), 
        //Cube(-1, +1, 0), Cube(-1, 0, +1), Cube(0, -1, +1),
        ArrayList misVecinitas(){
            //List<Nodo> listavecinas = Arrays.asList(new Nodo(1,-1,0),new Nodo(1,0,-1),new Nodo(0,1,-1),new Nodo(-1,1,0),new Nodo(-1,0,1),new Nodo(0,-1,1));
            int vecinass[][] = new int[][]{
                {1,-1,0},
                {1,0,-1},
                {0,1,-1},
                {-1,1,0},
                {-1,0,1},
                {0,-1,1}
            };
            
            ArrayList<Nodo> vecinas;
            vecinas = new ArrayList<Nodo>();
            Nodo vecina = new Nodo();
            
            for (int i=0;i<vecinass.length;i++){
                vecinas.add(new Nodo(this.x+vecinass[i][0],this.y+vecinass[i][1],this.z+vecinass[i][2]));
            }
            
            return vecinas;
        }
        
        @Override
        public String toString(){
            String res;
            res = "X= " + this.x + "; Z= " + this.z + "; Y= " +this.y ;
            return res;
        }
        
    }
     
    public double heuristicaDeCubos(Nodo a, Nodo b){
            return (abs(a.x - b.x) + abs(a.y - b.y) + abs(a.z - b.z)) / 2;
    }
    
    public Coordenada deCubicaAOffset(Nodo a){
        Coordenada c = new Coordenada();
        c.x = a.x + (a.z + (a.z & 1)) / 2;
        c.y = a.z;
        return c;
    }
    
    //Calcula el A*
    public int CalcularAEstrella(){

        boolean encontrado = true;
        int result = 1;
        
        //AQUÍ ES DONDE SE DEBE IMPLEMENTAR A*
        Coordenada cab = mundo.getCaballero();
        Coordenada drg = mundo.getDragon();
        Nodo ini = new Nodo();
        Nodo end = new Nodo();
        //this.camino[cab.y][cab.x] = 'X';
        //this.camino_expandido[cab.y][cab.x] = 1;
        //this.camino[drg.y][drg.x] = 'D';
        
        /*ArrayList<Nodo> listaInterior = new ArrayList<Nodo>();
        ArrayList<Nodo> listaFrontera = new ArrayList<Nodo>();
        listaFrontera.add(ini.transformarACubicas(cab));
        while(!listaFrontera.isEmpty()){
            
        }*/
        
        

        //Si ha encontrado la solución, es decir, el camino, muestra las matrices camino y camino_expandidos y el número de nodos expandidos
        if(encontrado){
            System.out.println("Caballero está en: "+cab.getY()+","+cab.getX()+"\n En coordenadas cúbicas: "+ini.transformarACubicas(cab).toString());
            System.out.println("Los vecinos del caballero son: ");
            ArrayList<Nodo> vecinas = ini.misVecinitas();
            for(int i=0;i<vecinas.size();i++){
                System.out.println(vecinas.get(i));
            }
            for(int i=0;i<vecinas.size();i++){
                System.out.println(deCubicaAOffset(vecinas.get(i)).getY()+","+deCubicaAOffset(vecinas.get(i)).getX()+"-> "+mundo.getCelda(deCubicaAOffset(vecinas.get(i)).getX(), deCubicaAOffset(vecinas.get(i)).getY()));
            }
            
            System.out.println("El dragón se encuentra en: "+drg.getY()+","+drg.getX()+"\n En coordenadas cubicas: "+end.transformarACubicas(drg).toString());
            System.out.println("f(h): "+heuristicaDeCubos(ini,end));
            
            
            
            
            //Mostrar las soluciones
            System.out.println("Camino");
            mostrarCamino();

            System.out.println("Camino explorado");
            mostrarCaminoExpandido();
            
            System.out.println("Nodos expandidos: "+expandidos);
        }

        return result;
    }
   
    //Muestra la matriz que contendrá el camino después de calcular A*
    public void mostrarCamino(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                System.out.print(camino[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    //Muestra la matriz que contendrá el orden de los nodos expandidos después de calcular A*
    public void mostrarCaminoExpandido(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                    System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                if(camino_expandido[i][j]>-1 && camino_expandido[i][j]<10)
                    System.out.print(" ");
                System.out.print(camino_expandido[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    public void reiniciarAEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    public float getCosteTotal(){
        return coste_total;
    }
}


