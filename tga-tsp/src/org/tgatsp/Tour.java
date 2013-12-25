package org.tgatsp;


import java.util.*;

public class Tour implements Comparable<Tour>
{
	private final Cliente[] tour;
	private int length;
	private float fitness;
	private float window;
	private boolean killingFlag;
	
	public Tour (int size)
	{
		this.tour = new Cliente[size];
		//while(tour.size() < size) tour.add(null);
		length=-1;
		fitness=-1;
	}
	
	public Tour (Cliente[] t, int length)
	{
		this.tour=t;
		this.length=length;
		this.fitness=1/(float)length;
	}
	
	/*public Tour (Tour t)
	{
		this.tour=new ArrayList<Cliente>(t.getTour());
		this.length=t.length;
	}*/
	
	public int compareTo(Tour o)
	{
		if(this.length!=-1 && o.length!=-1)
			return this.length-o.length;
		else if(this.length!=-1)
			return this.length-o.getlength();
		else if(o.length!=-1)
			return this.getlength()-o.length;
		return this.getlength()-o.getlength();
	}
	
	/*public void addCliente(Cliente c)
	{
		if(!tour.contains(c))
		{
			tour.add(c);
		}
		else
		{
			throw new RuntimeException("Customer already present in tour");
		}
	}*/
	
	/*public void addCliente(int i, Cliente c)
	{
		if(!tour.contains(c))
		{
			tour.set(i,c);
		}
		else
		{
			throw new RuntimeException("Customer already present in tour");
		}
	}*/
	
	/*public void setCliente(int i, Cliente c)
	{
		tour.set(i,c);
	}
	
	public void insertCliente(int i, Cliente c)
	{
		tour.add(i,c);
	}
	
	public Cliente removeCliente(int i)
	{
		return tour.remove(i);
	}
	
	public Cliente getCliente(int i)
	{
		return tour.get(i);
	}*/
	
	private void calculatelength()
	{
		int temp =0;
		int i;
		for(i=1; i<tour.length; i++)
		{
			temp+=tour[i].calculateDistance(tour[i-1]);    // Distance between i and i-1
		}
		temp+=tour[i-1].calculateDistance(tour[0]); //Distanza tra l'ultimo ed il primo
		length= temp;	
	}
	
	public int getlength()
	{
		if (length!=-1)
		{
			return length;
		}
		else
		{
			this.calculatelength();
			return length;
		}
	}
	
	public float getFitness()
	{
		if(fitness!=-1)
			return fitness;
		else if(length!=-1)
		{
			fitness=1/(float)length;
			return fitness;
		}
		else
		{
			calculatelength();
			fitness=1/(float)length;
			return fitness;
		}
	}
	
	public void setWindow(Float window)
	{
		this.window=window;
	}
	
	public Float getWindow()
	{
		return window;
	}
	
	public void setKillingFlag(boolean flag)
	{
		this.killingFlag=flag;
	}
	
	public boolean getKillingFlag()
	{
		return killingFlag;
	}
	
	public Cliente[] getTour()
	{
		return tour;
	}
	
	public void markToRecalculate()
	{
		this.length = -1;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this==obj || this.tour.equals(obj))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
	    return 0;
	}
	
	public void mutate(Random rand)
	{
		
		int customerToDisplace = rand.nextInt(this.tour.length);
		int insertionPosition;
		do
		{
			insertionPosition = rand.nextInt(this.tour.length);
			
		} while (insertionPosition == customerToDisplace);
		if(insertionPosition>customerToDisplace)
		{
			Cliente temp = tour[customerToDisplace];
			for(int i=customerToDisplace; i<insertionPosition; i++)
			{
				tour[i]=tour[i+1];
			}
			tour[insertionPosition]=temp;
		}
		else
		{
			Cliente temp = tour[customerToDisplace];
			for(int i=customerToDisplace; i>insertionPosition; i--)
			{
				tour[i]=tour[i-1];
			}
			tour[insertionPosition]=temp;
		}
		TGA.mutationCount++;
		//System.out.println(chromosome);
		//Cliente c = this.removeCliente(customerToDisplace);
		//this.insertCliente(insertionPosition, c);
		//System.out.println(chromosome);
	}
	
	public int getSize()
	{
		return tour.length;
	}
	
	public String toString()
	{
		return ""+length;
	}
	
	public static void localSearch(Cliente[] t)
	{
		int dim = t.length;
	//	int rand = r.nextInt(dim/10);
		for(int i = 0; i < dim - 1; i++)
		{
			for(int k = i + 2; k < dim; k++)
			{
				Tour.twoOpt(t, i, k,dim);
			}
		}
		
	}
	
