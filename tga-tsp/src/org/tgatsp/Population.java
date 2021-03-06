package org.tgatsp;


import java.util.*;

public class Population {
	
	private final ArrayList<Solution> population; //TODO provare pure LinkedList
	private Float sumFitness;
	
	public Population(int size)
	{
		this.population=new ArrayList<Solution>(size);
	}
	
	public Population(ArrayList<Solution> pop)
	{
		this.population=pop;
	}
	
	public ArrayList<Solution> getPopulation()
	{
		return population;
	}
	
	public void addSolution (Solution s)
	{
		population.add(s);
	}
	
	public void kill(Solution s)
	{
		for (Iterator<Solution> it = population.iterator(); it.hasNext();)
		{
			if(it.next().equals(s))
			{
				it.remove();
				break;
			}
		}
	}
	
	public Integer getSize()
	{
		return population.size();
	}
	
	/*
	 * Returns the new Highlander
	 */
	private Solution calculateFitnessSum()
	{
		float max =0;
		float currentSum=0;
		Solution temp=null;
		for (Solution s : population)
		{
			if(s.getFitness()>max)
			{
				max=s.getFitness();
				temp=s;
			}
			currentSum+=s.getFitness();
		}
		sumFitness=currentSum;
		return temp;
	}
	
	public void evaluate()
	{
		calculateFitnessSum();
		population.get(0).setWindow(population.get(0).getFitness()/sumFitness);
		for(int i=1; i<population.size(); i++)
		{
			population.get(i).setWindow(population.get(i-1).getWindow()+(population.get(i).getFitness()/sumFitness));
		}
	}
	
	public Solution selectParent(Random rand)
	{
		Float random=rand.nextFloat();
		int i;
		for(i=0; i<population.size(); i++)
		{
			if(population.get(i).getWindow()>random)
				break;
		}
		return population.get(i);
	}
	
	public void survive(Population newGeneration, Random rand)
	{
		this.population.ensureCapacity(this.getSize()+newGeneration.getSize());
		this.population.addAll(this.getSize(), newGeneration.getPopulation());
		TGA.DuncanMacLeod=calculateFitnessSum(); //We set the new Highlander
		
		population.get(0).setWindow((1/population.get(0).getFitness())/sumFitness);
		for(int i=1; i<population.size(); i++)
		{
			population.get(i).setWindow(population.get(i-1).getWindow()+((1/population.get(i).getFitness())/sumFitness));
		}
		
		int toKill=TGA.offspringsPerEpoch;
		float victim;
		int j;
		while(toKill>0)
		{
			victim=rand.nextFloat();
			for (j=0; j<population.size(); j++)
			{
				if(population.get(j).getWindow()>victim)
					break;
			}
			
			if(population.get(j).getKillingFlag()==false && !population.get(j).equals(TGA.DuncanMacLeod))
			{
				population.get(j).setKillingFlag(true); //marked to be killed
				toKill--;
			}
		}
		
		for (Iterator<Solution> it = population.iterator(); it.hasNext();)
		{
			if(it.next().getKillingFlag())
			{
				it.remove();
			}
		}
		population.trimToSize();
		if(population.size()!=newGeneration.getSize())
			throw new RuntimeException("You killed more than expected!");
		
	}
	
	public static void randomPopulation(int startIndex, int num, Population p, Random r)
	{
		ArrayList<Cliente> arrayClienti = new ArrayList<Cliente> (Arrays.asList(Cliente.listaClienti));
		int rand1;
		int j=startIndex;
		while(j<num+startIndex)
		{
			int k = arrayClienti.size();
			Tour t = new Tour(arrayClienti.size());
			ArrayList<Cliente> temp = new ArrayList<Cliente> (arrayClienti);
			for(int i =0; i<arrayClienti.size(); i++)
			{
			   	rand1 = r.nextInt(k);
			   	t.addCliente(i, temp.remove(rand1));
			   	k--;
			}
			Clan c = new Clan(j,TGA.tabuCoefficient*TGA.populationSize);
			Solution s = new Solution(t,c,null);
			System.out.println(s.toString());
			p.addSolution(s);
			j++;
			
		}
	}
	
	
	public static void nearestNeighbour(Population p,int startIndex, int num, Random r)
	{
		ArrayList<Cliente> arrayClienti = new ArrayList<Cliente> (Arrays.asList(Cliente.listaClienti));
		int rand1;
		int j=startIndex;
		while(j<(num+j))
		{
			int k = arrayClienti.size();
			Tour t = new Tour(arrayClienti.size());
			ArrayList<Cliente> temp = new ArrayList<Cliente> (arrayClienti);
			rand1 = r.nextInt(k);
			float tmp = 0;
			int q;
			int count = 1;
		   	t.addCliente(0, temp.remove(rand1));
			for(int i =0; i<arrayClienti.size()-1; i++)
			{
				Cliente ctmp;
				//System.out.println("Nodo:"+ctmp.toString());
				tmp=t.getCliente(i).calculateDistance(temp.get(0));
				ctmp = temp.get(0);
				q = 0;
				for(int l =1; l<temp.size(); l++)
				{
					float d= t.getCliente(i).calculateDistance(temp.get(l));
					if(d < tmp)
					{
						tmp = d;
						ctmp= temp.get(l);
						q = l;
					}
				}
				
			//	System.out.println(t.toString());
				t.addCliente(count,ctmp);
				count++;
				temp.remove(q);
			//	System.out.println(t.toString());
			}
			Clan c = new Clan(j,TGA.tabuCoefficient*TGA.populationSize);
	                //Solution s = new Solution(t,c,null);
			//System.out.println(s.toString());
			//p.addSolution(s);
			Solution s=new Solution(t,c,null);
			p.addSolution(s);
			j++;
		}	
	}	
}
