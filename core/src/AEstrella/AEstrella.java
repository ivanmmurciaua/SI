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
    private class Nodo{
    
        //f(n)
        protected float f;
        //Total del peso hasta el momento
        protected  int g;
        //f(h) - Heurística
        private float h;
        //Coordenada x
        private int x;
        //Coordenada y
        private int y;
        //Coordenada z
        private int z;
        //The padre
        protected Nodo padre;
        
        //Constructor por defecto de la clase Nodo
        Nodo(){
            this.f = 0;
            this.g = 0;
            this.h = 0;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.padre = null;
        }
        
        //Constructor con datos
        Nodo(Coordenada c, int f, int g, int h){
            this.g = g;
            this.h = h;
            this.f = this.g + this.h;
            this.x = c.getX() - (c.getY() + (c.getY()&1)) / 2;
            this.z = c.getY();
            this.y = -this.x-this.z;
            this.padre = null;
        }
         
        //Constructor de copia de la clase nodo
        Nodo(Nodo n){
            this.f = n.f;
            this.g = n.g;
            this.h = n.h;
            this.x = n.x;
            this.y = n.y;
            this.z = n.z;
            this.padre = n.padre;
        }
        
        //Constructor para las vecinas, constructor de Nodo con coordenadas
        Nodo(int x, int y, int z){
            this.f = 0;
            this.g = 0;
            this.h = 0;
            this.x = x;
            this.y = y;
            this.z = z;
            this.padre = new Nodo();
        }

        //Sacamos las vecinas
        ArrayList misVecinitas(){
            ArrayList<Nodo> vecinas;
            vecinas = new ArrayList<Nodo>();
            int vecinass[][] = new int[][]{
                {1,-1,0},
                {1,0,-1},
                {0,1,-1},
                {-1,1,0},
                {-1,0,1},
                {0,-1,1}
            };
            
            for(int[] vecinas1 : vecinass) 
                vecinas.add(new Nodo(this.x + vecinas1[0], this.y + vecinas1[1], this.z + vecinas1[2]));
            return vecinas;
        }
        
        //Obtenemos el nodo con menor f y a su vez, menor h
        public Nodo obtenerMenorf(ArrayList<Nodo> lf){
            Nodo nod;
            if(lf.size() == 1){
                nod = lf.get(0);
            }
            else{
                nod = lf.get(0);
                for(int i=1;i<lf.size();i++){
                    if(lf.get(i).f <= nod.f){
                        if(lf.get(i).h < nod.h){
                            nod = lf.get(i);
                        }
                    }
                }
            }
            return nod;
        }
        
        //Metodo que nos devuelve si un nodo está en una lista pasada por parametro
        public boolean estaEn(ArrayList<Nodo> l){
            for(int i=0;i<l.size();i++){
                if(l.get(i).equals(this)){
                    return true;
                }
            }
            return false;
        }
        
        /* HEURISTICAS */
        
        //Heuristica relacionada con las coordenadas cúbicas
        public void setH(Nodo b){
            this.h = max (abs(this.x - b.x), max(abs(this.y - b.y), abs(this.z - b.z)));        
        }
        
        //Heuristica 0
        public void setH0(Nodo b){
            this.h = 0;
        }
        
        //Heuristica Euclídea con coordenadas cartesianas
        public void setHEuclidea(Nodo b){
           this.h = (float) sqrt(pow((b.deCubicaAOffset().getX()-this.deCubicaAOffset().getX()),2)+(pow((b.deCubicaAOffset().getY()-this.deCubicaAOffset().getY()),2)) );
        }
        
        //Heuristica Manhattan con coordenadas cartesianas
        public void setHManhattan(Nodo b){
            this.h = abs((b.deCubicaAOffset().getX()-this.deCubicaAOffset().getX())+(b.deCubicaAOffset().getY()-this.deCubicaAOffset().getY()));
        }
        
        //Getter del atributo h
        public float getH(){
            return this.h;
        }
        
        //Getter del atributo f
        public float getF(){
            return this.f;
        }
        
        //Setter del nodo padre
        public void setFather(Nodo n){
            this.padre = n;
        }
        
        //Setter del atributo f
        public void setF(){
            this.f = this.g + this.h;
        }
        
        //Getter del padre
        public Nodo getFather(){
            return this.padre;
        }
        
        //Setter del peso del camino
        public int setG(Nodo a){
            int res = 0;
            switch(mundo.getCelda(this.deCubicaAOffset().getX(), this.deCubicaAOffset().getY())){
                case 'c' : res = a.g + 1;
                         break;
                case 'h' : res = a.g + 2;
                         break;
                case 'a' : res = a.g + 3;
                         break;
            }
            return res;
        }
        
        //Metodo para transformar un nodo de coordenadas cubicas a cartesianas
        public Coordenada deCubicaAOffset(){
            Coordenada c = new Coordenada();
            c.x = this.x + (this.z + (this.z & 1)) / 2;
            c.y = this.z;
            return c;
        }

        //Equals de la clase Nodo
        public boolean equals(Nodo b){
            return (this.x == b.x && this.y == b.y && this.z == b.z);
        }
        
        //toString de la clase Nodo
        @Override
        public String toString(){
            String res;
            res = "COORDENADAS: ["+this.deCubicaAOffset().getY()+","+this.deCubicaAOffset().getX()+"] ;CUBICAS: X= " + this.x + "; Z= " + this.z + "; Y= " +this.y + "; F = "+this.f+"; G= "+this.g+"; H= "+this.h+"; FATHER = "+this.padre;
            return res;
        }
        
    }
    
    //Metodo para obviar las vecinas no transitables
    public ArrayList<Nodo> eligiendoVecinas(ArrayList<Nodo> vec, ArrayList<Nodo> li){
        ArrayList<Nodo> elegidas = new ArrayList<Nodo>();
        for(int i=0;i<vec.size();i++){
            if(!vec.get(i).estaEn(li)){
                if(this.mundo.getCelda(vec.get(i).deCubicaAOffset().getX(), vec.get(i).deCubicaAOffset().getY()) != 'b' && this.mundo.getCelda(vec.get(i).deCubicaAOffset().getX(), vec.get(i).deCubicaAOffset().getY()) != 'p'){
                   elegidas.add(vec.get(i));
                }
            }
        }
        return elegidas;
    }
     
    //Calcula el A*
    public int CalcularAEstrella(){

        boolean encontrado = false;
        int result = -1;
       
        //AQUÍ ES DONDE SE DEBE IMPLEMENTAR A*
        Nodo n;
        Nodo end;
        
        int g = 0;
        int h = 0;
        
        end = new Nodo(this.mundo.getDragon(),0,g,h);
        n = new Nodo(this.mundo.getCaballero(),0,g,h);
        //Asignamos la heurística
        n.setH(end);
        
        ArrayList<Nodo> listaInterior = new ArrayList<Nodo>();
        ArrayList<Nodo> listaFrontera = new ArrayList<Nodo>();
        listaFrontera.add(n);
        
        
        
        while(!listaFrontera.isEmpty()){
            /*System.out.println("LISTA INTERIOR");
            for(int i=0;i<listaInterior.size();i++){
                System.out.println(listaInterior.get(i));
            }
            System.out.println("LISTA FRONTERA");
            for(int i=0;i<listaFrontera.size();i++){
                System.out.println(listaFrontera.get(i));
            }*/
            n = n.obtenerMenorf(listaFrontera);
            
            if(n.equals(end)){
                encontrado = true;
                break;
            }
            else{ //b , p
                listaFrontera.remove(n);
                listaInterior.add(n);
                ArrayList<Nodo> vecinas = n.misVecinitas();
                vecinas = eligiendoVecinas(vecinas,listaInterior);
                
                for(int i=0;i<vecinas.size();i++){
                    g = vecinas.get(i).setG(n);
                    
                    if(!vecinas.get(i).estaEn(listaFrontera)){
                        vecinas.get(i).setH(end);
                        vecinas.get(i).g = g;
                        vecinas.get(i).setF();
                        vecinas.get(i).setFather(n);
                        listaFrontera.add(vecinas.get(i));
                    }
                    else{
                        if(g < vecinas.get(i).g){
                            vecinas.get(i).setFather(n);
                            vecinas.get(i).g = g;
                            vecinas.get(i).setF();
                        }
                    }
                }
            }
        }
        
        
        
        //Si ha encontrado la solución, es decir, el camino, muestra las matrices camino y camino_expandidos y el número de nodos expandidos
        if(encontrado){
            result = 1;
            for(int i=0;i<listaInterior.size();i++){
                this.camino_expandido[listaInterior.get(i).deCubicaAOffset().getY()][listaInterior.get(i).deCubicaAOffset().getX()] = i;
                this.expandidos = i;
            }
            
            Nodo solucion = n;
            this.coste_total = solucion.padre.f;
            while(solucion != null){
                this.camino[solucion.deCubicaAOffset().getY()][solucion.deCubicaAOffset().getX()] = 'X';    
                solucion = solucion.padre;
            }
            
            System.out.println("Caballero está en: "+mundo.getCaballero().getY()+","+mundo.getCaballero().getX()+"\n En coordenadas cúbicas: "+n.toString());     
            System.out.println("El dragón se encuentra en: "+mundo.getDragon().getY()+","+mundo.getDragon().getX()+"\n En coordenadas cubicas: "+end.toString());
  
            //Mostrar las soluciones
            System.out.println("Camino");
            mostrarCamino();

            System.out.println("Camino explorado");
            mostrarCaminoExpandido();
            
            System.out.println("Nodos expandidos: "+expandidos);
            System.out.println(this.coste_total);
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