/*	 public static void twoOpt(Tour t, int inf, int sup)
     {

             Integer len_prec = t.getlength();
             Tour temporaneo = new Tour(t.getSize());
             //int counter = 0;
             
         //    System.out.println(t);
                     for(int i =0; i<inf; i++)
                     {
                             temporaneo.setCliente(i, t.getCliente(i));
                     }
             
                     for(int j=0; j<sup-inf; j++)
                             {
                             temporaneo.setCliente(j+inf, t.getCliente(sup-j-1));
                             }
             
                     for(int k=sup; k<t.getSize(); k++)
                     {
                             temporaneo.setCliente(k, t.getCliente(k));
                     }
                     //counter++;
        //     System.out.println(temporaneo);
             
             if(temporaneo.getlength() < len_prec)
             {
                     for(int k = inf; k<sup; k++)
                     {
                             t.setCliente(k,temporaneo.getCliente(k));
                     }
                     t.markToRecalculate();
                     //twopt.twoOptc(t, r);
             }
                             
     }*/
		
	 
	 /*2-opt sbagliato, ma migliore!!!*/
	public static boolean twoOpt(Cliente[] t, int inf, int sup, int dim)
	{

		Integer len_prev_edges = t[inf].calculateDistance(t[(inf-1+dim)%dim])+t[sup].calculateDistance(t[(sup-1+dim)%dim]);
		Integer len_new_edges;
		Cliente[] temporaneo = new Cliente[sup-inf+2];
		//int counter = 0;
		
		
		//	System.out.println(t);
			temporaneo[0]=t[(inf-1+dim)%dim];
		
			for(int j=0; j<sup-inf; j++)
				{
					temporaneo[j+1]= t[sup-j-1];
				}
		
			temporaneo[sup-inf+1]=t[sup];
			//counter++;
		//	for(Cliente c : temporaneo)
		//	System.out.println(c);
			
		len_new_edges= temporaneo[0].calculateDistance(temporaneo[1])+temporaneo[sup-inf+1].calculateDistance(temporaneo[sup-inf]);
		
		if(len_new_edges<len_prev_edges)
		{
			
			for(int k = inf; k<=sup; k++)
			{
				t[k]=temporaneo[k-inf+1];
			}
			return true;
			//t.markToRecalculate();
			//twopt.twoOptc(t, r);
		}
		return false;
				
	}
	
	
	public static boolean twoOptbool(Cliente[] t, int inf, int sup, int dim)
	{

		int len_prev_edges = t[inf].calculateDistance(t[(inf-1+dim)%dim])+t[sup].calculateDistance(t[(sup-1+dim)%dim]);
		int len_new_edges= t[(inf-1+dim)%dim].calculateDistance(t[(sup-1+dim)%dim])+t[sup].calculateDistance(t[inf]);;
		
		if(len_new_edges<len_prev_edges)
		{
			Cliente[] temporaneo = new Cliente[sup-inf+2];
			//int counter = 0;
			
			
			//	System.out.println(t);
			temporaneo[0]=t[(inf-1+dim)%dim];
			
			for(int j=0; j<sup-inf; j++)
			{
				temporaneo[j+1]= t[sup-j-1];
			}
			
			temporaneo[sup-inf+1]=t[sup];
			
			for(int k = inf; k<=sup; k++)
			{
				t[k]=temporaneo[k-inf+1];
			}
			
			//t.markToRecalculate();
			return true;
			//twopt.twoOptc(t, r);
		}
		
			//counter++;
		//	for(Cliente c : temporaneo)
		//	System.out.println(c);
			
		
		
		return false;
				
	}
	
	public static void twoOptSwap(Cliente[] t, int i, int IdxMaxImpr, int dim)
	{
			
		for (int j = i; j < i +((IdxMaxImpr-i)/2); j++) 
		{
			  Cliente temp = t[j];
			  t[j] = t[IdxMaxImpr +i  - 1 - j];
			  t[IdxMaxImpr+i - 1 - j] = temp;
		}
			
			
	}
	
	/*public static void twoOptSublist(Tour t, int inf, int sup, int dim)
	{

		Integer len_prev_edges = t.getCliente(inf).calculateDistance(t.getCliente((inf-1+dim)%dim))+t.getCliente(sup).calculateDistance(t.getCliente((sup-1+dim)%dim));
		Integer len_new_edges;
		LinkedList<Cliente> temporaneo;
		//int counter = 0;
		
		//if(inf!=0)
			temporaneo=new LinkedList<Cliente>(t.getTour().subList(inf, sup)); //sup+1 escluso, quindi da inf-1 a sup
		/*else
		{
			//temporaneo[0]=t.getCliente((inf-1+dim)%dim);
			for(int j=0; j<sup-inf; j++)
			{
				temporaneo.add(t.getCliente(inf+j));
			}
	
			//temporaneo[sup-inf+1]=t.getCliente(sup);
		}*/
		//System.out.println(t);
		//System.out.println(temporaneo);
			//System.out.println(t)
			/*temporaneo[0]=t.getCliente((inf-1+dim)%dim);
		
			for(int j=0; j<sup-inf; j++)
				{
					temporaneo[j+1]= t.getCliente(sup-j-1);
				}
		
			temporaneo[sup-inf+1]=t.getCliente(sup);
			*/
			//counter++;
			//for(Cliente c : temporaneo)
			//System.out.println(c);
			
	/*	len_new_edges= t.getCliente((inf-1+dim)%dim).calculateDistance(t.getCliente((sup-1+dim)%dim))+t.getCliente(sup).calculateDistance(t.getCliente(inf));
		
		if(len_new_edges<len_prev_edges)
		{
			int count=0;
			for(Iterator<Cliente> it = temporaneo.descendingIterator(); it.hasNext();)
			{
				t.setCliente(inf+count, it.next());
				count++;
			}
			/*for(int k = 0; k<sup-inf; k++)
			{
				t.setCliente(k+inf,temporaneo[sup-inf-k]);
			}
			
			
			t.markToRecalculate();
			//twopt.twoOptc(t, r);
		}
				
	}*/
	
	public static boolean fixedRadius(Cliente[] t)
	{
		int dim = t.length;
		boolean improve=false;
		float radius;
		LinkedList<Integer> neighbours=new LinkedList<Integer>();
		//	int rand = r.nextInt(dim/10);
		Cliente tmp;
		for(int i = 1; i < dim; i++)
		{
			tmp=t[i];
			radius=tmp.calculateDistance(t[i-1]);
			for(int j=0;j<dim;j++)
			{
				if(j==i-1 || j==i) continue;
				if(tmp.calculateDistance(t[j])<radius)
				{
					neighbours.add(j);
				}
			}
			for(Integer k: neighbours)
			{
				if(i<k)
				{
					if(Tour.twoOpt(t, i, k,dim))
						improve=true;
				}
				else
				{
					if(improve=Tour.twoOpt(t, k, i,dim))
						improve=true;
				}
			}
			neighbours.clear();	
		}
		return improve;
	}
	
	
	public static boolean fixedRadiusNolook(Cliente[] t, boolean noLook[])
	{
		int dim = t.length;
		float radius;
		boolean improve_flag;
		boolean global_improve=false;
		//	int rand = r.nextInt(dim/10);
		for(int i = 1; i < dim; i++)
		{
			if(noLook[t[i].getId()] == true) continue;
			improve_flag = false;
			radius=t[i].calculateDistance(t[(i-1+dim)%dim]);
			for(int j=0;j<dim;j++)
			{
				if(j==i-1 || j==i) continue;
				if(t[i].calculateDistance(t[j])<radius)
				{
					if(i<j)
					{	
						if(Tour.twoOptbool(t, i, j,dim))
						{
							noLook[t[i].getId()] = false;
							noLook[t[j].getId()] = false;
							improve_flag = true;
							global_improve=true;
						}
					}
					else
					{
						if(Tour.twoOptbool(t, j, i,dim))
						{
							noLook[t[i].getId()] = false;
							noLook[t[j].getId()] = false;
							improve_flag = true;
							global_improve=true;
						}
					}
				}
			}
			if(improve_flag == false) noLook[t[i].getId()] = true;
		}
		return global_improve;
	}
	
	public static boolean fixedRadiusNolookNear(Cliente[] t, boolean noLook[])
	{
		int dim = t.length;
		float radius;
		boolean improve_flag;
		boolean global_improve=false;
		int improvement;
		int index=-1;
		int IdxMaxImpr=-1;
		//	int rand = r.nextInt(dim/10);
		for(int i = 0; i < dim; i++)
		{
			if(noLook[t[i].getId()] == true) continue;
			improve_flag = false;
			radius=t[i].calculateDistance(t[(i-1+dim)%dim]);
			improvement=0;
			IdxMaxImpr=-1;
			for(int j=0;j<Cliente.nearest[0].length;j++)
			{
				index=-1;
				if(t[i].calculateDistance(Cliente.nearest[t[i].getId()-1][j])>=radius)
					break;
				else
				{		
					for(int k=0; k<dim; k++)
					{
						if(Cliente.nearest[t[i].getId()-1][j].equals(t[k]))
						{
							index=k;
							break;
						}
					}
					int prev_edges=t[i].calculateDistance(t[(i-1+dim)%dim])+t[index].calculateDistance(t[(index-1+dim)%dim]);
					int after_edges=t[(i-1+dim)%dim].calculateDistance(t[(index-1+dim)%dim])+t[i].calculateDistance(t[index]);
					if(after_edges<prev_edges && (prev_edges-after_edges)>improvement)
					{
						improvement=prev_edges-after_edges;
						IdxMaxImpr=index;
						improve_flag = true;
						global_improve=true;
					}
				}
			}
			if(improve_flag)
			{	
				if(i>IdxMaxImpr)
				{
					int temp=i;
					i=IdxMaxImpr;
					IdxMaxImpr=temp;
				}
				for (int j = i; j < i +((IdxMaxImpr-i)/2); j++) 
				{
					  Cliente temp = t[j];
					  t[j] = t[IdxMaxImpr +i  - 1 - j];
					  t[IdxMaxImpr+i - 1 - j] = temp;
				}
				noLook[t[i].getId()] = false;
				noLook[t[IdxMaxImpr].getId()] = false;
				
			}
			else
				noLook[t[i].getId()] = true;
		}
		return global_improve;
	}
	
	public static boolean fixedRadiusNolookNearFirst(Cliente[] t, boolean noLook[])
	{
		int dim = t.length;
		float radius;
		boolean improve_flag;
		boolean global_improve=false;
		//int improvement;
		int index=-1;
		//int IdxMaxImpr=-1;
		//	int rand = r.nextInt(dim/10);
		for(int i = 0; i < dim; i++)
		{
			if(noLook[t[i].getId()] == true) continue;
			improve_flag = false;
			radius=t[i].calculateDistance(t[(i-1+dim)%dim]);
			//improvement=0;
			//IdxMaxImpr=-1;
			for(int j=0;j<Cliente.nearest[0].length;j++)
			{
				index=-1;
				if(t[i].calculateDistance(Cliente.nearest[t[i].getId()-1][j])>=radius)
					break;
				else
				{		
					for(int k=0; k<dim; k++)
					{
						if(Cliente.nearest[t[i].getId()-1][j].equals(t[k]))
						{
							index=k;
							break;
						}
					}
					int prev_edges=t[i].calculateDistance(t[(i-1+dim)%dim])+t[index].calculateDistance(t[(index-1+dim)%dim]);
					int after_edges=t[(i-1+dim)%dim].calculateDistance(t[(index-1+dim)%dim])+t[i].calculateDistance(t[index]);
					if(after_edges<prev_edges)
					{
						if(i<index)
						{	
							Tour.twoOptSwap(t, i, index,dim);
							noLook[t[i].getId()] = false;
							noLook[t[index].getId()] = false;
							improve_flag = true;
							global_improve=true;
							break;
							
						}
						else if(i>index)
						{
							Tour.twoOptSwap(t, index, i,dim);				
							noLook[t[i].getId()] = false;
							noLook[t[index].getId()] = false;
							improve_flag = true;
							global_improve=true;
							break;
						}
						
					}
				}
			}
			
			if(improve_flag == false) noLook[t[i].getId()] = true;
		}
		return global_improve;
	}
	
	public static void threeOpt(Cliente[] t)
	{
		int dim = t.length;
		
		for(int i=0; i<dim;i++)
		{
			for(int j=i+1;j<i+200 && j<dim;j++)
			{
				for(int k=j+1; k<j+200 && k<dim; k++)
				{
					threeOptSwap(t,i,j,k);
				}
				
			}
			int q=0;
		}
	}
	
	public static boolean threeOptSwap(Cliente[] t, int first, int second, int third)
	{
		int dim= t.length;
		int prev_length=t[(first-1+dim)%dim].calculateDistance(t[first])+t[second-1].calculateDistance(t[second])+t[third-1].calculateDistance(t[third]);
		int after_length=t[(first-1+dim)%dim].calculateDistance(t[second])+t[second-1].calculateDistance(t[third-1])+t[third].calculateDistance(t[first]);
		
		if(after_length<prev_length)
		{
			ArrayList<Cliente> tmp= new ArrayList<Cliente>(Arrays.asList(t));
			if(first>second || first>third)
				Collections.rotate(tmp, -first);
			
		//	System.out.println(tmp);
			Collections.reverse(tmp.subList(first, second));
			ArrayList<Cliente> swap;
			swap= new ArrayList<Cliente>(tmp.subList(second, third));
			tmp.subList(second, third).clear();
			tmp.addAll(first, swap);
		//	System.out.println(tmp);
			tmp.toArray(t);
		/*	for(Cliente c : t)
			{
				System.out.println(c);
			}*/
			return true;
		}
		return false;
	}
	
	public static boolean fixedRadiusNolook3Opt(Cliente[] t, boolean noLook[])
	{
		int dim = t.length;
		int radius;
		int second_radius;
		boolean improve_flag;
		boolean global_improve=false;
		for(int i = 1; i < dim; i++)
		{
			if(noLook[t[i].getId()] == true) continue;
			improve_flag = false;
			radius=t[i].calculateDistance(t[(i-1+dim)%dim]);
			for(int j=i+1;j<dim;j++)
			{
				if(t[(i-1+dim)%dim].calculateDistance(t[j])<radius)
				{		
				second_radius=t[i].calculateDistance(t[(i-1+dim)%dim])+t[j].calculateDistance(t[(j-1+dim)%dim]);
				for(int l=j+1; l<dim; l++)
				{
					if(t[(i-1+dim)%dim].calculateDistance(t[j])+t[i].calculateDistance(t[l])<second_radius)
					{	
						if(threeOptSwap(t,i,j,l))
						{
							noLook[t[i].getId()] = false;
							noLook[t[j].getId()] = false;
							noLook[t[l].getId()] = false;
							improve_flag = true;
							global_improve=true;
							break;
						}
					}
				}
				}
				if(improve_flag) break;
			}
			if(improve_flag==false) noLook[t[i].getId()]=true;
		}
		return global_improve;
	}
			
	
}
